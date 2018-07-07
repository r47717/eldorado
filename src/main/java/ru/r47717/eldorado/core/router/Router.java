package ru.r47717.eldorado.core.router;

import ru.r47717.eldorado.core.di.Container;
import ru.r47717.eldorado.core.di.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

public class Router implements RouterInterface {

    private Map<String, RouterEntry> routes = new HashMap<>();

    @Override
    public Map<String, RouterEntry> getRoutes() {
        return routes;
    }

    @Override
    public RouterEntry retrieve(String route) {
        return getMatch(route);
    }


    @Override
    public void get(String name, String route, Class controller, String fn) {
        method(name,"get", route, controller, fn);
    }

    @Override
    public void get(String name, String route, Function<String, String> closure) {
        method(name,"get", route, closure);
    }

    @Override
    public void post(String name, String route, Class controller, String fn) {
        method(name,"post", route, controller, fn);
    }

    @Override
    public void put(String name, String route, Class controller, String fn) {
        method(name,"put", route, controller, fn);
    }

    @Override
    public void delete(String name, String route, Class controller, String fn) {
        method(name,"delete", route, controller, fn);
    }

    @Override
    public void method(String name, String method, String pattern, Class controller, String fn) {
        if (routes.containsKey(pattern)) {
            return;
        }

        RouterEntry entry = new RouterEntry();
        entry.name = name;
        entry.isClosure = false;
        entry.closure = null;
        entry.method = method;
        entry.pattern = pattern;
        entry.controller = controller;
        entry.fn = fn;
        entry.segments = parsePattern(pattern);

        routes.put(pattern, entry);
    }

    @Override
    public void method(String name, String method, String pattern, Function<String, String> closure) {
        if (routes.containsKey(pattern)) {
            return;
        }

        RouterEntry entry = new RouterEntry();
        entry.name = name;
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
                requestParam.setIsParameter(true);
                requestParam.setName(segment.substring(1, segment.length() - 1).trim());
            } else {
                requestParam.setName(segment);
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

                if (patternSegment.getIsParameter()) {
                    patternSegment.setValue(segment);
                } else if (!patternSegment.getName().equals(segment)) {
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
