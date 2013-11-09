package de.freese.spring.embedded;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * @author Thomas Freese
 */
public class MyTableDAO extends JdbcDaoSupport
{
	/**
     *
     */
	public MyTableDAO()
	{
		super();
	}

	/**
     *
     * 
     */
	public void backup()
	{
		File file = new File("backup");

		if (!file.exists())
		{
			file.mkdirs();
		}
		else
		{
			for (File child : file.listFiles())
			{
				child.delete();
			}
		}

		// nicht für memory-db
		// jdbcTemplate.execute("BACKUP DATABASE TO 'backup/' BLOCKING");
		getJdbcTemplate().execute("SCRIPT 'backup/data.sql'");
	}

	/**
	 * @throws InterruptedException Falls was schief geht.
	 */
	public void insert() throws InterruptedException
	{
		for (int i = 0; i < 5; i++)
		{
			final String name = Integer.toString(i);
			final long timestamp = System.currentTimeMillis();
			// getJdbcTemplate().update("insert into my_table (id, name, datum) values (?, ?, ?)", nextSequence(), name, new Date(timestamp));
			getJdbcTemplate().update("insert into my_table (id, name, datum) values (next value for test_seq, ?, ?)", name, new Date(timestamp));
			// jdbcTemplate.update("insert into my_table (id, name, datum) values (?, ?, ?)", new PreparedStatementSetter()
			// {
			// @Override
			// public void setValues(PreparedStatement ps) throws SQLException
			// {
			// ps.setLong(1, timestamp);
			// ps.setString(2, name);
			// ps.setTimestamp(3, new java.sql.setTimestamp(timestamp));
			// }
			// });
			TimeUnit.SECONDS.sleep(1);
		}
	}

	/**
	 * @return long
	 */
	public long nextSequence()
	{
		long nextSeq = getJdbcTemplate().queryForObject("call next value for test_seq", Long.class);

		return nextSeq;
	}

	/**
	 * @return List
	 */
	public List<Map<String, Object>> select()
	{
		List<Map<String, Object>> result = getJdbcTemplate().queryForList("select * from my_table");

		return result;
	}
}
