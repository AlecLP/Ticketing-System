package com.synit.service;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.synit.domain.Role;
import com.synit.domain.User;
import com.synit.repository.RoleRepository;
import com.synit.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	
	public User getUserByEmail(String email) {
		return userRepository.findById(email).orElse(null);
	}
	
	public User saveUser(User user) {
		HashSet<Role> roleSet = new HashSet<>();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		Role employeeRole = roleRepository.findById((long)1).orElse(null);
		roleSet.add(employeeRole);
		user.setRoles(roleSet);
		User savedUser = userRepository.save(user);
		return savedUser;
	}

}
