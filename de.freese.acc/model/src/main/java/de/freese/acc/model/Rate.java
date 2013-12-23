package de.freese.acc.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Thomas Freese
 */
@Entity
public class Rate implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7604009042267377154L;

	/**
	 * 
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date date = null;

	/**
	 * 
	 */
	@Id
	@GeneratedValue
	private Long id = null;
	/**
	 * 
	 */
	private float rate;

	/**
	 * Erstellt ein neues {@link Rate} Object.
	 */
	@SuppressWarnings("unused")
	private Rate()
	{
		// Für JPA
		super();
	}

	/**
	 * Erstellt ein neues {@link Rate} Object.
	 * 
	 * @param date {@link Date}
	 * @param rate float
	 */
	public Rate(final Date date, final float rate)
	{
		super();

		this.date = date;
		this.rate = rate;
	}

	/**
	 * @return {@link Date}
	 */
	public Date getDate()
	{
		return this.date;
	}

	/**
	 * @return float
	 */
	public float getRate()
	{
		return this.rate;
	}
}
