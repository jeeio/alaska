package io.jee.alaska.alibaba.alidayu;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import io.jee.alaska.util.Result;

public class AlidayuShortMessagingHandler implements ShortMessagingHandler {
	
	private String signName, serverUrl, appKey, appSecret;
	

	public AlidayuShortMessagingHandler(String signName, String serverUrl, String appKey, String appSecret) {
		this.signName = signName;
		this.serverUrl = serverUrl;
		this.appKey = appKey;
		this.appSecret = appSecret;
	}

	@Override
	public Result<?> send(String signName, Map<String, String> param, String template, String... mobiles) {
		TaobaoClient client = new DefaultTaobaoClient(serverUrl, appKey, appSecret);
		String mobile = mobiles[0];
		if (mobiles.length > 1) {
			for (int i = 1; i < mobiles.length; i++) {
				mobile += "," + mobiles[i];
			}
		}
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(signName);
		if(param!=null&&!param.isEmpty()){
			try {
				req.setSmsParamString(new ObjectMapper().writeValueAsString(param));
			} catch (JsonProcessingException e1) {
				return Result.error(e1.getMessage());
			}
		}
		req.setRecNum(mobile);
		req.setSmsTemplateCode(template);
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
			return Result.result(rsp.isSuccess(), rsp.getSubMsg());
		} catch (ApiException e) {
			return Result.error("短信发送失败");
		}
	}

	@Override
	public Result<?> send(Map<String, String> param, String template, String... mobiles) {
		return this.send(signName, param, template, mobiles);
	}

}
