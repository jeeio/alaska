package io.jee.alaska.firewall.spring.data.jpa;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jee.alaska.firewall.FirewallService;

@Service
@Transactional
public class FirewallJpaServiceImpl implements FirewallService {
	
	@Resource
	private FirewallActionCountRepository firewallRepository;

	@Override
	public boolean verifyActionCount(String keyword, int count, byte type) {
		long time = System.currentTimeMillis();
		long size = firewallRepository.count(new Specification<FirewallActionCount>() {
			
			@Override
			public Predicate toPredicate(Root<FirewallActionCount> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return query.where(cb.equal(root.get("keyword"), keyword+"-"+type), cb.gt(root.get("timeout"), time)).getRestriction();
			}
		});
		return size<count;
	}
	
	@Override
	public void addActionCount(String keyword, long minuteAfter, byte type) {
		FirewallActionCount firewall = new FirewallActionCount();
		firewall.setKeyword(keyword+"-"+type);
		firewall.setTimeout(System.currentTimeMillis() + (minuteAfter*60*1000));
		firewallRepository.save(firewall);
	}
	
	public void clearActionCount() {
		long time = System.currentTimeMillis();
		firewallRepository.deleteByTimeoutLessThan(time);
	}

	@Service
	@Profile("master")
	public class ClearFirewallService{
		
		@Scheduled(fixedRate=60*1000*5)
		public void clear(){
			clearActionCount();
		}
		
	}

}
