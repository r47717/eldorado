package ru.r47717.eldorado.core.router;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class RouterEntry {
    public boolean isClosure;
    String method;
    String pattern;
    public Class controller;
    public String fn;
    public Function<String, String> closure;
    public Map<Integer, SegmentData> segments;

    RouterEntry() {
        this.isClosure = false;
        this.controller = null;
        this.closure = null;
        this.segments = new HashMap<>();
    }

    RouterEntry(RouterEntry that) {
        this.isClosure = that.isClosure;
        this.method = that.method;
        this.pattern = that.pattern;
        this.controller = that.controller;
        this.fn = that.fn;
        this.closure = that.closure;
        this.segments = new HashMap<>();
        that.segments.forEach((k, v) -> this.segments.put(k, new SegmentData(v)));
    }
}
