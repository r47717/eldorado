package ru.r47717.eldorado.core.router;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class RouterEntry {
    String name;
    boolean isClosure;
    String method;
    String pattern;
    Class controller;
    String fn;
    Function<String, String> closure;
    Map<Integer, SegmentData> segments;

    RouterEntry() {
        this.isClosure = false;
        this.controller = null;
        this.closure = null;
        this.segments = new HashMap<>();
    }

    RouterEntry(RouterEntry that) {
        this.name = that.name;
        this.isClosure = that.isClosure;
        this.method = that.method;
        this.pattern = that.pattern;
        this.controller = that.controller;
        this.fn = that.fn;
        this.closure = that.closure;
        this.segments = new HashMap<>();
        that.segments.forEach((k, v) -> this.segments.put(k, new SegmentData(v)));
    }

    public String getName() {
        return name;
    }

    public boolean isClosure() {
        return isClosure;
    }

    public String getMethod() {
        return method;
    }

    public String getPattern() {
        return pattern;
    }

    public Class getController() {
        return controller;
    }

    public String getFn() {
        return fn;
    }

    public Function<String, String> getClosure() {
        return closure;
    }

    public Map<Integer, SegmentData> getSegments() {
        return segments;
    }
}
