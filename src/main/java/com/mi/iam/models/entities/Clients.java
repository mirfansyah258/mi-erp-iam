package com.mi.iam.models.entities;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

  @NotBlank
  @Column(name = "client_id", unique = true, length = 32, nullable = false)
  private String clientId;

  @NotBlank
  @Column(length = 64, nullable = false)
  private String name;

  @Column(length = 128)
  private String description;

  @NotNull
  @Min(value = 0, message = "is_active must be at least 0")
  @Max(value = 1, message = "is_active cannot exceed 1")
  @Column(name = "is_active", length = 1, nullable = false)
  private Integer isActive;

  @Column(name = "web_origins", length = 128)
  private String webOrigins;

  @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP")
  private LocalDateTime updatedAt;

  @OneToMany(targetEntity = ClientRoles.class, mappedBy = "clients", orphanRemoval = false, fetch = FetchType.LAZY)
  @JsonIgnore
	private List<ClientRoles> roles;

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