package com.rab3tech.customer.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.FundTransfer;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Integer> {
	
	
}