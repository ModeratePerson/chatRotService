package com.moderatePerson.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.moderatePerson.utils.AliPay.AlipayUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {
    @Bean
    public AlipayClient alipayClient(){
        return new DefaultAlipayClient(AlipayUtil.gatewayUrl, AlipayUtil.app_id, AlipayUtil.merchant_private_key, "json", AlipayUtil.charset, AlipayUtil.alipay_public_key, AlipayUtil.sign_type);
    }

    @Bean
    public AlipayTradePagePayRequest alipayTradePagePayRequest(){
        return new AlipayTradePagePayRequest();
    }
}
