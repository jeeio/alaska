package io.jee.alaska.alibaba.alipay;

import java.util.HashMap;
import java.util.Map;

import io.jee.alaska.alibaba.alipay.config.AlipayConfig;
import io.jee.alaska.alibaba.alipay.util.AlipaySubmit;

public class AlipayServiceImpl implements AlipayService {
	
	public AlipayServiceImpl(String partner, String key) {
		AlipayConfig.seller_id = partner;
		AlipayConfig.partner = partner;
		AlipayConfig.key = key;
	}

	@Override
	public String pay(String notify_url, String return_url, String out_trade_no, String subject, String total_fee, String body) {

		//////////////////////////////////////////////////////////////////////////////////
				
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", AlipayConfig.payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		sParaTemp.put("body", body);
		//其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.O9yorI&treeId=62&articleId=103740&docType=1
        //如sParaTemp.put("参数名","参数值");
		
		sParaTemp.put("it_b_pay", "30m");
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");

		// 建立请求
		return sHtmlText;
	}

}
