package org.example.storageservice.accessor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class FileNodeAccessor {
  private final RestTemplate restTemplate = new RestTemplate();
  public String writeJson(String filename, String json, String nodeAddress) {
    String baseUrl = nodeAddress;
    String url = String.format("%s/write-json?filename=%s", baseUrl, filename);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(json, headers);

    ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
    );

    return response.getStatusCode().toString();

  }

  public String readFile(String filename, String nodeAddress) {
    String baseUrl = nodeAddress;
    String url = String.format("%s/read-file?filename=%s", baseUrl, filename);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
    );

    return response.getBody();
  }

  public List<String> fetchAllFilesList(String nodeAddress) {
    String baseUrl = nodeAddress;
    String url = String.format("%s/files", baseUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(null, headers);

    ResponseEntity<List<String>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<String>>() {}
    );

    return response.getBody();
  }

}
