package io.jee.alaska.data.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

public class PageInput implements Serializable {

	private static final long serialVersionUID = 5481440889834048736L;
	
	private int page;
	private int size;
	private Map<String, Direction> orders;
	
	public PageInput() {}
	
	public PageInput(int page, int size) {
		this(page, size, null);
	}
	
	public PageInput(int page, int size, Direction direction, String... properties) {
		this(page, size, new Sort(direction, properties));
	}
	
	public PageInput(int page, int size, Sort sort){
		this.page = page;
		this.size = size;
		if(sort !=null){
			orders = new LinkedHashMap<>();
			sort.forEach(item -> {
				orders.put(item.getProperty(), item.getDirection());
			});
		}
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Map<String, Direction> getOrders() {
		return orders;
	}

	public void setOrders(Map<String, Direction> orders) {
		this.orders = orders;
	}

	public PageRequest toPageRequest(){
		if(orders ==null){
			return PageRequest.of(page, size);
		}else{
			List<Order> orderList = new ArrayList<>();
			orders.entrySet().forEach(order -> {
				orderList.add(new Order(order.getValue(), order.getKey()));
			});
			return PageRequest.of(page, size, Sort.by(orderList));
		}
	}
	
	public static PageInput of(PageRequest pageRequest) {
		return new PageInput(pageRequest.getPageNumber(), pageRequest.getPageSize(), pageRequest.getSort());
	}
	
}
