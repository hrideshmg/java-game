package com.mygdx.breaker;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

class Brick extends Coordinates {
  int width, height;
  boolean destroyed = false;

  public Brick(int x, int y, int width, int height) {
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
