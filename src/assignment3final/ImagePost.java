package assignment3final;

import java.io.IOException;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class ImagePost extends Post {

	private BorderPane container;
	private ImageView imgView;
	private Image img;
	
	public ImagePost(ImageMessage msg, ObservableValue<? extends Number> width) {
		super(msg, width);
		try {
			this.img = msg.getImageFromString();
			container = new BorderPane();
			imgView = new ImageView(img);
			imgView.setPreserveRatio(true);
			imgView.fitWidthProperty().bind(width);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		container.setCenter(imgView);
		this.getChildren().add(container);
	}
}
