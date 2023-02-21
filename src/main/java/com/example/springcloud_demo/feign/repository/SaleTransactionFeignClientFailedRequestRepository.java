package com.example.springcloud_demo.feign.repository;

import com.example.springcloud_demo.feign.entity.SaleTransactionResultFail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleTransactionFeignClientFailedRequestRepository extends JpaRepository<SaleTransactionResultFail, Integer> {
}