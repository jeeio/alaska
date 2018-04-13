package io.jee.data.framework.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>Title: 统一定义id的entity基类
 * <p>Descriptor: 一般用于基于数据库的实体类
 * <p>Copyright (c) CAISAN 2018
 * @author XieXiaoXu on 2018年4月13日
 *
 */
public abstract class AbstractEntity implements Serializable {
	/** UID */
	private static final long serialVersionUID = -5797027386139497608L;
	protected Long id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
