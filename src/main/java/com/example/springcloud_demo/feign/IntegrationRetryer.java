package com.example.springcloud_demo.feign;

import com.example.springcloud_demo.feign.exception.RetryableForSaleTransactionException;
import feign.Request;
import feign.RetryableException;
import feign.Retryer;
import lombok.RequiredArgsConstructor;

import static com.example.springcloud_demo.feign.FeignErrorDecoderHandlerForSaleTransaction.getFeignClientName;
import static com.example.springcloud_demo.feign.FeignErrorDecoderHandlerForSaleTransaction.getFeignMethodName;
import static com.example.springcloud_demo.feign.FeignErrorDecoderHandlerForSaleTransaction.getRequestBody;

@RequiredArgsConstructor
public class IntegrationRetryer implements Retryer {

    private final ExternalFailService externalFailService;
    private final int maxAttempts;
    private final long backoff;
    private int attempt;

    public IntegrationRetryer(int maxAttempts, long backoff, ExternalFailService externalFailService) {
        this.maxAttempts = maxAttempts;
        this.backoff = backoff;
        this.attempt = 1;
        this.externalFailService = externalFailService;
    }

    @Override // If it doesn't throw an exception, Feign will continue to retry the call
    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            final Request request = e.request();
            final String methodKey = ((RetryableForSaleTransactionException) e).getMethodKey();
            FeignClientFailedRequest feignClientFailedRequest = FeignClientFailedRequest
                    .builder()
                    .feignClientName(getFeignClientName(methodKey))
                    .requestBody(getRequestBody(request))
                    .httpStatus(e.status())
                    .method(getFeignMethodName(methodKey))
                    .feignException(e)
                    .build();
            externalFailService.handleFail(feignClientFailedRequest);
            throw e;
        }
        try {
            Thread.sleep(backoff);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Retryer clone() {
        return new IntegrationRetryer(maxAttempts, backoff, externalFailService);
    }
}