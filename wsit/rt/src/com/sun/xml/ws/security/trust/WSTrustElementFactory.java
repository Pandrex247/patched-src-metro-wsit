/*
 * $Id: WSTrustElementFactory.java,v 1.22 2008-06-26 20:58:10 jdg6688 Exp $
 */

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

package com.sun.xml.ws.security.trust;

import com.sun.xml.ws.security.trust.elements.OnBehalfOf;
import com.sun.xml.ws.security.trust.elements.str.DirectReference;
import com.sun.xml.ws.security.trust.elements.str.KeyIdentifier;

import com.sun.xml.ws.api.security.trust.Claims;
import com.sun.xml.ws.api.security.trust.WSTrustException;
import com.sun.xml.ws.security.trust.elements.AllowPostdating;
import com.sun.xml.ws.security.trust.elements.BinarySecret;
import com.sun.xml.ws.security.trust.elements.BaseSTSRequest;
import com.sun.xml.ws.security.trust.elements.BaseSTSResponse;
import com.sun.xml.ws.security.trust.elements.CancelTarget;
import com.sun.xml.ws.security.trust.elements.Entropy;
import com.sun.xml.ws.security.trust.elements.IssuedTokens;
import com.sun.xml.ws.security.trust.elements.Lifetime;
import com.sun.xml.ws.security.trust.elements.RenewTarget;
import com.sun.xml.ws.security.trust.elements.Renewing;
import com.sun.xml.ws.security.trust.elements.RequestSecurityToken;
import com.sun.xml.ws.security.trust.elements.RequestSecurityTokenResponse;
import com.sun.xml.ws.security.trust.elements.RequestSecurityTokenResponseCollection;
import com.sun.xml.ws.security.trust.elements.RequestedProofToken;
import com.sun.xml.ws.security.trust.elements.RequestedAttachedReference;
import com.sun.xml.ws.security.trust.elements.RequestedUnattachedReference;
import com.sun.xml.ws.security.trust.elements.RequestedSecurityToken;
import com.sun.xml.ws.security.trust.elements.SecondaryParameters;
import com.sun.xml.ws.api.security.trust.Status;
import com.sun.xml.ws.security.trust.elements.UseKey;
import com.sun.xml.ws.security.trust.elements.ValidateTarget;
import com.sun.xml.ws.security.trust.impl.WSTrustElementFactoryImpl;
import java.net.URI;

import com.sun.xml.ws.policy.impl.bindings.AppliesTo;

import com.sun.xml.ws.security.EncryptedKey;
import com.sun.xml.ws.security.trust.elements.str.Reference;
import com.sun.xml.ws.security.trust.elements.str.SecurityTokenReference;
import com.sun.xml.ws.security.Token;
import com.sun.xml.ws.security.secconv.WSSCVersion;
import com.sun.xml.ws.security.wsu10.AttributedDateTime;
import java.util.List;

import javax.xml.transform.Source;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

/**
 * A Factory for creating the WS-Trust schema elements, and marshalling/un-marshalling them
 * <p>
 * The default Implementation classes for all these WS-Trust schema Elements would assume
 * that JAXB Bindings were generated for ws-trust.xsd schema in a particular fixed namespace/package.
 * The default implementation classes for all these WS-Trust Element Interfaces would hence wrap 
 * the schema generated classes.
 * </p>
 * <p>
 * An STS Service can create a RequestSecurityToken from the JAXBBean(i.e RequestSecurityTokenType) 
 * it receives, as an SEI method parameter, in the following manner
 * </P>
 * <PRE>
 * RequestSecurityTokenType tok=...//obtained as JAXWS SEI method paramater
 * ObjectFactory factory = new ObjectFactory();
 * JAXBElement&lt;RequestSecurityTokenType&gt; rst= factory.createRequestSecurityToken(tok);
 * WSTrustElementFactory fact= ..
 * RequestSecurityToken requestSecurityToken= fact.createRSTFrom(rst);
 * </PRE>
 * <p>
 *  To get back a JAXB Bean from an instance of RequestSecurityToken the following can be done
 * <PRE>
 * JAXBElement&lt;RequestSecurityTokenType&gt; elem = fact.toJAXBElement(requestSecurityToken);
 * RequestSecurityTokenType tok = elem.getValue();
 * </PRE>
 * </p>
 * @author Kumar Jayanti
 */
public abstract class WSTrustElementFactory {
    
    private static JAXBContext jaxbContext = null;
    private static JAXBContext jaxbContext13 = null;
    
