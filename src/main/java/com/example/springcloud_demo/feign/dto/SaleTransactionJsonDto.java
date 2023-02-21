package com.example.springcloud_demo.feign.dto;

import lombok.Data;

@Data
public class SaleTransactionJsonDto implements SaleTransactionJson {

    SaleTransactionDtoIntegration saleTransaction;
}