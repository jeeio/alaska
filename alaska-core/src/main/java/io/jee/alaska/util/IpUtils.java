package io.jee.alaska.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class IpUtils {

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(StringUtils.hasText(ip)){
			ip = ip.split(",")[0];
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	public static String randomMac(String prefix) {
		List<String> mac = null;
        if(StringUtils.hasText(prefix)) {
        	mac = new ArrayList<>(Arrays.asList(prefix.split(":")));
        }else {
        	mac = new ArrayList<>();
        }
        Random random = new Random();
        for (int i = mac.size(); i < 6; i++) {
        	mac.add(String.format("%02X", random.nextInt(0xff)));
		}
        return String.join(":", mac);
    }
	
}
