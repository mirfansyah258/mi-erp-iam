package com.mi.iam.models.entities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "client_roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRoles {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(length = 36, insertable = false, updatable = false, nullable = false)
  private String id;

  @Column(name = "client_id", length = 36, nullable = false)
  private String clientId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", referencedColumnName = "id", insertable = false, updatable = false)
  @Fetch(FetchMode.JOIN)
  private Clients clients;

  @NotBlank
  @Column(length = 64, nullable = false)
  private String name;

  @Column(length = 128)
  private String description;

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