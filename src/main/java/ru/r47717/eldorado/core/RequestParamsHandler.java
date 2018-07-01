package ru.r47717.eldorado.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RequestParamsHandler
{
    static class UrlPatternItem {
        String name;
        int position;
    }

    class RequestParam {
        String name;
        int position;
        String value;
    }

    private List<RequestParam> params = new ArrayList<>();

    public static List<UrlPatternItem> parsePattern(String pattern) {
        String[] segments = pattern.split("/");
        List<UrlPatternItem> list = new ArrayList<>();
        for (int i = 0; i < segments.length; i++) {
            if (!segments[i].isEmpty()) {
                String segment = segments[i].trim();
                if (segment.startsWith("{") && segment.endsWith("}")) {
                    UrlPatternItem urlPatternItem = new UrlPatternItem();
                    urlPatternItem.name = segment.substring(1, segment.length() - 1).trim();
                    urlPatternItem.position = 0;
                }
            }
        }

        return list;
    }

    private List<RequestParam> getRequestParams(String pattern, String body) {
        List<RequestParam> output = new ArrayList<>();
        List<String> patternItems = new LinkedList<>(Arrays.asList(pattern.split("/")));
        List<String> items = new LinkedList<>(Arrays.asList(body.split("/")));

        if (patternItems.size() > 0 && patternItems.get(0).isEmpty()) {
            patternItems.remove(0);  // trim first slash
        }

        if (items.size() > 0 && items.get(0).isEmpty()) {
            items.remove(0);  // trim first slash
        }

        for (int i = 0; i < items.size(); i++) {
            String segment = patternItems.get(i).trim();
            if (segment.startsWith("{") && segment.endsWith("}")) {
                RequestParam param = new RequestParam();
                param.name = segment.substring(1, segment.length() - 1).trim();
                param.position = i;
                param.value = items.get(i).trim();
                output.add(param);
            }
        }

        return output;
    }


}
