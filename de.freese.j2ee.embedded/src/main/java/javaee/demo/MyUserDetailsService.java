/**
 * 
 */
package javaee.demo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Thomas Freese
 */
public class MyUserDetailsService implements UserDetailsService
{
	/**
	 * 
	 */
	public MyUserDetailsService()
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

		for (String role : Arrays.asList("Role_A", "Role_B"))
		{
			authorities.add(new SimpleGrantedAuthority(role));
		}

		UserDetails userDetails = new User(username, "password", authorities);

		return userDetails;
	}
}