    static {
        try {
            jaxbContext = JAXBContext.newInstance("com.sun.xml.ws.security.trust.impl.bindings:com.sun.xml.ws.security.secconv.impl.bindings:com.sun.xml.ws.security.secext10:com.sun.xml.ws.policy.impl.bindings");
            jaxbContext13 = JAXBContext.newInstance("com.sun.xml.ws.security.trust.impl.wssx.bindings:com.sun.xml.ws.security.secconv.impl.wssx.bindings:com.sun.xml.ws.security.secext10:com.sun.xml.ws.policy.impl.bindings");
        } catch (JAXBException jbe) {
            throw new RuntimeException(jbe.getMessage(),jbe);
        }        
    }
    private static WSTrustElementFactory trustElemFactory 
            = new WSTrustElementFactoryImpl();
    private static WSTrustElementFactory trustElemFactory13 
            = new com.sun.xml.ws.security.trust.impl.wssx.WSTrustElementFactoryImpl();        

    public static JAXBContext getContext() {
        return jaxbContext;
    }

    public static JAXBContext getContext(WSTrustVersion wstVer) {
        if (wstVer instanceof com.sun.xml.ws.security.trust.impl.wssx.WSTrustVersion13){
                return jaxbContext13;
        }
        return jaxbContext;
    }

    public static WSTrustElementFactory newInstance() {
        return trustElemFactory;
    }
    
    public static WSTrustElementFactory newInstance(WSTrustVersion wstVer) {
        if (wstVer.getNamespaceURI().equals(WSTrustVersion.WS_TRUST_13_NS_URI)){
            return trustElemFactory13;
        }        
        return trustElemFactory;
    }
    
    public static WSTrustElementFactory newInstance(WSSCVersion wsscVer) {
        if (wsscVer.getNamespaceURI().equals(WSSCVersion.WSSC_13_NS_URI)){
            return com.sun.xml.ws.security.secconv.WSSCElementFactory13.newInstance();            
        }
        return com.sun.xml.ws.security.secconv.WSSCElementFactory.newInstance();        
    }
    
    /** 
     * Create an RST for Issue from the given arguments
     * Any of the arguments can be null since they are all optional, but one of tokenType and AppliesTo must be present 
     */
    public abstract RequestSecurityToken createRSTForIssue(URI tokenType, URI requestType, URI context, AppliesTo scopes, Claims claims, Entropy entropy, Lifetime lifetime) throws WSTrustException;
    
    /** 
     * create an RSTR for Issue from the given arguments
     * Any of the arguments can be null since they are all optional, but one of RequestedSecurityToken or RequestedProofToken should be returned  
     */
    public abstract RequestSecurityTokenResponse createRSTRForIssue(URI tokenType, URI context, RequestedSecurityToken token, AppliesTo scopes, RequestedAttachedReference attachedRef, RequestedUnattachedReference unattachedRef, RequestedProofToken proofToken, Entropy entropy, Lifetime lifetime) throws WSTrustException;

    /** 
     *Create  a collection of RequestSecurityTokenResponse(s)  
     */
    public abstract RequestSecurityTokenResponseCollection createRSTRCollectionForIssue(URI tokenType, URI context, RequestedSecurityToken token, AppliesTo scopes, RequestedAttachedReference attachedRef, RequestedUnattachedReference unattachedRef, RequestedProofToken proofToken, Entropy entropy, Lifetime lifetime) throws WSTrustException;

    /** 
     * Create a wst:IssuedTokens object
     */
    public abstract IssuedTokens createIssuedTokens(RequestSecurityTokenResponseCollection issuedTokens);
    
    /**
     * Create an Entropy with a BinarySecret
     */
    public abstract Entropy createEntropy(BinarySecret secret);
    
    /**
     * Create an Entropy with an xenc:EncryptedKey
     */
    public abstract Entropy createEntropy(EncryptedKey key);
    
    /**
     * Create SecondaryParameters
     */
    public abstract SecondaryParameters createSecondaryParameters();

    /**
     * Create a BinarySecret
     */
    public abstract BinarySecret createBinarySecret(byte[] rawValue, String type);
    /**
     * Create a BinarySecret
     */
    public abstract BinarySecret createBinarySecret(Element elem) throws WSTrustException;
    
    public abstract UseKey createUseKey(Token token, String sig);

    public abstract OnBehalfOf createOnBehalfOf(Token oboToken);
    
    public abstract ValidateTarget createValidateTarget(Token token);
    
    public abstract Status createStatus(String code, String reason);
    
    /**
     * Create a Lifetime.
     */
    public abstract Lifetime createLifetime(AttributedDateTime created,  AttributedDateTime expires);
    
