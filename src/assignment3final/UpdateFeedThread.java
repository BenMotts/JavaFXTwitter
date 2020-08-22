package assignment3final;

import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

public class UpdateFeedThread implements Runnable {

	private volatile boolean exit = false;
	ClientGUI ui;
	TwitterClient client;
	
	
	public UpdateFeedThread(TwitterClient client, ClientGUI ui) {
		this.ui = ui;
		this.client = client;
	}

	public void run() {
		while (!exit) {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (client.checkNewMessageRequest())
				Platform.runLater(() ->{
					ui.updateFeed();
				});
		}
	}

	public void stop() {
		exit = true;
	}

}
