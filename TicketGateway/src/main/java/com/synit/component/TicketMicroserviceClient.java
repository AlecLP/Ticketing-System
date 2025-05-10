package com.synit.component;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synit.common_dtos.TicketCreateDto;

@Component
public class TicketMicroserviceClient {
	
	private static final String TICKET_POST_URL = "http://localhost:8282/createTicket";
	private final ObjectMapper objectMapper = new ObjectMapper();
	
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
		new RestTemplate().postForEntity(TICKET_POST_URL, requestEntity, Void.class);
	}

}
