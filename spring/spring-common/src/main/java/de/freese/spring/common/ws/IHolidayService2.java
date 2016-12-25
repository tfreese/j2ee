/**
 * Created: 08.06.2012
 */

package de.freese.spring.common.ws;

import de.freese.spring.common.model.MyEmployee;
import de.freese.spring.common.model.MyHoliday;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author Thomas Freese
 */
@WebService
public interface IHolidayService2
{
	/**
	 * WebParam definiert Namen in WSDL.
	 * 
	 * @param employeeNumber int
	 * @return {@link List}
	 */
	@WebMethod
	public List<MyHoliday> getHolidays(@WebParam(name = "employeeNumber") int employeeNumber);

	/**
	 * WebParam definiert Namen in WSDL.
	 * 
	 * @param holiday {@link MyHoliday}
	 * @param employee {@link MyEmployee}
	 */
	@WebMethod
	public void setHoliday(@WebParam(name = "holiday") MyHoliday holiday, @WebParam(name = "employee") MyEmployee employee);
}