    /**
     * Create a RequestedProofToken.
     */
    public abstract RequestedProofToken createRequestedProofToken();
    
    /**
     * Create a RequestedSecurityToken.
     */
   public abstract RequestedSecurityToken createRequestedSecurityToken(Token token);
   
   public abstract RequestedSecurityToken createRequestedSecurityToken();
   
   public abstract DirectReference createDirectReference(String valueType, String uri);
   
   public abstract KeyIdentifier createKeyIdentifier(String valueType, String encodingType);
   
   public abstract SecurityTokenReference createSecurityTokenReference(Reference ref);

   /**
     * Create a RequestedAttachedReference.
     */
   public abstract RequestedAttachedReference createRequestedAttachedReference(SecurityTokenReference str);

   /**
     * Create a RequestedUnattachedReference.
     */
   public abstract RequestedUnattachedReference createRequestedUnattachedReference(SecurityTokenReference str);

    /**
     *Create an RST for a Renewal Request
     */
    public abstract RequestSecurityToken createRSTForRenew(URI tokenType, URI requestType, URI context, RenewTarget target, AllowPostdating apd, Renewing renewingInfo);
    
    /**
     *Create an RSTR for a Renewal Response
     */
    public  abstract RequestSecurityTokenResponse createRSTRForRenew(URI tokenType, URI context, RequestedSecurityToken token, RequestedAttachedReference attachedReference, RequestedUnattachedReference unattachedRef, RequestedProofToken proofToken, Entropy entropy, Lifetime lifetime) throws WSTrustException;;
    
    public abstract RenewTarget createRenewTarget(SecurityTokenReference str);
    
    public abstract CancelTarget createCancelTarget(SecurityTokenReference str);
    /**
     *Create an RST for Token Cancellation
     */
    public abstract RequestSecurityToken createRSTForCancel(URI requestType, CancelTarget target);
    
    /**
     *Create an RSTR for a Successful Token Cancellation
     */
    public abstract RequestSecurityTokenResponse createRSTRForCancel();
    
    /**
     *Create an RST for Token Validation
     *<p>
     *TODO: Not clear from Spec whether the Token to be validated is ever sent ?
     *TODO: There is a mention of special case where a SOAPEnvelope may be specified as 
     * a security token if the requestor desires the envelope to be validated.
     *</p>
     */
    public abstract RequestSecurityToken createRSTForValidate(URI tokenType, URI requestType);
    
    /**
     * create an RSTR for validate request.
     */
    public abstract RequestSecurityTokenResponse createRSTRForValidate(URI tokenType, RequestedSecurityToken token, Status status);

    public abstract RequestSecurityTokenResponseCollection createRSTRC(List<RequestSecurityTokenResponse> rstrs);

    /**
     * Create an Empty RST
     */
    public abstract RequestSecurityToken createRST();
    
    /**
     * Create an Empty RSTR
     */
    public abstract RequestSecurityTokenResponse createRSTR();
    
    
    /** 
     * create an RST from a Source
     */
    public abstract RequestSecurityToken createRSTFrom(Source src);
    
    /**
     * create an RST from DOM Element
     */
    public abstract RequestSecurityToken createRSTFrom(Element elem);
    
    /** 
     * create an RSTR from a Source
     */
    public abstract RequestSecurityTokenResponse createRSTRFrom(Source src);
    
    /**
     * create an RSTR from DOM Element
     */
    public abstract RequestSecurityTokenResponse createRSTRFrom(Element elem);
    
    /**
     * Create RSTR Collection from Source
     */
    public abstract RequestSecurityTokenResponseCollection createRSTRCollectionFrom(Source src);
    
    /**
     * Create RSTR Collection from Element
     */
    public abstract RequestSecurityTokenResponseCollection createRSTRCollectionFrom(Element elem);
    
    public abstract Claims createClaims(Element elem)throws WSTrustException;
    
    
    /**
     * create an RST from JAXBElement
     * <p>
     * NOTE: an STS Implementor can call
     * <PRE>
     * JAXBElement&lt;RequestSecurityTokenType&gt; elem= 
     * ObjectFactory.createRequestSecurityToken(&lt;JAXBBean for RST&gt;)
     * </PRE>
     * The JAXBBean for RST is the one generated from the ws-trust.xsd schema
     * The default implementation expects the packagename of the generated JAXB Beans to be fixed.
     * </p>
     */
    public abstract RequestSecurityToken createRSTFrom(JAXBElement elem);
    
