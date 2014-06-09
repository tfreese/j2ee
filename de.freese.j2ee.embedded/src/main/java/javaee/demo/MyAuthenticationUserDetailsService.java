/**
 * Created: 21.01.2014
 */

package javaee.demo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author Thomas Freese
 */
public class MyAuthenticationUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>
{
	/**
	 * Erstellt ein neues {@link MyAuthenticationUserDetailsService} Object.
	 */
	public MyAuthenticationUserDetailsService()
	{
		super();
	}

	/**
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException
	{
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

		for (String role : Arrays.asList("ROLE_A", "ROLE_B"))
		{
			authorities.add(new SimpleGrantedAuthority(role));
		}

		UserDetails userDetails = new User(username, "7c4a8d09ca3762af61e59520943dc26494f8941b", authorities);

		return userDetails;
	}

	/**
	 * @see org.springframework.security.core.userdetails.AuthenticationUserDetailsService#loadUserDetails(org.springframework.security.core.Authentication)
	 */
	@Override
	public UserDetails loadUserDetails(final PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException
	{
		if (!(token.getPrincipal() instanceof HttpServletRequest))
		{
			throw new IllegalArgumentException("Principal ist kein HttpServletRequest");
		}

		// HttpServletRequest request = (HttpServletRequest) token.getPrincipal();
		// String userID = request.getHeader("My-UserID");
		String userID = "täschtd";

		return loadUserByUsername(userID);
	}
}