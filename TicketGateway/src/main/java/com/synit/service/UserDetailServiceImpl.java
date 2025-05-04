package com.synit.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.synit.domain.Employee;
import com.synit.domain.Role;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	EmployeeService employeeService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Employee employee = employeeService.findByEmail(email);
		if(employee == null) {
			throw new UsernameNotFoundException(email);
		}
		Set<GrantedAuthority> ga = new HashSet<>();
		Set<Role> roles = employee.getRoles();
		for(Role role : roles) {
			ga.add(new SimpleGrantedAuthority(role.getRoleName().name()));
		}
		return new org.springframework.security.core.userdetails.User(employee.getEmail(), employee.getPassword(), ga);
	}
	
	

}
