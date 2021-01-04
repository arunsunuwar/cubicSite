package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rab3tech.dao.entity.Customer;

/**
 * @author nagendra
 * Spring Data JPA repository
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	public Optional<Customer> findByEmail(String email);
	
	public Optional<Customer> findByName(String name);
	
	public Optional<Customer> findByMobile(String mobile);
	
	@Query("SELECT t FROM Customer t where t.name = ?1 OR t.email = ?1")
    public List<Customer> searchCustomerInfo(String searchText);
}




