package com.mi.iam.models.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.mi.iam.models.entities.Users;

import javax.transaction.Transactional;

public interface UsersRepository extends JpaRepository<Users, String> {
  @Query("SELECT c FROM Users c " +
         "WHERE c.isActive = 1" +
         "AND (LOWER(c.username) LIKE %:searchTerm% " +
         "OR LOWER(c.email) LIKE %:searchTerm% " +
         "OR LOWER(c.firstname) LIKE %:searchTerm% " +
         "OR LOWER(c.lastname) LIKE %:searchTerm%)")
  Page<Users> getAllQuery(String searchTerm, Pageable pageable);

  Optional<Users> findByIdAndIsActive(String id, Integer isActive);

  @Modifying(clearAutomatically=true, flushAutomatically = true)
  @Transactional
  @Query(value = "UPDATE Users SET is_active = 0 WHERE id = ?1", nativeQuery = true)
  int disableUser(String UserId);

  @Modifying(clearAutomatically=true, flushAutomatically = true)
  @Transactional
  @Query(value = "UPDATE Users SET is_email_verified = 1 WHERE id = ?1", nativeQuery = true)
  int verifyEmailUser(String UserId);
}