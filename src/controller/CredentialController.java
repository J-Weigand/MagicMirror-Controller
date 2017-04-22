package controller;

import java.util.Optional;

import core.mirrorGateway;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.util.Pair;
import main.Main;

public class CredentialController {
	public static mirrorGateway mG = new mirrorGateway();

	public void login() {
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("SmartMirror");
		dialog.setHeaderText("Login to the selected SmartMirror");

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was
		// entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button
		// is clicked.
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
			mirrorGateway.setFtpUser(usernamePassword.getKey());
			mirrorGateway.setFtpPswd(usernamePassword.getValue());
			Main.logger.info("USER: " + mirrorGateway.getFtpUser() + " PASS: " + mirrorGateway.getFtpPswd());
		});
	}

	public String alertMessage(String title, String msg) {
		// Create and Build Alert Type
		Alert alert = new Alert(AlertType.WARNING);
		Button OK = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		OK.setDefaultButton(true);
		alert.setTitle("");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setHeaderText(title);
		alert.setResizable(false);
		alert.setOnCloseRequest(null);
		alert.setContentText(msg);
		// Get Selected Input
		Optional<ButtonType> result = alert.showAndWait();
		return result.get().getText();
	}

	public String infoMessage(String title, String msg) {
		// Create and Build Alert Type
		Alert alert = new Alert(AlertType.INFORMATION);
		Button OK = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
		OK.setDefaultButton(true);
		alert.setTitle("");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setHeaderText(title);
		alert.setResizable(false);
		alert.setOnCloseRequest(null);
		alert.setContentText(msg);
		// Get Selected Input
		Optional<ButtonType> result = alert.showAndWait();
		return result.get().getText();
	}
}
