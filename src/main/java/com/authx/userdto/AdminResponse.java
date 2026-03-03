package com.authx.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {

   private String userId;
   private String name;
   private boolean isAccountVerified;
   private String email;

}
