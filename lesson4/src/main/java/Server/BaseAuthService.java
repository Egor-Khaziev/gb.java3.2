package Server;

import Server.Interface.AuthService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    private class Entry {
        private String login;
        private String pass;
        private String nick;

        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }
    }

    private List<Entry> entries;

    private static Connection getSQLConnection() throws SQLException, ClassNotFoundException {

        Class.forName("org.sqlite.JDBC");

        Connection conn = DriverManager.getConnection("jdbc:sqlite:users.s2db");
        return conn;

    }

    private static void createSQLBase(Connection connection) throws SQLException {
        statement = connection.createStatement();
        statement.execute(
                "CREATE TABLE IF NOT exists 'users'" +
                        "('id' INTEGER primary key autoincrement, 'name' text, 'login' text, 'password' text);");
    }

    public BaseAuthService() throws SQLException, ClassNotFoundException {

        connection = getSQLConnection();
        System.out.println("SQL connection");
        createSQLBase(connection);
        System.out.println("SQL base created");
        writeSQLBase();
        System.out.println("SQL base wrote");

    }

    private void writeSQLBase() throws SQLException {
        statement.execute("INSERT INTO 'users' ('name','login','password') values ('nickA','A','A')");
        statement.execute("INSERT INTO 'users' ('name','login','password') values ('nickB','B','B')");
        statement.execute("INSERT INTO 'users' ('name','login','password') values ('nickC','C','C')");
    }

    @Override
    public void start() {
        System.out.println("Auth service start");
    }

    public void renameUser(String oldNickName, String newNickName) throws SQLException {

        //String s = "masa";
        String query = String.format("update users set name = \"%s\" where name = \"%s\"", newNickName, oldNickName);
        statement.executeUpdate(query);
    }

    @Override
    public String getNickByLoginPass(String loginName, String pass) throws SQLException {
        System.out.println("SQL base is reading");
        String nickNameResult = null;
        String loginResult = null;
        String passResult = null;

        resultSet = statement.executeQuery("select * from users");
        while (resultSet.next()) {
            nickNameResult = resultSet.getString("name");
            loginResult = resultSet.getString("login");
            passResult = resultSet.getString("password");
            if (loginResult.equals(loginName) && passResult.equals(pass)) {
                System.out.println("user found");
                return nickNameResult;
            }

        }
        return null;
//        for (Entry c : entries) {
//            if (c.login.equals(login) && c.pass.equals(pass)) {
//                return c.nick;
//            }
//        }
//        return null;
    }

    @Override
    public void stop() {
        System.out.println("Auth service stop");
    }

}
