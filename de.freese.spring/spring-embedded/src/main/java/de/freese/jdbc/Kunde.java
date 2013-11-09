/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package de.freese.jdbc;

/**
 * Entity = Persistentes Fachobjekt.
 * 
 * @author Thomas Freese
 */
public class Kunde
{
	/**
     *
     */
	private String nachname = null;

	/**
	 * PrimaryKey aus der Datenbank.
	 */
	private long pk = 0;

	/**
     *
     */
	private String vorname = null;

	/**
     *
     */
	public Kunde()
	{
		super();
	}

	/**
	 * @return String
	 */
	public String getNachName()
	{
		return this.nachname;
	}

	/**
	 * PrimaryKey aus der Datenbank.
	 * 
	 * @return long
	 */
	public long getPk()
	{
		return this.pk;
	}

	/**
	 * @return String
	 */
	public String getVorname()
	{
		return this.vorname;
	}

	/**
	 * @param nachname String
	 */
	public void setNachName(final String nachname)
	{
		this.nachname = nachname;
	}

	/**
	 * PrimaryKey aus der Datenbank.
	 * 
	 * @param pk long
	 */
	public void setPk(final long pk)
	{
		this.pk = pk;
	}

	/**
	 * @param vorname String
	 */
	public void setVorname(final String vorname)
	{
		this.vorname = vorname;
	}

	/**
	 * @return String
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Kunde(").append(this.pk).append("):");
		sb.append("nachname=").append(this.nachname).append(",");
		sb.append("vorname=").append(this.vorname);

		return sb.toString();
	}
}
