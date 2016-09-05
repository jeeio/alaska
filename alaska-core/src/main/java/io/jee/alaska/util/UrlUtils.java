package io.jee.alaska.util;

import javax.servlet.http.HttpServletRequest;

public class UrlUtils {
	
	public static StringBuffer currentProject(HttpServletRequest request){
		StringBuffer buffer = new StringBuffer(request.isSecure() ? "https://" : "http://");
		buffer.append(request.getServerName());
		if ((request.isSecure() && request.getServerPort() != 443)
				|| (!request.isSecure() && request.getServerPort() != 80)) {
			buffer.append(":");
			buffer.append(request.getServerPort());
		}
		buffer.append(request.getContextPath());
		return buffer;
	}

}
