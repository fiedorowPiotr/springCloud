package com.example.springcloud_demo.feign.config;

import com.example.springcloud_demo.feign.ExternalFailService;
import com.example.springcloud_demo.feign.FeignErrorDecoderHandlerForSaleTransaction;
import com.example.springcloud_demo.feign.IntegrationRetryer;
import com.example.springcloud_demo.feign.manual.ManualIntegrationProcessOne;
import com.example.springcloud_demo.feign.manual.ManualIntegrationProcessTwo;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.codec.Decoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ExternalServiceClientConfig {

    private final ExternalFailService externalFailService;
    private final FeignProperties feignProperties;
    private final ObjectMapper mapper;

    private <T> T createClient(Class<T> type, String uri, Retryer retryer, Decoder decoder) {
        return Feign.builder()
                .client(new OkHttpClient())
                .retryer(retryer)
                .encoder(new JacksonEncoder(mapper))
                .errorDecoder(new FeignErrorDecoderHandlerForSaleTransaction(externalFailService))
                .decoder(decoder)
                .logLevel(Logger.ErrorLogger.Level.FULL)
                .logger(new Slf4jLogger(type))
                .contract(new SpringMvcContract())
                .target(type, uri);
    }

    @Bean
    public ManualIntegrationProcessOne innerIntegrationForRefundClient(@Qualifier("feignDecoderCustom") Decoder decoder) {
        final FeignProperties.ExternalService externalService = feignProperties.getExternalService().get("service2");
        final FeignProperties.FeignRetryer retryer = externalService.getRetryer();
        return createClient(ManualIntegrationProcessOne.class, externalService.getUrl(),
                new IntegrationRetryer(retryer.getMaxAttempts(), retryer.getBackoff(), externalFailService), decoder);
    }

    @Bean
    public ManualIntegrationProcessTwo innerIntegrationForSale(@Qualifier("feignDecoderCustom") Decoder decoder) {
        final FeignProperties.ExternalService externalService = feignProperties.getExternalService().get("service3");
        final FeignProperties.FeignRetryer retryer = externalService.getRetryer();
        return createClient(ManualIntegrationProcessTwo.class, externalService.getUrl(),
                new IntegrationRetryer(retryer.getMaxAttempts(), retryer.getBackoff(), externalFailService), decoder);
    }
}