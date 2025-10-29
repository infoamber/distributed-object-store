package org.example.storageservice.service;

import org.springframework.stereotype.Component;

@Component
public class FileNodeServiceDiscovery {
  public String fetchNodeAddress(String nodeId) {
    return String.format("http://%s.file-node:8080", nodeId);
  }
}
