package kabasuji;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import kabasuji.views.MainView;
import javax.swing.*;

public class Main extends Application {
  public void start(Stage stage) {
    MainView mainView = new MainView(stage);
    Scene scene = new Scene(new JFXDecorator(stage, mainView.root), 800, 800);
    scene.getStylesheets().add(getClass().getResource("/resources/css/kabasuji.css").toExternalForm());
    stage.setMinWidth(700);
    stage.setMinHeight(700);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    if (!System.getProperty("os.name").contains("Linux")) {
      JFrame frame = new JFrame("Useless Swing SplashScreen");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      JLabel label = new JLabel("Please wait while we call thread.sleep(5000)");
      frame.getContentPane().add(label);
      frame.setLocation(500, 500);
      frame.setSize(500, 300);
      frame.setVisible(true);

      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      frame.setVisible(false);
      frame.dispose();
    }

    launch(args);
  }}
