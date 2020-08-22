package assignment3final;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ScrollScene {

	private ViewScene scrollWindow;
	BorderPane scrollContainer;
	private ScrollPane scroller;
	FlowPane scrollContent;
	
	public ScrollScene(String identity, String titleText) {
		scrollContainer = new BorderPane();
		scroller = new ScrollPane();
		
		scroller.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroller.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		scrollContent = new FlowPane();	
		scrollWindow = new ViewScene(scrollContainer, titleText, identity);

		scrollContent.prefWidthProperty().bind(scroller.widthProperty());
		scrollContainer.setCenter(scroller);
		
		scroller.setContent(scrollContent);	
	}
	
	public void darkMode() {
		scrollWindow.darkMode();
	}
	
	public void lightMode() {
		scrollWindow.lightMode();
	}
	
	public void setHeader(String identity) {
		scrollWindow.setHeader(identity);
	}
	
	public void clearScroller() {
		scrollContent.getChildren().clear();
	}
	
	public ViewScene getWindow() {
		return scrollWindow;
	}
	
	public ReadOnlyDoubleProperty getScrollWidthProperty() {
		return scrollContent.widthProperty();
	}
	
	public void addToScroller(VBox item) {
		scrollContent.getChildren().add(0, item);
	}
	
	public void removeFromScroller(VBox item) {
		if (scrollContent.getChildren().contains(item)) {
			scrollContent.getChildren().remove(item);
		}
	}
}
