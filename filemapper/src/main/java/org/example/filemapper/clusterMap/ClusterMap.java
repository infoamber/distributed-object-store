package org.example.filemapper.clusterMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@ToString
public class ClusterMap implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long version;
    private final Instant createdAt;
    private final List<TokenRange> ranges;

    @JsonCreator
    public ClusterMap(@JsonProperty("version") long version,
                      @JsonProperty("createdAt") Instant createdAt,
                      @JsonProperty("ranges") List<TokenRange> ranges) {
        this.version = version;
        this.createdAt = createdAt;
        this.ranges = List.copyOf(ranges);
    }

    public long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<TokenRange> getRanges() {
        return ranges;
    }
}

