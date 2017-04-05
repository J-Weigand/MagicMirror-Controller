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
			versionInfo();
		}
	}
	
	private void versionInfo() {
		String title = "Magic Mirror - Control Software";
		String msg = "Engineer: Joshua Weigand\n" + "Company: Weigand Tech\n" + "Last Modified: 04/03/2017\n"
				+ "Version: 2017.04\n";
		HomeController.credControl.infoMessage(title, msg);
	}

}
