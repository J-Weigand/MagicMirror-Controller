package controller;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.configMirror;
import core.mirrorGateway;
import core.networkScanner;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;
import main.Main;

public class HomeController {

	@FXML
	private ToggleButton time, news, weather, calendar;
	@FXML
	private ImageView Time, Weather, News;
	@FXML
	private Button applyBtn, resetBtn, conBtn, scanBtn;
	@FXML
	private Label message;
	@FXML
	private ComboBox<String> comboSelection;
	@FXML
	private CheckBox override, overrideTime;
	@FXML
	private TextField ipInput;

	private Integer mDelay = 3;
	private configMirror CM = new configMirror();
	private List<String> availPCs = new ArrayList<String>();
	public static networkScanner connManager = new networkScanner();
	public static CredentialController credControl = new CredentialController();
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	/**
	 * Method initiliazes first run instance
	 */
	public void initialize() {
		time.setSelected(true);
		news.setSelected(true);
		weather.setSelected(true);
		calendar.setSelected(true);
		Time.setVisible(true);
		Weather.setVisible(true);
		News.setVisible(true);
		time.setText("On");
		news.setText("On");
		weather.setText("On");
		calendar.setText("On");
	}

	/**
	 * Method activates and deactivates certain on-screen elements
	 * 
	 * @param event
	 */
	@FXML
	private void handleToggle(ActionEvent event) {
		Object source = (Object) event.getSource();
		// Time
		if (source == time) {
			if (!time.isSelected()) {
				Main.logger.info("[Time] Switch - OFF");
				messageSystem("Time - OFF", mDelay);
				overrideTime.setDisable(true);
				time.setText("Off");
				time.setSelected(false);
				Time.setVisible(false);
			} else {
				Main.logger.info("[Time] Switch - ON");
				messageSystem("Time - ON", mDelay);
				overrideTime.setDisable(false);
				time.setText("On");
				time.setSelected(true);
				Time.setVisible(true);
			}
		}
		// News
		if (source == news) {
			if (!news.isSelected()) {
				Main.logger.info("[News] Switch - OFF");
				messageSystem("News - OFF", mDelay);
				news.setText("Off");
				news.setSelected(false);
				News.setVisible(false);
			} else {
				Main.logger.info("[News] Switch - ON");
				messageSystem("News - ON", mDelay);
				news.setText("On");
				news.setSelected(true);
				News.setVisible(true);
			}
		}
		// Weather
		if (source == weather) {
			if (!weather.isSelected()) {
				Main.logger.info("[Weather] Switch - OFF");
				messageSystem("Weather - OFF", mDelay);
				weather.setText("Off");
				weather.setSelected(false);
				Weather.setVisible(false);
			} else {
				Main.logger.info("[Weather] Switch - ON");
				messageSystem("Weather - ON", mDelay);
				weather.setText("On");
				weather.setSelected(true);
				Weather.setVisible(true);
			}
		}
		// Calendar
		if (source == calendar) {
			if (!calendar.isSelected()) {
				Main.logger.info("[Calendar] Switch - OFF");
				messageSystem("Calendar - OFF", mDelay);
				calendar.setText("Off");
				calendar.setSelected(false);
				// Calendar.setVisible(false);
			} else {
				Main.logger.info("[Calendar] Switch - ON");
				messageSystem("Calendar - ON", mDelay);
				calendar.setText("On");
				calendar.setSelected(true);
				// Calendar.setVisible(true);
			}
		}
	}

	/**
	 * Method shows a timed message to alert user of any changes
	 * 
	 * @param msg
	 * @param dur
	 */
	private void messageSystem(String msg, int dur) {
		message.setText(msg);
		PauseTransition pause = new PauseTransition(Duration.seconds(dur));
		pause.setOnFinished(event -> message.setText(""));
		pause.play();
	}

	/**
	 * Method handles all check box (override) events
	 * 
	 * @param event
	 */
	@FXML
	private void handleOverride(ActionEvent event) {
		CheckBox source = (CheckBox) event.getSource();
		if (source == override) {
			if (override.isSelected()) {
				Main.logger.info("[Override] Selection - ON");
				scanBtn.setDisable(true);
				comboSelection.setVisible(false);
				ipInput.setVisible(true);
				ipInput.clear();
			} else {
				Main.logger.info("[Override] Selection - OFF");
				scanBtn.setDisable(false);
				comboSelection.setVisible(true);
				ipInput.setVisible(false);
				ipInput.clear();
			}
		} else if (source == overrideTime) {
			if (overrideTime.isSelected()) {
				Main.logger.info("[Override Time] Selection - ON");
				messageSystem("24 Hour Clock - Enabled", mDelay);
			} else {
				Main.logger.info("[Override Time] Selection - OFF");
				messageSystem("12 Hour Clock - Enabled", mDelay);
			}
		}
	}

