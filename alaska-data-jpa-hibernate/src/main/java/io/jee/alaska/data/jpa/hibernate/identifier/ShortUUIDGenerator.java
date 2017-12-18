package io.jee.alaska.data.jpa.hibernate.identifier;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import io.jee.alaska.util.StringUtils;

/**
 * 8位UUID生成
 * 36的8次方
 * @author Maohan
 *
 */
public class ShortUUIDGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return StringUtils.shortUUIDGenerator();
	}

}
