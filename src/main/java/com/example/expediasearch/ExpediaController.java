package com.example.expediasearch;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/api/offers")
@Validated
public class ExpediaController {

	@Value("${expedia.api.url}")
	private String expediaApiUrl;

	private final Logger logger = LoggerFactory.getLogger(ExpediaController.class);

	//////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	@GetMapping
	public ResponseEntity<Map<String, Object>> getOffers(
			@RequestParam @NotEmpty(message = "Origin city is required") String originCity,
			@RequestParam @NotEmpty(message = "Destination city is required") String destinationCity) {
		try {
			logger.info("getOffers({},{})", originCity, destinationCity);
			String apiUrl = expediaApiUrl + "&originCity=" + originCity + "&destinationCity=" + destinationCity;
			RestTemplate restTemplate = new RestTemplate();
			Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);
			logger.info("API called successfully");
			return ResponseEntity.ok(response);
		} finally {
			logger.info("/getOffers({},{})", originCity, destinationCity);
		}
	}
}