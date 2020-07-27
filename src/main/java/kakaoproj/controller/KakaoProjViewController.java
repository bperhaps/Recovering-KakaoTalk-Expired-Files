package kakaoproj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

import javafx.application.Platform;
import org.apache.commons.io.filefilter.MagicNumberFileFilter;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import kakaoproj.MainApp;

public class KakaoProjViewController {
	@FXML
	private Label toManualSrcLabel;
	@FXML
	private Label fromManualSrcLabel;
	@FXML
	private Label toAutomationSrcLabel;
	@FXML
	private Button manualStartButton;
	@FXML
	private Button automationStartButton;

	private MainApp mainApp;

	final String automationStartButtonText = "Automation Start";
	final String manualStartButtonText = "Manual Start";
	final String waitingText = "Plz Waiting...";

	public KakaoProjViewController() {}

	public void setmainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {
	}

	@FXML
	private void handleAutomationStart() throws Exception {
		if (toAutomationSrcLabel.getText().equals("Not selected...")) {
			alertError("Invalid Source",
					"Please correct invalid fields",
					"Source not selected"
			);
			return;
		}

		AutoDetection autoDetection = AutoDetection.getInstance();

		AutoDetection.ContentRoot contentRoot = autoDetection.getContentRoot();
		if(contentRoot == null) {
			alertError("Invalid Source",
					"Can not found Device or KakaoTalk temporary file folder",
					"The device cannot be found or the KakaoTalk temporary file folder cannot be found.\n" +
							"Please try manually."
					);
			return;
		}
		disableEverything();

		alertInform("Please Wait",
				"Please Wait.",
				"Copying files from the device."
		);

		new Thread(() -> {
			Path fromPath = autoDetection.copyFileFromDevice(contentRoot, Path.of(toAutomationSrcLabel.getText()));

//			FileHandler.exploreDir(
//					fromPath.toString(),
//					toAutomationSrcLabel.getText()
//			);

			enableEverything();
		}).start();
	}

	@FXML
	private void handleManualStart() throws Exception {

		if (toManualSrcLabel.getText().equals("Not selected...") || fromManualSrcLabel.getText().equals("Not selected...")) {
			alertError("Invalid Source",
					"Please correct invalid fields",
					"Source not selected"
					);
			return;
		}

		disableEverything();

		new Thread(() -> {
			FileHandler.exploreDir(
					fromManualSrcLabel.getText(),
					toManualSrcLabel.getText()
			);
			enableEverything();

		}).start();
	}

	@FXML
	private void handleAutomationToSrc() {
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		if(selectedDc == null) return;
		showToAutomationSrc(selectedDc.getPath());
	}

	@FXML
	private void handleManualToSrc() {
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		if(selectedDc == null) return;
		showToManualSrc(selectedDc.getPath());
	}

	@FXML
	private void handleManualFromSrc() {
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		if(selectedDc == null) return;
		showFromManualSrc(selectedDc.getPath());
	}

	private void alertError(String title, String header, String content) {
		generateAlert(title, header, content, AlertType.ERROR);
		return;
	}

	private void alertInform(String title, String header, String content) {
		generateAlert(title, header, content, AlertType.INFORMATION);
		return;
	}

	private void generateAlert(String title, String header, String content, AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

	private void disableEverything() {
		manualStartButton.setDisable(true);
		automationStartButton.setDisable(true);
		Platform.runLater(() -> {
			manualStartButton.setText(waitingText);
			automationStartButton.setText(waitingText);
		});

	}

	private void enableEverything() {
		manualStartButton.setDisable(false);
		automationStartButton.setDisable(false);
		Platform.runLater(()-> {
			manualStartButton.setText(manualStartButtonText);
			automationStartButton.setText(automationStartButtonText);
		});
	}


	private void showToAutomationSrc(String src) {
		this.toAutomationSrcLabel.setText(src);
	}

	private void showToManualSrc(String src) {
		this.toManualSrcLabel.setText(src);
	}

	private void showFromManualSrc(String src) {
		this.fromManualSrcLabel.setText(src);
	}

}
