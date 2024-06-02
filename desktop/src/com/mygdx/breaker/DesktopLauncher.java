package com.mygdx.breaker;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.breaker.BlockBreaker;

// Please note that on macOS your application needs to be started with the
// -XstartOnFirstThread JVM argument
public class DesktopLauncher {
  public static void main(String[] arg) {
    Lwjgl3ApplicationConfiguration config =
        new Lwjgl3ApplicationConfiguration();
    config.setWindowedMode(1920, 1080);
    config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
    config.setTitle("block_breaker");
    new Lwjgl3Application(new BlockBreaker(), config);
  }
}
