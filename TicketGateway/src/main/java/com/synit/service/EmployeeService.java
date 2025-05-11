package com.synit.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.synit.domain.Employee;
import com.synit.domain.Role;
import com.synit.repository.EmployeeRepository;
import com.synit.repository.RoleRepository;

@Service
public class EmployeeService {
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	public List<Employee> findAll(){
		return employeeRepository.findAll();
	}
	
	public Employee saveEmployee(Employee employee) {
		HashSet<Role> roleSet = new HashSet<>();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(employee.getPassword());
		employee.setPassword(hashedPassword);
		Role employeeRole = roleRepository.findById((long)1).orElse(null);
		roleSet.add(employeeRole);
		employee.setRoles(roleSet);
		Employee savedEmployee = employeeRepository.save(employee);
		return savedEmployee;
	}
	
	public Employee findByEmail(String email) {
		return employeeRepository.findByEmail(email);
	}
	
	public Employee findByEmployeeId(long id) {
		return employeeRepository.findById(id).orElse(null);
	}
	
	public List<String> findByManagerId(long managerId){
		List<Employee> employees = employeeRepository.findByManagerId("" +managerId);
		List<String> emails = employees.stream().map(Employee::getEmail).toList();
		return emails;
	}

}
