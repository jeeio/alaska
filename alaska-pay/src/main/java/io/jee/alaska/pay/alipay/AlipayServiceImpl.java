package io.jee.alaska.pay.alipay;

import javax.annotation.Resource;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;

import cn.hutool.extra.qrcode.QrCodeUtil;

public class AlipayServiceImpl implements AlipayService {
	
	@Resource
	private AlipayClient alipayClient;
	
	@Override
	public String pagePay(String notify_url, String return_url, String out_trade_no, String subject, String total_amount) {
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(return_url);
		alipayRequest.setNotifyUrl(notify_url);
		
		AlipayTradePagePayModel pagePayModel = new AlipayTradePagePayModel();
		pagePayModel.setOutTradeNo(out_trade_no);
		pagePayModel.setTotalAmount(total_amount);
		pagePayModel.setSubject(subject);
		pagePayModel.setTimeoutExpress("2h");
		pagePayModel.setProductCode("FAST_INSTANT_TRADE_PAY");
		
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
	
	@Override
	public byte[] codePay(String notify_url, String out_trade_no, String subject, String total_amount, int width, int height) {

		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setNotifyUrl(notify_url);
		
		AlipayTradePagePayModel pagePayModel = new AlipayTradePagePayModel();
		pagePayModel.setOutTradeNo(out_trade_no);
		pagePayModel.setTotalAmount(total_amount);
		pagePayModel.setSubject(subject);
		pagePayModel.setTimeoutExpress("2h");
		
		request.setBizModel(pagePayModel);
		
		
		String result = null;
		try {
			result = alipayClient.execute(request).getQrCode();
		} catch (AlipayApiException e) {
		}
		
		return QrCodeUtil.generatePng(result, width, height);
	}

}
