//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.03 at 09:23:03 AM COT 
//


package org.nianet.plexil.plexilxml2maude.jaxbmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}NodeList"/>
 *         &lt;element ref="{}Command"/>
 *         &lt;element ref="{}Assignment"/>
 *         &lt;element ref="{}FunctionCall"/>
 *         &lt;element ref="{}Update"/>
 *         &lt;element ref="{}Request"/>
 *         &lt;element ref="{}LibraryNodeCall"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nodeList",
    "command",
    "assignment",
    "functionCall",
    "update",
    "request",
    "libraryNodeCall"
})
@XmlRootElement(name = "NodeBody")
public class NodeBody {

    @XmlElement(name = "NodeList")
    protected NodeList nodeList;
    @XmlElement(name = "Command")
    protected Command command;
    @XmlElement(name = "Assignment")
    protected Assignment assignment;
    @XmlElement(name = "FunctionCall")
    protected FunctionCall functionCall;
    @XmlElement(name = "Update")
    protected Update update;
    @XmlElement(name = "Request")
    protected Request request;
    @XmlElement(name = "LibraryNodeCall")
    protected LibraryNodeCall libraryNodeCall;

    /**
     * Gets the value of the nodeList property.
     * 
     * @return
     *     possible object is
     *     {@link NodeList }
     *     
     */
    public NodeList getNodeList() {
        return nodeList;
    }

    /**
     * Sets the value of the nodeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link NodeList }
     *     
     */
    public void setNodeList(NodeList value) {
        this.nodeList = value;
    }

    /**
     * Gets the value of the command property.
     * 
     * @return
     *     possible object is
     *     {@link Command }
     *     
     */
    public Command getCommand() {
        return command;
    }

    /**
     * Sets the value of the command property.
     * 
     * @param value
     *     allowed object is
     *     {@link Command }
     *     
     */
    public void setCommand(Command value) {
        this.command = value;
    }

    /**
     * Gets the value of the assignment property.
     * 
     * @return
     *     possible object is
     *     {@link Assignment }
     *     
     */
    public Assignment getAssignment() {
        return assignment;
    }

    /**
     * Sets the value of the assignment property.
     * 
     * @param value
     *     allowed object is
     *     {@link Assignment }
     *     
     */
    public void setAssignment(Assignment value) {
        this.assignment = value;
    }

    /**
     * Gets the value of the functionCall property.
     * 
     * @return
     *     possible object is
     *     {@link FunctionCall }
     *     
     */
    public FunctionCall getFunctionCall() {
        return functionCall;
    }

    /**
     * Sets the value of the functionCall property.
     * 
     * @param value
     *     allowed object is
     *     {@link FunctionCall }
     *     
     */
    public void setFunctionCall(FunctionCall value) {
        this.functionCall = value;
    }

    /**
     * Gets the value of the update property.
     * 
     * @return
     *     possible object is
     *     {@link Update }
     *     
     */
    public Update getUpdate() {
        return update;
    }

    /**
     * Sets the value of the update property.
     * 
     * @param value
     *     allowed object is
     *     {@link Update }
     *     
     */
    public void setUpdate(Update value) {
        this.update = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link Request }
     *     
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request }
     *     
     */
    public void setRequest(Request value) {
        this.request = value;
    }

    /**
     * Gets the value of the libraryNodeCall property.
     * 
     * @return
     *     possible object is
     *     {@link LibraryNodeCall }
     *     
     */
    public LibraryNodeCall getLibraryNodeCall() {
        return libraryNodeCall;
    }

    /**
     * Sets the value of the libraryNodeCall property.
     * 
     * @param value
     *     allowed object is
     *     {@link LibraryNodeCall }
     *     
     */
    public void setLibraryNodeCall(LibraryNodeCall value) {
        this.libraryNodeCall = value;
    }

}