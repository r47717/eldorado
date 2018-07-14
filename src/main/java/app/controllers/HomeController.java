package app.controllers;

import app.services.MyService;
import app.services.MyService2;
import ru.r47717.eldorado.core.controllers.Controller;
import ru.r47717.eldorado.core.db.DB;
import ru.r47717.eldorado.core.di.Inject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class HomeController extends Controller {

    @Inject
    private MyService myService;


    @Inject
    private MyService2 myService2;


    public Map<String, String> index() {
        Map<String, String> map = new HashMap<>();
        map.put("echoService", myService.echoService("my injected text 1"));

        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	name text NOT NULL,\n"
                + "	email text\n"
                + ");";

        DB.query(sql);

//        DB.insert().into("users").values("(15, 'Mike', 'mchr@yandex.ru')").get();

        ResultSet rs = DB.select().from("users").fields("*").get();
        try {
            while(rs.next())
            {
                // read the result set
                System.out.println("id = " + rs.getInt("id"));
                System.out.println("name = " + rs.getString("name"));
                System.out.println("email = " + rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public Map<String, String> index2() {
        Map<String, String> map = new HashMap<>();
        map.put("test", "stuff 2");

        return map;
    }

    public Map<String, String> getPerson(String name) {
        Map<String, String> map = new HashMap<>();
        map.put("greeting", myService2.concatService("Hello, ", name));

        return map;
    }
}
