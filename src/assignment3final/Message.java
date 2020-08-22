package assignment3final;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

public class Message implements Comparable<Message> {

	// Private class variables
	private String messageBody;
	private String fromChannel;
	private Integer timeStamp;
	private String _class = Message.class.getSimpleName();

	// Class constructor
	public Message(String body, Integer timeStamp, String fromChannel) {
		this.messageBody = body;
		this.timeStamp = timeStamp;
		this.fromChannel = fromChannel;
	}

	// Class constructor from json
	public Message(@SuppressWarnings("exports") JSONObject msgJSON) {
		this.messageBody = (String) msgJSON.get("body");
		this.timeStamp = (int) (long) msgJSON.get("when");
		this.fromChannel = (String) msgJSON.get("from");
	}

	// Class constructor from json and time
	public Message(@SuppressWarnings("exports") JSONObject msgJSON, int timeStamp) {
		this.messageBody = (String) msgJSON.get("body");
		this.fromChannel = (String) msgJSON.get("from");
		this.timeStamp = timeStamp;
	}

	// Class getters
	public String getMessageBody() {
		return messageBody;
	}

	public Integer getTimeStamp() {
		return timeStamp;
	}

	public String getChannelFrom() {
		return fromChannel;
	}

	// If message contains a certain hashtag (For message serch functions)
	public boolean hasHashtag(String hash) {
		return messageBody.contains(" #" + hash);
	}

	// Turn to json object
	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("body", messageBody);
		obj.put("from", fromChannel);
		obj.put("_class", _class);
		obj.put("when", timeStamp);
		return obj;

	}

	@Override
	public int compareTo(Message o) {
		return this.getTimeStamp().compareTo(o.getTimeStamp());
		// TODO Auto-generated method stub
	}

}
