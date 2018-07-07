package ru.r47717.eldorado.core.providers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class Provider implements ProviderInterface {
    private String name;
    private String url;


    public Provider(String name, String url) {
        this.name = name;
        this.url = url;
    }


    private String sendHttp(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        try {
            Response response;
            response = client.newCall(request).execute();
            String html;
            html = response.body().string();
            return html;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public String callService(String uri, Map<String, String> params) {
        URL fullUrl;

        try {
            fullUrl = new URL(new URL(url), uri);
            String response = sendHttp(fullUrl.toString());
            return response;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
