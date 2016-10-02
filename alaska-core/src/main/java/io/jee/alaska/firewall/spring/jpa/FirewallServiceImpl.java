package io.jee.alaska.firewall.spring.jpa;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import io.jee.alaska.firewall.FirewallService;

@Service
@Transactional
public class FirewallServiceImpl implements FirewallService {
	
	@Resource
	private FirewallActionCountDao firewallDao;
	@Resource
	private FirewallTempStorageDao tempStorageDao;

	@Override
	public boolean verifyActionCount(String keyword, long minuteAfter, int count, byte type) {
		long time = System.currentTimeMillis()-minuteAfter*60*1000;
		long size = firewallDao.count(new Specification<FirewallActionCount>() {
			
			@Override
			public Predicate toPredicate(Root<FirewallActionCount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return query.where(cb.equal(root.get("keyword"), keyword), cb.gt(root.get("time"), time), cb.equal(root.get("type"), type)).getRestriction();
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

	@Transactional(isolation=Isolation.SERIALIZABLE)
	@Override
	public void addTempStorage(String key, String content, long expireMillis) {
		FirewallTempStorage firewallTempStorage = new FirewallTempStorage();
		firewallTempStorage.setKeyword(key);
		firewallTempStorage.setContent(content);
		firewallTempStorage.setExpire(System.currentTimeMillis()+expireMillis);
		tempStorageDao.save(firewallTempStorage);
	}

	@Transactional(isolation=Isolation.SERIALIZABLE)
	@Override
	public void addTempStorage(String key, String content, TimeUnit timeUnit, long duration) {
		this.addTempStorage(key, content, timeUnit.toMillis(duration));
	}

	@Override
	public FirewallTempStorage getTempStorage(String key) {
		FirewallTempStorage tempStorage = tempStorageDao.findOne(key);
		if(tempStorage!=null&&tempStorage.getExpire()>System.currentTimeMillis()){
			return tempStorage;
		}
		return null;
	}
	
	@Override
	public void removeTempStorage(FirewallTempStorage tempStorage) {
		tempStorageDao.delete(tempStorage);
	}

	@Override
	public void clearTempStorage() {
		tempStorageDao.deleteByTimeLessThan(System.currentTimeMillis());
	}
	
	@Service
	@Profile("master")
	public class ClearFirewallService{
		
		@Scheduled(fixedRate=60*1000*5)
		public void clear(){
			clearActionCount();
			clearTempStorage();
		}
		
	}

}
