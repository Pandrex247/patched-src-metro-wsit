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

package com.sun.xml.ws.rx.rm.runtime;

import com.sun.istack.NotNull;
import com.sun.xml.ws.rx.RxRuntimeException;
import com.sun.xml.ws.rx.rm.protocol.AcknowledgementData;
import com.sun.xml.ws.rx.rm.runtime.sequence.DuplicateMessageRegistrationException;
import com.sun.xml.ws.rx.rm.runtime.sequence.Sequence;
import com.sun.xml.ws.rx.rm.runtime.sequence.SequenceManager;
import com.sun.xml.ws.rx.rm.runtime.sequence.UnknownSequenceException;

/**
 * Handles outgoing application messages. This class encapsulates 
 * RM Source logic that is independent on of tha actual delivery mechanism 
 * or framework (such as JAX-WS fibers).
 *
 * @author Marek Potociar <marek.potociar at sun.com>
 */
class SourceMessageHandler implements RedeliveryTask.DeliveryHandler {
    private final SequenceManager sequenceManager;

    SourceMessageHandler(@NotNull SequenceManager sequenceManager) {
        assert sequenceManager != null;

        this.sequenceManager = sequenceManager;
    }

    /**
     * Registers outgoing message with the provided outbound sequence and
     * sets sequenceId and messageNumber properties on the outgoing message.
     *
     * Once the message is registered and properties are set, the message is placed into
     * a delivery queue and delivery callback is invoked.
     *
     * @throws UnknownSequenceException if no such sequence exits for a given sequence identifier
     */
    public void registerMessage(@NotNull ApplicationMessage outMessage, @NotNull String outboundSequenceId) throws DuplicateMessageRegistrationException, UnknownSequenceException {
        assert outMessage != null;
        assert outboundSequenceId != null;

        final Sequence outboundSequence = sequenceManager.getSequence(outboundSequenceId);
        outboundSequence.registerMessage(outMessage, true); // TODO it may not be needed to store message if AtMostOnce delivery
    }

    /**
     * Attaches RM acknowledgement information such as inbound sequence acknowledgements
     * (if there is an inbound sequence bound to the specified outbound sequence) or
     * outbound sequence acknowledgement requested flag to the outgoing message.
     *
     * @throws UnknownSequenceException if no such sequence exits for a given sequence identifier
     */
    public void attachAcknowledgementInfo(@NotNull ApplicationMessage outMessage) throws UnknownSequenceException {
        assert outMessage != null;
        assert outMessage.getSequenceId() != null;

        // inbound sequence acknowledgements
        outMessage.setAcknowledgementData(getAcknowledgementData(outMessage.getSequenceId()));
    }

    /**
     * Retrieves acknowledgement information for a given outbound (and inbound) sequence
     *
     * @param outboundSequenceId outbound sequence identifier
     * @return acknowledgement information for a given outbound sequence
     * @throws UnknownSequenceException if no such sequence exits for a given sequence identifier
     */
    public AcknowledgementData getAcknowledgementData(String outboundSequenceId) throws UnknownSequenceException {

        AcknowledgementData.Builder ackDataBuilder = AcknowledgementData.getBuilder();
        Sequence inboundSequence = sequenceManager.getBoundSequence(outboundSequenceId);
        if (inboundSequence != null && inboundSequence.isAckRequested()) {
            ackDataBuilder.acknowledgements(inboundSequence.getId(), inboundSequence.getAcknowledgedMessageIds());
            inboundSequence.clearAckRequestedFlag();
        }
        // outbound sequence ack requested flag
        final Sequence outboundSequence = sequenceManager.getSequence(outboundSequenceId);
        if (outboundSequence.hasUnacknowledgedMessages()) {
            ackDataBuilder.ackReqestedSequenceId(outboundSequenceId);
            outboundSequence.updateLastAcknowledgementRequestTime();
        }
        final AcknowledgementData acknowledgementData = ackDataBuilder.build();
        return acknowledgementData;
    }

    public void putToDeliveryQueue(ApplicationMessage message) throws RxRuntimeException {
        sequenceManager.getSequence(message.getSequenceId()).getDeliveryQueue().put(message);
    }
}
