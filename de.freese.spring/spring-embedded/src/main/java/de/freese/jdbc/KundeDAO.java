/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package de.freese.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import javax.sql.DataSource;

/**
 * DAO = DataAccessObject
 * <p/>
 * 
 * @author Thomas Freese
 */
public class KundeDAO
{
	/**
	 * ConnectionPool
	 */
	private DataSource dataSource = null;

	/**
	 * Sequence: In der Praxis wird dies über Sequences der Datenbank gemacht !!!
	 */
	private final AtomicLong sequence = new AtomicLong(1);

	/**
     *
     */
	public KundeDAO()
	{
		super();
	}

	/**
	 * @param id long
	 * @throws Exception Falls was schief geht.
	 */
	public void delete(final long id) throws Exception
	{
		Connection con = null;
		PreparedStatement stmt = null;

		try
		{
			con = getConnection();

			String sql = "delete from kunde where pk = ?";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, id);

			stmt.executeUpdate();
		}
		finally
		{
			// Erst Statement schliessen.
			if (stmt != null)
			{
				stmt.close();
			}

			// Dann die Connection.
			if (con != null)
			{
				con.close();
			}
		}
	}

	/**
	 * @return {@link Connection}
	 * @throws Exception Falls was schief geht.
	 */
	private Connection getConnection() throws Exception
	{
		return this.dataSource.getConnection();
	}

	/**
	 * @param kunde {@link Kunde}
	 * @throws Exception Falls was schief geht.
	 */
	public void insert(final Kunde kunde) throws Exception
	{
		Connection con = null;
		PreparedStatement stmt = null;

		try
		{
			con = getConnection();

			// TODO: Achtung !!!
			// Hier machen wir es uns einfach und erzeugen den PrimaryKey selbst.
			// In der Praxis wird dies über Sequences der Datenbank gemacht !!!
			long pk = this.sequence.getAndIncrement();
			kunde.setPk(pk);

			String sql = "insert into kunde (pk, nachname, vorname) values (?, ?, ?)";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, pk);
			stmt.setString(2, kunde.getNachName());
			stmt.setString(3, kunde.getVorname());

			stmt.executeUpdate();
		}
		finally
		{
			// Erst Statement schliessen.
			if (stmt != null)
			{
				stmt.close();
			}

			// Dann die Connection.
			if (con != null)
			{
				con.close();
			}
		}
	}

	/**
	 * Liefert alle Kunden.
	 * 
	 * @return {@link List}
	 * @throws Exception Falls was schief geht.
	 */
	public List<Kunde> selectAll() throws Exception
	{
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<Kunde> kunden = new ArrayList<Kunde>();

		try
		{
			con = getConnection();

			String sql = "select * from kunde";
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				Kunde kunde = new Kunde();

				// Der Zugriff über Spaltennamen ist sicherer als über Indices !
				kunde.setPk(rs.getLong("PK"));
				kunde.setNachName(rs.getString("NACHNAME"));
				kunde.setVorname(rs.getString("VORNAME"));

				kunden.add(kunde);
			}
		}
		finally
		{
			// Erst ResultSet schliessen.
			if (rs != null)
			{
				rs.close();
			}

			// Dann das Statement.
			if (stmt != null)
			{
				stmt.close();
			}

			// Dann die Connection.
			if (con != null)
			{
				con.close();
			}
		}

		return kunden;
	}

	/**
	 * Liefert nur einen bestimmten Kunden.
	 * 
	 * @param pk long; PrimaryKey
	 * @return {@link Kunde}
	 * @throws Exception Falls was schief geht.
	 */
	public Kunde selectByID(final long pk) throws Exception
	{
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Kunde kunde = null;

		try
		{
			con = getConnection();

			String sql = "select * from kunde where pk = ?";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, pk);
			rs = stmt.executeQuery();

			if (rs.next())
			{
				kunde = new Kunde();

				// Der Zugriff über Spaltennamen ist sicherer als über Indices !
				kunde.setPk(rs.getLong("PK"));
				kunde.setNachName(rs.getString("NACHNAME"));
				kunde.setVorname(rs.getString("VORNAME"));
			}
		}
		finally
		{
			// Erst ResultSet schliessen.
			if (rs != null)
			{
				rs.close();
			}

			// Dann das Statement.
			if (stmt != null)
			{
				stmt.close();
			}

			// Dann die Connection.
			if (con != null)
			{
				con.close();
			}
		}

		return kunde;
	}

	/**
	 * Liefert alle Kunden mit diesen Nachnamen.
	 * 
	 * @param nachname String
	 * @return {@link List}
	 * @throws Exception Falls was schief geht.
	 */
	public List<Kunde> selectByNachName(final String nachname) throws Exception
	{
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<Kunde> kunden = new ArrayList<Kunde>();

		try
		{
			con = getConnection();

			String sql = "select * from kunde where nachname = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, nachname);
			rs = stmt.executeQuery();

			while (rs.next())
			{
				Kunde kunde = new Kunde();

				// Der Zugriff über Spaltennamen ist sicherer als über Indices !
				kunde.setPk(rs.getLong("PK"));
				kunde.setNachName(rs.getString("NACHNAME"));
				kunde.setVorname(rs.getString("VORNAME"));

				kunden.add(kunde);
			}
		}
		finally
		{
			// Erst ResultSet schliessen.
			if (rs != null)
			{
				rs.close();
			}

			// Dann das Statement.
			if (stmt != null)
			{
				stmt.close();
			}

			// Dann die Connection.
			if (con != null)
			{
				con.close();
			}
		}

		return kunden;
	}

	/**
	 * ConnectionPool
	 * 
	 * @param dataSource {@link DataSource}
	 */
	public void setDataSource(final DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * @param kunde {@link Kunde}
	 * @throws Exception Falls was schief geht.
	 */
	public void update(final Kunde kunde) throws Exception
	{
		Connection con = null;
		PreparedStatement stmt = null;

		try
		{
			con = getConnection();

			String sql = "update kunde set nachname = ?, vorname = ? where pk = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, kunde.getNachName());
			stmt.setString(2, kunde.getVorname());
			stmt.setLong(3, kunde.getPk());

			stmt.executeUpdate();
		}
		finally
		{
			// Erst Statement schliessen.
			if (stmt != null)
			{
				stmt.close();
			}

			// Dann die Connection.
			if (con != null)
			{
				con.close();
			}
		}
	}
}
