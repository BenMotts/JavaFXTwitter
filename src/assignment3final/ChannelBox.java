package assignment3final;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.beans.value.ObservableValue;

public class ChannelBox extends VBox {

	private Label chan;
	private Button unsubBtn;
	
	public ChannelBox(String channelName, Button btn, ObservableValue<? extends Number> width) {
		
		this.setPadding(new Insets(5, 5, 5, 5));
		this.getStylesheets().add("/darkstyles.css");
		this.getStyleClass().addAll("bg-light", "border", "border-primary", "border-bottom");
		this.setMinHeight(40);
		
		chan = new Label(channelName);
		chan.setWrapText(true);
		chan.setFont(Font.font("Arial", FontWeight.BOLD, 15));
		chan.prefWidthProperty().bind(this.widthProperty());
		
		unsubBtn = btn;
		unsubBtn.getStyleClass().addAll("btn-round", "btn-dark");

		this.prefWidthProperty().bind(width);
		this.getChildren().addAll(chan, unsubBtn);	
	}
}