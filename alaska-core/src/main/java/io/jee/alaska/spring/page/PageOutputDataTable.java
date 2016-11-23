package io.jee.alaska.spring.page;

import java.util.List;

public class PageOutputDataTable<T> {

	private long recordsTotal;
	private List<T> data;

	public long getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
