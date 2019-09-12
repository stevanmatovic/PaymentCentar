package com.example.sep.repository;

import com.example.sep.entity.PayPalPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayPalPlanRepository extends JpaRepository<PayPalPlan,Long> {
  PayPalPlan findByPayPalId(String planId);
}
