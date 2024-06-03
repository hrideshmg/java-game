package com.mygdx.breaker;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.sql.*;
import java.util.ArrayList;

public class BlockBreaker extends ApplicationAdapter {
  ShapeRenderer shape;
  Ball ball;
  Player paddle;
  String name = "hridesh";
  static final int speed = 7;
  static final int initSize = 15;
  static final float speedFactor = 0.02f;
  static final int brickHeight = 50;
  static final int brickWidth = 90;
  static final int brickPadding = 10;
  public GameOver gameOver;
  ArrayList<Brick> bricks = new ArrayList<Brick>();
  Connection connection;

  private int score = 0;
  public void setScore(int score) { this.score = score; };
  public int getScore() { return this.score; };

  @Override
  public void create() {
    connection = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      connection = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/ball_souls", "hridesh", "robot123");
    } catch (Exception e) {
      System.out.println(e);
    }
    shape = new ShapeRenderer();
    ball = new Ball(50, 200, speed, initSize, speedFactor, this);
    paddle = new Player(50, 50, 100);
    gameOver = new GameOver(this);
    generateBricks();
  }

  public void generateBricks() {
    int screenHeight = Gdx.graphics.getHeight();
    int screenWidth = Gdx.graphics.getWidth();
    for (int y = screenHeight / 2; y < screenHeight - brickHeight * 2;
         y += brickHeight + brickPadding) {
      for (int x = brickWidth; x < screenWidth - brickWidth;
           x += brickWidth + brickPadding) {
        bricks.add(new Brick(x, y, brickWidth, brickHeight));
      }
    }
  }

  @Override
  public void render() {
    if (gameOver.isGameOver) {
      gameOver.showEndGameScreen();
      return;
    }
    ScreenUtils.clear(0, 0, 0, 0);
    ball.update(paddle);
    paddle.update();
    ball.draw(shape);
    paddle.draw(shape);
    for (Brick brick : bricks) {
      brick.draw(shape);
      ball.checkBrickCollision(brick);
    }
    for (int i = 0; i < bricks.size(); i++) {
      Brick brick = bricks.get(i);
      if (brick.destroyed) {
        bricks.remove(brick);
      }
    }
  }
  @Override
  public void dispose() {
    try {
      connection.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
