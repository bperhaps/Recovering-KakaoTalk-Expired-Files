package kakaoproj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;

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
	private Label toSrcLabel;
	@FXML
	private Label fromSrcLabel;
	@FXML
	private Button startButton;

	private String manualToSrc;
	private String manualFromSrc;

	private MainApp mainApp;

	public KakaoProjViewController() {}

	private void showToSrc(String src) {
		this.toSrcLabel.setText(src);
	}

	private void showFromSrc(String src) {
		this.fromSrcLabel.setText(src);
	}

	public void setmainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@FXML
	private void initialize() {
	}

	@FXML
	private void handleManualStart() throws Exception {

		if (toSrcLabel.getText().equals("Not selected...") || fromSrcLabel.getText().equals("Not selected...")) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid Source");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText("Source not selected");
			alert.showAndWait();
			return;
		}

		manualToSrc = toSrcLabel.getText();
		manualFromSrc = fromSrcLabel.getText();

		showToSrc("Plz Wait!!!");
		showFromSrc("DO NOT CLOSE THIS WINDOW!!");
		startButton.setDisable(true);

		Thread thread = new Thread(() -> {
			FileHandler.exploreDir(manualToSrc, manualFromSrc);
		});

		thread.start();
		thread.join();

		showToSrc("FINISHED");
		showFromSrc("FINISHED");

		startButton.setDisable(false);

	}

	@FXML
	private void handleManualToSrc() {
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		showToSrc(selectedDc.getPath());
	}

	@FXML
	private void handleManualFromSrc() {
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		showFromSrc(selectedDc.getPath());
	}
}
