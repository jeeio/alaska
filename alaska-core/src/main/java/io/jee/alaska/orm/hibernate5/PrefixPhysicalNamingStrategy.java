package io.jee.alaska.orm.hibernate5;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.util.StringUtils;

import io.jee.alaska.util.PropertiesUtils;

public class PrefixPhysicalNamingStrategy extends SpringPhysicalNamingStrategy {
	
	private String tablePrefix;
	
	public PrefixPhysicalNamingStrategy() {
		PropertiesUtils propertiesUtils = PropertiesUtils.loadProperties("application.properties");
		tablePrefix = propertiesUtils.getValue("alaska.orm.prefix");
		if(!StringUtils.hasText(tablePrefix)){
			tablePrefix = "ala_";
		}
	}

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
		String text = tablePrefix + name.getText();
		return super.toPhysicalTableName(Identifier.toIdentifier(text), jdbcEnvironment);
	}

}
