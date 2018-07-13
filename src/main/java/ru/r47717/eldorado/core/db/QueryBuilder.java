package ru.r47717.eldorado.core.db;


import java.util.Collection;

public class QueryBuilder {
    enum Statement {
        SELECT,
        INSERT,
        DELETE,
        UPDATE
    };

    enum OrderDirection {
        NONE,
        ASC,
        DESC
    };

    private Statement statement;
    private String fields = "";
    private String from = "";
    private String into = "";
    private String values = "";
    private String where = "";
    private String orderBy = "";
    private OrderDirection orderDirection;

    public QueryBuilder(Statement statement) {
        this.statement = statement;
    }

    public QueryBuilder fields(String fields) {
        this.fields = fields;
        return this;
    }

    public QueryBuilder from(String from) {
        this.from = from;
        return this;
    }

    public QueryBuilder into(String into) {
        this.into = into;
        return this;
    }

    public QueryBuilder values(String values) {
        this.values = values;
        return this;
    }

    public QueryBuilder where(String where) {
        this.where = where;
        return this;
    }

    public QueryBuilder orderBy(String orderBy, OrderDirection orderDirection) {
        this.orderBy = orderBy;
        this.orderDirection = orderDirection;
        return this;
    }

    public QueryBuilder orderBy(String orderBy) {
        return orderBy(orderBy, OrderDirection.ASC);
    }

    private String buildSelect() {
        String wherePart = where.isEmpty() ? "" : "where " + where;
        return "select " + fields + " from " + from + " " + wherePart;
    }

    private String buildInsert() {
        String fieldsPart = fields.isEmpty() ? "" : "(" + fields + ")";
        return "insert into " + into + " " + fieldsPart + " values " + values;
    }

    private String buildUpdate() {
        return "";
    }

    private String buildDelete() {
        return "";
    }

    public String get() {
        switch (statement) {
            case SELECT:
                return buildSelect();
            case INSERT:
                return buildInsert();
            case UPDATE:
                return buildUpdate();
            case DELETE:
                return buildDelete();
        }

        return null;
    }

    public String first() {
        return "";
    }
}
