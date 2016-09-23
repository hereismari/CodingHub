package com.es.codinghub.api.entities;

import com.es.codinghub.api.facade.OnlineJudge;

import org.json.JSONString;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class Contest implements JSONString {

    private OnlineJudge judge;
    private String name;

    private long timestamp;
    private long duration;

    public Contest() {}

    public Contest(OnlineJudge judge, String name, long timestamp, long duration) {
        this.judge = judge;
        this.name = name;
        this.timestamp = timestamp;
        this.duration = duration;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Contest == false)
            return false;
        Contest other = (Contest) obj;
        return	judge.equals(other.judge) &&
                name.equals(other.name);
    }

    @Override
    public int hashCode() {
        int hash = 0, prime = 31;
        hash = hash * prime + judge.hashCode();
        hash = hash * prime + name.hashCode();
        return hash;
    }

    @Override
    public String toJSONString() {
        JSONWriter stringer = new JSONStringer()
            .object()
                .key("name").value(name)
                .key("judge").value(judge)
                .key("timestamp").value(timestamp)
                .key("duration").value(duration)
            .endObject();

        return stringer.toString();
    }
}
