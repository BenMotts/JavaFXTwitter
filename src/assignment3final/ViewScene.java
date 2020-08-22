package assignment3final;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ViewScene extends BorderPane {
	
	BorderPane contentContainer;
	HBox titleContainer;
	Label titleText;
	
	private Label headerText;
	private HBox header;
	
	public ViewScene(BorderPane content, String title, String identity) {
		darkMode();
		
		titleContainer = initialiseTitle(title);		
		contentContainer = new BorderPane();	
		this.setCenter(contentContainer);	
		contentContainer.setTop(titleContainer);
		contentContainer.setCenter(content);
		
		header = initialiseHeader(identity);
		content.setTop(header);
	}
	
	public void setTitle(String text) {
		titleText.setText(text);
	}
	
	private HBox initialiseTitle(String title) {
		HBox toReturn = new HBox();
		toReturn.getStyleClass().add("bg-dark");
		toReturn.setPadding(new Insets(15, 12, 15, 12));
		
		titleText = new Label(title);
		titleText.getStyleClass().add("text-primary");
		titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		toReturn.getChildren().add(titleText);
		return toReturn;
	}
	
	public void setHeader(String identity) {
		headerText.setText("Current Channel: @" + identity);
	}
	
	public void darkMode() {
		this.getStylesheets().remove("/lightstyles.css");
		this.getStylesheets().add("/darkstyles.css");
	}
	
	public void lightMode() {
		this.getStylesheets().remove("/darkstyles.css");
		this.getStylesheets().add("/lightstyles.css");
	}
	
	private HBox initialiseHeader(String identity) {
		HBox toReturn = new HBox();
		toReturn.getStyleClass().add("bg-primary");
		toReturn .setPadding(new Insets(5, 10, 5, 10));
		
		headerText = new Label("Current Channel: " + identity);
		headerText.getStyleClass().add("text-dark");
		headerText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		
		toReturn.getChildren().add(headerText);
		return toReturn;
	}
	
}
