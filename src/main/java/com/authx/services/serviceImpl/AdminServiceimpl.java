package com.authx.services.serviceImpl;

import java.util.List;

import com.authx.entity.UserEntity;
import com.authx.model.Role;
import com.authx.services.AdminService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.authx.mapper.UserMapper;
import com.authx.repository.UserRepository;
import com.authx.userdto.AdminResponse;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AdminServiceimpl implements AdminService {

    private final UserRepository userRepo;
    
    @Override
    public List<AdminResponse> getAllUsers() {
          return userRepo.findAll().stream()
                            .map(UserMapper::toDTO)
                            .toList();    
    }

    @Override
    public void deleteUserById(Long id) {
          UserEntity existingUser = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(existingUser.getRole() == Role.ROLE_ADMIN){
            throw new RuntimeException("Admin account cannot be deleted");
        }
         userRepo.deleteById(id);

    }

}
