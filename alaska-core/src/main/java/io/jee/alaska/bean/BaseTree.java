package io.jee.alaska.bean;

import java.util.List;

public abstract class BaseTree<T> {

	private Integer id;
	private Integer parentId;
	private List<T> children;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<T> getChildren() {
		return children;
	}

	public void setChildren(List<T> list) {
		this.children = list;
	}
	
	
}
