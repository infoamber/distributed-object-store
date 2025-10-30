package org.example.filemapper.clusterMap;

import com.google.common.hash.Hashing;
import org.example.filemapper.FileNodeTracker;
import org.example.filemapper.utils.HashUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

/*
  Currently it will build and store/replace cluster map in zookeeper.
  TODO : Move it to standalone app. Increase version everytime, map is rebuilt and notify who ever needs it.
 */
public class ClusterMapBuilder {

    public static ClusterMap buildClusterMap() {
      List<String> nodes = FileNodeTracker.getActiveFileNodes();
      int virtualNodes = FileNodeTracker.getConfiguredVirtualNodes();
        if (nodes == null || nodes.isEmpty())
            throw new IllegalArgumentException("nodes cannot be empty");

        if (virtualNodes <= 0)
            virtualNodes = 0; // default to 0 for now

        // 1️⃣ Build list of (tokenHash, node)
        TreeMap<Long, String> ring = new TreeMap<>();
        for (String node : nodes) {
            for (int i = 0; i < virtualNodes; i++) {
                String vnodeId = node + "#" + i;
                long token = HashUtil.hash64(vnodeId);
                ring.put(token, node);
            }
        }

        // 2️⃣ Create token ranges from sorted tokens
        List<TokenRange> ranges = new ArrayList<>();
        List<Map.Entry<Long, String>> tokens = new ArrayList<>(ring.entrySet());

        for (int i = 0; i < tokens.size(); i++) {
            long start = tokens.get(i).getKey();
            long end = (i + 1 < tokens.size()) ? tokens.get(i + 1).getKey() : 0L; // wrap around
            String owner = tokens.get(i).getValue();
            ranges.add(new TokenRange(start, end, owner));
        }

        return new ClusterMap(1, Instant.now(), ranges);
    }


}

