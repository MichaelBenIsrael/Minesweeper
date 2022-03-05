package mines;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MinesFX extends Application{
	public static Stage s;
	public static Stage getStage() {
		return s;
	}
	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception {
		s=stage;
		@SuppressWarnings("unused")
		SweeperUI sweeper;
		StackPane root=new StackPane();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("sweeperFXML.fxml"));
			root = loader.load();
			sweeper =loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		stage.setTitle("The Amazing Mines Sweeper");
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.sizeToScene();
		stage.show();
		
	}
}
