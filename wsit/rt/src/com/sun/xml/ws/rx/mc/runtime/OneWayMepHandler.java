/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2008 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.xml.ws.rx.mc.runtime;

import com.sun.xml.ws.rx.util.TimestampedCollection;
import com.sun.xml.ws.rx.RxConfiguration;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import com.sun.xml.ws.api.pipe.Fiber;
import java.io.IOException;

/**
 *
 * @author Marek Potociar <marek.potociar at sun.com>
 */
class OneWayMepHandler extends McResponseHandlerBase {

    public OneWayMepHandler(RxConfiguration configuration, MakeConnectionSenderTask mcSenderTask, TimestampedCollection<String, Fiber> suspendedFiberStorage, String correlationId) {
        super(configuration, mcSenderTask, suspendedFiberStorage, correlationId);
    }

    public void onCompletion(Packet response) {
        Message responseMessage = response.getMessage();

        if (responseMessage != null) {
            super.processMakeConnectionHeaders(responseMessage);
        } else if (configuration.isReliableMessagingEnabled()) {
            // FIXME: This is an temporary workaround to be interoperable with MSFT:
            // if response message is null with RM turned on, it means that MSFT did not
            // send back anything (not even a sequence acknowledgement) and is waiting
            // for us until we send a MakeConnection message.
            super.mcSenderTask.scheduleMcRequest();
        }

        super.resumeParentFiber(response);
    }

    public void onCompletion(Throwable error) {
        if (configuration.isReliableMessagingEnabled() && isIOError(error)) {
            // FIXME: This is an temporary workaround to be interoperable with MSFT:
            // if response message is null with RM turned on, it means that MSFT did not
            // send back anything (not even a sequence acknowledgement) and is waiting
            // for us until we send a MakeConnection message.
            //
            // investigation shows that when MSFT returns HTTP 202, a SocketException is
            // raised in our transport layer
            super.mcSenderTask.scheduleMcRequest();
        }

        super.resumeParentFiber(error);
    }

    private boolean isIOError(Throwable error) {
        // normally the IOException comes wrapped into WebServiceException

        return error instanceof IOException || error.getCause() instanceof IOException;
    }
}
