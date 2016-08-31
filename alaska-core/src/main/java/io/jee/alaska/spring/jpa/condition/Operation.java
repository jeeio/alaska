package io.jee.alaska.spring.jpa.condition;

public enum Operation {
	/**
	 * 等于(为null会自动转换SQL)
	 */
	EQ(" = "),
	/**
	 * 不等于(为null会自动转换SQL)
	 */
	NEQ(" != "),
	/**
	 * 大于
	 */
	GT(" > "),
	/**
	 * 小于
	 */
	LT(" < "),
	/**
	 * 大于等于
	 */
	GET(" >= "),
	/**
	 * 小于等于
	 */
	LET(" <= "),
	/**
	 * 模糊
	 */
	LIKE(" like "),
	/**
	 * 包含
	 */
	IN(" in "),
	/**
	 * 不包含
	 */
	NIN(" not in ");

	private String keyword;

	private Operation(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}
}