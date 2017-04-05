package main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.HomeController;
import controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	public static Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		// Set Title
		stage.setTitle("Magic Mirror");
		// Create New Root Pane
		BorderPane root = new BorderPane();
		// Load FXMLs
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/viewLayout.fxml"));
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/viewHome.fxml"));
		// Create and Set Default Layout Controller
		MenuController controller = new MenuController();
		loader.setController(controller);
		HomeController controller2 = new HomeController();
		loader2.setController(controller2);
		// Load views
		Parent view = null;
		Parent view2 = null;
		try {
			view = loader.load();
			view2 = loader2.load();
			Main.logger.info("MenuController Loaded");
		} catch (IOException e1) {
			Main.logger.fatal("MenuController Failed to Load");
		}
		// Plug view into a scene
		Scene scene = new Scene(view);
		// Create Reference to BorderPane
		root = (BorderPane) view;
		// Plug viewHome into root pane
		root.setCenter(view2);
		// set the stage
		stage.setScene(scene);
		stage.show();
	}
}
