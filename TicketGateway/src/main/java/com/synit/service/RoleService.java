package com.synit.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synit.domain.Role;
import com.synit.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	RoleRepository roleRepository;
	
	public Set<Role> getDefaultRole(){
		Role role = roleRepository.findById((long)1).orElse(null);
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		return roles;
	}

}
