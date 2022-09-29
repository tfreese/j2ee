// Created: 16.12.2012
package de.freese.agentportal.common.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Thomas Freese
 */
@XmlRootElement(name = "newslist")
@XmlAccessorType(XmlAccessType.FIELD)
public class SecretNewsList
{
    /**
     *
     */
    @XmlElement(required = true)
    private List<SecretNews> news;

    /**
     * @return {@link List}
     */
    public List<SecretNews> getNews()
    {
        return this.news;
    }

    /**
     * @param news {@link List}
     */
    public void setNews(final List<SecretNews> news)
    {
        this.news = news;
    }
}
