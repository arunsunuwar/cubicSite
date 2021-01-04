package com.rab3tech.admin.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rab3tech.dao.entity.AccountType;

public interface AccountTypeRepository extends JpaRepository<AccountType, Integer> {

	public Optional<AccountType> findByName(String name);
	public Optional<AccountType> findByNameAndCode(String name,String code);
	
	 @Query("SELECT distinct a.name FROM AccountType a") 
	 public List<String> findMyAccountType();
}

