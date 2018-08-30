package io.jee.alaska.pay.wxpay;

import java.io.InputStream;

import io.jee.alaska.pay.wxpay.sdk.IWXPayDomain;
import io.jee.alaska.pay.wxpay.sdk.WXPayConstants;

public class WXPayConfig extends io.jee.alaska.pay.wxpay.sdk.WXPayConfig {
	
	private String appId,mchId,mchKey;
	
	public WXPayConfig(String appId, String mchId, String mchKey) {
		this.appId = appId;
		this.mchId = mchId;
		this.mchKey = mchKey;
	}
	
	@Override
	public String getAppID() {
        return appId;
    }

	@Override
    public String getMchID() {
        return mchId;
    }

    @Override
    public String getKey() {
        return mchKey;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

	@Override
	protected InputStream getCertStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IWXPayDomain getWXPayDomain() {
		IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
			@Override
			public DomainInfo getDomain(io.jee.alaska.pay.wxpay.sdk.WXPayConfig config) {
				return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
			}
        };
        return iwxPayDomain;
	}
    
}
