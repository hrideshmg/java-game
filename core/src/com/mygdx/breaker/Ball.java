package com.mygdx.breaker;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.Random;

class Ball {
  int x, y, speed, ballSize, xSpeed, ySpeed;
  float speedFactor;
  Color color = Color.WHITE;

  public Ball(int x, int y, int speed, int ballSize, float speedFactor) {
    this.x = x;
    this.y = y;
    this.speed = speed;
    this.ballSize = ballSize;
    this.speedFactor = speedFactor;
    this.xSpeed = speed;
    this.ySpeed = speed;
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
      }
      // Collision on left side
      else if ((ballRightBound > brickLeftBound) &&
               (ballRightBound - xSpeed) < brickLeftBound) {
        xSpeed = -xSpeed;
        brick.destroyed = true;
      }

      // Collision on top/bottom
      else if ((ballRightBound > brickLeftBound) &&
               (ballLeftBound < brickRightBound)) {
        ySpeed = -ySpeed;
        brick.destroyed = true;
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
      ySpeed = -ySpeed;
    }
    checkPlayerCollision(paddle);
  }

  public void draw(ShapeRenderer shape) {
    shape.begin(ShapeRenderer.ShapeType.Filled);
    shape.circle(x, y, ballSize);
    shape.setColor(color);
    shape.end();
  }

  public void gameOver() {
    Random rand = new Random();
    int padding = 400;
    this.x = rand.nextInt(padding, Gdx.graphics.getWidth() - padding);
    this.y = rand.nextInt(padding, Gdx.graphics.getHeight() / 2);
  }
}
