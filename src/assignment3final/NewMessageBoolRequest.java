package assignment3final;

import org.json.simple.JSONObject;

public class NewMessageBoolRequest extends Request {
	
	private int after;
	
	public NewMessageBoolRequest(String identity, int after) {
		super(NewMessageBoolRequest.class.getSimpleName(), identity);
		this.after = after;
	}
	
	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.putAll(super.toJSON());
		obj.put("after", after);
		return obj;	
	}
}
