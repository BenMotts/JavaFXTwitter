package assignment3final;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class MessageListResponse extends Response {

	private List<Message> messages;
	
	public MessageListResponse(List<Message> messages) {
		super(MessageListResponse.class.getSimpleName());
		this.messages = messages;
	}
	
	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		JSONArray messageArray = new JSONArray();
		
		obj.putAll(super.toJSON());
		for (Message msg : messages) {
			messageArray.add(msg.toJSON());
		}
		obj.put("messages", messageArray);
		return obj;	
	}
}
