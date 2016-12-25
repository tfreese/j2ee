/**
 * Created: 09.06.2012
 */

package de.freese.spring.common.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Thomas Freese
 */
@XmlRootElement(name = "employee")
public class MyEmployee implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 8704223817530184809L;

	/**
	 * 
	 */
	private String firstName = null;

	/**
	 * 
	 */
	private String lastName = null;

	/**
	 * 
	 */
	@XmlAttribute
	private int number = -1;

	/**
	 * Erstellt ein neues {@link MyEmployee} Object.
	 */
	public MyEmployee()
	{
		super();
	}

	/**
	 * @return String
	 */
	public String getFirstName()
	{
		return this.firstName;
	}

	/**
	 * @return String
	 */
	public String getLastName()
	{
		return this.lastName;
	}

	/**
	 * @return int
	 */
	public int getNumber()
	{
		return this.number;
	}

	/**
	 * @param firstName String
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @param lastName String
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @param number int
	 */
	public void setNumber(final int number)
	{
		this.number = number;
	}
}
