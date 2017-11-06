package io.jee.alaska.util;

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
	
	public static String randomMac() {
        Random random = new Random();
        String[] mac = {
                String.format("%02X", random.nextInt(0xff)),
                String.format("%02X", random.nextInt(0xff)),
                String.format("%02X", random.nextInt(0xff)),
                String.format("%02X", random.nextInt(0xff)),
                String.format("%02X", random.nextInt(0xff)),
                String.format("%02X", random.nextInt(0xff))
        };
        return String.join(":", mac);
    }
	
}
