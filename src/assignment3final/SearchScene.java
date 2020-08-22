package assignment3final;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SearchScene {

	private ViewScene window;
	private BorderPane container;
	private ScrollPane scroller;
	private FlowPane searchResults;
	
	private VBox searchCriteriaContainer;
	private HBox searchOptions;
	private HBox searchTextContainer;
	private RadioButton searchHashBtn;
	private RadioButton searchChannelBtn;
	private ToggleGroup searchOptionsGroup;
	private TextField searchText;
	private Button searchBtn;
	
	public SearchScene(String identity, Button searchBtn) {
		container = new BorderPane();
		searchResults = new FlowPane();
		scroller = new ScrollPane();
		scroller.setHbarPolicy(ScrollBarPolicy.NEVER);
		scroller.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		searchResults.prefWidthProperty().bind(scroller.widthProperty());
		container.setCenter(scroller);
		
		scroller.setContent(searchResults);
		
		this.searchBtn = searchBtn;
		initialiseOptionBox();
		initialiseOptionSearch();
		initialiseSearchContainer();
		
		searchOptions.prefWidthProperty().bind(scroller.widthProperty());
		
		window = new ViewScene(container, "Search", identity);
		container.setTop(searchCriteriaContainer);
	}
	
	public void darkMode() {
		window.darkMode();
	}
	
	public void lightMode() {
		window.lightMode();
	}
	
	public boolean isChannelSearch() {
		return searchChannelBtn.isSelected();
	}
	
	public boolean isHashSearch() {
		return searchHashBtn.isSelected();
	}
	
	public String getSearchText() {
		return searchText.getText();
	}
	
	public ViewScene getWindow() {
		return window;
	}
	
	public void clearSearch() {
		searchResults.getChildren().clear();
	}
	
	public void addToScroller(VBox item) {
		searchResults.getChildren().add(item);
	}
	
	public void addToScroller(HBox item) {
		searchResults.getChildren().add(item);
	}
	
	public void setHeader(String newIdentity) {
		window.setHeader(newIdentity);
	}
	
	public ReadOnlyDoubleProperty getMaxWidth() {
		return searchResults.widthProperty();
	}
	
	private void initialiseSearchContainer() {
		searchCriteriaContainer = new VBox(5);
		searchCriteriaContainer.setPadding(new Insets(5, 5, 5, 5));
		searchCriteriaContainer.getStylesheets().add("/darkstyles.css");
		searchCriteriaContainer.getStyleClass().add("bg-light");
		searchCriteriaContainer.getChildren().addAll(searchOptions, searchTextContainer);
		searchCriteriaContainer.prefWidthProperty().bind(scroller.widthProperty());
	}
	
	private void initialiseOptionSearch() {
		searchTextContainer = new HBox();
		searchTextContainer.getStylesheets().add("/darkstyles.css");
		
		searchText = new TextField();
		searchText.setStyle("-fx-border-style:hidden");
		
		searchBtn.setText("Search");
		searchBtn.getStyleClass().addAll("btn", "btn-dark");
		
		searchTextContainer.setAlignment(Pos.CENTER);
		searchTextContainer.getChildren().addAll(searchText, searchBtn);
		searchTextContainer.setSpacing(10);
	}
	
	private void initialiseOptionBox() {
		searchOptions = new HBox();
		searchOptionsGroup = new ToggleGroup();
		searchHashBtn = initialiseSearchOptionBtn("Hashtag");
		searchChannelBtn = initialiseSearchOptionBtn("Channel");
		searchHashBtn.setSelected(true);
		searchOptions.getChildren().addAll(searchHashBtn, searchChannelBtn);
	}
	
	private RadioButton initialiseSearchOptionBtn(String txt) {
		RadioButton toReturn = new RadioButton(txt);
		toReturn.getStyleClass().remove("radio-button");
		toReturn.getStyleClass().add("toggle-button");
		toReturn.getStylesheets().add("/darkstyles.css");
		toReturn.getStyleClass().addAll("btn", "btn-dark", "btn-square");
		toReturn.setToggleGroup(searchOptionsGroup);
		HBox.setHgrow(toReturn, Priority.ALWAYS);
		toReturn.setMaxWidth(Double.MAX_VALUE);
		return toReturn;
		
	}

}
