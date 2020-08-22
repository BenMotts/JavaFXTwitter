package assignment3final;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.event.*;

public class ClientGUI extends Application {

	private Stage window; // Primary stage being shown to user

	// Seperate scenes for main functionality and opening a channel
	private Scene openChannel;
	private Scene mainScene;

	// Seperate pages (BorderPane's) to contain specific functions
	private ScrollScene channelFeed;
	private ScrollScene channelSubs;
	private SearchScene channelSearch;
	private PostScene channelPost;

	// fullPane = main container for the content being shown to user on mainScene
	BorderPane fullPane;

	// Nav buttons
	private Button postButton;
	private Nav navbar;

	// Runnable class to allow live updating of the feed without refreshing the page
	private UpdateFeedThread liveFeedUpdater;

	// Host and Port for server socket connection
	String hostName;
	int portNumber;

	// Generic global variables for changing initial size of client window
	int hSize = 600;
	int vSize = 450;

	// Connection between UI and server
	private TwitterClient client;

	// Constructor
	public ClientGUI() {

		fullPane = new BorderPane();

		navbar = initialiseNav();

		channelFeed = new ScrollScene("", "Your Feed");
		channelSubs = new ScrollScene("", "Your Subscriptions");
		channelSearch = initialiseSearchScene();
		channelPost = new PostScene("", initialisePostBtn(), initialiseAttachImgBtn());

		fullPane.setLeft(navbar);
		fullPane.setCenter(channelFeed.getWindow());

		openChannel = initialiseOpenChannelScene();

		mainScene = new Scene(fullPane, hSize, vSize);
	}

	public static void main(String[] args) {

		launch(args);
	}

