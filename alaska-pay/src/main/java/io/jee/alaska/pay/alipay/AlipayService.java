package io.jee.alaska.pay.alipay;

public interface AlipayService {
	
	/**
	 * 跳转支付
	 * @param notify_url 服务器异步通知页面路径
	 * @param return_url 页面跳转同步通知页面路径
	 * @param out_trade_no 商户订单号
	 * @param subject 订单名称
	 * @param total_amount 付款金额
	 * @return
	 */
	String pagePay(String notify_url, String return_url, String out_trade_no, String subject, String total_amount);
	
	/**
	 * 二维码支付
	 * @param notify_url 服务器异步通知页面路径
	 * @param out_trade_no 商户订单号
	 * @param subject 订单名称
	 * @param total_amount 付款金额
	 * @return
	 */
	byte[] codePay(String notify_url, String out_trade_no, String subject, String total_amount, int width, int height);
	
}
