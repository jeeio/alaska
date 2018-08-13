package io.jee.alaska.data.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PageOutput<T> implements Iterable<T>, Serializable {
	private static final long serialVersionUID = -3268295024596371480L;
	private List<T> content = new ArrayList<>();
	private long total;
	private int pageNo;
	private int pageSize;

	public PageOutput() {
	}

	public PageOutput(List<T> content, long total, int pageNo, int pageSize) {
		this.content = content;
		this.total = total;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public List<T> getContent() {
		return Collections.unmodifiableList(content);
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public boolean hasPrevious() {
		return getPageNo() > 0;
	}

	public boolean hasNext() {
		return getPageNo() + 1 < getTotalPage();
	}

	public boolean isFirst() {
		return !hasPrevious();
	}

	public boolean isLast() {
		return !hasNext();
	}

	public int getTotalPage() {
		return getPageSize() == 0 ? 1 : (int) Math.ceil((double) total / (double) getPageSize());
	}

	public boolean hasContent() {
		return getContentSize() > 0;
	}

	public int getContentSize() {
		return content.size();
	}

	@Override
	public Iterator<T> iterator() {
		return getContent().iterator();
	}

	// public Page<T> toPage(Pageable pageable){
	// return new PageImpl<>(content, pageable, total);
	// }

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
