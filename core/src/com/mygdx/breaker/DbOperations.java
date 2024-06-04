package com.mygdx.breaker;

import java.sql.*;

public class DbOperations {
  Statement statement;

  public ResultSet getHighScore(Connection connection) {
    ResultSet resultSet;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery("SELECT * FROM gamers");
      return resultSet;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public ResultSet getHighScore(String name, Connection connection) {
    ResultSet resultSet;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery("SELECT * FROM gamers WHERE name='" +
                                         name + "'");
      return resultSet;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void updateHighScore(int score, String name, Connection connection) {
    try {
      ResultSet resultSet = getHighScore(name, connection);
      if (resultSet.next()) {
        if (score > resultSet.getInt("highscore")) {
          PreparedStatement prepared = connection.prepareStatement(
              "UPDATE gamers SET highscore=" + (score) + " WHERE name='" +
              name + "'");
          prepared.execute();
        }
      }
      statement.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
