package com.mi.iam.models.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.mi.iam.models.entities.Clients;

import jakarta.transaction.Transactional;

public interface ClientsRepository extends JpaRepository<Clients, String> {
  @Query("SELECT c FROM Clients c " +
         "WHERE c.isActive = 1" +
         "AND (LOWER(c.clientId) LIKE %:searchTerm% " +
         "OR LOWER(c.name) LIKE %:searchTerm% " +
         "OR LOWER(c.description) LIKE %:searchTerm% " +
         "OR LOWER(c.webOrigins) LIKE %:searchTerm%)")
  Page<Clients> getAllQuery(String searchTerm, Pageable pageable);

  Optional<Clients> findByIdAndIsActive(String id, Integer isActive);

  @Modifying(clearAutomatically=true, flushAutomatically = true)
  @Transactional
  @Query(value = "UPDATE clients SET is_active = 0 WHERE id = ?1", nativeQuery = true)
  int disableClient(String clientId);
}