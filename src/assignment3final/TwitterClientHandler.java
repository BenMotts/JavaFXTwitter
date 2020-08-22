package assignment3final;
// compile: javac -cp json-simple-1.1.1.jar;. ClientHandler.java

import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.*;
import org.json.simple.*; // required for JSON encoding and decoding

public class TwitterClientHandler extends Thread {
	private Socket client;
	private PrintWriter toClient;
	private BufferedReader fromClient;
	private int clientNum;
	private AtomicInteger timeStamp;
	private ConcurrentHashMap<String, Channel> channels;
	private BufferedWriter saveWriter;

	public TwitterClientHandler(Socket socket, int num, AtomicInteger timeStamp,
			ConcurrentHashMap<String, Channel> channels, BufferedWriter saveWriter) throws IOException {
		client = socket;
		toClient = new PrintWriter(client.getOutputStream(), true);
		fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		clientNum = num;
		this.channels = channels;
		this.timeStamp = timeStamp;
		this.saveWriter = saveWriter;
	}

	public void run() {
		try {
			String inputLine;
			@SuppressWarnings("unused")
			int currentTime;
			System.out.println("SERVER: Client " + clientNum + " connected");
			while ((inputLine = fromClient.readLine()) != null) {
				synchronized (TwitterClientHandler.class) {
					currentTime = timeStamp.incrementAndGet();
				}
				// Debug print to server console
				System.out.println("C" + clientNum + " >>> " + inputLine);
				// Parse request from client
				JSONObject jsonFromClient;
				synchronized (TwitterClientHandler.class) {
					jsonFromClient = (JSONObject) JSONValue.parse(inputLine);
				}
				// Initialise client response
				Response clientResponse = null;
				String requestType = (String) jsonFromClient.get("_class");
				switch (requestType) {
				case "OpenRequest":
					clientResponse = openRequestMethod((String) jsonFromClient.get("identity"));
					break;
				case "PublishRequest":
					clientResponse = publishRequestMethod((String) jsonFromClient.get("identity"),
							(JSONObject) jsonFromClient.get("message"));
					synchronized (TwitterClientHandler.class) {
						saveMessage((JSONObject) jsonFromClient.get("message"), timeStamp.get());
					}
					break;
				case "SubscribeRequest":
					clientResponse = subscribeRequestMethod((String) jsonFromClient.get("channel"),
							(String) jsonFromClient.get("identity"));
					break;
				case "UnsubscribeRequest":
					clientResponse = unsubscribeRequestMethod((String) jsonFromClient.get("channel"),
							(String) jsonFromClient.get("identity"));
					break;
				case "GetRequest":
					clientResponse = getRequestMethod((String) jsonFromClient.get("identity"),
							(int) (long) jsonFromClient.get("after"));
					break;
				case "GetSubsRequest":
					clientResponse = getSubsRequestMethod((String) jsonFromClient.get("identity"));
					break;
				case "ChannelSearchRequest":
					clientResponse = channelSearchRequestMethod((String) jsonFromClient.get("search"),
							(String) jsonFromClient.get("identity"));
					break;
				case "MessageSearchRequest":
					clientResponse = messageSearchRequestMethod((String) jsonFromClient.get("search"));
					break;
				case "NewMessageBoolRequest":
					clientResponse = messageBoolRequestMethod((String) jsonFromClient.get("identity"),
							(int) (long) jsonFromClient.get("after"));
					break;
				default:
					clientResponse = new ErrorResponse("INVALID REQUEST");
					break;
				}

				// Respond back to client
				System.out.println("C" + clientNum + " <<< " + clientResponse.toJSON().toJSONString());
				toClient.println(clientResponse.toJSON().toJSONString());
			}

		} catch (IOException e) {
			System.out.println("Exception while connected");
			System.out.println(e.getMessage());
		}
	}

	// If client sends open request for new channel
	public Response openRequestMethod(String channelName) {
		if (!channels.containsKey(channelName)) {
			Channel ch = new Channel(channelName);
			channels.put(channelName, ch);
		}
		return new SuccessResponse();
	}

	// If client sends request to publish to channel
	public Response publishRequestMethod(String channelName, JSONObject msgJSON) {
		Message msg;
		if (msgJSON.containsKey("image"))
			msg = (ImageMessage) new ImageMessage((String) msgJSON.get("body"), (int) (long) timeStamp.get(),
					(String) msgJSON.get("from"), (String) msgJSON.get("image"));
		else
			msg = new Message((String) msgJSON.get("body"), (int) (long) timeStamp.get(), (String) msgJSON.get("from"));
		if (channels.size() > 0 && channels.containsKey(channelName)) {
			channels.get(channelName).publish(msg);
			return new SuccessResponse();
		}
		return new ErrorResponse("MUST OPEN CHANNEL TO PUBLISH");
	}

	// If client sends subscribe request
	public Response subscribeRequestMethod(String channelName, String identity) {
		if (channels.size() > 0 && channels.containsKey(channelName)) {
			channels.get(identity).subscribeTo(channels.get(channelName));
			return new SuccessResponse();
		}
		return new ErrorResponse("NO SUCH CHANNEL: " + channelName);
	}

	// If client sends unsubscribe request
	public Response unsubscribeRequestMethod(String channelName, String identity) {
		if (channels.size() > 0 && channels.containsKey(channelName)) {
			channels.get(identity).unsubscribeFrom(channels.get(channelName));
			return new SuccessResponse();
		}
		return new ErrorResponse("NO SUCH CHANNEL: " + channelName);
	}

	// If client sends get request
	public Response getRequestMethod(String identity, int afterTime) {
		return new MessageListResponse(channels.get(identity).getAllSubscribedMessages(afterTime));
	}

	// If client sends request to get list of all subscriptions
	public Response getSubsRequestMethod(String identity) {
		return new SubsListResponse(channels.get(identity).getSubscribedNames());
	}

	public Response channelSearchRequestMethod(String search, String identity) {
		List<String> searchResults = new ArrayList<String>();
		for (Channel ch : channels.values()) {
			if (ch.containsSearch(search) && ch != channels.get(identity))
				searchResults.add(ch.getName());
		}
		return new SubsListResponse(searchResults);
	}

	public Response messageSearchRequestMethod(String search) {
		List<Message> searchResults = new ArrayList<Message>();
		for (Channel ch : channels.values()) {
			searchResults.addAll(ch.messageSearch(search));
		}
		return new MessageListResponse(searchResults);
	}

	public Response messageBoolRequestMethod(String identity, int after) {
		if (channels.get(identity).hasUnreadMessagesAfter(after))
			return new SuccessResponse();
		return new ErrorResponse("No new messages");
	}

	// Save a message to text file
	@SuppressWarnings({ "unchecked", "exports" })
	public void saveMessage(JSONObject msgJSON, int msgTime) {
		msgJSON.put("when", msgTime);
		try {
			saveWriter.write(msgJSON.toJSONString());
			saveWriter.newLine();
			saveWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}