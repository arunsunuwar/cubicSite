package com.rab3tech.customer.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rab3tech.dao.entity.Location1;

public interface Location1Repository  extends JpaRepository<Location1, Integer> {
	
	
	  @Query("SELECT distinct l.location FROM Location1 l") 
	  public List<String> findMyLocation();
}