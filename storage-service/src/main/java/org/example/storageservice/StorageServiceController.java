package org.example.storageservice;

import org.example.storageservice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class StorageServiceController {

  @Autowired
  StorageService storageService;

  @GetMapping("/hello")
  public String hello() {
    return "Hello from storage service";
  }

  @GetMapping("/files")
  public Map<String, List<String>> listFiles() {
    return storageService.getAllFiles();
  }


  @PostMapping("/write-json")
  public String writeJson(@RequestParam(name = "filename", required = true) String filename,
                          @RequestBody String json) {
    return storageService.writeJson(filename, json);

  }

  @GetMapping("/read-file")
  public String readFile(@RequestParam(name = "filename", required = true) String filename) {
    return storageService.readFile(filename);
  }

}
