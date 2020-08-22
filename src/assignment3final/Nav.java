package assignment3final;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Nav extends VBox {

	HBox settingsContainer;
	List<Button> buttons;
	List<Button> settingsBtns;

	public Nav(List<Button> buttons, List<Button> settingsBtns) {
		this.buttons = buttons;
		this.settingsBtns = settingsBtns;
		
		this.getStylesheets().add("/darkstyles.css");
		this.getStyleClass().add("bg-dark");
		this.setPrefWidth(125);
		this.setSpacing(15);
		this.setAlignment(Pos.CENTER);
		this.setFillWidth(true);
		this.getChildren().addAll(this.buttons);
		
		settingsContainer = initialiseSettings();
		
		this.getChildren().add(settingsContainer);
		
		for (Button btn : this.buttons) {
			styleButton(btn);
		}
		
		for (Button btn : this.settingsBtns) {
			styleButton(btn);
			btn.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
		}
	}
	
	public void lightMode() {
		this.getStylesheets().remove("/darkstyles.css");
		this.getStylesheets().add("/lightstyles.css");
	}

	public void darkMode() {
		this.getStylesheets().remove("/lightstyles.css");
		this.getStylesheets().add("/darkstyles.css");
	}
	
	private void styleButton(Button toStyle) {
		toStyle.getStyleClass().addAll("btn-dark", "btn-square");
		toStyle.setPrefHeight(50);
		toStyle.setMaxWidth(Double.MAX_VALUE);
	}
	
	private HBox initialiseSettings() {
		HBox toReturn = new HBox();
		toReturn.getChildren().addAll(settingsBtns);
		return toReturn;
	}
}
