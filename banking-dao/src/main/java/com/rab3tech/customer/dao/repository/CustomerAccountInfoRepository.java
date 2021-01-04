package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rab3tech.dao.entity.CustomerAccountInfo;

public interface CustomerAccountInfoRepository extends JpaRepository<CustomerAccountInfo, Long> {
	
	@Query("SELECT t FROM CustomerAccountInfo t where t.customerId.loginid = ?1")
    List<CustomerAccountInfo> customerInfoById(String customerId);
	
	@Query("SELECT t.tavBalance FROM CustomerAccountInfo t where t.customerId.loginid = ?1")
	Optional<CustomerAccountInfo> findByEmail(String email);
	
	public Optional<CustomerAccountInfo> findByAccountNumber(String accountNumber);
}
