package io.jee.alaska.data.jpa.hibernate.utils;

import org.springframework.core.convert.support.DefaultConversionService;

public class EnhanceDefaultConversionService extends DefaultConversionService {

	public EnhanceDefaultConversionService() {
		super();
		addConverter(new StringToDateConverter());
	}

}
