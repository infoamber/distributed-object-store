package org.example.storageservice.accessor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
public class FileMapperAccessor {
  private final RestTemplate restTemplate = new RestTemplate();

  private String baseUrl = "http://filemapper:8080"; // kubernetes should be able to resolve it automatically.

  public String getNodeUrlForFile(String filename) {
    String url = String.format("%s/getNodeForFile?filename=%s", baseUrl, filename);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, headers);

    ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            String.class
    );

    return response.getBody();
  }

  public List<String> getAllNodeUrlForFiles() {
    String url = String.format("%s/listNodes", baseUrl);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(null, headers);

    ResponseEntity<List<String>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<String>>() {}
    );

    return response.getBody();
  }
}
