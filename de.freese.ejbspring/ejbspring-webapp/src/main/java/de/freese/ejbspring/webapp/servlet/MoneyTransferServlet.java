/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.freese.ejbspring.webapp.servlet;

import de.freese.ejbspring.facade.IMoneyTransferFacade;
import de.freese.ejbspring.facade.IMoneyTransferResponse;
import de.freese.ejbspring.facade.impl.MoneyTransferRequestImpl;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 *
 * @author Thomas Freese (AuVi)
 */
//@Component("moneyTransferServlet")
public class MoneyTransferServlet extends HttpServlet
{
    /**
     *
     */
    private static final long serialVersionUID = 4096619025843701067L;

    /**
     *
     */
    @EJB(name = "ejbMoneyTransferServiceSLSB")
    private IMoneyTransferFacade moneyTransferFacadeEJB = null;

    /**
     *
     */
    @Autowired
    @Qualifier("springMoneyTransferFacade")
    private IMoneyTransferFacade moneyTransferFacadeSpring = null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * <p>
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * <p>
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter())
        {
            IMoneyTransferResponse moneyTransferResponseEJB = null;
            IMoneyTransferResponse moneyTransferResponseSpring = null;

            if (this.moneyTransferFacadeEJB != null)
            {
                moneyTransferResponseEJB = this.moneyTransferFacadeEJB.transfer(
                        new MoneyTransferRequestImpl("test", 1234.0D));
            }

            if (this.moneyTransferFacadeSpring != null)
            {
                moneyTransferResponseSpring = this.moneyTransferFacadeSpring.transfer(
                        new MoneyTransferRequestImpl("test", 4321.0D));
            }

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MoneyTransferServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MoneyTransferServlet at " + request.getContextPath() + "</h1>");

            if (moneyTransferResponseEJB != null)
            {
                out.println("MoneyTransferFacadeEJB is: " + moneyTransferFacadeEJB.getClass().getName() + "</br>");
                out.println("MoneyTransferResponseEJB is: " + moneyTransferResponseEJB.getKontostand() + "</br>");
            }

            if (moneyTransferResponseSpring != null)
            {
                out.println("MoneyTransferFacadeSpring is: " + moneyTransferFacadeSpring.getClass().getName() + "</br>");
                out.println("MoneyTransferResponseSpring is: " + moneyTransferResponseSpring.getKontostand() + "</br>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * @see javax.servlet.GenericServlet#init()
     */
    @Override
    public void init() throws ServletException
    {
        super.init();

        // DependencyInjection für dieses Servlet.
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());

        // WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        // AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        // beanFactory.autowireBean(this);
        //
        // this.moneyTransferFacadeSpring = context.getBean("springMoneyTransferFacade", IMoneyTransferFacade.class);
    }
}
