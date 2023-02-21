package com.example.springcloud_demo.feign;

import com.example.springcloud_demo.feign.exception.RetryableForSaleTransactionException;
import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

import static feign.FeignException.errorStatus;
import static org.apache.commons.lang.StringUtils.EMPTY;

@Log4j2
public class FeignErrorDecoderHandlerForSaleTransaction implements ErrorDecoder {

    private final ExternalFailService externalFailService;

    public FeignErrorDecoderHandlerForSaleTransaction(ExternalFailService externalFailService) {
        this.externalFailService = externalFailService;
    }

    @Override
    public Exception decode(final String methodKey, final Response response) {
        FeignException exception = errorStatus(methodKey, response);
        int status = response.status();

        if (status >= 500) {
            log.info("Wystąpił błąd (http status: {}), methodKey: {}, próba ponowienia... ", status, methodKey);
            return new RetryableForSaleTransactionException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    null,
                    response.request(),
                    methodKey);
        } else if (status >= 300) {
            FeignClientFailedRequest feignClientFailedRequest = FeignClientFailedRequest
                    .builder()
                    .feignClientName(getFeignClientName(methodKey))
                    .requestBody(getRequestBody(response.request()))
                    .httpStatus(status)
                    .method(getFeignMethodName(methodKey))
                    .feignException(exception)
                    .build();
            externalFailService.handleFail(feignClientFailedRequest);
        }
        return exception;
    }

    public static String getFeignClientName(String methodKey) {
        String[] parts = methodKey.split("#");
        return parts[0];
    }

    public static String getFeignMethodName(String methodKey) {
        String[] parts = methodKey.split("#");
        return parts[1];
    }

    public static String getRequestBody(Request request) {
        if (request.body() != null) {
            return new String(request.body(), StandardCharsets.UTF_8);
        }
        return EMPTY;
    }
}