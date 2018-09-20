/**
 *
 */
package de.freese;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import de.freese.sql.querydsl.QTEmployee;
import de.freese.sql.querydsl.TEmployee;

/**
 * @author Thomas Freese
 */
public class TEmployeeRowMapper implements RowMapper<TEmployee>
{
    /**
     *
     */
    public TEmployeeRowMapper()
    {
        super();
    }

    /**
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public TEmployee mapRow(final ResultSet rs, final int rowNum) throws SQLException
    {
        QTEmployee e = QTEmployee.tEmployee;

        TEmployee employee = new TEmployee();
        employee.setMyId(rs.getLong(e.getMetadata(e.myId).getName()));
        employee.setName(rs.getString(e.getMetadata(e.name).getName()));
        employee.setVorname(rs.getString(e.getMetadata(e.vorname).getName()));

        return employee;
    }
}
