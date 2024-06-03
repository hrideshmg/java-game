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
import java.sql.*;

public class GameOver {
  private Stage stage;
  private Table table;
  public boolean isGameOver;
  Label scoreLabel;
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
    scoreLabel = new Label("", skin);
    TextButton restartGameButton = new TextButton("Restart", skin);
    restartGameButton.addListener(new ChangeListener() {
      public void changed(ChangeEvent event, Actor actor) { restartGame(); }
    });
    Texture bg = new Texture(Gdx.files.internal("bg.png"));

    table.setBackground(new TextureRegionDrawable(bg));
    table.add(gameOverLabel);
    table.row();
    table.add(scoreLabel);
    table.row();
    table.add(restartGameButton);
  }

  public void endGame()  {
    isGameOver = true;
    scoreLabel.setText("Highscore: " + this.game.ball.score);
    
    Statement statement;
    try {
      statement = game.connection.createStatement();
      ResultSet resultSet=statement.executeQuery("SELECT * FROM gamers WHERE name='"+game.name+"'");
      if (resultSet.next()) {
      if (game.ball.score>resultSet.getInt("highscore")){
        PreparedStatement prepared=game.connection.prepareStatement("UPDATE gamers SET highscore="+(game.ball.score)+" WHERE name='"+game.name+"'");
        prepared.execute();
      }
      }else{
        System.err.println("Player not found");
      }
      resultSet.close();
      statement.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
   
    
  }
  public void showEndGameScreen() { stage.draw(); }
  public void restartGame() {
    isGameOver = false;
    game.ball.respawnBall();
    game.ball.score = 0;
  }
}
