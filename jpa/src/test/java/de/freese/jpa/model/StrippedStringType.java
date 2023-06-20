// Created: 20.06.23
package de.freese.jpa.model;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

/**
 * @author Thomas Freese
 */
public class StrippedStringType implements UserType<String> {
    @Override
    public String assemble(final Serializable cached, final Object owner) {
        return deepCopy((String) cached);
    }

    @Override
    public String deepCopy(final String value) {
        if (value == null) {
            return null;
        }

        return value.strip();
    }

    @Override
    public Serializable disassemble(final String value) {
        return deepCopy(value);
    }

    @Override
    public boolean equals(final String x, final String y) {
        return Objects.equals(x, y);
    }

    @Override
    public int getSqlType() {
        return Types.VARCHAR;
    }

    @Override
    public int hashCode(final String x) {
        return Objects.hashCode(x);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public String nullSafeGet(final ResultSet rs, final int position, final SharedSessionContractImplementor session, final Object owner) throws SQLException {
        String value = rs.getString(position);

        if (rs.wasNull()) {
            return null;
        }

        return value.strip();
    }

    @Override
    public void nullSafeSet(final PreparedStatement st, final String value, final int index, final SharedSessionContractImplementor session) throws SQLException {
        if (value == null) {
            st.setNull(index, getSqlType());
        }
        else {
            st.setString(index, value.strip());
        }
    }

    @Override
    public Class<String> returnedClass() {
        return String.class;
    }
}
