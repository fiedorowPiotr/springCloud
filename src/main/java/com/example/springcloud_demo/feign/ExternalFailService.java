package com.example.springcloud_demo.feign;

import com.example.springcloud_demo.feign.dto.SaleTransactionJson;
import com.example.springcloud_demo.feign.dto.SaleTransactionJsonDto;
import com.example.springcloud_demo.feign.entity.SaleTransactionResultFail;
import com.example.springcloud_demo.feign.repository.SaleTransactionFeignClientFailedRequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class ExternalFailService {

    private final SaleTransactionFeignClientFailedRequestRepository saleTransactionFeignClientFailedRequestRepository;
    private final ObjectMapper objectMapper;

    public void handleFail(FeignClientFailedRequest feignClientFailedRequest) {
        final String feignClientName = feignClientFailedRequest.getFeignClientName();

        SaleTransactionResultFail saleTransactionResultFail = SaleTransactionResultFail.builder()
                .externalServiceName(feignClientName)
                .payload(feignClientFailedRequest.getRequestBody())
                .failReason(feignClientFailedRequest.getFeignException().getMessage())
                .build();

        try {
            saleTransactionFeignClientFailedRequestRepository.save(saleTransactionResultFail);
        } catch (Exception e) {
            log.error("Error while saving log message do database, message will be logged to console");
            logErrorMessage(feignClientFailedRequest);
        }
    }

    private void logErrorMessage(FeignClientFailedRequest feignClientFailedRequest) {
        try {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            final SaleTransactionJson saleTransactionJsonCommon = objectMapper.readValue(feignClientFailedRequest.getRequestBody(), SaleTransactionJsonDto.class);
            log.error("Błąd podczas zapisu(httpStatus: {}) nieudanej próby wysyłki na proces: {} dla saleTransactionId: {}. Powód: {},  request: {}",
                    feignClientFailedRequest.getHttpStatus(),
                    feignClientFailedRequest.getFeignClientName(),
                    saleTransactionJsonCommon.getSaleTransaction().getSaleTransactionId(),
                    feignClientFailedRequest.getFeignException().getMessage(),
                    feignClientFailedRequest.getRequestBody()
            );
        } catch (JsonProcessingException exception) {
            log.error("Cannot deserialize payload!");
        }
    }
}