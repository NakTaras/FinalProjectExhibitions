package com.my.db.constant;

public class Constants {
    public static final String SQL_FIELD_ID = "id";
    public static final String SQL_FIELD_LOGIN = "login";
    public static final String SQL_FIELD_NAME = "name";
    public static final String SQL_FIELD_ADDRESS = "address";
    public static final String SQL_FIELD_SHORT_NAME = "short_name";
    public static final String SQL_FIELD_TOPIC = "topic";
    public static final String SQL_FIELD_DESCRIPTION = "description";
    public static final String SQL_FIELD_START_DATE_TIME = "start_date_time";
    public static final String SQL_FIELD_END_DATE_TIME = "end_date_time";
    public static final String SQL_FIELD_PRICE = "price";
    public static final String SQL_FIELD_POSTER_IMG = "poster_img";
    public static final String SQL_FIELD_STATUS = "status";
    public static final String SQL_FIELD_AMOUNT = "amount";
    public static final String SQL_FIELD_TICKETS_AMOUNT = "tickets_amount";
    public static final String SQL_FIELD_AMOUNT_OF_BOUGHT_TICKETS = "amount_of_bought_tickets";


    public static final String SQL_ADD_USER = "INSERT INTO user (login, password, role_id) VALUES (?, ?, (SELECT id FROM role WHERE name = ?));";
    public static final String SQL_GET_USER_BY_LOGIN = "SELECT user.id, user.login, role.name FROM user INNER JOIN role ON user.role_id = role.id WHERE login = ? AND password = ?;";
    public static final String SQL_ADD_LOCATION = "INSERT INTO location (name) VALUES (?);";
    public static final String SQL_ADD_LOCATION_ADDRESS = "INSERT INTO location_address_has_language (location_id, language_id, address) VALUES (?, (SELECT id FROM language WHERE short_name = ?), ?);";
    public static final String SQL_GET_ALL_LOCATION = "SELECT * FROM location;";
    public static final String SQL_ADD_EXHIBITION = "INSERT INTO exhibition (topic, description, start_date_time, end_date_time, price, poster_img) VALUES (?, ?, ?, ?, ?, ?);";
    public static final String SQL_GET_EXHIBITION_BY_ID = "SELECT * FROM exhibition WHERE id = ?;";
    public static final String SQL_ADD_ROW_TO_EXHIBITION_HAS_LOCATION = "INSERT INTO exhibition_has_location (exhibition_id, location_id) VALUES (?, ?);";
    public static final String SQL_GET_LOCATION_BY_EXHIBITION_ID = "SELECT id, name FROM location INNER JOIN exhibition_has_location ehl on location.id = ehl.location_id WHERE exhibition_id = ?;";
    public static final String SQL_GET_ADDRESS_BY_LOCATION_ID = "SELECT short_name, address FROM location INNER JOIN location_address_has_language lahl on location.id = lahl.location_id INNER JOIN language l on lahl.language_id = l.id WHERE location_id = ?;";
    public static final String SQL_GET_ALL_EXHIBITIONS = "SELECT * FROM exhibition;";
    public static final String SQL_EXHIBITIONS_ON_PAGE_BY_DEFAULT = "SELECT * FROM exhibition WHERE status = 1 LIMIT ?, 2;";
    public static final String SQL_EXHIBITIONS_ON_PAGE_BY_TOPIC = "SELECT * FROM exhibition WHERE status = 1 ORDER BY topic LIMIT ?, 2;";
    public static final String SQL_EXHIBITIONS_ON_PAGE_BY_PRICE = "SELECT * FROM exhibition WHERE status = 1 ORDER BY price LIMIT ?, 2;";
    public static final String SQL_EXHIBITIONS_ON_PAGE_BY_DATE = "SELECT * FROM exhibition WHERE status = 1 AND ? BETWEEN start_date_time AND end_date_time LIMIT ?, 2;";
    public static final String SQL_CANCEL_EXHIBITION_BY_ID = "UPDATE exhibition SET status = 0 WHERE id = ?;";
    public static final String SQL_BUY_TICKETS = "INSERT INTO user_has_exhibition (user_id, exhibition_id, amount_of_bought_tickets) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE amount_of_bought_tickets = user_has_exhibition.amount_of_bought_tickets + ?;";
    public static final String SQL_GET_AMOUNT_OF_EXHIBITIONS = "SELECT COUNT(*) as 'amount' FROM exhibition WHERE status = 1;";
    public static final String SQL_GET_AMOUNT_OF_EXHIBITIONS_BY_DATE = "SELECT COUNT(*) as 'amount' FROM exhibition WHERE status = 1 AND ? BETWEEN start_date_time AND end_date_time;";
    public static final String SQL_GET_AMOUNT_OF_SOLD_TICKETS_BY_EXHIBITION_ID = "SELECT SUM(amount_of_bought_tickets) as 'tickets_amount' FROM user_has_exhibition WHERE exhibition_id = ? GROUP BY exhibition_id;";
    public static final String SQL_GET_EXHIBITIONS_STATISTICS = "SELECT id, topic, start_date_time, end_date_time, price FROM exhibition WHERE status = 1;";
    public static final String SQL_GET_DETAILED_STATISTICS_BY_EXHIBITION_ID = "SELECT user.login, amount_of_bought_tickets FROM user_has_exhibition INNER JOIN user ON user.id = user_has_exhibition.user_id WHERE user_has_exhibition.exhibition_id = ?;";

}
