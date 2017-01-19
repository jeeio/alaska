package io.jee.alaska.spring.page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

public class PageInputBSTable {

	private int offset;
	private int limit;
	private String sort;
	private String order;
	
	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public PageRequest toPageRequest(){
		Direction direction = Direction.fromStringOrNull(order);
		return new PageRequest(offset/limit, limit, direction, sort);
	}

}
