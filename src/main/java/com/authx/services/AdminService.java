package com.authx.services;

import java.util.List;

import com.authx.userdto.AdminResponse;

public interface AdminService {

    List<AdminResponse> getAllUsers();

    void deleteUserById(Long id);
}
