/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
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

/*
 * ServerInboundSequence.java
 *
 * @author Mike Grogan
 * Created on November 21, 2005, 2:50 PM
 *
 */
package com.sun.xml.ws.rm.jaxws.runtime.server;

import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.rm.SequenceSettings;
import com.sun.xml.ws.api.rm.server.ServerSequence;
import com.sun.xml.ws.rm.InvalidMessageNumberException;
import com.sun.xml.ws.rm.RMMessage;
import com.sun.xml.ws.rm.RMException;
import com.sun.xml.ws.rm.jaxws.runtime.InboundSequence;
import com.sun.xml.ws.rm.jaxws.runtime.OutboundSequence;
import com.sun.xml.ws.rm.jaxws.runtime.SequenceConfig;
import com.sun.xml.ws.rm.jaxws.util.LoggingHelper;
import com.sun.xml.ws.rm.localization.LocalizationMessages;
import com.sun.xml.ws.runtime.util.Session;

import java.net.URI;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An <code>ServerInboundSequence</code> represents a sequence of incoming messages.  For an 
 * <code>RMDestination</code>, an <code>InboundSequnce</code> consists of all
 * the requests to a service from a particular proxy.
 */
public class ServerInboundSequence extends InboundSequence implements ServerSequence {

    private static final Logger logger = Logger.getLogger(LoggingHelper.getLoggerName(ServerInboundSequence.class));
    /**
     * Session associated with this sequence.
     */
    private Session session;

    public ServerInboundSequence(URI acksTo, String inboundId, String outboundId, SequenceConfig config) {      
        super(acksTo, config, true);

        setCompanionSequence(new ServerOutboundSequence(this, outboundId, config));
        if (inboundId == null) {
            String newid = "uuid:" + UUID.randomUUID();
            setId(newid);
        } else {
            setId(inboundId);
        }
    }

    /**
     *  Gets the original message in a the Sequence with a given message number.
     *  
     *  @param duplicate Subsequent message with same number.
     *  @return the original message.
     */
    public RMMessage getOriginalMessage(RMMessage duplicate) throws InvalidMessageNumberException {
        int number = duplicate.getMessageNumber();
        return get(number);
    }

    /**
     * Accessor for the <code>session</code> field.
     *
     * @return The value of the session field.
     */
    public Session getSession() {
        return session;
    }

    /**
     * Mutator for the <code>session</code> field.
     */
    public void setSession(Session s) {
        session = s;
    }

    /**
     * If ordered delivery is required, resume processing the next Message
     * in the Sequence if it is waiting for this message to be delivered.
     * This method is called after ServerPipe.process returns for this message.
     * while waiting for gaps in the sequence to fill that can now be processed.
     *
     *@param message The message to  be processed
     */
    public boolean isDeliverable(RMMessage message) {
        if (!getConfig().isOrdered()) {
            return true;
        }

        try {
            int num = message.getMessageNumber();

            //if immediate predecessor has not been processed, wait fcor it
            if (num > 1) {
                RMMessage mess = get(num - 1);
                if (mess == null || !mess.isComplete()) {
                    return false;
                }
            }
        } catch (Exception e) {
        // TODO: exception handling
        }
        return true;
    }

    /**
     * Used to re-populate a sequence with persisted messages
     * after a restart.  Do not use for other purposes.
     *
     * @param index The index to add message at.
     * @param message The JAX-WS message to add
     * @param complete Indicates whether to mark the message as complete.
     */
    public void resetMessage(int index, Message message, boolean complete) {
        try {
            RMMessage rmMessage = new RMMessage(message, getConfig().getRMVersion());
            set(index, rmMessage);

            if (complete) {
                rmMessage.complete();
            }
        } catch (RMException e) {
            logger.log(Level.SEVERE, LocalizationMessages.WSRM_3020_COULD_NOT_RESET_MESSAGE(index, getId()), e);
        }
    }

    /**
     * Implementation of ServerSequence.getSequenceSettings..
     */
    public SequenceSettings getSequenceSettings() {
        SequenceConfig settings = getConfig();
        settings.setSequenceId(getId());

        OutboundSequence oseq = getOutboundSequence();

        settings.setCompanionSequenceId((oseq != null) ? oseq.getId() : null);
        return settings;
    }

    /**
     * Return value determines whether the interval since last activity
     * exceeds the inactivity timeout setting.
     *
     * @return true if sequence has expired.
     *         false otherwise.
     */
    public boolean isExpired() {
        return System.currentTimeMillis() - this.getLastActivityTime() > getConfig().getInactivityTimeout();
    }

    /**
     * Return value reports whether ordered delivery is configured for this
     * sequence.
     *
     * @return true if sequence is ordered.
     *         false otherwise.
     */
    public boolean isOrdered() {
        return getConfig().isOrdered();
    }

    /**
     * When ordered delivery is required, this method is used to determine
     * whether the messages with lower message numbers have already been 
     * processed.
     */
// TODO remove method as it seems to be unused
//    public boolean isPredecessorComplete(RMMessage message) {
//        try {
//            int num = message.getMessageNumber();
//
//            //if immediate predecessor has not been processed, wait fcor it
//            if (num > 1) {
//                RMMessage mess = get(num - 1);
//                if (mess == null || !mess.isComplete()) {
//                    return false;
//                }
//            }
//        } catch (InvalidMessageNumberException e) {
//        // TODO: exception handling
//        }
//
//        return true;
//    }

    /**
     * If ordered delivery is required, resume processing the next Message
     * in the Sequence if it is waiting for this message to be delivered.
     * This method is called when RMServerTube.processResponse is called for this message.
     * while waiting for gaps in the sequence to fill that can now be processed.
     *
     * @param message The message to be processed
     */
    public void releaseNextMessage(RMMessage message) throws RMException {
        message.complete();
        decreaseStoredMessages();

        //notify immediate successor if it is waiting 
        int num = message.getMessageNumber();

        if (num < getNextIndex() - 1 && get(num + 1) != null) {
            // TODO logging
            System.out.println("resuming " + (num + 1));
            get(num + 1).resume();
        }
    }
}


