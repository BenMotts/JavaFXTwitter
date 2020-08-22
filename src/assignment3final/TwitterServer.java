package assignment3final;
import java.net.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class TwitterServer {

	static ConcurrentHashMap<String, Channel> channels;
	
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}

		int portNumber = Integer.parseInt(args[0]);
		// Create map collection to hold channels, string key to allow fast access
		channels = new ConcurrentHashMap<String, Channel>();

		// Create integer (to be passed to new clients) to allow tracking of timestamp
		AtomicInteger timeStamp = new AtomicInteger();

		// Create int to hold client number
		int clientCount = 0;

		try {
			// Buffered reader to load existing data (Catch clause triggered if file does
			// not exist)
			BufferedReader loadFileReader = new BufferedReader(new FileReader("msg-data.txt"));
			loadSavedMessages(loadFileReader, channels);
			System.out.println("SERVER: Loaded existing messages");
		} catch (FileNotFoundException e) {
			System.out.println("SERVER: No existing messages found");
		}
		// Buffered writer for saving data
		BufferedWriter saveFileWriter = new BufferedWriter(new FileWriter("msg-data.txt"));

		// Create new socket for clients to connect to
		try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));) {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				clientCount++;
				// Create client handler thread object (Which handles requests) for new client
				new TwitterClientHandler(clientSocket, clientCount, timeStamp, channels, saveFileWriter).start();
			}

		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
	}

	// Load existing messages from a buffered reader (connected to existing file)
	public static void loadSavedMessages(BufferedReader msgReader, ConcurrentHashMap<String, Channel> channels) {
		String line = null;
		try {
			while ((line = msgReader.readLine()) != null) {
				// Create JSONObject from file line
				JSONObject msgObj = (JSONObject) JSONValue.parse(line);

				// If channel message is from does not exist
				if (!channels.containsKey(msgObj.get("from")))
					channels.put(msgObj.get("from").toString(), new Channel(msgObj.get("from").toString()));
				// Add message to channel it is 'from'
				if (msgObj.get("image") != null)
					channels.get(msgObj.get("from")).publish(new ImageMessage(msgObj));
				channels.get(msgObj.get("from")).publish(new Message(msgObj));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}