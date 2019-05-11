package com.thebund1st.tiantong.boot.wechatpay;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.thebund1st.tiantong.boot.wechatpay.webhooks.WeChatPayWebhookConfiguration;
import com.thebund1st.tiantong.core.OnlinePaymentResultNotificationIdentifierGenerator;
import com.thebund1st.tiantong.time.Clock;
import com.thebund1st.tiantong.wechatpay.IpAddressExtractor;
import com.thebund1st.tiantong.wechatpay.NonceGenerator;
import com.thebund1st.tiantong.wechatpay.WeChatPayOnlinePaymentGateway;
import com.thebund1st.tiantong.wechatpay.WxPayUnifiedOrderRequestTypeJsApiPopulator;
import com.thebund1st.tiantong.wechatpay.WxPayNativeUnifiedOrderRequestTypeNativePopulator;
import com.thebund1st.tiantong.wechatpay.WxPayUnifiedOrderRequestProviderSpecificRequestPopulatorDispatcher;
import com.thebund1st.tiantong.wechatpay.webhooks.WeChatPayNotifyPaymentResultCommandAssembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static java.util.Arrays.asList;

@Slf4j
@Configuration
@Import({WeChatPayWebhookConfiguration.class, WeChatPayPropertiesConfiguration.class})
public class WeChatPayConfiguration {

    @Autowired
    private OnlinePaymentResultNotificationIdentifierGenerator onlinePaymentResultNotificationIdentifierGenerator;

    @Autowired
    private Clock clock;

    @Bean
    public NonceGenerator nonceGenerator() {
        return new NonceGenerator();
    }

    @Bean
    public IpAddressExtractor ipAddressExtractor() {
        return new IpAddressExtractor();
    }

    @Bean
    public WxPayService wxPayService(WeChatPayProperties weChatPayProperties) {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(weChatPayProperties.getAppId());
        payConfig.setMchId(weChatPayProperties.getMerchantId());
        payConfig.setMchKey(weChatPayProperties.getMerchantKey());
        payConfig.setUseSandboxEnv(weChatPayProperties.isSandbox());
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    @Bean
    public WxPayUnifiedOrderRequestProviderSpecificRequestPopulatorDispatcher weChatPayCreateOrderRequestPopulatorDispatcher() {
        return
                new WxPayUnifiedOrderRequestProviderSpecificRequestPopulatorDispatcher(asList(
                        new WxPayNativeUnifiedOrderRequestTypeNativePopulator(),
                        new WxPayUnifiedOrderRequestTypeJsApiPopulator())
                );
    }

    @Bean
    public WeChatPayOnlinePaymentGateway weChatPayOnlinePaymentGateway(WeChatPayProperties weChatPayProperties) {
        return new WeChatPayOnlinePaymentGateway(wxPayService(weChatPayProperties),
                nonceGenerator(),
                ipAddressExtractor(),
                weChatPayProperties.paymentResultNotificationWebhookEndpointUri(),
                weChatPayProperties.refundResultNotificationWebhookEndpointUri(),
                weChatPayCreateOrderRequestPopulatorDispatcher(),
                onlinePaymentResultNotificationIdentifierGenerator,
                clock
        );
    }

    @Bean
    public WeChatPayNotifyPaymentResultCommandAssembler weChatPayNotifyPaymentResultCommandAssembler(
            WxPayService wxPayService) {
        return new WeChatPayNotifyPaymentResultCommandAssembler(wxPayService);
    }

}
