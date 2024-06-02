package com.mygdx.breaker;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

public class BlockBreaker extends ApplicationAdapter {
  ShapeRenderer shape;
  Ball ball;
  Paddle paddle;
  static final int speed = 7;
  static final int initSize = 15;
  static final float speedFactor = 0.02f;
  static final int blockHeight = 50;
  static final int blockWidth = 90;
  static final int blockPadding = 10;
  ArrayList<Block> blocks = new ArrayList<Block>();

  @Override
  public void create() {
    shape = new ShapeRenderer();
    ball = new Ball(50, 200, speed, initSize, speedFactor);
    paddle = new Paddle(50, 50, 100);

    int screenHeight = Gdx.graphics.getHeight();
    int screenWidth = Gdx.graphics.getWidth();

    for (int y = screenHeight / 2; y < screenHeight - blockHeight * 2;
         y += blockHeight + blockPadding) {
      for (int x = blockWidth; x < screenWidth - blockWidth;
           x += blockWidth + blockPadding) {
        blocks.add(new Block(x, y, blockWidth, blockHeight));
      }
    }
  }

  @Override
  public void render() {
    ScreenUtils.clear(0, 0, 0, 0);
    ball.update(paddle);
    paddle.update();
    ball.draw(shape);
    paddle.draw(shape);
    for (Block block : blocks) {
      block.draw(shape);
      ball.checkBlockCollision(block);
    }
    for (int i = 0; i < blocks.size(); i++) {
      Block block = blocks.get(i);
      if (block.destroyed) {
        blocks.remove(block);
      }
    }
  }
}

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

  public void checkPaddleCollision(Paddle paddle) {
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

  public void checkBlockCollision(Block block) {
    int ballLeftBound = this.x - this.ballSize;
    int ballRightBound = this.x + this.ballSize;
    int ballLowerBound = this.y - this.ballSize;
    int ballUpperBound = this.y + this.ballSize;

    int blockLeftBound = block.x;
    int blockRightBound = block.x + block.width;
    int blockLowerBound = block.y;
    int blockUpperBound = block.y + block.width;

    if ((ballUpperBound <= blockUpperBound) &&
        (ballLowerBound >= blockLowerBound)) {
      // Collision on right side
      if ((ballLeftBound < blockRightBound) &&
          ((ballLeftBound - xSpeed) > blockRightBound)) {
        xSpeed = -xSpeed;
        block.destroyed = true;
      }
      // Collision on left side
      else if ((ballRightBound > blockLeftBound) &&
               (ballRightBound - xSpeed) < blockLeftBound) {
        xSpeed = -xSpeed;
        block.destroyed = true;
      }

      // Collision on top/bottom
      else if ((ballRightBound > blockLeftBound) &&
               (ballLeftBound < blockRightBound)) {
        ySpeed = -ySpeed;
        block.destroyed = true;
      }
    }
  }
  public void update(Paddle paddle) {
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
    checkPaddleCollision(paddle);
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

class Paddle {
  int x, y, paddleHeight, paddleWidth;

  public Paddle(int x, int y, int paddleSize) {
    this.x = x;
    this.y = y;
    this.paddleWidth = paddleSize * 5;
    this.paddleHeight = paddleSize;
  }

  public void update() {
    this.x = Gdx.input.getX();
    if (this.x > Gdx.graphics.getWidth() - paddleWidth) {
      this.x = Gdx.graphics.getWidth() - paddleWidth;
    }
  }

  public void draw(ShapeRenderer shape) {
    shape.begin(ShapeRenderer.ShapeType.Filled);
    shape.rect(x, y, paddleWidth, paddleHeight);
    shape.end();
  }
}

class Block {
  int x, y, width, height;
  boolean destroyed = false;

  public Block(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public void draw(ShapeRenderer shape) {
    shape.begin(ShapeRenderer.ShapeType.Filled);
    shape.rect(x, y, width, height);
    shape.end();
  }
}
