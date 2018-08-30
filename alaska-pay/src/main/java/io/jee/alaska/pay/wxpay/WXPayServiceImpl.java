package io.jee.alaska.pay.wxpay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import cn.hutool.extra.qrcode.QrCodeUtil;
import io.jee.alaska.pay.wxpay.sdk.WXPay;

public class WXPayServiceImpl implements WXPayService {
	
	@Resource
	private WXPay wxpay;

	@Override
	public byte[] codePay(String notify_url, String out_trade_no, String subject, String total_amount, String ip,
			int width, int height) {
		Map<String, String> data = new HashMap<String, String>();
        data.put("body", subject);
        data.put("out_trade_no", out_trade_no);
        data.put("device_info", "WEB");
        data.put("fee_type", "CNY");
        data.put("total_fee", new BigDecimal(total_amount).multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString());
        data.put("spbill_create_ip", ip);
        data.put("notify_url", notify_url);
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        data.put("product_id", out_trade_no);

        Map<String, String> resp = null;
        try {
            resp = wxpay.unifiedOrder(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return QrCodeUtil.generatePng(resp.get("code_url"), width, height);
	}

}
