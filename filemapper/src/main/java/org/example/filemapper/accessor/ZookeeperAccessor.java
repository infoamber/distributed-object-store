package org.example.filemapper.accessor;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ZookeeperAccessor {

  private final CuratorFramework client;

  public ZookeeperAccessor() {
    client = CuratorFrameworkFactory.newClient(
            System.getenv("ZK_CONNECT_STRING"),
            new ExponentialBackoffRetry(1000, 3)
    );
    client.start();
  }

  public List<String> getAliveFileNodes() throws Exception {
    return client.getChildren().forPath("/file-nodes");
  }

  public String getNodeData(String nodeId) throws Exception {
    byte[] data = client.getData().forPath("/file-nodes/" + nodeId);
    return new String(data);
  }

  public Map<String, String> getAliveNodesWithData() throws Exception {
    Map<String, String> result = new HashMap<>();

    List<String> nodes = client.getChildren().forPath("/file-nodes");
    for (String nodeId : nodes) {
      byte[] dataBytes = client.getData().forPath("/file-nodes/" + nodeId);
      result.put(nodeId, new String(dataBytes));
    }

    return result;
  }
}
