package db;

import java.sql.*;

public class UserSQL {
    private static Connection conn = null;
    private static Statement statement = null;

    public UserSQL() {
        try {
            System.out.println("Creating database if non exists.");
            conn = DriverManager.getConnection("jdbc:sqlite:database/usersettings.db");
            statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"userSettings\" (\n" +
                    "\t\"userID\"\tVARCHAR NOT NULL,\n" +
                    "\t\"workToggle\"\tINT NOT NULL,\n" +
                    "\t\"reminderToggle\"\tINT NOT NULL\n"+
                    ");");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isUserAdded(String userID)
    {
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM userSettings WHERE userID = '" + userID + "'");
            if(rs.next())
            {
                String user = rs.getString("userID");
                return true;

            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addUser(String userID)
    {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO userSettings(userID, reminderToggle, workToggle) VALUES (?,?,?);");
            ps.setString(1, userID);
            ps.setInt(2, 0);
            ps.setInt(3, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getDefaultToggle(String userID){
        int toggle = 0;
        try {
            ResultSet rs = statement.executeQuery("SELECT reminderToggle FROM userSettings WHERE userID = '" + userID + "'");
            if(rs.next())
            {
                toggle = rs.getInt("reminderToggle");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toggle;
    }

    public static boolean setDefaultToggle(String userID, int amount){
        if (amount > 60) amount = 60;
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE userSettings SET reminderToggle = ? WHERE userID = ?;");
            ps.setInt(1, amount);
            ps.setString(2, userID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setDefaultWorkToggle(String userID, int amount){
        if (amount > 240) amount = 240;
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE userSettings SET workToggle = ? WHERE userID = ?;");
            ps.setInt(1, amount);
            ps.setString(2, userID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getDefaultWorkToggle(String userID){
        int toggle = 0;
        try {
            ResultSet rs = statement.executeQuery("SELECT workToggle FROM userSettings WHERE userID = '" + userID + "'");
            if(rs.next())
            {
                toggle = rs.getInt("workToggle");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toggle;
    }




}
