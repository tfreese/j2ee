/**
 * 
 */
package javaee.demo;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Thomas Freese
 */
public class LoginController
{
	/**
	 * 
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	/**
	 * 
	 */
	private AuthenticationManager authenticationManager = null;

	/**
     * 
     */
	private String password = null;

	/**
	 * 
	 */
	private String userName = null;

	/**
	 * 
	 */
	public LoginController()
	{
		super();
	}

	/**
	 * @return String
	 */
	public String getPassword()
	{
		return this.password;
	}

	/**
	 * @return String
	 */
	public String getUserName()
	{
		return this.userName;
	}

	// /**
	// *
	// */
	// public void login()
	// {
	// String url = "j_spring_security_check";
	// FacesContext.getCurrentInstance().getExternalContext().dispatch(url);
	//
	// FacesContext.getCurrentInstance().responseComplete();
	// }

	// /**
	// *
	// */
	// public String login()
	// {
	// ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
	//
	// RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
	// .getRequestDispatcher("/j2ee-embedded/j_spring_security_check");
	//
	// dispatcher.forward((ServletRequest) context.getRequest(),
	// (ServletResponse) context.getResponse());
	//
	// FacesContext.getCurrentInstance().responseComplete();
	//
	// return null;
	// }
	/**
	 * @return String
	 */
	public String login()
	{
		LOGGER.info("Login started for: {}", this.userName);

		if ((this.userName == null) || (this.password == null))
		{
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "UserName or Password is empty");
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);
			LOGGER.info("Login not started because userName or Password is empty: {}", this.userName);

			return null;
		}

		try
		{
			Authentication request = new UsernamePasswordAuthenticationToken(this.userName, this.password);

			Authentication result = this.authenticationManager.authenticate(request);
			SecurityContextHolder.getContext().setAuthentication(result);
		}
		catch (AuthenticationException ex)
		{
			LOGGER.info("Login failed for {}: {}", this.userName, ex.getMessage());
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, facesMsg);

			return null;
		}

		this.password = null;

		return "/secured/date.xhtml";
	}

	/**
	 * @param authenticationManager {@link AuthenticationManager}
	 */
	public void setAuthenticationManager(final AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}

	/**
	 * @param password String
	 */
	public void setPassword(final String password)
	{
		this.password = password;
	}

	/**
	 * @param userName String
	 */
	public void setUserName(final String userName)
	{
		this.userName = userName;
	}
}
