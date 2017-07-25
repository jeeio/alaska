package io.jee.alaska.data.page;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PageSample<T> {

	private List<T> content;
	private long total;
	
	public PageSample() {}

	public PageSample(List<T> content, long total) {
		this.content = content;
		this.total = total;
	}

	public List<T> getContent() {
		return content;
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
	
	public Page<T> toPage(Pageable pageable){
		return new PageImpl<>(content, pageable, total);
	}

}
