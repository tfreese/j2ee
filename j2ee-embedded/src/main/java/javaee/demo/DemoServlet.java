/**
 * 05.11.2013
 */
package javaee.demo;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Thomas Freese (AUEL)
 */
@WebServlet(name = "demoServlet", urlPatterns = "/demo-servlet")
public class DemoServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 891637777095320320L;

	/**
	 * Erstellt ein neues {@link DemoServlet} Objekt.
	 */
	public DemoServlet()
	{
		super();
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
		throws ServletException, IOException
	{
		resp.getWriter().append(new Date().toString());
	}
}
