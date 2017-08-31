package io.jee.alaska.alibaba.alipay;

public interface AlipayService {
	
	/**
	 * 支付
	 * @param notify_url 服务器异步通知页面路径
	 * @param return_url 页面跳转同步通知页面路径
	 * @param out_trade_no 商户订单号
	 * @param subject 订单名称
	 * @param body 订单描述
	 * @param total_amount 付款金额
	 * @return
	 */
	String pay(String notify_url, String return_url, String out_trade_no, String subject, String body, String total_amount, String qr_pay_mode);
	
}
