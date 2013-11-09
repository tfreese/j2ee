/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package de.freese.jdbc;

import java.util.List;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Thomas Freese
 */
public class Main
{
	/**
	 * @param args String[]
	 * @throws Exception Falls was schief geht.
	 */
	public static void main(final String[] args) throws Exception
	{
		// final org.apache.commons.dbcp.BasicDataSource bds = new BasicDataSource();
		// bds.setMaxActive(2);
		// bds.setMinIdle(1);
		// bds.setMaxIdle(2);
		// bds.setMaxWait(5000L);
		// bds.setTestOnBorrow(true);
		// bds.setPoolPreparedStatements(true);
		// bds.setDriverClassName("oracle.jdbc.OracleDriver"); //org.hsqldb.jdbcDriver
		// bds.setValidationQuery("select sysdate from dual"); //"SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS"
		// bds.setUrl(url); //jdbc:hsqldb:mem:Test-DB
		// bds.setUsername(user); //"sa"
		// bds.setPassword(password); //""

		try (AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml"))
		{
			applicationContext.registerShutdownHook();

			KundeDAO dao = applicationContext.getBean("daoKunde", KundeDAO.class);

			// Insert Kunden.
			System.out.println("Insert Kunden.");
			Kunde kunde = new Kunde();
			kunde.setNachName("Albers");
			kunde.setVorname("Hans");
			dao.insert(kunde);

			kunde = new Kunde();
			kunde.setNachName("Albers");
			kunde.setVorname("Hugo");
			dao.insert(kunde);

			kunde = new Kunde();
			kunde.setNachName("Müller");
			kunde.setVorname("Heinz");
			dao.insert(kunde);

			// Select: Kunde mit PK = 1.
			System.out.println();
			System.out.println("Select: Kunde mit PK = 1.");
			kunde = dao.selectByID(1);
			System.out.println(kunde);

			// Select: Alle Kunden mit Namen Albers.
			System.out.println();
			System.out.println("Select: Alle Kunden mit Namen Albers.");
			List<Kunde> kunden = dao.selectByNachName("Albers");

			for (Kunde k : kunden)
			{
				System.out.println(k);
			}

			// Select: Alle Kunden.
			System.out.println();
			System.out.println("Select: Alle Kunden.");
			kunden = dao.selectAll();

			for (Kunde k : kunden)
			{
				System.out.println(k);
			}

			// Update: Kunde mit PrimaryKey 1 (Albers -> Meyer).
			System.out.println();
			System.out.println("Update: Kunde mit PrimaryKey 1 (Albers -> Meyer).");
			kunde = new Kunde();
			kunde.setPk(1);
			kunde.setNachName("Meyer");
			kunde.setVorname("Hans");
			dao.update(kunde);

			// Select: Kunde mit PK = 1.
			System.out.println();
			System.out.println("Select: Kunde mit PK = 1.");
			kunde = dao.selectByID(1);
			System.out.println(kunde);

			// Delete: Kunde mit PrimaryKey 1.
			System.out.println();
			System.out.println("Delete: Kunde mit PrimaryKey 1.");
			dao.delete(1);

			// Select: Kunde mit PK = 1.
			System.out.println();
			System.out.println("Select: Kunde mit PK = 1.");
			kunde = dao.selectByID(1);
			System.out.println(kunde);

			// Select: Alle Kunden.
			System.out.println();
			System.out.println("Select: Alle Kunden.");
			kunden = dao.selectAll();

			for (Kunde k : kunden)
			{
				System.out.println(k);
			}
		}
	}
}
