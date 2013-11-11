
package org.mule.transport.legstar.test.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.legstar.test.coxb.record1 package. 
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

    private final static QName _Record1_QNAME = new QName("http://coxb.test.legstar.com/record1", "record1");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.legstar.test.coxb.record1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Record1 }
     * 
     */
    public Record1 createRecord1() {
        return new Record1();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Record1 }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://coxb.test.legstar.com/record1", name = "record1")
    public JAXBElement<Record1> createRecord1(Record1 value) {
        return new JAXBElement<Record1>(_Record1_QNAME, Record1 .class, null, value);
    }

    private final static QName _Record2_QNAME = new QName(
            "http://coxb.test.legstar.com/record2", "record2");

    /**
     * Create an instance of {@link Record2 }
     * 
     */
    public Record2 createRecord2() {
        return new Record2();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Record2 }{@code >}
     * 
     */
    @XmlElementDecl(namespace = "http://coxb.test.legstar.com/record2", name = "record2")
    public JAXBElement < Record2 > createRecord2(Record2 value) {
        return new JAXBElement < Record2 >(_Record2_QNAME, Record2.class, null,
                value);
    }
}
