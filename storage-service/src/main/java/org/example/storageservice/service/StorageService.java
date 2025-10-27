package org.example.storageservice.service;

import org.example.storageservice.accessor.FileMapperAccessor;
import org.example.storageservice.accessor.FileNodeAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageService {
  @Autowired
  private final FileMapperAccessor fileMapperAccessor;
  @Autowired
  private final FileNodeAccessor fileNodeAccessor;

  public StorageService(FileMapperAccessor fileMapperAccessor, FileNodeAccessor fileNodeAccessor) {
    this.fileMapperAccessor = fileMapperAccessor;
    this.fileNodeAccessor = fileNodeAccessor;
  }

  public String writeJson(String filename, String json) {
    String nodeAddress = fileMapperAccessor.getNodeUrlForFile(filename);
    fileNodeAccessor.writeJson(filename, json, nodeAddress);
    return "Data written to node : " + nodeAddress;
  }

  public String readFile(String filename) {
    String nodeAddress = fileMapperAccessor.getNodeUrlForFile(filename);
    return fileNodeAccessor.readFile(filename, nodeAddress);
  }

  public Map<String, List<String>> getAllFiles() {
    List<String> nodeAddresses = fileMapperAccessor.getAllNodeUrlForFiles();
    Map<String, List<String>> allFiles = new HashMap<>();
    for (String nodeAddress: nodeAddresses) {
      List<String> files = fileNodeAccessor.fetchAllFilesList(nodeAddress);
      allFiles.put(nodeAddress, files);
    }
    return allFiles;
  }
}