	public void start(Stage primaryStage) {
		window = primaryStage;

		TextInputDialog hostNameInput = new TextInputDialog();
		hostNameInput.setHeaderText("Enter Host Name");
		hostNameInput.showAndWait();

		TextInputDialog portNumberInput = new TextInputDialog();
		portNumberInput.setHeaderText("Enter Port Number");
		portNumberInput.showAndWait();

		client = new TwitterClient(hostNameInput.getEditor().getText(),
				Integer.parseInt(portNumberInput.getEditor().getText()), this);

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent e) {
				liveFeedUpdater.stop();
				Platform.exit();
				System.exit(0);
			}
		});

		window.setScene(openChannel);
		window.setTitle("Twitter Client");
		window.show();

		new Thread(() -> {
			client.openConnection();
		}).start();

	}

	public void darkMode() {
		channelFeed.darkMode();
		channelSubs.darkMode();
		channelSearch.darkMode();
		channelPost.darkMode();
		navbar.darkMode();
	}

	public void lightMode() {
		channelFeed.lightMode();
		channelSubs.lightMode();
		channelSearch.lightMode();
		channelPost.lightMode();
		navbar.lightMode();
	}

	// Runnable for thread, consistently sends get requests to find new messages
	// (live feed)
	public Runnable updateFeed() {
		for (Message msg : client.getRequest(client.getLastMessageRequest())) {
			if (msg instanceof ImageMessage) {
				ImageMessage iMsg = (ImageMessage) msg;
				channelFeed.addToScroller(new ImagePost(iMsg, channelFeed.getScrollWidthProperty()));
			} else {
				channelFeed.addToScroller(new Post(msg, channelFeed.getScrollWidthProperty()));
			}
		}
		return liveFeedUpdater;
	}

	// open a new channel
	public void openRequest(String newIdentity) {
		if (client.openRequest(newIdentity)) {
			channelFeed.setHeader(newIdentity);
			channelSearch.setHeader(newIdentity);
			channelSubs.setHeader(newIdentity);
			channelPost.setHeader(newIdentity);
			window.setScene(mainScene);
		} else {
			showErrorBox("Unable to open channel with name :" + newIdentity);
		}
	}

	// Show feed pane (in center of main with nav remaining on left)
	public void showFeed() {
		fullPane.setCenter(channelFeed.getWindow());
		channelFeed.clearScroller();

		for (Message msg : client.getRequest(0)) {
			if (msg instanceof ImageMessage) {
				ImageMessage iMsg = (ImageMessage) msg;
				channelFeed.addToScroller(new ImagePost(iMsg, channelFeed.getScrollWidthProperty()));
			} else {
				channelFeed.addToScroller(new Post(msg, channelFeed.getScrollWidthProperty()));
			}
		}

		liveFeedUpdater = new UpdateFeedThread(client, this);
		Thread live = new Thread(liveFeedUpdater);
		live.setDaemon(true);
		live.start();
	}

	public void showSearch() {
		liveFeedUpdater.stop();
		fullPane.setCenter(channelSearch.getWindow());
	}

	public void showSubscriptions() {
		liveFeedUpdater.stop();
		fullPane.setCenter(channelSubs.getWindow());
		channelSubs.clearScroller();
		for (String ch : client.getSubRequest()) {
			Button unsubBtn = new Button("Unsubscribe");

			ChannelBox chanSubBox = new ChannelBox(ch, unsubBtn, channelSubs.getScrollWidthProperty());

			EventHandler<ActionEvent> unsubBtnClick = new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					client.unsubRequest(ch);
					channelSubs.removeFromScroller(chanSubBox);
				}
			};

			unsubBtn.setOnAction(unsubBtnClick);

			channelSubs.addToScroller(chanSubBox);
		}
	}

	public void showPublishScene() {
		liveFeedUpdater.stop();
		fullPane.setCenter(channelPost.getWindow());
	}

	public void showErrorBox(String error) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText("An Error Has Occurred");
		alert.setContentText(error);
		alert.showAndWait();
	}

	// UI object initialisation classes

	private Button initialisePostBtn() {
		Button toReturn = new Button("Publish");

		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (!channelPost.isTextEmpty()) {
					if (channelPost.hasImage())
						client.publishRequest(channelPost.getText(), channelPost.getImage());
					else
						client.publishRequest(channelPost.getText());
					showFeed();
				}
			}
		};

		toReturn.setOnAction(event);
		return toReturn;
	}

	private Button initialiseAttachImgBtn() {
		Button toReturn = new Button("Attach Image");
		FileChooser fileCh = new FileChooser();
		fileCh.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"));

		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				channelPost.setImage(fileCh.showOpenDialog(window));
			}
		};

		toReturn.setOnAction(event);
		return toReturn;
	}

	private SearchScene initialiseSearchScene() {

		Button searchBtn = new Button();

		SearchScene toReturn = new SearchScene("", searchBtn);

		EventHandler<ActionEvent> subBtnClicked = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
			}
		};

		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (toReturn.isChannelSearch() && toReturn.getSearchText().length() > 0) {
					List<String> channels = client.channelSearchRequest(toReturn.getSearchText());
					toReturn.clearSearch();
					for (String ch : channels) {
						Button btn = new Button("Subscribe");

						EventHandler<ActionEvent> subBtnClicked = new EventHandler<ActionEvent>() {
							public void handle(ActionEvent e) {
								client.subRequest(ch);
								showSubscriptions();
							}
						};
						
						btn.setOnAction(subBtnClicked);
						toReturn.addToScroller(new ChannelBox(ch, btn, toReturn.getMaxWidth()));
					}
				} else if (toReturn.isHashSearch() && toReturn.getSearchText().length() > 0) {
					List<Message> messages = client.messageSearchRequest(toReturn.getSearchText());
					toReturn.clearSearch();
					for (Message msg : messages) {
						if (msg instanceof ImageMessage)
							toReturn.addToScroller(new ImagePost((ImageMessage) msg, toReturn.getMaxWidth()));
						else
							toReturn.addToScroller(new Post(msg, toReturn.getMaxWidth()));
					}
				}
			}
		};

		searchBtn.setOnAction(event);
		return toReturn;
	}

	private OpenChannelScene initialiseOpenChannelScene() {

		Button openChannelBtn = new Button("Open");

		OpenChannelScene toReturn = new OpenChannelScene(openChannelBtn, new BorderPane(), hSize, vSize);
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (toReturn.hasText()) {
					openRequest(toReturn.getText());
					showFeed();
				}
			}
		};

		openChannelBtn.setOnAction(event);

		return toReturn;
	}

	private Nav initialiseNav() {
		Nav toReturn = new Nav(loadNavBtnEvents(), loadSettingsBtnEvents());

		return toReturn;
	}

	private List<Button> loadSettingsBtnEvents() {
		List<Button> toReturn = new ArrayList<Button>();

		toReturn.add(new Button("Light"));
		toReturn.add(new Button("Dark"));
		toReturn.add(new Button("Logout"));

		EventHandler<ActionEvent> setLightTheme = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				lightMode();
			}
		};

		EventHandler<ActionEvent> setDarkTheme = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				darkMode();
			}
		};

		EventHandler<ActionEvent> showOpenScene = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				window.setScene(openChannel);
			}
		};

		toReturn.get(0).setOnAction(setLightTheme);
		toReturn.get(1).setOnAction(setDarkTheme);
		toReturn.get(2).setOnAction(showOpenScene);

		return toReturn;
	}

	private HBox loadThemeBtns() {
		HBox toReturn = new HBox();

		Button darkTheme = new Button("Dark");
		Button lightTheme = new Button("Light");
		Button signOut = new Button("Logout");

		toReturn.getChildren().addAll(darkTheme, lightTheme, signOut);
		return toReturn;
	}

	private List<Button> loadNavBtnEvents() {
		List<Button> toReturn = new ArrayList<Button>();

		toReturn.add(new Button("Feed"));
		toReturn.add(new Button("Post"));
		toReturn.add(new Button("Subscriptions"));
		toReturn.add(new Button("Search"));

		EventHandler<ActionEvent> loadFeed = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (fullPane.getCenter() != channelFeed.getWindow())
					showFeed();
			}
		};

		EventHandler<ActionEvent> loadSubs = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (fullPane.getCenter() != channelSubs.getWindow())
					showSubscriptions();
			}
		};

		EventHandler<ActionEvent> loadSearch = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (fullPane.getCenter() != channelSearch.getWindow())
					showSearch();
			}
		};
		EventHandler<ActionEvent> loadCreatePost = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (fullPane.getCenter() != channelPost.getWindow())
					showPublishScene();
			}
		};

		toReturn.get(0).setOnAction(loadFeed);
		toReturn.get(1).setOnAction(loadCreatePost);
		toReturn.get(2).setOnAction(loadSubs);
		toReturn.get(3).setOnAction(loadSearch);

		return toReturn;
	}
}
