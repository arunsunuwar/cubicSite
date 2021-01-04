package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rab3tech.dao.entity.CreditCard;
import com.rab3tech.dao.entity.Customer;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long>{
	
	@Query("SELECT t FROM CreditCard t where t.email = ?1")
    public CreditCard getByEmail(String email);
	
	@Query("SELECT t.ccv FROM CreditCard t where t.email = ?1") 
	public CreditCard getCCVByEmail(String email);
	
	/*
	 * @Query("SELECT t.front FROM CreditCard t where t.email = ?1") public
	 * CreditCard getFront(String email);
	 * 
	 * @Query("SELECT t.back FROM CreditCard t where t.email = ?1") public
	 * CreditCard getBack(String email);
	 */
	
	public Optional<CreditCard> findByEmail(String email);
	
}
