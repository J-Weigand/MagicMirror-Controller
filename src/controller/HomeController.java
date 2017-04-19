package controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import core.configMirror;
import core.mirrorGateway;
import core.networkScanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	@FXML
	private CheckBox override;
	@FXML
	private TextField ipInput;

	private static int COUNT = 0;
	private List<String> availPCs = new ArrayList<String>();
	public static networkScanner connManager = new networkScanner();
	public static CredentialController credControl = new CredentialController();
	private configMirror CM = new configMirror();

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
	private void handleOverride(ActionEvent event) {
		CheckBox source = (CheckBox) event.getSource();
		if (source == override) {
			if (override.isSelected()) {
				Main.logger.info("[Override] Selection - ON");
				selection.setVisible(false);
				connectBtn.setText("Connect");
				ipInput.setVisible(true);
				COUNT++;
			} else {
				Main.logger.info("[Override] Selection - OFF");
				selection.setVisible(true);
				connectBtn.setText("Scan");
				ipInput.setVisible(false);
				COUNT--;
			}
		}
	}

	@FXML
	private void handleButton(ActionEvent event) throws UnknownHostException, IOException {
		Button source = (Button) event.getSource();
		if (source == applyBtn) {
			Main.logger.info("Apply Button - Clicked");
			if (!CM.createConfig(timeOn.isSelected(), newsOn.isSelected(), weatherOn.isSelected())) {
				message.setText("An error occured while creating new config file");
			} else {
				if (mirrorGateway.uploadConfig()) {
					message.setText("Display Updated Successfully");
				} else {
					credControl.infoMessage("Update Unsuccessful",
							"An error occured while updating MagicMirror preferences. Please try again.");
				}
			}
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
						availPCs = connManager.scanNetwork();
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
			} else if (source == connectBtn && override.isSelected() || COUNT == 1) {
				if (selection.getValue() != null) {
					mirrorGateway.setFtpHost(selection.getValue());
					message.setText("Attempting to connect to: " + selection.getValue());
					// selection.setDisable(true);
					// connectBtn.setDisable(true);
					credControl.login();
					if (mirrorGateway.testConnection()) {
						credControl.infoMessage("Login Successful", "Magic Mirror connection achieved");
						connectBtn.setText("Disconnect");
						message.setText("Connected to: " + selection.getValue());
						connectBtn.setDisable(false);
						override.setDisable(true);
						COUNT++;
					} else {
						credControl.infoMessage("Connection Refused",
								"The username and password used is incorrect. Please try again.");
						credControl.login();
					}
				}
			} else if (COUNT == 2) {
				message.setText("Diconnected from: " + selection.getValue());
				initialize();
				connectBtn.setText("Scan");
				COUNT = 0;
			}
		}
	}
}
