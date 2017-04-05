package controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import core.connectionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import main.Main;

public class HomeController {

	@FXML
	private ToggleButton timeOn, timeOff, newsOn, newsOff, weatherOn, weatherOff;
	@FXML
	private ImageView Time, Weather, News;
	@FXML
	private Button applyBtn, cancelBtn, connectBtn;
	@FXML
	private Label message;
	@FXML
	private ComboBox<String> selection;

	private static int COUNT = 0;
	private List<String> availPCs = new ArrayList<String>();
	public static connectionManager connManager = new connectionManager();
	public static CredentialController credControl = new CredentialController();

	public void initialize() {
		timeOn.setSelected(true);
		newsOn.setSelected(true);
		weatherOn.setSelected(true);
		timeOff.setSelected(false);
		newsOff.setSelected(false);
		weatherOff.setSelected(false);
		Time.setVisible(true);
		Weather.setVisible(true);
		News.setVisible(true);
	}

	@FXML
	private void handleToggle(ActionEvent event) {
		ToggleButton source = (ToggleButton) event.getSource();
		// Time
		if (source == timeOn) {
			Main.logger.info("[Time] Switch - ON");
			timeOn.setSelected(true);
			timeOff.setSelected(false);
			Time.setVisible(true);
		} else if (source == timeOff) {
			Main.logger.info("[Time] Switch - OFF");
			timeOn.setSelected(false);
			timeOff.setSelected(true);
			Time.setVisible(false);
		}
		// News
		if (source == newsOn) {
			Main.logger.info("[News] Switch - ON");
			newsOn.setSelected(true);
			newsOff.setSelected(false);
			News.setVisible(true);
		} else if (source == newsOff) {
			Main.logger.info("[News] Switch - OFF");
			newsOn.setSelected(false);
			newsOff.setSelected(true);
			News.setVisible(false);
		}
		// Weather
		if (source == weatherOn) {
			Main.logger.info("[Weather] Switch - ON");
			weatherOn.setSelected(true);
			weatherOff.setSelected(false);
			Weather.setVisible(true);
		} else if (source == weatherOff) {
			Main.logger.info("[Weather] Switch - OFF");
			weatherOn.setSelected(false);
			weatherOff.setSelected(true);
			Weather.setVisible(false);
		}
	}

	@FXML
	private void handleButton(ActionEvent event) throws UnknownHostException, IOException {
		Button source = (Button) event.getSource();
		if (source == applyBtn) {
			Main.logger.info("Apply Button - Clicked");
			message.setText("Display Updated");
		}
		if (source == cancelBtn) {
			Main.logger.info("Cancel Button - Clicked");
			initialize();
		}
		if (source == connectBtn) {
			Main.logger.info("Connect Button - Clicked");
			if (COUNT == 0) {
				selection.setValue("Loading...");
				message.setText("Scanning Network... Please Wait");
				Runnable expensiveTask = (() -> {
					try {
						availPCs = connManager.scanNetwork("192.168.0");
					} catch (IOException e) {
						Main.logger.error("Network Scan failed to execute");
						e.printStackTrace();
					}

					selection.getItems().addAll(availPCs);

					Platform.runLater(() -> {
						selection.setDisable(false);
						selection.setValue(availPCs.get(0));
						message.setText("Network Scan Complete");
						connectBtn.setDisable(false);
					});
				});
				new Thread(expensiveTask).start();

				// List<String> TEMPORARY = new ArrayList<String>();
				// TEMPORARY.add("192.168.0.1");
				// selection.getItems().addAll(TEMPORARY);
				
				connectBtn.setDisable(true);
				connectBtn.setText("Connect");
				COUNT++;
			} else if (COUNT == 1) {
				if (selection.getValue() != null) {
					message.setText("Attempting to connect to: " + selection.getValue());
					selection.setDisable(true);
					connectBtn.setDisable(true);
					credControl.login();
					credControl.infoMessage("Login Successful", "Magic Mirror connection achieved");
					connectBtn.setText("Disconnect");
					message.setText("Connected to: " + selection.getValue());
					connectBtn.setDisable(false);
					COUNT++;
				}
			} else if (COUNT == 2) {
				message.setText("Diconnected from: " + selection.getValue());
				initialize();
				COUNT = 0;
			}
		}
	}
}
