package ru.r47717.eldorado.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UrlStringHandler
{
    class UrlPatternItem {
        String name;
        int position;
    }

    public List<UrlPatternItem> parsePattern(String pattern) {
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
}
