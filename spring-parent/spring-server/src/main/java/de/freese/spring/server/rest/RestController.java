/**
 * Created: 02.04.2013
 */

package de.freese.spring.server.rest;

import de.freese.spring.common.model.MyEmployee;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Thomas Freese<br>
 *         https://github.com/krams915/spring-data-rest-tutorial<br>
 *         https://subversion.assembla.com/svn/pablo-examples/spring-rest-example/<br>
 *         https://github.com/eugenp/REST<br>
 *         https://github.com/gcase/spring-data-rest-datatable-example
 */
@Controller
@RequestMapping(value = "/movie", headers = "Accept=application/json, application/xml")
public class RestController
{
	/**
	 * Erstellt ein neues {@link RestController} Object.
	 */
	public RestController()
	{
		super();
	}

	/**
	 * @param name String
	 * @return {@link MyEmployee}
	 */
	@RequestMapping(value = "/emp/{name}", method = RequestMethod.GET)
	@ResponseBody
	public MyEmployee getEmplyee(@PathVariable final String name)
	{
		MyEmployee employee = new MyEmployee();
		employee.setFirstName(name);
		employee.setLastName(name);
		employee.setNumber(1);

		return employee;
	}

	// /**
	// * @param name String
	// * @return {@link ModelAndView}
	// */
	// @RequestMapping(value = "/emp2/{name}", method = RequestMethod.GET, produces =
	// {
	// "application/xml", "application/json"
	// })
	// @ResponseBody
	// public ModelAndView getEmplyee2(@PathVariable final String name)
	// {
	// MyEmployee employee = new MyEmployee();
	// employee.setFirstName(name);
	// employee.setLastName(name);
	// employee.setNumber(1);
	//
	// return new ModelAndView("", "employee", employee);
	// }

	/**
	 * @param name String
	 * @return String
	 */
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	@ResponseBody
	public String getMovie(@PathVariable final String name)
	{
		return "Your Movie: " + name;
	}

	// /**
	// * @param name String
	// * @param model {@link ModelMap}
	// * @return String
	// */
	// @RequestMapping(value = "/{name}", method = RequestMethod.GET)
	// public String getMovie(@PathVariable final String name, final ModelMap model)
	// {
	// model.addAttribute("movie", name);
	// return "movie";
	// }
}
