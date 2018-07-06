package app;

import ru.r47717.eldorado.core.api.ApiManagerInterface;

public class PublishedApi {
    public static void make(ApiManagerInterface api)
    {
        // add APIs to be published here

        api.add("healthcheck", "/healthcheck");
    }
}
