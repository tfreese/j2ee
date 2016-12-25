/**
 * Created: 06.06.2012
 */

package de.freese.webservice.client;

import de.freese.spring.common.model.EmployeeType;
import de.freese.spring.common.model.HolidayType;
import de.freese.spring.common.model.ListHolidayRequest;
import de.freese.spring.common.model.ListHolidayResponse;
import de.freese.spring.common.model.MyEmployee;
import de.freese.spring.common.model.MyHoliday;
import de.freese.spring.common.model.SaveHolidayRequest;
import de.freese.spring.common.ws.IHolidayService;
import de.freese.spring.common.ws.IHolidayService2;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Thomas Freese
 */
public class WebServiceClient
{
	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceClient.class);

	/**
	 * @param applicationContext {@link ApplicationContext}
	 * @param beanID String
	 * @throws Exception Falls was schief geht.
	 */
	private static void jaxRPC(final ApplicationContext applicationContext, final String beanID) throws Exception
	{
		IHolidayService holidayService = applicationContext.getBean(beanID, IHolidayService.class);

		// Speichern.
		DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

		HolidayType holiday = new HolidayType();
		holiday.setStartDate(datatypeFactory.newXMLGregorianCalendarDate(2012, 07, 03, 0));
		holiday.setEndDate(datatypeFactory.newXMLGregorianCalendarDate(2012, 07, 07, 0));

		EmployeeType employee = new EmployeeType();
		employee.setNumber(BigInteger.valueOf(42));
		employee.setLastName("Freese");
		employee.setFirstName("Thomas");

		SaveHolidayRequest saveHolidayRequest = new SaveHolidayRequest();
		saveHolidayRequest.setHoliday(holiday);
		saveHolidayRequest.setEmployee(employee);

		holidayService.setHoliday(saveHolidayRequest);

		// Laden.
		ListHolidayRequest listHolidayRequest = new ListHolidayRequest();
		listHolidayRequest.setEmployeeNumber(BigInteger.valueOf(42));
		ListHolidayResponse response = holidayService.getHolidays(listHolidayRequest);

		for (HolidayType h : response.getHolyday())
		{
			LOGGER.info("StartDate: {}", h.getStartDate());
			LOGGER.info("EndDate: {}", h.getEndDate());
		}
	}

	/**
	 * @param applicationContext {@link ApplicationContext}
	 * @param beanID String
	 * @throws Exception Falls was schief geht.
	 */
	private static void jaxWS(final ApplicationContext applicationContext, final String beanID) throws Exception
	{
		IHolidayService2 holidayService = applicationContext.getBean(beanID, IHolidayService2.class);

		MyHoliday holiday = new MyHoliday();
		holiday.setStartDate(new Date());
		holiday.setEndDate(new Date(System.currentTimeMillis() + 86400000L));

		MyEmployee employee = new MyEmployee();
		employee.setNumber(42);
		employee.setLastName("Freese");
		employee.setFirstName("Thomas");

		holidayService.setHoliday(holiday, employee);

		List<MyHoliday> holidays = holidayService.getHolidays(42);

		for (MyHoliday h : holidays)
		{
			LOGGER.info("StartDate: {}", h.getStartDate());
			LOGGER.info("EndDate: {}", h.getEndDate());
		}
	}

	/**
	 * @param args String[]
	 * @throws Exception Falls was schief geht.
	 */
	public static void main(final String[] args) throws Exception
	{
		AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]
		{
			"springContext.xml"
		});
		applicationContext.registerShutdownHook();

		jaxRPC(applicationContext, "holidayWebService");
		jaxWS(applicationContext, "holidayWebService2");
		jaxWS(applicationContext, "holidayWebService21");
		jaxWS(applicationContext, "holidayWebService22");
	}

	/**
	 * Erstellt ein neues {@link WebServiceClient} Object.
	 */
	public WebServiceClient()
	{
		super();
	}
}
