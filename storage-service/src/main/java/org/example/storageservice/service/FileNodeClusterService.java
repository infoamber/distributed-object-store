package org.example.storageservice.service;

import org.example.storageservice.accessor.FileMapperAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileNodeClusterService {
  @Autowired
  private final FileMapperAccessor fileMapperAccessor;
  private final FileNodeServiceDiscovery fileNodeServiceDiscovery;

  public FileNodeClusterService(FileMapperAccessor fileMapperAccessor, FileNodeServiceDiscovery fileNodeServiceDiscovery) {
    this.fileMapperAccessor = fileMapperAccessor;
    this.fileNodeServiceDiscovery = fileNodeServiceDiscovery;
  }

  public List<String> getAllHealthyNodesAddresses() {
    List<String> nodeIds = fileMapperAccessor.getAllHealthyNodeIdsForFiles();
    List<String> nodeAddresses = new ArrayList<>();
    for(String nodeID: nodeIds) {
      nodeAddresses.add(fileNodeServiceDiscovery.fetchNodeAddress(nodeID));
    }
    return nodeAddresses;
  }

  public List<String> getAllHealthyNodesIds() {
    List<String> nodeIds = fileMapperAccessor.getAllHealthyNodeIdsForFiles();
    return nodeIds;
  }
}
