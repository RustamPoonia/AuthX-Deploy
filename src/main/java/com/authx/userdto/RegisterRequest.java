package com.authx.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Name should not be empty")
    private String name;
    @Email(message = "Enter valid email address")
    @NotBlank(message = "Email should not be empty")
    private String email;
    @Size(min=6,message="Password must be atleast 6 character")
    private String password;
    // private String conformPassword;
}
