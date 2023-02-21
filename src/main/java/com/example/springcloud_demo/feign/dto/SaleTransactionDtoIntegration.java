package com.example.springcloud_demo.feign.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class SaleTransactionDtoIntegration implements SaleTransactionJsonCommon {

    private BigInteger saleTransactionId;
}