	/**
	 * Method controls all button events in the home screen
	 * 
	 * @param event
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@FXML
	private void handleButton(ActionEvent event) throws UnknownHostException, IOException {
		Button source = (Button) event.getSource();
		if (source == applyBtn) {
			Main.logger.info("Apply Button - Clicked");
			if (conBtn.getText().equals("Connect")) {
				credControl.alertMessage("Connect", "Please connect to your SmartMirror to continue");
			} else {
				save();
			}
		} else if (source == resetBtn) {
			Main.logger.info("Cancel Button - Clicked");
			initialize();
		} else if (source == scanBtn) {
			Main.logger.info("Connect Button - Clicked");
			scanNetwork();
		} else if (source == conBtn && conBtn.getText() == "Disconnect") {
			messageSystem("Disconnected from SmartMirror ", mDelay);
			conBtn.setText("Connect");
			comboSelection.setDisable(false);
			ipInput.setDisable(false);
			scanBtn.setDisable(false);
			override.setDisable(false);
		} else if (source == conBtn) {
			Main.logger.info("Connect Button - Clicked");
			if (override.isSelected() && ipInput.getText().isEmpty()) {
				Main.logger.info("Invalid 1");
				credControl.alertMessage("Connect", "Please connect to your SmartMirror to continue");
				return;
			} else if (!override.isSelected() && comboSelection.getValue() == null) {
				Main.logger.info("Invalid 2");
				credControl.alertMessage("Connect", "Please connect to your SmartMirror to continue");
				return;
			} else {
				piLogin();
			}
		}
	}

	/**
	 * Method scans the local network for potential hosts
	 */
	private void scanNetwork() {
		comboSelection.setValue("Loading...");
		message.setText("Scanning Network... Please Wait");
		Runnable expensiveTask = (() -> {
			try {
				if (!availPCs.isEmpty())
					availPCs.clear();
				availPCs = connManager.scanNetwork();
			} catch (IOException e) {
				Main.logger.error("Network Scan failed to execute");
				e.printStackTrace();
			}

			if (!comboSelection.getItems().isEmpty())
				comboSelection.getItems().clear();
			comboSelection.getItems().addAll(availPCs);

			Platform.runLater(() -> {
				comboSelection.setDisable(false);
				scanBtn.setDisable(false);
				comboSelection.setValue(availPCs.get(0));
				messageSystem("Network Scan Complete", mDelay);
			});
		});
		comboSelection.setDisable(true);
		scanBtn.setDisable(true);
		new Thread(expensiveTask).start();
	}

	/**
	 * Method attempts and ensures proper login to RaspPi
	 */
	private void piLogin() {
		Main.logger.info("Entered Pi Login");

		if (override.isSelected()) {
			Main.logger.info("Entered Pi Login - 1");
			if (ipInput.getText() != null) {
				if (!validateIP(ipInput.getText())) {
					Main.logger.info("Validating IP Address");
					credControl.alertMessage("Connect", "Enter a valid IP address.");
					return;
				}
				messageSystem("Attempting to connect to: " + ipInput.getText(), mDelay);
				credControl.login();
				if (mirrorGateway.testConnection(ipInput.getText().toString())) {
					credControl.infoMessage("Login Successful", "SmartMirror connection achieved");
					conBtn.setText("Disconnect");
					ipInput.setDisable(true);
					message.setText("Connected to: " + ipInput.getText());
					comboSelection.setDisable(true);
					scanBtn.setDisable(true);
					override.setDisable(true);
				} else {
					credControl.alertMessage("Login Unsuccessful",
							"Username and passwword are incorrect. Please try again.");
				}
			} else {
				credControl.alertMessage("Connect", "Enter a valid IP address.");
			}
		}

		if (!override.isSelected()) {
			Main.logger.info("Entered Pi Login - 2");
			if (comboSelection.getValue() != null) {
				mirrorGateway.setFtpHost(comboSelection.getValue());
				messageSystem("Attempting to connect to: " + comboSelection.getValue(), mDelay);
				credControl.login();
				if (mirrorGateway.testConnection(null)) {
					credControl.infoMessage("Login Successful", "SmartMirror connection achieved");
					conBtn.setText("Disconnect");
					message.setText("Connected to: " + comboSelection.getValue());
					comboSelection.setDisable(true);
					scanBtn.setDisable(true);
					override.setDisable(true);
				} else {
					credControl.alertMessage("Connection Refused",
							"The username and password used is incorrect. Please try again.");
					credControl.login();
				}
			}
		}
	}

	/**
	 * Method pushes new configuration file to RaspPi
	 */
	private void save() {
		if (!CM.createConfig(time.isSelected(), overrideTime.isSelected(), news.isSelected(), weather.isSelected(),
				calendar.isSelected())) {
			messageSystem("An error occured. Please try again", mDelay);
		} else {
			if (mirrorGateway.uploadConfig()) {
				messageSystem("Display Updated Successfully", mDelay);
			} else {
				credControl.alertMessage("Update Unsuccessful",
						"An error occured while updating MagicMirror preferences. Please try again.");
			}
		}
	}

	/**
	 * Method checks for a valid IP address
	 * 
	 * @param ip
	 * @return
	 */
	public boolean validateIP(final String ip) {
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile(IPADDRESS_PATTERN);
		matcher = pattern.matcher(ip);
		return matcher.matches();
	}
}
