package io.jee.alaska.firewall.spring.jpa;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jee.alaska.firewall.FirewallService;

@Service
@Transactional
public class FirewallServiceImpl implements FirewallService {
	
	@Resource
	private FirewallActionCountDao firewallDao;

	@Override
	public boolean verifyActionCount(String keyword, long minuteAfter, int count, byte type) {
		long time = System.currentTimeMillis()-minuteAfter*60*1000;
		long size = firewallDao.count(new Specification<FirewallActionCount>() {
			
			@Override
			public Predicate toPredicate(Root<FirewallActionCount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return query.where(cb.equal(root.get("keyword"), "keyword"), cb.gt(root.get("time"), time), cb.equal(root.get("type"), type)).getRestriction();
			}
		});
		return size<count;
	}
	
	@Override
	public void addActionCount(String keyword, byte type) {
		FirewallActionCount firewall = new FirewallActionCount();
		firewall.setKeyword(keyword);
		firewall.setTime(System.currentTimeMillis());
		firewall.setType(type);
		firewallDao.save(firewall);
	}
	
	@Override
	public void clearActionCount() {
		long time = System.currentTimeMillis() - (1000l*60*60*24*7);
		firewallDao.deleteByTimeLessThan(time);
	}

}
