package com.mygdx.breaker;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player extends Coordinates {
  int paddleHeight, paddleWidth;
  Color color = new Color(0.76f, 0.76f, 0.76f, 1f);

  public Player(int x, int y, int paddleSize) {
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
    shape.setColor(color);
    shape.rect(x, y, paddleWidth, paddleHeight);
    shape.end();
  }
}
