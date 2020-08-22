package assignment3final;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class OpenChannelScene extends Scene {
	
	private BorderPane container;
	private HBox headerContainer;
	private HBox openChannelInputContainer;
	private Label header;
	private TextField channelInput;
	Button openChannelBtn;

	public OpenChannelScene(Button openChannelBtn, BorderPane container, double hSize, double vSize) {
		super(container, hSize, vSize);
		this.container = container;
		this.openChannelBtn = openChannelBtn;
		container.getStylesheets().add("/darkstyles.css");
		
		headerContainer =initialiseHeaderContainer();
		openChannelInputContainer = initialiseInputContainer();
		header = initialiseHeader();		
		channelInput = initialiseChannelInput();
		styleBtn();
		
		container.setTop(headerContainer);
		container.setCenter(openChannelInputContainer);
		
		openChannelInputContainer.getChildren().addAll(channelInput, openChannelBtn);
		headerContainer.getChildren().add(header);
	}
	
	public boolean hasText() {
		return channelInput.getLength() > 0;
	}
	
	public String getText() {
		return channelInput.getText();
	}
	
	
	
	private TextField initialiseChannelInput() {
		TextField toReturn = new TextField();
		toReturn.setPrefWidth(250);
		toReturn.setFont(Font.font("Arial", 18));
		return toReturn;
	}
	
	private HBox initialiseHeaderContainer() {
		HBox toReturn = new HBox();
		toReturn.getStyleClass().add("bg-dark");
		return toReturn;
	}
	
	private HBox initialiseInputContainer() {
		HBox toReturn = new HBox();
		toReturn.setAlignment(Pos.CENTER);
		toReturn.setSpacing(10);
		return toReturn;
	}
	
	private void styleBtn() {
		openChannelBtn.getStyleClass().addAll("btn", "btn-primary", "btn-square");
		openChannelBtn.setFont(Font.font("Arial", 18));
	}
	
	private Label initialiseHeader() {
		Label toReturn = new Label("Open a Channel");
		toReturn.setPadding(new Insets(15, 12, 15, 12));
		toReturn.getStyleClass().add("text-primary");
		toReturn.setFont(Font.font("Arial", FontWeight.BOLD, 35));
		return toReturn;
	}

}
