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

import com.synit.domain.Role;
import com.synit.domain.User;

@Service
public class UserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userService.getUserByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException(email);
		}
		Set<GrantedAuthority> ga = new HashSet<>();
		Set<Role> roles = user.getRoles();
		for(Role role : roles) {
			ga.add(new SimpleGrantedAuthority(role.getRoleName().name()));
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), ga);
	}
	
	

}
