package com.synit.common_dtos;

import java.util.Map;

public class TicketDecisionsDto {
	
	private Map<Long, String> decisions;
	private EmployeeDto assignee;
	private EmployeeDto employee;

	public EmployeeDto getAssignee() {
		return assignee;
	}

	public void setAssignee(EmployeeDto assignee) {
		this.assignee = assignee;
	}

	public EmployeeDto getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeDto employee) {
		this.employee = employee;
	}

	public Map<Long, String> getDecisions() {
		return decisions;
	}

	public void setDecisions(Map<Long, String> decisions) {
		this.decisions = decisions;
	}
	
	public TicketDecisionsDto() {}

}
