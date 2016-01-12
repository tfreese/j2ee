/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the template in
 * the editor.
 */
package de.efreest.web.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Freese
 */
@ManagedBean(name = "pageController")
@RequestScoped
//@Controller("pageController")
//@Scope("request")
public class PageController implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private static final Logger LOGGER = LoggerFactory.getLogger("PageController");

    /**
     * Wird durch f:param gesetzt.
     */
    @ManagedProperty(value = "#{param.pageID}")
    private String pageID = null;

    /**
     * Erstellt ein neues {@link PageController} Object.
     */
    public PageController()
    {
        super();

        LOGGER.info("create PageController");
    }

    /**
     * Wird durch f:param gesetzt.
     * <p>
     *
     * @return String
     */
    public String getPageID()
    {
        return this.pageID;
    }

    /**
     * Outcome für NavigationRule.
     * <p>
     *
     * @return String
     */
    public String nextPage1()
    {
        return showPage();
    }

    /**
     * Wird durch f:param gesetzt.
     * <p>
     *
     * @param pageID String
     */
    public void setPageID(final String pageID)
    {
        this.pageID = pageID;
    }

    /**
     * Liefert outcome in Abhängigkeit von pageID, benötigt navigation-rule in faces.config.<br>
     * Oder den Namen der xhtml-Datei für Implizite Navigation liefern.
     * <p>
     *
     * @return String
     */
    public String showPage()
    {
        LOGGER.info("showPage");

        if ("1".equals(this.pageID))
        {
            return "page1";
        }
        else if ("2".equals(this.pageID))
        {
            return "page2";
        }

        return "nextPage";
    }
}
