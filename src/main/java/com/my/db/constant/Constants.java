package com.my.db.constant;

public class Constants {
    public static final String SQL_FIELD_ID = "id";
    public static final String SQL_FIELD_LOGIN = "login";
    public static final String SQL_FIELD_NAME = "name";
    public static final String SQL_FIELD_ADDRESS = "address";
    public static final String SQL_FIELD_TOPIC = "topic";
    public static final String SQL_FIELD_DESCRIPTION = "description";
    public static final String SQL_FIELD_START_DATE = "start_date";
    public static final String SQL_FIELD_END_DATE = "end_date";
    public static final String SQL_FIELD_START_TIME = "start_time";
    public static final String SQL_FIELD_END_TIME = "end_time";
    public static final String SQL_FIELD_PRICE = "price";
    public static final String SQL_FIELD_POSTER_IMG = "poster_img";


    public static final String SQL_ADD_USER = "INSERT INTO user (login, password, role_id) VALUES (?, ?, (SELECT id FROM role WHERE name = ?));";
    public static final String SQL_GET_USER_BY_LOGIN = "SELECT user.id, user.login, role.name FROM user INNER JOIN role ON user.role_id = role.id WHERE login = ? AND password = ?;";
    public static final String SQL_ADD_LOCATION = "INSERT INTO location (name, address) VALUES (?, ?);";
    public static final String SQL_GET_ALL_LOCATION = "SELECT * FROM location;";
    public static final String SQL_ADD_EXHIBITION = "INSERT INTO exhibition (topic, description, start_date_time, end_date_time, price, poster_img) VALUES (?, ?, ?, ?, ?, ?);";
    public static final String SQL_GET_EXHIBITION_BY_ID = "SELECT * FROM exhibition WHERE id = ?;";
    public static final String SQL_ADD_ROW_TO_EXHIBITION_HAS_LOCATION = "INSERT INTO exhibition_has_location (exhibition_id, location_id) VALUES (?, ?);";
}
