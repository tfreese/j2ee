package de.freese.spring.embedded;

import com.mysema.query.codegen.JavaTypeMappings;
import com.mysema.query.sql.codegen.ExtendedBeanSerializer;
import com.mysema.query.sql.codegen.MetaDataExporter;
import java.io.File;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;

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
		// System.setProperty("org.slf4j.simpleLogger.log.org.springframework.jdbc.core.JdbcTemplate", "debug");

		try (AbstractApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml"))
		{
			applicationContext.registerShutdownHook();

			// DataSource dataSource = applicationContext.getBean("dataSource",DataSource.class);
			MyTableDAO dao = applicationContext.getBean("daoMyTable", MyTableDAO.class);
			System.out.println(dao.nextSequence());

			dao.insert();

			List<Map<String, Object>> result = dao.select();

			for (Map<String, Object> row : result)
			{
				System.out.println(row);
			}

			dao.backup();

			// QueryDSL
			MetaDataExporter exporter = new MetaDataExporter();
			// exporter.setConfiguration(configuration);
			exporter.setNamePrefix("Q");
			// exporter.setNamingStrategy(namingStrategy);
			exporter.setTypeMappings(new JavaTypeMappings());
			exporter.setBeanSerializer(new ExtendedBeanSerializer());
			exporter.setPackageName("de.freese.spring.embedded.domain");
			exporter.setTargetFolder(new File("src/main/generated"));
			exporter.setColumnAnnotations(true);
			exporter.setValidationAnnotations(true);
			exporter.setExportTables(true);
			exporter.setExportViews(false);
			exporter.setTableNamePattern("MY_%");

			Connection connection = DataSourceUtils.getConnection(dao.getJdbcTemplate().getDataSource());
			exporter.export(connection.getMetaData());
		}
	}
}
