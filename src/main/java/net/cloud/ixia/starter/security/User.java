package net.cloud.ixia.starter.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
  private String firstName;
  private String lastName;
  private String userName;
  private String uuid;
}
