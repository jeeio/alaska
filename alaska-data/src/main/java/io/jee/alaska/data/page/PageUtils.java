package io.jee.alaska.data.page;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageUtils {

	public static PageRequest ofLinked(int page, int size) {
		return ofLinked(page, size, null);
	}

	public static PageRequest ofLinked(int page, int size, Direction direction, String... properties) {
		return ofLinked(page, size, new Sort(direction, properties));
	}
	
	public static PageRequest ofLinked(int page, int size, Sort sort) {
		page = (page < 1) ? 0 : page -1;
		return PageRequest.of(page, size, sort);
	}

	public static <T> PageOutputLinked<T> toPageLinked(Page<T> page, Pageable pageable){
		return new PageOutputLinked<>(page, pageable);
	}
	
	public static <T> PageOutputDataTable<T> toPageDataTable(Page<T> page){
		PageOutputDataTable<T> outputDataTable = new PageOutputDataTable<>();
		outputDataTable.setRecordsTotal(page.getTotalElements());
		outputDataTable.setRecordsFiltered(page.getTotalElements());
		outputDataTable.setData(page.getContent());
		return outputDataTable;
	}
	
	public static <T> PageOutputBSTable<T> toPageBSTable(Page<T> page){
		PageOutputBSTable<T> outputBSTable = new PageOutputBSTable<>();
		outputBSTable.setTotal(page.getTotalElements());
		outputBSTable.setRows(page.getContent());
		return outputBSTable;
	}

}
