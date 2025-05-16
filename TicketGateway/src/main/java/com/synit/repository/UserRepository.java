package com.synit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.synit.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}
