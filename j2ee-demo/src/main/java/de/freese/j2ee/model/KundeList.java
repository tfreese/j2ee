// Created: 16.12.2012
package de.freese.j2ee.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * @author Thomas Freese
 */
@XmlRootElement(name = "kundelist")
@XmlAccessorType(XmlAccessType.FIELD)
public class KundeList {
    @XmlElement(required = true)
    private List<Kunde> kunden;

    public List<Kunde> getKunden() {
        return this.kunden;
    }

    public void setKunden(final List<Kunde> kunden) {
        this.kunden = kunden;
    }
}
