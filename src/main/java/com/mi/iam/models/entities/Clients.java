package com.mi.iam.models.entities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Clients {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(length = 36, insertable = false, updatable = false, nullable = false)
  private String id;

  @Column(name = "client_id", unique = true, length = 32, nullable = false)
  private String clientId;

  @Column(length = 64, nullable = false)
  private String name;

  @Column(length = 128)
  private String description;

  @Column(name = "is_active", length = 1, nullable = false)
  private Integer isActive;

  @Column(name = "web_origins", length = 128)
  private String webOrigins;

  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
    createdAt = now.toLocalDateTime();
    updatedAt = now.toLocalDateTime();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime();
  }
}