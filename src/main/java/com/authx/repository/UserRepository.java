package com.authx.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.authx.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

       Optional<UserEntity> findByEmail(String email);
       Optional<UserEntity> findById(Long id);
       Boolean existsByEmail(String email);

}
