package com.example.filenode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

  @Value("${DATA_BASE_PATH:/data}")
  private String basePath;

  @Value("${POD_NAME:local-node}")
  private String podName;

  @GetMapping("/hello")
  public String hello() {
    return "Hello from node " + podName + "! Data path: " + basePath + "/" + podName;
  }

  @GetMapping("/files")
  public List<String> listFiles() {
    File folder = new File(basePath, podName); // data/nodename
    if (!folder.exists() || !folder.isDirectory()) {
      return List.of();
    }
    return Arrays.stream(folder.listFiles())
            .map(File::getName)
            .collect(Collectors.toList());
  }

  @PostMapping("/write-json")
  public String writeJson(@RequestParam(name = "filename", required = true) String filename,
                          @RequestBody String json) {
    try {
      // Ensure folder exists
      File folder = new File(basePath, podName);
      if (!folder.exists()) {
        if (!folder.mkdirs()) {
          return "Failed to create folder: " + folder.getAbsolutePath();
        }
      }

      // Create the file if it doesn't exist
      File file = new File(folder, filename);
      if (!file.exists()) {
        if (!file.createNewFile()) {
          return "Failed to create file: " + file.getAbsolutePath();
        }
      }

      // Write JSON to file (overwrite)
      try (FileWriter writer = new FileWriter(file, false)) {
        writer.write(json);
      }

      return "JSON written to " + file.getAbsolutePath();

    } catch (IOException e) {
      e.printStackTrace();
      return "Failed to write JSON: " + e.getMessage();
    }
  }

  @GetMapping("/read-file")
  public String readFile(@RequestParam(name = "filename", required = true) String filename) {
      // Ensure folder exists
      File folder = new File(basePath, podName);
      if (!folder.exists()) {
        return "Failed to read folder: " + folder.getAbsolutePath();
      }

      File file = new File(folder, filename);
      if (!file.exists()) {
        return "No file exist: " + file.getAbsolutePath();
      }

      // read and return value
      StringBuilder content = new StringBuilder();
      try (FileReader fr = new FileReader(file);
           BufferedReader br = new BufferedReader(fr)) {

        String line;
        while ((line = br.readLine()) != null) {
          content.append(line);
        }

      } catch (IOException e) {
        e.printStackTrace();
        return "Failed to read JSON: " + e.getMessage();
      }


    return content.toString();
  }

}
