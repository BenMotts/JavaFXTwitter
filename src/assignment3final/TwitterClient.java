package assignment3final;

import java.io.*;
import java.net.*;
import java.util.*;
import org.json.simple.*; // required for JSON encoding and decoding

public class TwitterClient {

	private static String identity;
	static int lastGetRequest = 0;
	private static String hostName;
	private static int portNumber;
	private PrintWriter out;
	private BufferedReader in;
	private boolean keepRunning;

	public TwitterClient(String hostName, int portNumber, ClientGUI ui) {
		TwitterClient.hostName = hostName;
		TwitterClient.portNumber = portNumber;
	}

	public void openConnection() {

		try (Socket serverSocket = new Socket(hostName, portNumber)) {

			// Wirters for json stream to and from server
			out = new PrintWriter(serverSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

			keepRunning = true;

			// Keep socket open
			while (keepRunning) {

			}

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
			System.exit(1);
		}
	}

	// To check that functions do not encounter errors (Should be not possible due
	// to UI but a double check)
	private boolean isIdentitySet() {
		return identity != null;
	}

	// Get timestamp of last get request to allow live feed updating
	public int getLastMessageRequest() {
		return lastGetRequest;
	}

	// Set clients identity/open channel
	public void setIdentity(String identity) {
		TwitterClient.identity = identity;
	}

	public String getIdentity() {
		return identity;
	}

	// Open a new channel belonging to this client
	public boolean openRequest(String newIdentity) {
		Request req = new OpenRequest(newIdentity);
		try {
			if (isResponseSuccess(sendRequest(req))) {
				identity = newIdentity;
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Unsubscribe from channel this channel is following
	public void unsubRequest(String channelName) {
		if (isIdentitySet()) {
			Request req = new UnsubscribeRequest(channelName, identity);
			try {
				sendRequest(req);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Subscribe to channel
	public boolean subRequest(String channelName) {
		if (isIdentitySet()) {
			Request req = new SubscribeRequest(channelName, identity);
			try {
				return isResponseSuccess(sendRequest(req));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// Return channels with names similar to the search string
	public List<String> channelSearchRequest(String search) {
		List<String> toReturn = new ArrayList<String>();

		JSONObject response = null;

		if (isIdentitySet()) {
			Request req = new ChannelSearchRequest(search, identity);
			try {
				response = sendRequest(req);
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSONArray channels = (JSONArray) response.get("channels");

			for (int i = 0; i < channels.size(); ++i) {
				toReturn.add((String) channels.get(i));
			}
		}
		return toReturn;
	}

	// Return messages which contain a hashtag the same as the search string
	public List<Message> messageSearchRequest(String search) {
		List<Message> toReturn = new ArrayList<Message>();

		JSONObject response = null;

		if (isIdentitySet()) {
			Request req = new MessageSearchRequest(search, identity);
			try {
				response = sendRequest(req);
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSONArray messages = (JSONArray) response.get("messages");

			for (int i = 0; i < messages.size(); ++i) {
				JSONObject jsonMsg = (JSONObject) messages.get(i);
				Message msg;
				if (jsonMsg.containsKey("image")) {
					msg = new ImageMessage((String) jsonMsg.get("body"), (int) (long) jsonMsg.get("when"),
							(String) jsonMsg.get("from"), (String) jsonMsg.get("image"));
				} else {
					msg = new Message((String) jsonMsg.get("body"), (int) (long) jsonMsg.get("when"),
							(String) jsonMsg.get("from"));
				}
				toReturn.add(msg);
			}
		}
		return toReturn;
	}

	// Return list of channels this client is subscribed to
	public List<String> getSubRequest() {
		List<String> toReturn = new ArrayList<String>();

		JSONObject response = null;

		if (isIdentitySet()) {
			Request req = new GetSubsRequest(identity);
			try {
				response = sendRequest(req);
			} catch (IOException e) {
				e.printStackTrace();
			}

			JSONArray channels = (JSONArray) response.get("channels");
			for (int i = 0; i < channels.size(); ++i) {
				toReturn.add((String) channels.get(i));
			}
		}
		return toReturn;
	}

	public boolean checkNewMessageRequest() {
		JSONObject response = null;
		if (isIdentitySet()) {
			Request req = new NewMessageBoolRequest(identity, lastGetRequest);
			try {
				return isResponseSuccess(sendRequest(req));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// Publish new post to server
	public boolean publishRequest(String argument) {
		if (isIdentitySet()) {
			Message msg = new Message(argument, 0, identity);
			Request req = new PublishRequest(msg, identity);
			try {
				return isResponseSuccess(sendRequest(req));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean publishRequest(String argument, File img) {
		if (isIdentitySet()) {
			ImageMessage msg = new ImageMessage(argument, 0, identity, img);
			Request req = new PublishRequest(msg, identity);
			try {
				return isResponseSuccess(sendRequest(req));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// Get list of messages for this client (Set afterTime to number to filter
	// timestamp)
	public List<Message> getRequest(int afterTime) {
		List<Message> toReturn = new ArrayList<Message>();
		if (isIdentitySet()) {
			Request req = new GetRequest(afterTime, identity);

			JSONObject response = null;
			try {
				response = sendRequest(req);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JSONArray messages = (JSONArray) response.get("messages");

			for (int i = 0; i < messages.size(); ++i) {
				JSONObject jsonMsg = (JSONObject) messages.get(i);
				Message msg;
				if (jsonMsg.containsKey("image"))
					msg = new ImageMessage((String) jsonMsg.get("body"), (int) (long) jsonMsg.get("when"),
							(String) jsonMsg.get("from"), (String) jsonMsg.get("image"));
				else {
					msg = new Message((String) jsonMsg.get("body"), (int) (long) jsonMsg.get("when"),
							(String) jsonMsg.get("from"));
				}
				toReturn.add(msg);
			}
		}
		if (toReturn.size() > 0) {
			lastGetRequest = toReturn.get(toReturn.size() - 1).getTimeStamp();
		}
		return toReturn;
	}

	// Serialise request and send to server via JSON
	private JSONObject sendRequest(Request req) throws IOException {
		JSONObject obj = req.toJSON();
		String jsonString = obj.toJSONString();
		out.println(jsonString);
		// Get response from client handler
		String fromHandler = in.readLine();
		if (fromHandler == null)
			return null;

		return (JSONObject) JSONValue.parse(fromHandler);
	}

	// Check if request is successful on server
	private boolean isResponseSuccess(JSONObject response) {
		String res = (String) response.get("_class");
		return res.contentEquals("SuccessResponse");
	}
}