package com.api.imagestorage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.imagestorage.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
