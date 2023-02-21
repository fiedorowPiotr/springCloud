package com.example.springcloud_demo.feign.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleTransaction {
    private Long saleTransactionId;
    private String name;
    private BigDecimal amount;
}