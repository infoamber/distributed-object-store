package org.example.filemapper;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class FileMapperController {

  /*
    Currently  this is hardcoded values as per configuration and pods in File node service.
    We know the sticky dns for the nodes. This File mapper service will be running in same kube cluster,
    So those fileNodes pods will be accessible by this service using DNS.

    As a followup, FileNodes should register themselves in a service registry and announce their ip or so,
    FileMapper should be able to look into registry and figure out fileNodes cluster information.
   */
  private final List<String> nodes = Arrays.asList(
          "http://file-node-0.file-node:8080",
          "http://file-node-1.file-node:8080",
          "http://file-node-2.file-node:8080"
  );

  @GetMapping("/listNodes")
  public List<String> listNodes() {
    return nodes;
  }

  @GetMapping("/getNodeForFile")
  public String getNodeForFile(@RequestParam(name = "filename", required = true) String filename) {
    // Currently a simple implementation to decide node based on fileName
    // Later we will move to persist uuid mapping or deterministic uuid generation options.
    int hash = Math.abs(filename.hashCode());
    String node = nodes.get(hash % nodes.size()); // Simple hash based for now. Later we will move to consistent hashing or other options.
    return node;
  }
}
