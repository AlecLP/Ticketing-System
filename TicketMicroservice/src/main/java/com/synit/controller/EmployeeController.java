package com.synit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.synit.domain.Employee;
import com.synit.service.EmployeeService;

@RestController
@CrossOrigin(origins="http://localhost:8181")
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@PostMapping("/createEmployee")
	public ResponseEntity<String> createEmployee(@RequestBody Employee employee){
		employeeService.saveEmployee(employee);
		return ResponseEntity.ok("Employee created.");
	}

}
