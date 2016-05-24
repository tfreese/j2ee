/**
 *
 */
package de.freese.jpa.jdbc;

import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;

/**
 * {@code SpringExceptionTranslator} is an {@link SQLExceptionTranslator} implementation which uses Spring's exception translation functionality internally
 * <p>
 * Usage example
 * </p>
 *
 * <pre>
 * {
 *     &#064;code
 *     Configuration configuration = new com.querydsl.sql.Configuration(templates);
 *     configuration.setExceptionTranslator(new SpringExceptionTranslator());
 * }
 * </pre>
 * <p>
 *
 * @author Thomas Freese
 */
public class SpringExceptionTranslator implements com.querydsl.sql.SQLExceptionTranslator
{

    /**
     *
     */
    private final SQLExceptionTranslator translator;

    /**
     *
     */
    public SpringExceptionTranslator()
    {
        super();

        this.translator = new SQLStateSQLExceptionTranslator();
    }

    /**
     * @param translator {@link SQLExceptionTranslator}
     */
    public SpringExceptionTranslator(final SQLExceptionTranslator translator)
    {
        super();

        this.translator = translator;
    }

    /**
     * @see com.querydsl.sql.SQLExceptionTranslator#translate(java.sql.SQLException)
     */
    @Override
    public RuntimeException translate(final SQLException e)
    {
        return this.translator.translate(null, null, e);
    }

    /**
     * @see com.querydsl.sql.SQLExceptionTranslator#translate(java.lang.String, java.util.List, java.sql.SQLException)
     */
    @Override
    public RuntimeException translate(final String sql, final List<Object> bindings, final SQLException e)
    {
        return this.translator.translate(null, sql, e);
    }
}