    /**
     * create an RSTR from JAXBElement
     * <p>
     * NOTE: an STS Implementor can call
     * <PRE>
     * JAXBElement&lt;RequestSecurityTokenResponseType&gt; elem= 
     * ObjectFactory.createRequestSecurityTokenResponse(&lt;JAXBBean for RSTR&gt;);
     * </PRE>
     * The &lt;JAXBBean for RSTR&gt; is the one generated from the ws-trust.xsd schema
     * The default implementation expects the packagename of the generated JAXB Beans to be fixed.
     * </p>
     */
    public  abstract RequestSecurityTokenResponse createRSTRFrom(JAXBElement elem);
    
    /**
     * create an RSTR Collection from JAXBElement
     * <p>
     * NOTE: an STS Implementor can call
     * <PRE>
     * JAXBElement&lt;RequestSecurityTokenResponseCollectionType&gt; elem= 
     * ObjectFactory.createRequestSecurityTokenResponseCollection(&lt;JAXBBean for RSTR Collection&gt;
     * </PRE>
     * The &lt;JAXBBean for RSTR Collection&gt; is the one generated from the ws-trust.xsd schema
     * The default implementation expects the packagename of the generated JAXB Beans to be fixed.
     * </p>
     */
    public abstract RequestSecurityTokenResponseCollection createRSTRCollectionFrom(JAXBElement elem);
    
    public abstract SecurityTokenReference createSecurityTokenReference(JAXBElement elem);
    
    public abstract JAXBElement toJAXBElement(BaseSTSRequest request);

    public abstract JAXBElement toJAXBElement(BaseSTSResponse response);
     /**
     * convert an SecurityTokenReference to a JAXBElement
     */
    public abstract JAXBElement toJAXBElement(SecurityTokenReference str);
   
    
    /**
     * convert an RST to a JAXBElement
     */
    public abstract JAXBElement toJAXBElement(RequestSecurityToken rst);
    
    /**
     * convert an RSTR to a JAXBElement
     */
    public abstract JAXBElement toJAXBElement(RequestSecurityTokenResponse rstr);
    
    /**
     * convert an RSTR Collection to a JAXBElement
     */
    public abstract JAXBElement toJAXBElement(RequestSecurityTokenResponseCollection rstrCollection);
    
    public abstract Source toSource(BaseSTSRequest request);

    public abstract Source toSource(BaseSTSResponse response);
    /**
     * Marshal an RST to a Source.
     * <p>
     * Note: Useful for Dispatch Client implementations  
     * </p>
     */
    public abstract Source toSource(RequestSecurityToken rst);
    
    /**
     * Marshal an RSTR  to a Source
     * <p>
     * Note: Useful for STS implementations which are JAXWS Providers  
     * </p>
     */
    public abstract Source toSource(RequestSecurityTokenResponse rstr);
    
    /**
     * Marshal an RSTR Collection to a Source
     * <p>
     * Note: Useful for STS implementations which are JAXWS Providers  
     * </p>
     */
    public abstract Source toSource(RequestSecurityTokenResponseCollection rstrCollection);
    
    public abstract Element toElement(BaseSTSRequest request);

    public abstract Element toElement(BaseSTSResponse response);
    /**
     * Marshal an RST to a DOM Element.
     * <p>
     * Note: Useful for Dispatch Client implementations  
     * </p>
     */
    public abstract Element toElement(RequestSecurityToken rst);
    
    /**
     * Marshal an RSTR  to DOM Element
     * <p>
     * Note: Useful for STS implementations which are JAXWS Providers  
     * </p>
     */
    public abstract Element toElement(RequestSecurityTokenResponse rstr);
    
    public abstract Element toElement(RequestSecurityTokenResponse rstr, Document doc);
    
    /**
     * Marshal an RSTR Collection to a DOM Element
     * <p>
     * Note: Useful for STS implementations which are JAXWS Providers  
     * </p>
     */
    public abstract Element toElement(RequestSecurityTokenResponseCollection rstrCollection);
    
    /**
     * Marshal an BinarySecret to a DOM Element
     * <p>
     * Note: Useful for STS implementations which are JAXWS Providers  
     * </p>
     */
    
     public abstract Element toElement(BinarySecret binarySecret);

     /**
     * Marshal an STR to a DOM Element.
     * <p>
     * Note: Useful for Dispatch Client implementations
     * </p>
     */
     public abstract Element toElement(SecurityTokenReference str, Document doc);

     /**
     * Marshal an BinarySecret to a DOM Element.
     * <p>
     * Note: Useful for Dispatch Client implementations
     * </p>
     */
     public abstract Element toElement(BinarySecret binarySecret, Document doc);
}
