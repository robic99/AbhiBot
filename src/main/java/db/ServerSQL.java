package db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class ServerSQL {
    private static Connection conn = null;
    private static Statement statement = null;

    public ServerSQL() {
        try {
            System.out.println("Creating database if non exists.");
            conn = DriverManager.getConnection("jdbc:sqlite:database/server.db");
            statement = conn.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"serverSettings\" (\n" +
                    "\t\"guildID\"\tVARCHAR NOT NULL,\n" +
                    "\t\"prefix\"\tVARCHAR NOT NULL DEFAULT '!',\n" +
                    "\tPRIMARY KEY(\"guildID\")\n" +
                    ")");
            String guildId = Files.readString(Path.of("guildid.txt"), StandardCharsets.UTF_8);
            if (!guildExists(guildId))
            {
                addGuild(guildId);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addGuild(String guildID) {
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO serverSettings(guildID) VALUES (?);");
            ps.setString(1, guildID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean guildExists(String guildID) {
        try {
            ResultSet rs = statement.executeQuery("SELECT count(1)\n" +
                    "FROM serverSettings\n" +
                    "WHERE guildID = '" + guildID + "'");
            while (rs.next()) {
                if (rs.getInt("count(1)") == 1) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void setPrefix(String prefix, String guildID) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE serverSettings SET prefix = ? WHERE guildID = ?");
            ps.setString(1, prefix);
            ps.setString(2, guildID);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getPrefix(String guildID) {
        try {
            ResultSet rs = statement.executeQuery("SELECT prefix FROM serverSettings WHERE guildID = '" + guildID + "'");
            if(rs.next()){
                return rs.getString("prefix");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static void main(String[] args) {

    }
}
