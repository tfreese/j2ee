// Created: 16.12.2012
package de.freese.agentportal.common.model;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * @author Thomas Freese
 */
@XmlRootElement(name = "newslist")
@XmlAccessorType(XmlAccessType.FIELD)
public class SecretNewsList
{
    @XmlElement(required = true)
    private List<SecretNews> news;

    public List<SecretNews> getNews()
    {
        return this.news;
    }

    public void setNews(final List<SecretNews> news)
    {
        this.news = news;
    }
}
