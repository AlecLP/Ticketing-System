package com.synit.component;

import java.io.IOException;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synit.common_dtos.TicketCreateDto;
import com.synit.common_dtos.TicketDecisionsDto;
import com.synit.common_dtos.TicketDto;
import com.synit.common_dtos.TicketHistoryDto;

@Component
public class TicketMicroserviceClient {
	
	private static final String TICKET_POST_URL = "http://localhost:8282/createTicket";
	private static final String TICKET_GET_BY_EMAILS_URL = "http://localhost:8282/getByEmails";
	private static final String TICKET_PROCESS_TICKETS_URL = "http://localhost:8282/processApprovalsAndRejections";
	private static final String TICKET_GET_ADMIN_TICKETS_URL = "http://localhost:8282/getAdminTickets";
	private static final String TICKET_SEND_ADMIN_DECISIONS ="http://localhost:8282/processAdminDecisions";
	private static final String TICKET_HISTORY_URL = "http://localhost:8282/getTicketHistory";
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RestTemplate restTemplate = new RestTemplate();
	
	public void sendCreateTicketRequest(TicketCreateDto ticketDto, MultipartFile file) throws IOException {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		
		HttpHeaders jsonHeaders = new HttpHeaders();
		jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> dtoPart = new HttpEntity<>(objectMapper.writeValueAsString(ticketDto), jsonHeaders);
		body.add("ticket", dtoPart);
		
		if(file != null && !file.isEmpty()) {
			HttpHeaders fileHeaders = new HttpHeaders();
			fileHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
			InputStreamResource resource = new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename(), file.getSize());
			HttpEntity<InputStreamResource> filePart = new HttpEntity<>(resource, fileHeaders);
			body.add("file", filePart);
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		restTemplate.postForEntity(TICKET_POST_URL, requestEntity, Void.class);
	}
	
	public List<TicketDto> sendGetByEmailsRequest(List<String> emails) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<List<String>> request = new HttpEntity<>(emails, headers);
		ResponseEntity<List<TicketDto>> response = restTemplate.exchange(
				TICKET_GET_BY_EMAILS_URL,
				HttpMethod.POST,
				request,
				new ParameterizedTypeReference<List<TicketDto>>() {}
		);
		
		return response.getBody();
	}
	
	public void sendProcessTicketsRequest(TicketDecisionsDto dto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<TicketDecisionsDto> request = new HttpEntity<>(dto, headers);
		restTemplate.postForEntity(TICKET_PROCESS_TICKETS_URL, request, Void.class);
	}
	
	public List<TicketDto> sendGetAdminTicketsRequest(String email) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> request = new HttpEntity<>(email, headers);
		ResponseEntity<List<TicketDto>> response = restTemplate.exchange(
				TICKET_GET_ADMIN_TICKETS_URL,
				HttpMethod.POST,
				request,
				new ParameterizedTypeReference<List<TicketDto>>() {}
		);
		return response.getBody();
	}
	
	public void sendAdminDecisions(TicketDecisionsDto dto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<TicketDecisionsDto> request = new HttpEntity<>(dto, headers);
		restTemplate.postForEntity(TICKET_SEND_ADMIN_DECISIONS, request, Void.class);
	}
	
	public List<TicketHistoryDto> sendGetTicketHistoryRequest(Long id){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<Long> request = new HttpEntity<>(id, headers);
		ResponseEntity<List<TicketHistoryDto>> response = restTemplate.exchange(
				TICKET_HISTORY_URL,
				HttpMethod.POST,
				request,
				new ParameterizedTypeReference<List<TicketHistoryDto>>() {}
		);
		
		return response.getBody();
	}
}
