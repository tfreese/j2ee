package de.freese.agentportal.generated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java element interface generated in the de.freese.agentportal.generated package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the Java representation for XML content. The Java representation of XML content can
 * consist of schema derived interfaces and classes representing the binding of schema type definitions, element declarations and model groups. Factory methods
 * for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory
{
    /**
     *
     */
    private final static QName _SecretNewsEntity_QNAME = new QName("http://ws.server.agentportal.freese.de/", "secretNewsEntity");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.freese.agentportal.generated
     */
    public ObjectFactory()
    {
        super();
    }

    /**
     * Create an instance of {@link SecretNewsEntity }
     *
     * @return {@link SecretNewsEntity}
     */
    public SecretNewsEntity createSecretNewsEntity()
    {
        return new SecretNewsEntity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecretNewsEntity }{@code >}
     *
     * @param value {@link SecretNewsEntity}
     * @return {@link JAXBElement}
     */
    @XmlElementDecl(namespace = "http://ws.server.agentportal.freese.de/", name = "secretNewsEntity")
    public JAXBElement<SecretNewsEntity> createSecretNewsEntity(final SecretNewsEntity value)
    {
        return new JAXBElement<>(_SecretNewsEntity_QNAME, SecretNewsEntity.class, null, value);
    }

    /**
     * Create an instance of {@link SecretNewsEntityArray }
     *
     * @return {@link SecretNewsEntityArray}
     */
    public SecretNewsEntityArray createSecretNewsEntityArray()
    {
        return new SecretNewsEntityArray();
    }

}
