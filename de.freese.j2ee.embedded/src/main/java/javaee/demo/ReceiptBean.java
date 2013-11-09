/**
 * 06.11.2013
 */
package javaee.demo;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * @author Thomas Freese (AUEL)
 */
@ManagedBean(name = "receipt")
@SessionScoped
public class ReceiptBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7650194762920216861L;

	/**
	 * 
	 */
	private Date date = null;

	/**
	 * Erstellt ein neues {@link ReceiptBean} Objekt.
	 */
	public ReceiptBean()
	{
		super();
	}

	/**
	 * @return {@link Date}
	 */
	public Date getDate()
	{
		return this.date;
	}

	/**
	 * @param date {@link Date}
	 */
	public void setDate(final Date date)
	{
		this.date = date;
	}
}
