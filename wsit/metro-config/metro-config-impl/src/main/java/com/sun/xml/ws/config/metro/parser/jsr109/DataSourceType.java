/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-28 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.08.18 at 11:59:48 AM EEST 
//


package com.sun.xml.ws.config.metro.parser.jsr109;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.sun.xml.bind.Locatable;
import com.sun.xml.bind.annotation.XmlLocation;
import org.xml.sax.Locator;


/**
 * 
 * 
 *         Configuration of a DataSource.
 *         
 *       
 * 
 * <p>Java class for data-sourceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="data-sourceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{http://java.sun.com/xml/ns/javaee}descriptionType" minOccurs="0"/>
 *         &lt;element name="name" type="{http://java.sun.com/xml/ns/javaee}jndi-nameType"/>
 *         &lt;element name="class-name" type="{http://java.sun.com/xml/ns/javaee}fully-qualified-classType" minOccurs="0"/>
 *         &lt;element name="server-name" type="{http://java.sun.com/xml/ns/javaee}string" minOccurs="0"/>
 *         &lt;element name="port-number" type="{http://java.sun.com/xml/ns/javaee}xsdIntegerType" minOccurs="0"/>
 *         &lt;element name="database-name" type="{http://java.sun.com/xml/ns/javaee}string" minOccurs="0"/>
 *         &lt;element name="url" type="{http://java.sun.com/xml/ns/javaee}jdbc-urlType" minOccurs="0"/>
 *         &lt;element name="user" type="{http://java.sun.com/xml/ns/javaee}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://java.sun.com/xml/ns/javaee}string" minOccurs="0"/>
 *         &lt;element name="property" type="{http://java.sun.com/xml/ns/javaee}propertyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="login-timeout" type="{http://java.sun.com/xml/ns/javaee}xsdIntegerType" minOccurs="0"/>
 *         &lt;element name="transactional" type="{http://java.sun.com/xml/ns/javaee}xsdBooleanType" minOccurs="0"/>
 *         &lt;element name="isolation-level" type="{http://java.sun.com/xml/ns/javaee}isolation-levelType" minOccurs="0"/>
 *         &lt;element name="initial-pool-size" type="{http://java.sun.com/xml/ns/javaee}xsdIntegerType" minOccurs="0"/>
 *         &lt;element name="max-pool-size" type="{http://java.sun.com/xml/ns/javaee}xsdIntegerType" minOccurs="0"/>
 *         &lt;element name="min-pool-size" type="{http://java.sun.com/xml/ns/javaee}xsdIntegerType" minOccurs="0"/>
 *         &lt;element name="max-idle-time" type="{http://java.sun.com/xml/ns/javaee}xsdIntegerType" minOccurs="0"/>
 *         &lt;element name="max-statements" type="{http://java.sun.com/xml/ns/javaee}xsdIntegerType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "data-sourceType", propOrder = {
    "description",
    "name",
    "className",
    "serverName",
    "portNumber",
    "databaseName",
    "url",
    "user",
    "password",
    "property",
    "loginTimeout",
    "transactional",
    "isolationLevel",
    "initialPoolSize",
    "maxPoolSize",
    "minPoolSize",
    "maxIdleTime",
    "maxStatements"
})
public class DataSourceType
    implements Locatable
{

    protected DescriptionType description;
    @XmlElement(required = true)
    protected JndiNameType name;
    @XmlElement(name = "class-name")
    protected FullyQualifiedClassType className;
    @XmlElement(name = "server-name")
    protected com.sun.xml.ws.config.metro.parser.jsr109.String serverName;
    @XmlElement(name = "port-number")
    protected XsdIntegerType portNumber;
    @XmlElement(name = "database-name")
    protected com.sun.xml.ws.config.metro.parser.jsr109.String databaseName;
    protected JdbcUrlType url;
    protected com.sun.xml.ws.config.metro.parser.jsr109.String user;
    protected com.sun.xml.ws.config.metro.parser.jsr109.String password;
    protected List<PropertyType> property;
    @XmlElement(name = "login-timeout")
    protected XsdIntegerType loginTimeout;
    protected XsdBooleanType transactional;
    @XmlElement(name = "isolation-level")
    protected IsolationLevelType isolationLevel;
    @XmlElement(name = "initial-pool-size")
    protected XsdIntegerType initialPoolSize;
    @XmlElement(name = "max-pool-size")
    protected XsdIntegerType maxPoolSize;
    @XmlElement(name = "min-pool-size")
    protected XsdIntegerType minPoolSize;
    @XmlElement(name = "max-idle-time")
    protected XsdIntegerType maxIdleTime;
    @XmlElement(name = "max-statements")
    protected XsdIntegerType maxStatements;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;
    @XmlLocation
    @XmlTransient
    protected Locator locator;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType }
     *     
     */
    public DescriptionType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType }
     *     
     */
    public void setDescription(DescriptionType value) {
        this.description = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link JndiNameType }
     *     
     */
    public JndiNameType getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link JndiNameType }
     *     
     */
    public void setName(JndiNameType value) {
        this.name = value;
    }

    /**
     * Gets the value of the className property.
     * 
     * @return
     *     possible object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public FullyQualifiedClassType getClassName() {
        return className;
    }

    /**
     * Sets the value of the className property.
     * 
     * @param value
     *     allowed object is
     *     {@link FullyQualifiedClassType }
     *     
     */
    public void setClassName(FullyQualifiedClassType value) {
        this.className = value;
    }

    /**
     * Gets the value of the serverName property.
     * 
     * @return
     *     possible object is
     *     {@link com.sun.xml.ws.config.metro.parser.jsr109.String }
     *     
     */
    public com.sun.xml.ws.config.metro.parser.jsr109.String getServerName() {
        return serverName;
    }

    /**
     * Sets the value of the serverName property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.sun.xml.ws.config.metro.parser.jsr109.String }
     *     
     */
    public void setServerName(com.sun.xml.ws.config.metro.parser.jsr109.String value) {
        this.serverName = value;
    }

    /**
     * Gets the value of the portNumber property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getPortNumber() {
        return portNumber;
    }

    /**
     * Sets the value of the portNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setPortNumber(XsdIntegerType value) {
        this.portNumber = value;
    }

    /**
     * Gets the value of the databaseName property.
     * 
     * @return
     *     possible object is
     *     {@link com.sun.xml.ws.config.metro.parser.jsr109.String }
     *     
     */
    public com.sun.xml.ws.config.metro.parser.jsr109.String getDatabaseName() {
        return databaseName;
    }

    /**
     * Sets the value of the databaseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.sun.xml.ws.config.metro.parser.jsr109.String }
     *     
     */
    public void setDatabaseName(com.sun.xml.ws.config.metro.parser.jsr109.String value) {
        this.databaseName = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link JdbcUrlType }
     *     
     */
    public JdbcUrlType getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link JdbcUrlType }
     *     
     */
    public void setUrl(JdbcUrlType value) {
        this.url = value;
    }

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link com.sun.xml.ws.config.metro.parser.jsr109.String }
     *     
     */
    public com.sun.xml.ws.config.metro.parser.jsr109.String getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.sun.xml.ws.config.metro.parser.jsr109.String }
     *     
     */
    public void setUser(com.sun.xml.ws.config.metro.parser.jsr109.String value) {
        this.user = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link com.sun.xml.ws.config.metro.parser.jsr109.String }
     *     
     */
    public com.sun.xml.ws.config.metro.parser.jsr109.String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.sun.xml.ws.config.metro.parser.jsr109.String }
     *     
     */
    public void setPassword(com.sun.xml.ws.config.metro.parser.jsr109.String value) {
        this.password = value;
    }

    /**
     * Gets the value of the property property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyType }
     * 
     * 
     */
    public List<PropertyType> getProperty() {
        if (property == null) {
            property = new ArrayList<PropertyType>();
        }
        return this.property;
    }

    /**
     * Gets the value of the loginTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getLoginTimeout() {
        return loginTimeout;
    }

    /**
     * Sets the value of the loginTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setLoginTimeout(XsdIntegerType value) {
        this.loginTimeout = value;
    }

    /**
     * Gets the value of the transactional property.
     * 
     * @return
     *     possible object is
     *     {@link XsdBooleanType }
     *     
     */
    public XsdBooleanType getTransactional() {
        return transactional;
    }

    /**
     * Sets the value of the transactional property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdBooleanType }
     *     
     */
    public void setTransactional(XsdBooleanType value) {
        this.transactional = value;
    }

    /**
     * Gets the value of the isolationLevel property.
     * 
     * @return
     *     possible object is
     *     {@link IsolationLevelType }
     *     
     */
    public IsolationLevelType getIsolationLevel() {
        return isolationLevel;
    }

    /**
     * Sets the value of the isolationLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link IsolationLevelType }
     *     
     */
    public void setIsolationLevel(IsolationLevelType value) {
        this.isolationLevel = value;
    }

    /**
     * Gets the value of the initialPoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getInitialPoolSize() {
        return initialPoolSize;
    }

    /**
     * Sets the value of the initialPoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setInitialPoolSize(XsdIntegerType value) {
        this.initialPoolSize = value;
    }

    /**
     * Gets the value of the maxPoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
     * Sets the value of the maxPoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setMaxPoolSize(XsdIntegerType value) {
        this.maxPoolSize = value;
    }

    /**
     * Gets the value of the minPoolSize property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getMinPoolSize() {
        return minPoolSize;
    }

    /**
     * Sets the value of the minPoolSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setMinPoolSize(XsdIntegerType value) {
        this.minPoolSize = value;
    }

    /**
     * Gets the value of the maxIdleTime property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getMaxIdleTime() {
        return maxIdleTime;
    }

    /**
     * Sets the value of the maxIdleTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setMaxIdleTime(XsdIntegerType value) {
        this.maxIdleTime = value;
    }

    /**
     * Gets the value of the maxStatements property.
     * 
     * @return
     *     possible object is
     *     {@link XsdIntegerType }
     *     
     */
    public XsdIntegerType getMaxStatements() {
        return maxStatements;
    }

    /**
     * Sets the value of the maxStatements property.
     * 
     * @param value
     *     allowed object is
     *     {@link XsdIntegerType }
     *     
     */
    public void setMaxStatements(XsdIntegerType value) {
        this.maxStatements = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

    public Locator sourceLocation() {
        return locator;
    }

    public void setSourceLocation(Locator newLocator) {
        locator = newLocator;
    }

}
