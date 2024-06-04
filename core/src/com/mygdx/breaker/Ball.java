package com.mygdx.breaker;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;

public class Ball extends Coordinates {
  int speed, xSpeed, ySpeed;
  float speedFactor, ballSize;
  Color color = new Color(0.76f, 0.76f, 0.76f, 1.0f);
  BlockBreaker game;

  public Ball(float x, float y, int speed, int ballSize, float speedFactor,
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
    float ballLeftBound = this.x - this.ballSize;
    float ballRightBound = this.x + this.ballSize;
    float ballLowerBound = this.y - this.ballSize;

    float paddleLeftBound = paddle.x;
    float paddleRightBound = paddle.x + paddle.paddleWidth;
    float paddleUpperBound = paddle.y + paddle.paddleHeight;

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
    if (brick.destroyed)
      return;
    float ballLeftBound = this.x - this.ballSize;
    float ballRightBound = this.x + this.ballSize;
    float ballLowerBound = this.y - this.ballSize;
    float ballUpperBound = this.y + this.ballSize;

    float brickLeftBound = brick.x;
    float brickRightBound = brick.x + brick.width;
    float brickLowerBound = brick.y;
    float brickUpperBound = brick.y + brick.width;

    if ((ballUpperBound <= brickUpperBound) &&
        (ballLowerBound >= brickLowerBound)) {
      // Collision on right side
      if ((ballLeftBound < brickRightBound) &&
          ((ballLeftBound - xSpeed) > brickRightBound)) {
        xSpeed = -xSpeed;
        brick.destroyed = true;
        game.setScore(game.getScore() + 1);
      }
      // Collision on left side
      else if ((ballRightBound > brickLeftBound) &&
               (ballRightBound - xSpeed) < brickLeftBound) {
        xSpeed = -xSpeed;
        brick.destroyed = true;
        game.setScore(game.getScore() + 1);
      }

      // Collision on top/bottom
      else if ((ballRightBound > brickLeftBound) &&
               (ballLeftBound < brickRightBound)) {
        ySpeed = -ySpeed;
        brick.destroyed = true;
        game.setScore(game.getScore() + 1);
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
    shape.setColor(color);
    shape.circle(x, y, ballSize);
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
