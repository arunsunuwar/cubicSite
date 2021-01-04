package com.rab3tech.admin.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rab3tech.dao.entity.Location;


public interface LocationRepository  extends JpaRepository<Location, Integer> {
	
	public Optional<Location> findById(int id);
	
	public Optional<Location> findByLcode(String lcode);
	
	/*
	 * @Query("SELECT distinct l.location FROM Location l") public List<String>
	 * findMyLocation();
	 */

}
