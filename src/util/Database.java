package util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Database {
    public Connection connection;

    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://10.19.124.172:10255/lite_app", "root", "catlab1a509");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    public List<String> queryLiteAppID() {
        try {
            String querySql = "select lite_app_id from download_mission where lite_download = 1 and full_download = 1";

            PreparedStatement queryStmt = connection.prepareStatement(querySql);


            ResultSet resultSet = queryStmt.executeQuery();

            List<String> queryResult = new ArrayList<>();

            while (resultSet.next()) {
                queryResult.add(resultSet.getString(1));
            }

            return queryResult;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    public void updatePermissionCompared(String liteAppID, int status){
        try {
            String querySql = "update download_mission set permission_compared=? where lite_app_id=?";

            PreparedStatement updateStatement = connection.prepareStatement(querySql);

            updateStatement.setInt(1, status);
            updateStatement.setString(2, liteAppID);

            updateStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public String getTask() {
        try {
            String querySql = "select lite_app_id from download_mission where lite_download = 1 and full_download = 1 and permission_compared = 0";

            PreparedStatement queryStmt = connection.prepareStatement(querySql);


            ResultSet resultSet = queryStmt.executeQuery();

            List<String> queryResult = new ArrayList<>();

            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void main(String[] args) {
        Database database = new Database();
        System.out.println(database.getTask());
//        database.updatePermissionCompared("com.opera.app.newslite", 1);
//        System.out.println(database.getTask());
    }
}
