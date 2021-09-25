// Created: 16.12.2012
package de.freese.j2ee.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Thomas Freese
 */
@XmlRootElement(name = "kundelist")
@XmlAccessorType(XmlAccessType.FIELD)
public class KundeList
{
    /**
     *
     */
    @XmlElement(required = true)
    private List<Kunde> kunden;

    /**
     * @return {@link List}<Kunde>
     */
    public List<Kunde> getKunden()
    {
        return this.kunden;
    }

    /**
     * @param kunden {@link List}<Kunde>
     */
    public void setKunden(final List<Kunde> kunden)
    {
        this.kunden = kunden;
    }
}
