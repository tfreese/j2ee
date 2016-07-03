/**
 *
 */
package de.freese;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import de.freese.querydsl.QTPerson;
import de.freese.querydsl.TPerson;

/**
 * @author Thomas Freese
 */
public class PersonRowMapper implements RowMapper<TPerson>
{

    /**
     *
     */
    public PersonRowMapper()
    {
        super();
    }

    /**
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     */
    @Override
    public TPerson mapRow(final ResultSet rs, final int rowNum) throws SQLException
    {

        QTPerson p = QTPerson.tPerson;

        TPerson person = new TPerson();
        person.setMyId(rs.getLong(p.getMetadata(p.myId).getName()));
        person.setName(rs.getString(p.getMetadata(p.name).getName()));
        person.setVorname(rs.getString(p.getMetadata(p.vorname).getName()));

        return person;
    }
}
