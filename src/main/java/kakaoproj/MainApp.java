package kakaoproj;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import kakaoproj.controller.KakaoProjViewController;

public class MainApp extends Application{

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage){
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Recovering KakaoTalk Expired Files");

		initRootLayout();
		showKakaoProjView();
	}

	public void initRootLayout(){
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
			rootLayout = (BorderPane)loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showKakaoProjView(){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/view/KakaoProjView.fxml"));
			AnchorPane kakaoProjView = (AnchorPane) loader.load();

			rootLayout.setCenter(kakaoProjView);

			KakaoProjViewController controller = loader.getController();

			controller.setmainApp(this);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	public Stage getPrimaryStage(){
		return primaryStage;
	}

	public static void main(String[] args){
		launch(args);
	}
}
