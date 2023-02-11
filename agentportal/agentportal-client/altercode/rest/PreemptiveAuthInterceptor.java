/**
 * Created: 17.12.2012
 */

package de.freese.agentportal.client.rest;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.HttpContext;

/**
 * @author Thomas Freese
 */
public class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
    /**
     *
     */
    private final String password;

    /**
     *
     */
    private final String username;

    /**
     * Erstellt ein neues {@link PreemptiveAuthInterceptor} Object.
     *
     * @param username String
     * @param password String
     */
    public PreemptiveAuthInterceptor(final String username, final String password) {
        super();

        this.username = username;
        this.password = password;
    }

    /**
     * @see org.apache.http.HttpRequestInterceptor#process(org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext)
     */
    @Override
    public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

        // authState.setAuthScope(AuthScope.ANY);

        authState.update(new BasicScheme(), new UsernamePasswordCredentials(this.username, this.password));
    }
}
