package org.example.filemapper;
import org.example.filemapper.accessor.ZookeeperAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class FileMapperController {

  @Autowired
  private final ZookeeperAccessor zookeeperAccessor;

  /*
    Currently  this is hardcoded values as per configuration and pods in File node service.
    We know the sticky dns for the nodes. This File mapper service will be running in same kube cluster,
    So those fileNodes pods will be accessible by this service using DNS.

    As a followup, FileNodes should register themselves in a service registry and announce their ip or so,
    FileMapper should be able to look into registry and figure out fileNodes cluster information.
   */
  private final List<String> nodes = Arrays.asList(
          "file-node-0",
          "file-node-1",
          "file-node-2"
  );

  public FileMapperController(ZookeeperAccessor zookeeperAccessor) {
    this.zookeeperAccessor = zookeeperAccessor;
  }

  @GetMapping("/listNodes")
  public List<String> listHealthyNodes() throws Exception {
    Map<String, String> fileNodeDetails = zookeeperAccessor.getAliveNodesWithData();
    return fileNodeDetails.values().stream().toList();
  }

  /*
    Returns nodes address, where file should be kept of read from.
    TODO : Update logic to return replica nodes as well.
   */
  @GetMapping("/getNodeForFile")
  public String getNodeForFile(@RequestParam(name = "filename", required = true) String filename) {
    // Currently a simple implementation to decide node based on fileName
    // Later we will move to persist uuid mapping or deterministic uuid generation options.
    int hash = Math.abs(filename.hashCode());
    String node = nodes.get(hash % nodes.size()); // Simple hash based for now. Later we will move to consistent hashing or other options.
    return node;
  }
}
