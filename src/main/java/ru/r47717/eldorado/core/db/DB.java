package ru.r47717.eldorado.core.db;
import java.nio.file.Paths;
import java.sql.*;

public class DB {
    private static String dbPath = Paths.get("").toAbsolutePath().toString() + "/data/sqlite.db";
    public static String DB_DATA = "jdbc:sqlite:" + dbPath;


    public static void query(String sql) {
        try (Connection conn = DriverManager.getConnection(DB_DATA);
            java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


     public static void sampleQuery() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	name text NOT NULL,\n"
                + "	email text\n"
                + ");";

        query(sql);

        sql = "insert into users ('name', 'email') values ('Mike', 'mchr@yandex.ru');";

        query(sql);
    }

    public static QueryBuilder select() {
        return new QueryBuilder(QueryBuilder.Statement.SELECT);
    }

    public static QueryBuilder insert() {
        return new QueryBuilder(QueryBuilder.Statement.INSERT);
    }

    public static QueryBuilder update() {
        return new QueryBuilder(QueryBuilder.Statement.UPDATE);
    }

    public static QueryBuilder delete() {
        return new QueryBuilder(QueryBuilder.Statement.DELETE);
    }
}
