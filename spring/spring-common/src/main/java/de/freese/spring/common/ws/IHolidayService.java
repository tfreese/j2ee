/**
 * Created: 08.06.2012
 */

package de.freese.spring.common.ws;

import de.freese.spring.common.model.ListHolidayRequest;
import de.freese.spring.common.model.ListHolidayResponse;
import de.freese.spring.common.model.SaveHolidayRequest;

/**
 * @author Thomas Freese
 */
public interface IHolidayService
{
	/**
	 * 
	 */
	public static final String NAMESPACE = "http://mycompany.com/hr/schemas";

	/**
	 * @param request {@link ListHolidayRequest}
	 * @return {@link ListHolidayResponse}
	 */
	public ListHolidayResponse getHolidays(ListHolidayRequest request);

	/**
	 * @param request {@link SaveHolidayRequest}
	 */
	public void setHoliday(SaveHolidayRequest request);
}
