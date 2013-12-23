package de.freese.acc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * @author Thomas Freese
 */
@Entity
@NamedQuery(name = "mailAddressUsed", query = "SELECT COUNT(d) FROM Depot d WHERE d.mailAddress = :mailAddress")
public class Depot implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1338391269687398555L;

	/**
	 * 
	 */
	private BigDecimal balance = null;

	/**
	 * 
	 */
	@Id
	@GeneratedValue
	private Long id = null;
	/**
	 * 
	 */
	private String mailAddress = null;

	/**
	 * 
	 */
	@OneToMany(cascade =
	{
			CascadeType.PERSIST, CascadeType.MERGE
	})
	private List<Order> orders = new ArrayList<Order>();

	/**
	 * 
	 */
	private String password = null;

	/**
	 * Erstellt ein neues {@link Depot} Object.
	 */
	@SuppressWarnings("unused")
	private Depot()
	{
		// Für JPA
		super();
	}

	/**
	 * Erstellt ein neues {@link Depot} Object.
	 * 
	 * @param mailAddress String
	 * @param password String
	 */
	public Depot(final String mailAddress, final String password)
	{
		super();

		this.mailAddress = mailAddress;
		this.password = password;
		this.balance = new BigDecimal("25000");
	}

	/**
	 * @param securityToBuy {@link Security}
	 * @param count int
	 */
	public void addBuyOrder(final Security securityToBuy, final int count)
	{
		this.orders.add(new Order(securityToBuy, count, securityToBuy.getLatestRate()));
		BigDecimal rate = new BigDecimal(Float.toString(securityToBuy.getLatestRate().getRate()));
		BigDecimal orderValue = rate.multiply(new BigDecimal(Integer.toString(count)));
		this.balance = this.balance.subtract(orderValue);
	}

	/**
	 * @return {@link BigDecimal}
	 */
	public BigDecimal getBalance()
	{
		return this.balance;
	}

	/**
	 * @return String
	 */
	public String getMailAddress()
	{
		return this.mailAddress;
	}

	/**
	 * @return String
	 */
	public String getPassword()
	{
		return this.password;
	}
}
