package com.mi.iam.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersChangePassword {
  private String oldPassword;
  private String newPassword;
}
