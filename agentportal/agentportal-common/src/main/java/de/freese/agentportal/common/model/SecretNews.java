// Created: 16.12.2012
package de.freese.agentportal.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "SECRET_NEWS", uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE"}))
@NamedQuery(name = "selectAllSecretNews", query = "SELECT sne FROM SecretNews sne WHERE sne.securityLevel <= :securityLevel ORDER BY sne.timestamp DESC")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"securityLevel", "title", "timestamp", "text"})
public class SecretNews implements Serializable {
    public static final int SECURITY_LEVEL_HIGH = 2;

    public static final int SECURITY_LEVEL_LOW = 1;

    @Serial
    private static final long serialVersionUID = -5472649703030734238L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "seq_news", initialValue = 1, allocationSize = 1)
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private Long id;

    @Column(name = "LEVEL", nullable = false)
    @NotNull
    private int securityLevel = SecretNews.SECURITY_LEVEL_LOW;

    @Column(name = "TEXT", nullable = false)
    @Size(min = 10)
    private String text;

    @Column(name = "TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @XmlElement(required = false)
    private Date timestamp;

    @Column(name = "TITLE", nullable = false)
    @Size(min = 5)
    private String title;

    public Long getId() {
        return this.id;
    }

    public int getSecurityLevel() {
        return this.securityLevel;
    }

    public String getText() {
        return this.text;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public String getTitle() {
        return this.title;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setSecurityLevel(final int securityLevel) {
        this.securityLevel = securityLevel;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SecretNewsEntity [id=");
        builder.append(this.id);
        builder.append(", securityLevel=");
        builder.append(this.securityLevel);
        builder.append(", title=");
        builder.append(this.title);
        builder.append(", timestamp=");
        builder.append(this.timestamp);
        builder.append(", text=");
        builder.append(this.text);
        builder.append("]");

        return builder.toString();
    }
}
