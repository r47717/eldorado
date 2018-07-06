package ru.r47717.eldorado.core.api;

import ru.r47717.eldorado.core.router.RouterInterface;
import java.util.List;


public interface ApiManagerInterface {
    void build(RouterInterface router);
    List<ApiEntry> getApi();
}
