package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import main.Main;

public class MenuController {

	@FXML
	private MenuItem menuQuit, menuAbout;

	@FXML
	private void handleMenuItem(ActionEvent event) {
		MenuItem source = (MenuItem) event.getSource();
		if(source == menuQuit){
			Main.logger.info("menuQuit - Clicked");
			Platform.exit();
		}
		if (source == menuAbout){
			Main.logger.info("menuAbout - Clicked");
		}
	}
}
