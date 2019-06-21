package ru.r47717.eldorado.core.api;

import ru.r47717.eldorado.core.router.Router;
import java.util.List;


public interface ApiManager
{
    void build(Router router);
    List<ApiEntry> getApi();
    void registerMyself();
}
