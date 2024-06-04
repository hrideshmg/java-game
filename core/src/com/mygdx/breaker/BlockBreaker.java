package com.mygdx.breaker;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;

interface ConfigurationVariables {
  int speed = 7;
  int initSize = 15;
  float speedFactor = 0.03f;
  int brickHeight = 50;
  int brickWidth = 90;
  int brickPadding = 10;
}

public class BlockBreaker
    extends ApplicationAdapter implements ConfigurationVariables {
  ShapeRenderer shape;
  Ball ball;
  Player paddle;
  String name = "hridesh";
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
    ArrayList<Color> colors = new ArrayList<Color>();
    colors.add(new Color(16777215));
    colors.add(new Color(10092543));
    colors.add(Color.GREEN);
    colors.add(Color.YELLOW);
    colors.add(Color.ORANGE);
    colors.add(Color.RED);
    Iterator<Color> iterator = colors.iterator();
    Color chosenColor = Color.WHITE;

    int screenHeight = Gdx.graphics.getHeight();
    int screenWidth = Gdx.graphics.getWidth();

    for (int y = screenHeight / 2; y < screenHeight - brickHeight * 2;
         y += brickHeight + brickPadding) {
      if (!iterator.hasNext()) {
        iterator = colors.iterator();
      }
      chosenColor = iterator.next();
      for (int x = brickWidth; x < screenWidth - brickWidth;
           x += brickWidth + brickPadding) {
        bricks.add(new Brick(x, y, brickWidth, brickHeight, chosenColor));
      }
    }
  }

  @Override
  public void render() {
    if (Gdx.input.isKeyPressed(Input.Keys.X)) {
      Gdx.app.exit();
    }

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
        float factor = 0.02f;
        brick.width -= brick.width * factor;
        brick.height -= brick.height * factor;
        brick.x += (brick.width * factor) / 2;
        brick.y += (brick.height * factor) / 2;
        if (brick.height <= 5) {
          bricks.remove(brick);
        }
      }
    }
  }
  @Override
  public void dispose() {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    bricks.clear();
    gameOver.dispose();
  }
}
