package de.freese.acc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * @author Thomas Freese
 */
@Entity
@NamedQueries(
{
		@NamedQuery(name = "findAllSecurities", query = "SELECT s FROM Security s"),
		@NamedQuery(name = "countSecurities", query = "SELECT count(s) FROM Security s")
})
public class Security implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 2600574439256874153L;

	/**
	 * 
	 */
	@Id
	@GeneratedValue
	private Long id = null;

	/**
	 * 
	 */
	private String isin = null;

	/**
	 * 
	 */
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	private List<Rate> rates = new ArrayList<Rate>();

	/**
	 * Erstellt ein neues {@link Security} Object.
	 */
	public Security()
	{
		super();
	}

	/**
	 * @return Long
	 */
	public Long getId()
	{
		return this.id;
	}

	/**
	 * @return String
	 */
	public String getIsin()
	{
		return this.isin;
	}

	/**
	 * @return {@link Rate}
	 */
	public Rate getLatestRate()
	{
		if ((this.rates == null) || (this.rates.size() == 0))
		{
			return null;
		}
		return this.rates.get(this.rates.size() - 1);
	}

	/**
	 * @return {@link List}
	 */
	public List<Rate> getRates()
	{
		return this.rates;
	}

	/**
	 * @param isin String
	 */
	public void setIsin(final String isin)
	{
		this.isin = isin;
	}

	/**
	 * @param rates {@link List}
	 */
	public void setRates(final List<Rate> rates)
	{
		this.rates = rates;
	}
}
