package com.mezocode.healthcare.doctor.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaService {

    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String RECAPTCHA_SECRET = "6LdZhCEpAAAAAFofUjX2yyO9np1xjrSfuRju0Ykw"; // Replace with your secret key

    public String isResponseValid(String clientRecaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", RECAPTCHA_SECRET);
        form.add("response", clientRecaptchaResponse);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(RECAPTCHA_URL, HttpMethod.POST, request, String.class);

        return responseEntity.getBody();

    }
}