package de.freese.j2ee.cdi;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Max;

/**
 * @author Thomas Freese
 */
@Named
@RequestScoped
public class FakultaetView
{
	/**
	 * 
	 */
	private long fakt = 0;

	/**
	 * 
	 */
	@Inject
	@Recursive
	private IFakultaet fakultaet = null;

	/**
	 * 
	 */
	@Max(5)
	private int n = 0;

	/**
	 * Erstellt ein neues {@link FakultaetView} Object.
	 */
	public FakultaetView()
	{
		super();
	}

	/**
	 * @return String
	 */
	public String calculate()
	{
		System.out.println("Test " + getN());
		setFakt(this.fakultaet.getFakultaet(getN()));

		return null;
	}

	/**
	 * @return return
	 */
	public long getFakt()
	{
		return this.fakt;
	}

	/**
	 * @return int
	 */
	public int getN()
	{
		return this.n;
	}

	/**
	 * @param fakt long
	 */
	public void setFakt(final long fakt)
	{
		this.fakt = fakt;
	}

	/**
	 * @param n int
	 */
	public void setN(final int n)
	{
		this.n = n;
	}
}
