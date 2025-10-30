package org.example.filemapper.clusterMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;


// TokenRange uses unsigned 64-bit values stored in long
@ToString
public class TokenRange implements Serializable {

    private static final long serialVersionUID = 1L;
    private final long start; // inclusive (unsigned)
    private final long end;   // exclusive (unsigned)
    private final String owner; // node id (e.g., "10.0.0.1:9000")

    @JsonCreator
    public TokenRange(@JsonProperty("start") long start,
                      @JsonProperty("end") long end,
                      @JsonProperty("owner") String owner) {
        this.start = start;
        this.end = end;
        this.owner = Objects.requireNonNull(owner);
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public String getOwner() {
        return owner;
    }

    // check if unsignedHash in [start, end)
    public boolean contains(long unsignedHash) {
        if (start <= end) {
            return unsignedHash >= start && unsignedHash < end;
        } else {
            // wrap-around range (start > end)
            return unsignedHash >= start || unsignedHash < end;
        }
    }

    @Override
    public String toString() {
        return "TokenRange{" + Long.toUnsignedString(start) + "," + Long.toUnsignedString(end) + "->" + owner + "}";
    }
}

