package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rab3tech.dao.entity.PayeeInfo;


public interface PayeeRepository extends JpaRepository<PayeeInfo, Integer>  {

	@Query("SELECT t FROM PayeeInfo t where t.payeeStatus.id = 1  and t.customerId=?1") 
	List<PayeeInfo> findPendingPayee(String customerId);
	
	@Query("SELECT t FROM PayeeInfo t where t.payeeStatus.id = 2 and t.customerId=?1") 
	List<PayeeInfo> findApprovedPayee(String customerId);
	
	/*
	 * @Modifying
	 * 
	 * @Query("DELETE FROM PayeeInfo t where t.id = ?1") void rejectPayee(int cid);
	 */
	
	
	@Query("SELECT t FROM PayeeInfo t where t.id = ?1") 
	Optional<PayeeInfo> findURNbyId(int cid);
	
	@Modifying
	@Query("UPDATE PayeeInfo t SET t.payeeStatus.id = 2 where t.id = ?1") 
	void updateToApproved(int cid);
	
	@Modifying
	@Query("UPDATE PayeeInfo t SET t.payeeStatus.id = 3 where t.id = ?1") 
	void rejectPayee(int cid);
	
	public Optional<PayeeInfo> findByUrnAndId(int urn,int id);
}
