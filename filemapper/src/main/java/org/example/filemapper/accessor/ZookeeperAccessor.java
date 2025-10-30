package org.example.filemapper.accessor;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.example.filemapper.clusterMap.ClusterMap;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public void storeClusterMap(ClusterMap clusterMap) throws Exception {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(clusterMap);
        oos.flush();

        String nodePath = "/cluster_map/cluster_map_";
        // Create ephemeral node with nodeUrl as data
        client.create().creatingParentsIfNeeded().withMode(
                CreateMode.PERSISTENT_SEQUENTIAL
        ).forPath(nodePath, bos.toByteArray());
    }

    public ClusterMap fetchLatestClusterMap() throws Exception {
        List<String> allMapNodes = client.getChildren().forPath("/cluster_map"); // get all nodes at /cluster_map/
        Optional<String> latestNode = allMapNodes.stream()
                .max(String::compareTo);

        if (latestNode.isEmpty()) {
            throw new RuntimeException("No cluster_map node exists");
        }

        String latestPath = "/cluster_map/" + latestNode.get();
        byte[] dataBytes = client.getData().forPath(latestPath);
        ByteArrayInputStream bis = new ByteArrayInputStream(dataBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (ClusterMap) ois.readObject();

    }
}
