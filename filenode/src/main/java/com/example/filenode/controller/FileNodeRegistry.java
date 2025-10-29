package com.example.filenode.controller;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryOneTime;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class FileNodeRegistry {

  private final CuratorFramework client;
  private final String nodePath;

  private final String nodeId = System.getenv("POD_NAME");

  public FileNodeRegistry() throws Exception {

    client = CuratorFrameworkFactory.builder()
            .connectString(System.getenv("ZK_CONNECT_STRING"))
            .retryPolicy(new ExponentialBackoffRetry(1000, 10))
            .build();
    client.start();
    if (!client.blockUntilConnected(10, TimeUnit.SECONDS)) {
      throw new RuntimeException("Cannot connect to ZooKeeper");
    }

    nodePath = "/file-nodes/" + nodeId;
    // Create ephemeral node with nodeUrl as data
    client.create().creatingParentsIfNeeded().withMode(
            org.apache.zookeeper.CreateMode.EPHEMERAL
    ).forPath(nodePath, nodeId.getBytes());
  }

  public void close() {
    client.close();
  }
}
