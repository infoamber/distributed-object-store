package org.example.filemapper;

import java.util.Arrays;
import java.util.List;

/*
This class knows exactly how many and which nodes are currently configured to be in the system.
This should be a configuration based thing.
If node is added, It should follow these step :
1. Add in th list.
2. Rebuild ClusterMap (token range and owner node mapping)
3. Update ClusterMap with new version.
4. Data movement and read handling with new map.
 */
public class FileNodeTracker {

  private static List<String> nodes = Arrays.asList(
          "file-node-0",
          "file-node-1",
          "file-node-2",
          "file-node-3",
          "file-node-4",
          "file-node-5"
  );
  public static List<String> getActiveFileNodes() {
    return nodes;
  }

  public void addNodes(String nodeName) {
    // Will implement it later.

  }

  public static int getConfiguredVirtualNodes() {
    return nodes.size();
  }
}
