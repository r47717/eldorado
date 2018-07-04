package ru.r47717.eldorado.core.router;

import ru.r47717.eldorado.core.di.Container;
import ru.r47717.eldorado.core.di.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class Router implements RouterInterface {

    private Map<String, RouterEntry> routes = new HashMap<>();


    public RouterEntry retrieve(String route) {
        return getMatch(route);
    }


    public void get(String route, Class controller, String fn) {
        method("get", route, controller, fn);
    }

    public void get(String route, Function<String, String> closure) {
        method("get", route, closure);
    }

    public void post(String route, Class controller, String fn) {
        method("post", route, controller, fn);
    }

    public void put(String route, Class controller, String fn) {
        method("put", route, controller, fn);
    }

    public void delete(String route, Class controller, String fn) {
        method("delete", route, controller, fn);
    }

    public void method(String method, String pattern, Class controller, String fn) {
        if (routes.containsKey(pattern)) {
            return;
        }

        RouterEntry entry = new RouterEntry();
        entry.isClosure = false;
        entry.closure = null;
        entry.method = method;
        entry.pattern = pattern;
        entry.controller = controller;
        entry.fn = fn;
        entry.segments = parsePattern(pattern);

        routes.put(pattern, entry);
    }

    public void method(String method, String pattern, Function<String, String> closure) {
        if (routes.containsKey(pattern)) {
            return;
        }

        RouterEntry entry = new RouterEntry();
        entry.isClosure = true;
        entry.closure = closure;
        entry.method = method;
        entry.pattern = pattern;
        entry.segments = parsePattern(pattern);

        routes.put(pattern, entry);
    }


    private Map<Integer, SegmentData> parsePattern(String pattern) {
        String[] segments = Arrays.stream(pattern.split("/"))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toArray(String[]::new);

        Map<Integer, SegmentData> map = new HashMap<>();

        for (int i = 0; i < segments.length; i++) {
            String segment = segments[i];
            if (segment.isEmpty()) {
                continue;
            }
            SegmentData requestParam = new SegmentData();
            requestParam.position = i;
            if (segment.startsWith("{") && segment.endsWith("}")) {
                requestParam.isParameter = true;
                requestParam.name = segment.substring(1, segment.length() - 1).trim();
            } else {
                requestParam.name = segment;
            }
            map.put(i, requestParam);
        }

        return map;
    }


    private RouterEntry getMatch(String uri) {
        RouterEntry match = routes.get(uri.trim());

        if (match != null) {
            return match;
        }

        String[] routeSegments = Arrays.stream(uri.split("/"))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toArray(String[]::new);

        for (Map.Entry<String, RouterEntry> entry: routes.entrySet()) {
            match = new RouterEntry(entry.getValue());
            Map<Integer, SegmentData> patternSegments = match.segments;

            if (routeSegments.length != patternSegments.size()) {
                match = null;
                continue;
            }

            for (int i = 0; i < routeSegments.length; i++) {
                String segment = routeSegments[i];
                SegmentData patternSegment = patternSegments.get(i);

                if (patternSegment == null) {
                    match = null;
                    break;
                }

                if (patternSegment.isParameter) {
                    patternSegment.value = segment;
                } else if (!patternSegment.name.equals(segment)) {
                    match = null;
                    break;
                }
            }

            if (match != null) {
                break;
            }
        }


        return match;
    }


    public void dump() {
        routes.forEach((k, v) -> {
            System.out.print(k + " ");
            System.out.println(v);
        });
    }
}
