package assignment3final;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;

public class PostScene {
	private ViewScene postWindow;
	BorderPane createPostContainer;
	private VBox postControlsContainer;
	// TODO : create attach media btn to post images and videos

	private TextArea postText;
	private Label postTextLabel;
	private Button postBtn;
	private Button attachImgBtn;
	private File imgFile;
	
	public PostScene(String identity, Button postBtn, Button attachImgBtn) {
		createPostContainer = new BorderPane();
		this.attachImgBtn = attachImgBtn;
		this.postBtn = postBtn;
		postWindow = new ViewScene(createPostContainer, "Create Post", identity);

		initialiseUI();
	}
	
	public void darkMode() {
		postWindow.darkMode();
	}
	
	public void lightMode() {
		postWindow.lightMode();
	}
	
	public void setImage(File img) {
		imgFile = img;
	}
	
	public boolean hasImage() {
		return imgFile != null;
	}
	
	public File getImage() {
		return imgFile;
	}

	public boolean isTextEmpty() {
		return postText.getText().length() == 0;
	}

	public String getText() {
		return postText.getText();
	}

	public void setHeader(String identity) {
		postWindow.setHeader(identity);
	}

	public ViewScene getWindow() {
		return postWindow;
	}

	private void initialiseUI() {
		postControlsContainer = new VBox();
		postControlsContainer.getStylesheets().add("/darkstyles.css");

		postText = new TextArea();
		postText.setPrefRowCount(2);
		postText.setStyle("-fx-border-style: hidden");
		postTextLabel = new Label("Text to publish");

		attachImgBtn.getStyleClass().addAll("btn", "btn-primary", "btn-round");
		postBtn.getStyleClass().addAll("btn", "btn-primary", "btn-round");

		postControlsContainer.getChildren().addAll(postTextLabel, postText, attachImgBtn, postBtn);

		createPostContainer.setCenter(postControlsContainer);

		postControlsContainer.setPadding(new Insets(30, 24, 30, 24));
		postControlsContainer.setSpacing(10);
		postControlsContainer.setAlignment(Pos.CENTER_LEFT);

		postTextLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
	}

}
