package org.example.filemapper;
import org.example.filemapper.accessor.ZookeeperAccessor;
import org.example.filemapper.clusterMap.ClusterMap;
import org.example.filemapper.clusterMap.ClusterMapBuilder;
import org.example.filemapper.clusterMap.TokenRangeFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FileMapperController {

  @Autowired
  private final ZookeeperAccessor zookeeperAccessor;

  @Autowired
  private final TokenRangeFinder tokenRangeFinder;

  /*
    Currently  this is hardcoded values as per configuration and pods in File node service.
    We know the sticky dns for the nodes. This File mapper service will be running in same kube cluster,
    So those fileNodes pods will be accessible by this service using DNS.

    As a followup, FileNodes should register themselves in a service registry and announce their ip or so,
    FileMapper should be able to look into registry and figure out fileNodes cluster information.
   */

  public FileMapperController(ZookeeperAccessor zookeeperAccessor, TokenRangeFinder tokenRangeFinder) {
    this.zookeeperAccessor = zookeeperAccessor;
    this.tokenRangeFinder = tokenRangeFinder;
  }

  @GetMapping("/listHealthyNodes")
  public List<String> listHealthyNodes() throws Exception {
    Map<String, String> fileNodeDetails = zookeeperAccessor.getAliveNodesWithData();
    return fileNodeDetails.values().stream().toList();
  }

  /*
    Returns nodes address, where file should be kept of read from.
    TODO : Update logic to fetch node from cluster map.
   */
  @GetMapping("/getNodeForFile")
  public String getNodeForFile(@RequestParam(name = "filename", required = true) String filename) throws Exception {
    return tokenRangeFinder.findOwner(filename);

  }

  @PostMapping("/buildClusterMap")
  public String buildClusterMap() throws Exception {
    ClusterMap clusterMap = ClusterMapBuilder.buildClusterMap();
    zookeeperAccessor.storeClusterMap(clusterMap);
    return  clusterMap.toString();
  }
}
