package com.authx.controller;

import java.util.List;
import java.util.Optional;

import com.authx.services.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.authx.entity.UserEntity;
import com.authx.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin APIs",description = "Admin can perform operation granted to ROLE_ADMIN")
public class AdminController {

    
    private final UserRepository userRepository;
    private final AdminService adminService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get All users form Database (Only Admin can access)")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete user with given id (Only Admin can access)")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){

        adminService.deleteUserById(id);

        return ResponseEntity.ok().body("User Deleted successfully!");

    }
    
}
