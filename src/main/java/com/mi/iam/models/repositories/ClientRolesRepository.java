package com.mi.iam.models.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mi.iam.models.entities.ClientRoles;

public interface ClientRolesRepository extends JpaRepository<ClientRoles, String> {
  @Query("SELECT cr FROM ClientRoles cr " +
         "WHERE LOWER(cr.name) LIKE %:searchTerm% " +
         "OR LOWER(cr.description) LIKE %:searchTerm%")
  Page<ClientRoles> getAllQuery(String searchTerm, Pageable pageable);
}