package org.example.filemapper.clusterMap;

import org.example.filemapper.accessor.ZookeeperAccessor;
import org.example.filemapper.utils.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Given a value / object key. find which token range it lies in and with it's owner.
 */
@Component
public class TokenRangeFinder {

    @Autowired
    private final ZookeeperAccessor zookeeperAccessor;

    public TokenRangeFinder(ZookeeperAccessor zookeeperAccessor) {
        this.zookeeperAccessor = zookeeperAccessor;
    }

    public String findOwner(String objectKey) throws Exception {
        long hashValue = HashUtil.hash64(objectKey);
        List<TokenRange> tokenRanges = zookeeperAccessor.fetchLatestClusterMap().getRanges();
        for (TokenRange range : tokenRanges) {
            if (range.contains(hashValue)) {
                return range.getOwner();
            }
        }
        return null; // not found
    }
}

