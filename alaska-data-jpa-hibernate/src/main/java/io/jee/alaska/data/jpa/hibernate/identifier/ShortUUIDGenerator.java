package io.jee.alaska.data.jpa.hibernate.identifier;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * 8位UUID生成
 * 36的8次方
 * @author Maohan
 *
 */
public class ShortUUIDGenerator implements IdentifierGenerator {

	public final static String[] chars = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C",
			"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
			"Y", "Z" };

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % chars.length]);
		}
		return shortBuffer.toString();
	}

}
