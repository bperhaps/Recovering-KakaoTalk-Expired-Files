package kakaoproj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

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
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid Source");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText("Source not selected");
			alert.showAndWait();
			return;
		}

		AutoDetection autoDetection = AutoDetection.getInstance();

		disableEverything();

		Path fromPath = autoDetection.start();
		if(fromPath == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid Source");
			alert.setHeaderText("Can not found Device or KakaoTalk temporary file folder");
			alert.setContentText("The device cannot be found or the KakaoTalk temporary file folder cannot be found.\n" +
					"Please try manually.");
			alert.showAndWait();
			enableEverything();
			return;
		}

		Thread thread = new Thread(() -> {
			FileHandler.exploreDir(
					fromPath.toString(),
					toAutomationSrcLabel.getText()
			);

			enableEverything();
			automationStartButton.setText(automationStartButtonText);
		});
		thread.start();

		automationStartButton.setText(waitingText);
	}

	@FXML
	private void handleManualStart() throws Exception {

		if (toManualSrcLabel.getText().equals("Not selected...") || fromManualSrcLabel.getText().equals("Not selected...")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid Source");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText("Source not selected");
			alert.showAndWait();
			return;
		}

		disableEverything();

		Thread thread = new Thread(() -> {
			FileHandler.exploreDir(
					fromManualSrcLabel.getText(),
					toManualSrcLabel.getText()
			);
			enableEverything();
			manualStartButton.setText(manualStartButtonText);
		});

		manualStartButton.setText(waitingText);

		thread.start();
	}

	@FXML
	private void handleAutomationToSrc() {
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		showToAutomationSrc(selectedDc.getPath());
	}

	@FXML
	private void handleManualToSrc() {
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		showToManualSrc(selectedDc.getPath());
	}

	@FXML
	private void handleManualFromSrc() {
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		showFromManualSrc(selectedDc.getPath());
	}

	private void disableEverything() {
		manualStartButton.setDisable(true);
		automationStartButton.setDisable(true);
	}

	private void enableEverything() {
		manualStartButton.setDisable(false);
		automationStartButton.setDisable(false);
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
