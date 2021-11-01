package com.lcc.monastery.service.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcc.monastery.advice.exception.CommunicationErrorException;
import com.lcc.monastery.model.recaptcha.VerifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RecaptchaService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${google.recaptcha.key.secret}")
    private String secretKey;

    @Value("${google.recaptcha.key.url}")
    private String recaptchaUrl;

    public Boolean verifyRecaptcha(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("secret", secretKey);
            params.add("response", token);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(recaptchaUrl, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK)
                return objectMapper.readValue(response.getBody(), VerifyResponse.class).isSuccess();
        } catch (Exception e) {
            throw new CommunicationErrorException();
        }
        throw new CommunicationErrorException();
    }
}
