package GreenCode.server;

import java.sql.*;

public class DBconnection implements AuthService {

    private static Connection connection;
    private static Statement statement;

    public static  void connect() throws SQLException{
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("Connect - error");
        }
    }

    public static String getNicknameByLoginAndPassword(String login, String password){
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

    public static void disconnect(){
        try {
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error close connection");
        }
    }
}
