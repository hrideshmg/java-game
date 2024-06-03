package com.mygdx.breaker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameOver {
  private Stage stage;
  private Table table;
  public boolean isGameOver;
  BlockBreaker game;

  public GameOver(BlockBreaker game) {
    this.game = game;
    stage = new Stage(new StretchViewport(1920, 1080));
    Gdx.input.setInputProcessor(stage);
    table = new Table();
    table.setFillParent(true);
    stage.addActor(table);

    Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    Label gameOverLabel = new Label("GAME OVER", skin);
    TextButton restartGameButton = new TextButton("Restart", skin);
    restartGameButton.addListener(new ChangeListener() {
      public void changed(ChangeEvent event, Actor actor) { restartGame(); }
    });
    Texture bg = new Texture(Gdx.files.internal("bg.png"));

    table.setBackground(new TextureRegionDrawable(bg));
    table.add(gameOverLabel);
    table.row();
    table.add(restartGameButton);
  }

  public void endGame() { isGameOver = true; }
  public void showEndGameScreen() { stage.draw(); }
  public void restartGame() {
    isGameOver = false;
    game.ball.respawnBall();
  }
}
