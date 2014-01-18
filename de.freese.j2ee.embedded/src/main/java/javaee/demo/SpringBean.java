/**
 * 
 */
package javaee.demo;

import java.io.Serializable;

/**
 * @author Thomas Freese
 */
public class SpringBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4200733042440646149L;

	/**
	 * 
	 */
	private String value = null;

	/**
	 * 
	 */
	public SpringBean()
	{
		super();
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		if (this.value == null)
		{
			this.value = getClass().getName();

		}

		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(final String value)
	{
		this.value = value;
	}

	// FacesContext fc = FacesContext.getCurrentInstance();
	// Application a = fc.getApplication();
	// if (a.getProjectStage() == ProjectStage.Development) {
	// // Beliebiger Code
	// }
	// if (fc.isProjectStage(ProjectStage.Development)) {
	// // Beliebiger Code
	// }
}
