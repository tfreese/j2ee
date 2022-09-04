// Created: 16.12.2012
package de.freese.agentportal.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "SECRET_NEWS", uniqueConstraints = @UniqueConstraint(columnNames =
        {
                "TITLE"
        }))
@NamedQuery(name = "selectAllSecretNews", query = "SELECT sne FROM SecretNews sne WHERE sne.securityLevel <= :securityLevel ORDER BY sne.timestamp DESC")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder =
        {
                "securityLevel", "title", "timestamp", "text"
        })
public class SecretNews implements Serializable
{
    /**
     *
     */
    public static final int SECURITY_LEVEL_HIGH = 2;
    /**
     *
     */
    public static final int SECURITY_LEVEL_LOW = 1;
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -5472649703030734238L;
    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @SequenceGenerator(name = "seq", sequenceName = "seq_news", initialValue = 1, allocationSize = 1)
    @Column(name = "ID", nullable = false)
    @XmlAttribute
    private Long id;
    /**
     *
     */
    @Column(name = "LEVEL", nullable = false)
    @NotNull
    private int securityLevel = SecretNews.SECURITY_LEVEL_LOW;
    /**
     *
     */
    @Column(name = "TEXT", nullable = false)
    @Size(min = 10)
    private String text;
    /**
     *
     */
    @Column(name = "TIMESTAMP", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @XmlElement(required = false)
    private Date timestamp;
    /**
     *
     */
    @Column(name = "TITLE", nullable = false)
    @Size(min = 5)
    private String title;

    /**
     * @return Long
     */
    public Long getId()
    {
        return this.id;
    }

    /**
     * @return int
     */
    public int getSecurityLevel()
    {
        return this.securityLevel;
    }

    /**
     * @return String
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * @return {@link Date}
     */
    public Date getTimestamp()
    {
        return this.timestamp;
    }

    /**
     * @return String
     */
    public String getTitle()
    {
        return this.title;
    }

    /**
     * @param id Long
     */
    public void setId(final Long id)
    {
        this.id = id;
    }

    /**
     * @param securityLevel int
     */
    public void setSecurityLevel(final int securityLevel)
    {
        this.securityLevel = securityLevel;
    }

    /**
     * @param text String
     */
    public void setText(final String text)
    {
        this.text = text;
    }

    /**
     * @param timestamp {@link Date}
     */
    public void setTimestamp(final Date timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * @param title String
     */
    public void setTitle(final String title)
    {
        this.title = title;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
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
