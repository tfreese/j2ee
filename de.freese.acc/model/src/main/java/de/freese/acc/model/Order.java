package de.freese.acc.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Thomas Freese
 */
@Entity
@Table(name = "ORDERS")
public class Order implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 7684461465477042111L;

	/**
	 * 
	 */
	private int count = 0;

	/**
	 * 
	 */
	@Id
	@GeneratedValue
	private Long id = null;
	/**
	 * 
	 */
	private Rate rate = null;
	/**
	 * 
	 */
	private Security security = null;

	/**
	 * Erstellt ein neues {@link Order} Object.
	 */
	@SuppressWarnings("unused")
	private Order()
	{
		// Für JPA
		super();
	}

	/**
	 * Erstellt ein neues {@link Order} Object.
	 * 
	 * @param security {@link Security}
	 * @param count int
	 * @param rate {@link Rate}
	 */
	public Order(final Security security, final int count, final Rate rate)
	{
		super();

		this.security = security;
		this.count = count;
		this.rate = rate;
	}

	/**
	 * @return int
	 */
	public int getCount()
	{
		return this.count;
	}

	/**
	 * @return {@link Rate}
	 */
	public Rate getRate()
	{
		return this.rate;
	}

	/**
	 * @return {@link Security}
	 */
	public Security getSecurity()
	{
		return this.security;
	}
}
