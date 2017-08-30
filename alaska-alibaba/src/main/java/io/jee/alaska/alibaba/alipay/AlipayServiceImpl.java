package io.jee.alaska.alibaba.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;

public class AlipayServiceImpl implements AlipayService {
	
	static boolean sandbox;
	
	public AlipayServiceImpl(String appId, String merchantPrivateKey, String alipayPublicKey, boolean sandbox) {
		AlipayConfig.app_id = appId;
		AlipayConfig.merchant_private_key = merchantPrivateKey;
		AlipayConfig.alipay_public_key = alipayPublicKey;
		AlipayServiceImpl.sandbox = sandbox;
	}

	@Override
	public String pay(String notify_url, String return_url, String out_trade_no, String subject, String body, String total_amount) {
		
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(sandbox?AlipayConfig.gatewayUrl_sandbox:AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(return_url);
		alipayRequest.setNotifyUrl(notify_url);
		
		AlipayTradePagePayModel pagePayModel = new AlipayTradePagePayModel();
		pagePayModel.setOutTradeNo(out_trade_no);
		pagePayModel.setTotalAmount(total_amount);
		pagePayModel.setSubject(subject);
		pagePayModel.setBody(body);
		pagePayModel.setTimeoutExpress("30m");
		pagePayModel.setProductCode("FAST_INSTANT_TRADE_PAY");
		//pagePayModel.setQrPayMode("0");
		
		
		alipayRequest.setBizModel(pagePayModel);
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
//		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
//				+ "\"total_amount\":\""+ total_amount +"\"," 
//				+ "\"subject\":\""+ subject +"\"," 
//				+ "\"body\":\""+ body +"\"," 
//				+ "\"timeout_express\":\"10m\"," 
//				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		//请求
		String result = null;
		try {
			result = alipayClient.pageExecute(alipayRequest).getBody();
		} catch (AlipayApiException e) {
		}
		
		//输出
		return result;
	}

}
