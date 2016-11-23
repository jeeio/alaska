package io.jee.alaska.spring.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageUtils {

	public static PageRequest toPageRequest(int page, int size) {
		return toPageRequest(page, size, null);
	}

	public static PageRequest toPageRequest(int page, int size, Direction direction, String... properties) {
		return toPageRequest(page, size, new Sort(direction, properties));
	}
	
	public static PageRequest toPageRequest(int page, int size, Sort sort) {
		page = (page < 1) ? 0 : page -1;
		return new PageRequest(page, size, sort);
	}

	public static <T> PageLinked<T> toPageLinked(Page<T> page, Pageable pageable){
		return new PageLinked<>(page, pageable);
	}
	
	public static <T> PageOutputDataTable<T> toPageDataTable(Page<T> page, Pageable pageable){
		PageOutputDataTable<T> outputDataTable = new PageOutputDataTable<>();
		outputDataTable.setRecordsTotal(page.getTotalElements());
		outputDataTable.setRecordsFiltered(page.getTotalElements());
		outputDataTable.setData(page.getContent());
		return outputDataTable;
	}

}
