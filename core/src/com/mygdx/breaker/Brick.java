package com.mygdx.breaker;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

class Brick extends Coordinates {
  int width, height;
  public boolean destroyed = false;
  private Color color;

  public Brick(int x, int y, int width, int height, Color color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
  }

  public void draw(ShapeRenderer shape) {
    shape.begin(ShapeRenderer.ShapeType.Filled);
    shape.setColor(color);
    shape.rect(x, y, width, height);
    shape.end();
  }
}
