//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.02 at 12:19:29 PM MESZ 
//


package de.freese.spring.common.model;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.freese.spring.common.model.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.freese.spring.common.model.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HolidayType }
     * 
     */
    public HolidayType createHolidayType() {
        return new HolidayType();
    }

    /**
     * Create an instance of {@link ListHolidayResponse }
     * 
     */
    public ListHolidayResponse createListHolidayResponse() {
        return new ListHolidayResponse();
    }

    /**
     * Create an instance of {@link ListHolidayRequest }
     * 
     */
    public ListHolidayRequest createListHolidayRequest() {
        return new ListHolidayRequest();
    }

    /**
     * Create an instance of {@link SaveHolidayRequest }
     * 
     */
    public SaveHolidayRequest createSaveHolidayRequest() {
        return new SaveHolidayRequest();
    }

    /**
     * Create an instance of {@link EmployeeType }
     * 
     */
    public EmployeeType createEmployeeType() {
        return new EmployeeType();
    }

}
