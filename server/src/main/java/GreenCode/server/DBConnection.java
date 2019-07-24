package GreenCode.server;

import GreenCode.common.Log4j;

import java.sql.*;

class DBConnection {

    private static Connection connection;
    private static Statement statement;

    static  void connect() throws SQLException{
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();
            Log4j.log.info("Start DB Connection");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Connect - error");
        }
    }

    static String getNicknameByLoginAndPassword(String login, String password){
        Log4j.log.info("Try login: " + login);
        String sql = String.format("SELECT nickname FROM accounts\n" +
                "WHERE login = '%s'\n" +
                "AND password = '%s'",login,password);
        try{
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("GetNickname - false");
        }
        return null;
    }

    static void disconnect(){
        try {
            connection.close();
            System.out.println("DB connection closed");
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error close connection");
        }
    }
}
