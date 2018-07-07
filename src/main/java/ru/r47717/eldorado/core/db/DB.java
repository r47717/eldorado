package ru.r47717.eldorado.core.db;

import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB {
    private static String url = "jdbc:sqlite:" + Paths.get("").toAbsolutePath().toString() + "/data/sqlite.db";

    public static void query(String sql) {
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Map<Integer, String> select(String sql) {
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            Map<Integer, String> map = new HashMap<>();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                map.put(id, name + " " + email);
            }

            return map;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static void sampleQuery() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	email text\n"
                + ");";

        query(sql);

        sql = "insert into users values (1, 'Mike', 'mchr@yandex.ru');";

        query(sql);

        sql = "select * from users;";

        System.out.println(select(sql).toString());
    }
}
