/**
 * Created: 08.06.2012
 */

package de.freese.webservice.client;

import de.freese.spring.common.model.ListHolidayRequest;
import de.freese.spring.common.model.ListHolidayResponse;
import de.freese.spring.common.model.SaveHolidayRequest;
import de.freese.spring.common.ws.IHolidayService;
import org.springframework.ws.client.core.WebServiceTemplate;

/**
 * @author Thomas Freese
 */
public class HolidayWebService implements IHolidayService
{
	/**
	 *  
	 */
	private WebServiceTemplate webServiceTemplate = null;

	/**
	 * Erstellt ein neues {@link HolidayWebService} Object.
	 */
	public HolidayWebService()
	{
		super();
	}

	/**
	 * @see de.freese.spring.common.ws.IHolidayService#getHolidays(de.freese.spring.common.model.ListHolidayRequest)
	 */
	@Override
	public ListHolidayResponse getHolidays(final ListHolidayRequest request)
	{
		ListHolidayResponse response = (ListHolidayResponse) this.webServiceTemplate.marshalSendAndReceive(request);

		if (response == null)
		{
			response = new ListHolidayResponse();
		}

		return response;
	}

	/**
	 * @see de.freese.spring.common.ws.IHolidayService#setHoliday(de.freese.spring.common.model.SaveHolidayRequest)
	 */
	@Override
	public void setHoliday(final SaveHolidayRequest request)
	{
		this.webServiceTemplate.marshalSendAndReceive(request);
	}

	/**
	 * @param webServiceTemplate {@link WebServiceTemplate}
	 */
	public void setWebServiceTemplate(final WebServiceTemplate webServiceTemplate)
	{
		this.webServiceTemplate = webServiceTemplate;
	}
}
