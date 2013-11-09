/**
 * Created: 06.06.2012
 */

package de.freese.spring.server.ws;

import de.freese.spring.common.model.MyEmployee;
import de.freese.spring.common.model.MyHoliday;
import de.freese.spring.common.ws.IHolidayService2;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Thomas Freese
 */
// @Service
@WebService(endpointInterface = "de.freese.spring.common.ws.IHolidayService2")
public class HolidayWebServiceEndpointWS implements IHolidayService2, InitializingBean
{
	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayWebServiceEndpointWS.class);

	/**
	 * 
	 */
	private final Map<Integer, Date[]> holidays = new HashMap<>();

	/**
	 * Erstellt ein neues {@link HolidayWebServiceEndpointWS} Object.
	 */
	public HolidayWebServiceEndpointWS()
	{
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception
	{
		// Empty
	}

	/**
	 * @see de.freese.spring.common.ws.IHolidayService2#getHolidays(int)
	 */
	@SuppressWarnings("boxing")
	@Override
	public List<MyHoliday> getHolidays(final int employeeNumber)
	{
		LOGGER.info("EmplyeeNumber {}", employeeNumber);

		Date[] calendars = this.holidays.get(employeeNumber);

		if (calendars == null)
		{
			return null;
		}

		MyHoliday holiday = new MyHoliday();
		holiday.setStartDate(calendars[0]);
		holiday.setEndDate(calendars[1]);

		List<MyHoliday> response = new ArrayList<>();
		response.add(holiday);

		return response;
	}

	/**
	 * @see de.freese.spring.common.ws.IHolidayService2#setHoliday(de.freese.spring.common.model.MyHoliday, de.freese.spring.common.model.MyEmployee)
	 */
	@SuppressWarnings("boxing")
	@Override
	public void setHoliday(final MyHoliday holiday, final MyEmployee employee)
	{
		LOGGER.info("StartDate: {}", holiday.getStartDate());
		LOGGER.info("EndDate: {}", holiday.getEndDate());
		LOGGER.info("Name: {}, {}", employee.getLastName(), employee.getFirstName());
		LOGGER.info("Nummer: {}", employee.getNumber());

		this.holidays.put(employee.getNumber(), new Date[]
		{
				holiday.getStartDate(), holiday.getEndDate()
		});
	}
}
