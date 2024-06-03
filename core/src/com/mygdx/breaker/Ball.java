package com.mygdx.breaker;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.sql.SQLException;
import java.util.Random;

public class Ball extends Coordinates {
  int speed, ballSize, xSpeed, ySpeed;
  float speedFactor;
  Color color = Color.WHITE;
  BlockBreaker game;

  public Ball(int x, int y, int speed, int ballSize, float speedFactor,
              BlockBreaker game) {
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.ballSize = ballSize;
    this.speedFactor = speedFactor;
    this.xSpeed = speed;
    this.ySpeed = speed;
    this.game = game;
  }

  public void checkPlayerCollision(Player paddle) {
    int ballLeftBound = this.x - this.ballSize;
    int ballRightBound = this.x + this.ballSize;
    int ballLowerBound = this.y - this.ballSize;

    int paddleLeftBound = paddle.x;
    int paddleRightBound = paddle.x + paddle.paddleWidth;
    int paddleUpperBound = paddle.y + paddle.paddleHeight;

    if (ballLowerBound <= paddleUpperBound) {
      // Collision on right side
      if ((ballLeftBound < paddleRightBound) &&
          ((ballLeftBound - xSpeed) > paddleRightBound)) {
        xSpeed = -xSpeed;
      }
      // Collision on left side
      else if ((ballRightBound > paddleLeftBound) &&
               (ballRightBound - xSpeed) < paddleLeftBound) {
        xSpeed = -xSpeed;
      }

      // Otherwise, it must be a collision on top
      else if ((ballRightBound > paddleLeftBound) &&
               (ballLeftBound < paddleRightBound)) {
        y += 10;

        int speedUp =
            Math.round(Math.abs(paddle.x + (paddle.paddleWidth / 2) - this.x) *
                       speedFactor);
        xSpeed = xSpeed > 0 ? speed + speedUp : -(speed + speedUp);
        ySpeed = speed;
      }
    }
  }

  public void checkBrickCollision(Brick brick) {
    int ballLeftBound = this.x - this.ballSize;
    int ballRightBound = this.x + this.ballSize;
    int ballLowerBound = this.y - this.ballSize;
    int ballUpperBound = this.y + this.ballSize;

    int brickLeftBound = brick.x;
    int brickRightBound = brick.x + brick.width;
    int brickLowerBound = brick.y;
    int brickUpperBound = brick.y + brick.width;

    if ((ballUpperBound <= brickUpperBound) &&
        (ballLowerBound >= brickLowerBound)) {
      // Collision on right side
      if ((ballLeftBound < brickRightBound) &&
          ((ballLeftBound - xSpeed) > brickRightBound)) {
        xSpeed = -xSpeed;
        brick.destroyed = true;
        score += 1;
      }
      // Collision on left side
      else if ((ballRightBound > brickLeftBound) &&
               (ballRightBound - xSpeed) < brickLeftBound) {
        xSpeed = -xSpeed;
        brick.destroyed = true;
        score += 1;
      }

      // Collision on top/bottom
      else if ((ballRightBound > brickLeftBound) &&
               (ballLeftBound < brickRightBound)) {
        ySpeed = -ySpeed;
        brick.destroyed = true;
        score += 1;
      }
    }
  }
  public void update(Player paddle) {
    x += xSpeed;
    y += ySpeed;
    if (x > Gdx.graphics.getWidth() - ballSize) {
      xSpeed = -xSpeed;
    }
    if (x < 0 + ballSize) {
      xSpeed = -xSpeed;
    }
    if (y > Gdx.graphics.getHeight() - ballSize) {
      ySpeed = -ySpeed;
    }
    if (y < 0 - ballSize) {
      game.gameOver.endGame();
    }
    checkPlayerCollision(paddle);
  }

  public void draw(ShapeRenderer shape) {
    shape.begin(ShapeRenderer.ShapeType.Filled);
    shape.circle(x, y, ballSize);
    shape.setColor(color);
    shape.end();
  }

  public void respawnBall() {
    Random random = new Random();
    int padding = 50;
    this.x = random.nextInt(padding, Gdx.graphics.getWidth() - padding);
    this.y = random.nextInt(padding, Gdx.graphics.getHeight() / 2 - padding);
    this.xSpeed = random.nextBoolean() ? speed : -speed;
    this.ySpeed = speed;
    game.bricks.clear();
    game.generateBricks();
  }
}
