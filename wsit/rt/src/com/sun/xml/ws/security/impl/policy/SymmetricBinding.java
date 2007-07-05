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


package com.sun.xml.ws.security.impl.policy;
import com.sun.xml.ws.policy.AssertionSet;
import com.sun.xml.ws.policy.NestedPolicy;
import com.sun.xml.ws.policy.PolicyAssertion;
import com.sun.xml.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.ws.security.policy.AlgorithmSuite;
import com.sun.xml.ws.security.policy.MessageLayout;
import com.sun.xml.ws.security.policy.Token;
import com.sun.xml.ws.security.policy.SecurityAssertionValidator;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import static com.sun.xml.ws.security.impl.policy.Constants.*;

/**
 * 
 * 
 * 
 * @author K.Venugopal@sun.com
 *  
 */

public class SymmetricBinding extends PolicyAssertion implements com.sun.xml.ws.security.policy.SymmetricBinding, SecurityAssertionValidator{    
    
    private AssertionFitness fitness = AssertionFitness.IS_VALID;   
    boolean populated = false;     
    Token protectionToken ;
    Token signatureToken ;   
    Token encryptionToken ; 
    MessageLayout layout = MessageLayout.Lax;   
    AlgorithmSuite algSuite;      
    boolean includeTimestamp=false;   
    boolean disableTimestampSigning=false;
    boolean contentOnly = true;     
    String protectionOrder = SIGN_ENCRYPT;     
    boolean protectToken = false;      
    boolean protectSignature = false;           
    
    /** 
     * 
     * Creates a new instance of SymmetricBinding
     *     
     */     
    
    public SymmetricBinding() {  
    }     
    
    public SymmetricBinding(AssertionData name,Collection<PolicyAssertion> nestedAssertions, AssertionSet nestedAlternative) {            
        
        super(name,nestedAssertions,nestedAlternative); 
    }            
    
    public Token getEncryptionToken() {    
        populate();      
        return encryptionToken;    
    }          
    
    public Token getSignatureToken() {  
        populate();       
        return signatureToken;      
    }          
    
    public Token getProtectionToken() { 
        populate();  
        return protectionToken; 
    }          
    
    public void setAlgorithmSuite(AlgorithmSuite algSuite) {    
        this.algSuite = algSuite;         
    }      
    
    public AlgorithmSuite getAlgorithmSuite() { 
        populate();      
        if(algSuite == null){         
            algSuite = new  com.sun.xml.ws.security.impl.policy.AlgorithmSuite();          
            logger.log(Level.FINE, "Using Default Algorithm Suite Basic128");    
        
        }    
        return algSuite;    
    }           
    
    public void includeTimeStamp(boolean value) {   
        includeTimestamp = value;            
    }                
    
    public boolean isIncludeTimeStamp() {      
        populate();           
        return includeTimestamp;      
    }   
    
    public boolean isDisableTimestampSigning(){
        populate();
        return disableTimestampSigning;
    }
    
    public void setLayout(MessageLayout layout) {    
        this.layout = layout;          
    }            
    
    public MessageLayout getLayout() {
        populate();         
        return layout;         
    }              
    
    public void setEncryptionToken(Token token) {  
        encryptionToken = token ;          
    }              
    
    public void setSignatureToken(Token token) {  
        signatureToken = token;  
    }             
    
    public void setProtectionToken(Token token) {  
        protectionToken = token;    
    }               
    
    public boolean isSignContent() { 
        populate();         
        return contentOnly;       
    }              
    
    public void setSignContent(boolean contentOnly) {  
        
        this.contentOnly = contentOnly;
    }              
    
    public void setProtectionOrder(String order) {
        this.protectionOrder = order;       
    }           
    
    public String getProtectionOrder() {        
        populate();         
        return protectionOrder;        
    }              
    
    public void setTokenProtection(boolean value) { 
        this.protectToken = value;      
    }          
    
    
    public void setSignatureProtection(boolean value) {         
        
        this.protectSignature = value;     
    }               
    
    public boolean getTokenProtection() {    
        populate();         
        return protectToken;    
    }              
    
    public boolean getSignatureProtection() {    
        populate();         
        return protectSignature;       
    }             
    
    private void populate(){       
        populate(false);      
    }             
    
    private synchronized AssertionFitness populate(boolean isServer) {     
        if(!populated){                      
            NestedPolicy policy = this.getNestedPolicy();
            if(policy == null){                            
                if(logger.getLevel() == Level.FINE){        
                    logger.log(Level.FINE,"NestedPolicy is null");      
                }                           
                populated = true;        
                return fitness;          
            }               
            AssertionSet as = policy.getAssertionSet();    
            Iterator<PolicyAssertion> ast = as.iterator();       
            while(ast.hasNext()){                           
                PolicyAssertion assertion = ast.next();     
                if(PolicyUtil.isSignatureToken(assertion)){  
                    this.signatureToken = ((com.sun.xml.ws.security.impl.policy.Token)assertion).getToken();      
                }else if(PolicyUtil.isEncryptionToken(assertion)){ 
                    this.encryptionToken =((com.sun.xml.ws.security.impl.policy.Token)assertion).getToken();   
                }else if(PolicyUtil.isProtectionToken(assertion)){                   
                    this.protectionToken = ((com.sun.xml.ws.security.impl.policy.Token)assertion).getToken(); 
                }else if(PolicyUtil.isAlgorithmAssertion(assertion)){    
                    this.algSuite = (AlgorithmSuite) assertion;         
                }else if(PolicyUtil.isIncludeTimestamp(assertion)){      
                    this.includeTimestamp = true;                        
                }else if(PolicyUtil.isEncryptBeforeSign(assertion)){       
                    this.protectionOrder = ENCRYPT_SIGN;                 
                }else if (PolicyUtil.isSignBeforeEncrypt(assertion)){
                    this.protectionOrder = SIGN_ENCRYPT;
                }else if(PolicyUtil.isContentOnlyAssertion(assertion)){  
                    this.contentOnly = false;                            
                }else if(PolicyUtil.isMessageLayout(assertion)){         
                    layout = ((Layout)assertion).getMessageLayout();     
                }else if(PolicyUtil.isProtectTokens(assertion)){         
                    this.protectToken = true;                            
                }else if(PolicyUtil.isEncryptSignature(assertion)){      
                    this.protectSignature = true;                        
                } else if(PolicyUtil.disableTimestampSigning(assertion)){
                    this.disableTimestampSigning = true;
                } else{                                    
                    if(!assertion.isOptional()){         
                        log_invalid_assertion(assertion, isServer,SymmetricBinding);   
                        fitness = AssertionFitness.HAS_UNKNOWN_ASSERTION;    
                    }                              
                }             
            }                   
            populated = true;     
        }             
        return fitness;
    }           
    
    public AssertionFitness validate(boolean isServer) {    
        return populate(isServer);       
    } 
}
