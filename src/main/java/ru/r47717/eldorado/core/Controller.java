package ru.r47717.eldorado.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface Controller {
    Map<String, String> handle(HttpServletRequest request, HttpServletResponse response);
}

