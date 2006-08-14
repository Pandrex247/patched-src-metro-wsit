/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License).  You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the license at
 * https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at https://glassfish.dev.java.net/public/CDDLv1.0.html.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * you own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.xml.ws.policy.sourcemodel;

import java.util.Collection;
import java.util.Map.Entry;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamWriter;
import com.sun.xml.txw2.TXW;
import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.output.StaxSerializer;
import com.sun.xml.ws.policy.PolicyConstants;
import com.sun.xml.ws.policy.PolicyException;

public class XmlPolicyModelMarshaller extends PolicyModelMarshaller {
    
    public void marshal(PolicySourceModel model, Object storage) throws PolicyException {
        if (storage instanceof TypedXmlWriter) {
            marshal(model, (TypedXmlWriter) storage);
        } else if (storage instanceof XMLStreamWriter) {
            marshal(model, (XMLStreamWriter) storage);
        } else {
            throw new PolicyException(Messages.STORAGE_TYPE_NOT_SUPPORTED.format(storage.getClass().getName()));
        }
    }
    
    public void marshal(Collection<PolicySourceModel> models, Object storage) throws PolicyException {
        for (PolicySourceModel model : models) {
            marshal(model, storage);
        }
    }
    
    /**
     * Marshal a policy onto the given TypedXmlWriter.
     *
     * @param model A policy source model.
     * @param writer A typed XML writer.
     */
    private static void marshal(PolicySourceModel model, TypedXmlWriter writer) throws PolicyException {
        TypedXmlWriter policy = writer._element(PolicyConstants.POLICY, TypedXmlWriter.class);
        marshalPolicyAttributes(model, policy);
        marshal(model.getRootNode(), policy);
    }
    
    /**
     * Marshal a policy onto the given XMLStreamWriter.
     *
     * @param model A policy source model.
     * @param writer An XML stream writer.
     */
    private static void marshal(PolicySourceModel model, XMLStreamWriter writer) throws PolicyException {
        StaxSerializer serializer = new StaxSerializer(writer);
        TypedXmlWriter policy = TXW.create(PolicyConstants.POLICY, TypedXmlWriter.class, serializer);
        policy._namespace(PolicyConstants.POLICY_NAMESPACE_URI, PolicyConstants.POLICY_NAMESPACE_PREFIX);
        marshalPolicyAttributes(model, policy);
        marshal(model.getRootNode(), policy);
        policy.commit();
        serializer.flush();
    }
    
    /**
     * Marshal the Policy root element attributes onto the TypedXmlWriter.
     *
     * @param model The policy source model.
     * @param writer The typed XML writer.
     */
    private static void marshalPolicyAttributes(PolicySourceModel model, TypedXmlWriter writer) {
        String policyId = model.getPolicyId();
        if (policyId != null) {
            writer._attribute(PolicyConstants.POLICY_ID, policyId);
        }
        
        String policyName = model.getPolicyName();
        if (policyName != null) {
            writer._attribute(PolicyConstants.POLICY_NAME, policyName);
        }
    }
    
    /**
     * Marshal given ModelNode and child elements on given TypedXmlWriter.
     *
     * @param rootNode The ModelNode that is marshalled.
     * @param writer The TypedXmlWriter onto which the ModelNode is marshalled.
     */
    private static void marshal(ModelNode rootNode, TypedXmlWriter writer) {
        for (ModelNode node : rootNode) {
            AssertionData data = node.getNodeData();
            if (data == null || !data.isPrivateAttributeSet()) {
                TypedXmlWriter child = null;
                if (data == null) {
                    child = writer._element(node.getType().asQName(), TypedXmlWriter.class);
                } else {
                    child = writer._element(data.getName(), TypedXmlWriter.class);
                    String value = data.getValue();
                    if (value != null) {
                        child._pcdata(value);
                    }
                    for (Entry<QName, String> entry : data.getAttributesSet()) {
                        child._attribute(entry.getKey(), entry.getValue());
                    }
                }
                marshal(node, child);
            }
        }
    }
    
}
