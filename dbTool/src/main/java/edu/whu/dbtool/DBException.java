package edu.whu.dbtool;

import java.sql.SQLException;

public class DBException extends SQLException {
    String message = null;

    public DBException(String info) {
        message = info;
    }

    @Override
    public String toString() {
        return "SQL Error" + message;
    }
}
