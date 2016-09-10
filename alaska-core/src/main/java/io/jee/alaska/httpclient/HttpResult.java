package io.jee.alaska.httpclient;

public class HttpResult {

	private boolean success;
	private int statusCode;
	private String content;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "HttpResult [success=" + success + ", statusCode=" + statusCode
				+ ", content=" + content + "]";
	}
	
}
