package kakaoproj.view;

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

	private String toSrc;
	private String fromSrc;

	private MainApp mainApp;

	public KakaoProjViewController(){
	}

	private void showToSrc(String src){
		this.toSrcLabel.setText(src);
	}

	private void showFromSrc(String src){
		this.fromSrcLabel.setText(src);
	}


	@FXML
	private void initialize(){
	}

	public void setmainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}

	@FXML
	private void handleStart() throws Exception{

		if(toSrcLabel.getText().equals("Not selected...") || fromSrcLabel.getText().equals("Not selected...")){
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Invalid Source");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText("Source not selected");

			alert.showAndWait();

			return ;
		}

		toSrc = toSrcLabel.getText();
		fromSrc = fromSrcLabel.getText();

		showToSrc("Plz Wait!!!");
		showFromSrc("DO NOT CLOSE THIS WINDOW!!");
		startButton.setDisable(true);

		searchDir(fromSrc);

		showToSrc("FINISHED");
		showFromSrc("FINISHED");

	}

	private void searchDir(String src) throws Exception {
		File dir = new File(src);
		File[] fileList = dir.listFiles();

		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];
			if (file.isFile()) {
				fileCopy(file);
			} else if (file.isDirectory()) {
				searchDir(file.getCanonicalPath().toString());
			}
		}


	}

	private void fileCopy(File file) throws Exception{
		String extension = "";
		FileInputStream inputStream = new FileInputStream(file.getCanonicalPath().toString());
		if( (extension = getExtension(file)) == ""){
			return ;
		}

		SimpleDateFormat sf = new SimpleDateFormat("yyyy.MM.dd");
		File folder = new File(toSrc +
								"/" +
								sf.format(file.lastModified()));

		folder.mkdirs();

		FileOutputStream outputStream = new FileOutputStream(toSrc +
																"/" +
																sf.format(file.lastModified()) +
																"/" +
																file.getName() +
																"." +
																getExtension(file));


		FileChannel fcin = inputStream.getChannel();
		FileChannel fcout = outputStream.getChannel();

		long size = fcin.size();
		fcin.transferTo(0, size, fcout);

		fcin.close();
		fcout.close();

		outputStream.close();
		inputStream.close();

	}

	private String getExtension(File filename) throws Exception {
		FileInputStream is = null;
		try {
			Metadata metadata = new Metadata();
			is = new FileInputStream(filename);
			ContentHandler contenthandler = new BodyContentHandler();
			Parser parser = new AutoDetectParser();
			ParseContext pc = new ParseContext();

			metadata.set(Metadata.RESOURCE_NAME_KEY, filename.getName());
			parser.parse(is, contenthandler, metadata, pc);

			if( is != null )
				is.close();

			return metadata.get(Metadata.CONTENT_TYPE).split("/")[1];
		} catch (Exception e){
			e.printStackTrace();

			if( is != null )
				is.close();

			return "";
		}
	}

	@FXML
	private void handleToSrc(){
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc = dc.showDialog(mainApp.getPrimaryStage());
		showToSrc(selectedDc.getPath());
	}

	@FXML
	private void handleFromSrc(){
		DirectoryChooser dc = new DirectoryChooser();
		File selectedDc  = dc.showDialog(mainApp.getPrimaryStage());
		showFromSrc(selectedDc.getPath());
	}


}
