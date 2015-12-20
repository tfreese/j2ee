/**
 * Created: 06.06.2012
 */

package de.freese.spring.server.ws;

import de.freese.spring.common.model.EmployeeType;
import de.freese.spring.common.model.HolidayType;
import de.freese.spring.common.model.ListHolidayRequest;
import de.freese.spring.common.model.ListHolidayResponse;
import de.freese.spring.common.model.SaveHolidayRequest;
import de.freese.spring.common.ws.IHolidayService;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import javax.xml.datatype.XMLGregorianCalendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 * @author Thomas Freese
 */
@Endpoint
public class HolidayWebServiceEndpointRPC implements IHolidayService, InitializingBean
{
	/** 
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HolidayWebServiceEndpointRPC.class);

	/**
	 * 
	 */
	private final Map<BigInteger, XMLGregorianCalendar[]> holidays = new HashMap<>();

	/**
	 * Erstellt ein neues {@link HolidayWebServiceEndpointRPC} Object.
	 */
	public HolidayWebServiceEndpointRPC()
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
	 * @see de.freese.spring.common.ws.IHolidayService#getHolidays(de.freese.spring.common.model.ListHolidayRequest)
	 */
	@Override
	@PayloadRoot(namespace = NAMESPACE, localPart = "ListHoliday-Request")
	@ResponsePayload
	public ListHolidayResponse getHolidays(final ListHolidayRequest request)
	{
		LOGGER.info("EmplyeeNumber {}", request.getEmployeeNumber());

		XMLGregorianCalendar[] calendars = this.holidays.get(request.getEmployeeNumber());

		if (calendars == null)
		{
			return null;
		}

		HolidayType holiday = new HolidayType();
		holiday.setStartDate(calendars[0]);
		holiday.setEndDate(calendars[1]);

		ListHolidayResponse response = new ListHolidayResponse();
		response.getHolyday().add(holiday);

		return response;
	}

	/**
	 * @see de.freese.spring.common.ws.IHolidayService#setHoliday(de.freese.spring.common.model.SaveHolidayRequest)
	 */
	@Override
	@PayloadRoot(namespace = NAMESPACE, localPart = "SaveHoliday-Request")
	public void setHoliday(final SaveHolidayRequest request)
	{
		EmployeeType employee = request.getEmployee();
		HolidayType holiday = request.getHoliday();

		LOGGER.info("StartDate: {}", holiday.getStartDate());
		LOGGER.info("EndDate: {}", holiday.getEndDate());
		LOGGER.info("Name: {}, {}", employee.getLastName(), employee.getFirstName());
		LOGGER.info("Nummer: {}", employee.getNumber());

		this.holidays.put(employee.getNumber(), new XMLGregorianCalendar[]
		{
				holiday.getStartDate(), holiday.getEndDate()
		});
	}
}
