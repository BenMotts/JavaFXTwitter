package assignment3final;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {

	public void start(Stage s) throws Exception {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 500, 500);
		s.setScene(scene);
		s.setTitle("test");
		s.show();
	}
	
	public static void main (String[]args) {
		launch(args);
	}
}
