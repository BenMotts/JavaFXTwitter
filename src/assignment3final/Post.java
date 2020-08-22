package assignment3final;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Post extends VBox {
	
	private Label text;
	private Label chan;
	private Label time;
	
	public Post(Message msg, ObservableValue<? extends Number> width) {
		this.setPadding(new Insets(5, 5, 5, 5));
		this.getStylesheets().add("/darkstyles.css");
		this.getStyleClass().addAll("bg-light", "border", "border-primary", "border-bottom");
		this.setMinHeight(50);
		
		text = new Label(msg.getMessageBody());
		chan = new Label(msg.getChannelFrom());
		time = new Label("At: " + msg.getTimeStamp());
		
		
		text.setWrapText(true);
		text.prefWidthProperty().bind(this.widthProperty());
		
		chan.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		
		this.prefWidthProperty().bind(width);
		this.getChildren().addAll(chan, text, time);		
	}
}
