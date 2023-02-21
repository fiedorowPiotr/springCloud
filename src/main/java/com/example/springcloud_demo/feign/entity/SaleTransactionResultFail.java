package com.example.springcloud_demo.feign.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@Table(name = "sale_transaction_result_fail")
@EqualsAndHashCode
@NoArgsConstructor
public class SaleTransactionResultFail implements Serializable {

    @Id
    @SequenceGenerator(name = "sale_transaction_result_fail_gen", sequenceName = "sale_transaction_result_fail_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_transaction_result_fail_gen")
    @Column(name = "id")
    private Integer id;

    @Column(name = "fail_reason", nullable = false)
    private String failReason;

    @Column(name = "sale_transaction_reference", nullable = false)
    private String failedSaleTrans;

    @Column(name = "external_service_name")
    private String externalServiceName;

    @Builder
    public SaleTransactionResultFail(String failReason, String payload, String externalServiceName) {
        this.failReason = failReason;
        this.failedSaleTrans = payload;
        this.externalServiceName = externalServiceName;
    }

    @Builder
    public SaleTransactionResultFail(String failReason, String failedSaleTrans) {
        this.failReason = failReason;
        this.failedSaleTrans = failedSaleTrans;
    }
}