/**
 * Created: 09.06.2012
 */

package de.freese.spring.common.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Thomas Freese
 */
public class MyHoliday implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = -7586372866214813052L;

	/**
	 * 
	 */
	private Date startDate = null;

	/**
	 * 
	 */
	private Date endDate = null;

	/**
	 * Erstellt ein neues {@link MyHoliday} Object.
	 */
	public MyHoliday()
	{
		super();
	}

	/**
	 * @return {@link Date}
	 */
	public Date getEndDate()
	{
		return this.endDate;
	}

	/**
	 * @return {@link Date}
	 */
	public Date getStartDate()
	{
		return this.startDate;
	}

	/**
	 * @param endDate {@link Date}
	 */
	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @param startDate {@link Date}
	 */
	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}
}
