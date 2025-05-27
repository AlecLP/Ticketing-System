package com.synit.controller;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.synit.domain.Role;
import com.synit.domain.User;
import com.synit.dtos.SignUpDto;
import com.synit.service.RoleService;
import com.synit.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	
	@PostMapping(value = "/signup")
	public String signup(@RequestBody SignUpDto info) {
		System.out.println("Signing up User");
		String email = info.getEmail();
		String password = info.getPassword();
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		Set<Role> role = roleService.getDefaultRole();
		user.setRoles(role);
		userService.saveUser(user);
		System.out.println("Signed up User: " +email);
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "signup";
	}
	
	@GetMapping("/login")
	public String login(@RequestParam(required=false) String logout, @RequestParam(required=false) String error,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model) {
		String message = "";
		if(error != null) {
			message = "Invalid Credentials";
		}
		if(logout != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth != null) {
				new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
			}
			message = "Logged out";
			return "login";
		}
		model.addAttribute("Message", message);
		return "login";
	}
	
	@GetMapping("/employeeDashboard")
	public String employeeDashboard(Principal principal, Model model) {
		model.addAttribute("name", principal.getName());
		return "employeeDashboard";
	}
	
	@GetMapping(value = "/accessDeniedPage")
	public String accessDenied(Principal principal, Model model) {
		String message = principal.getName() + ", Unauthorized access";
		model.addAttribute("Message", message);
		return "accessDeniedPage";
	}

}
