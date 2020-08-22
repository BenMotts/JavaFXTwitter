package assignment3final;

import org.json.simple.JSONObject;

public class MessageSearchRequest extends Request {
	private String searchText;

	public MessageSearchRequest(String searchText, String identity) {
		super(MessageSearchRequest.class.getSimpleName(), identity);
		this.searchText = searchText;
	}

	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("search", searchText);
		obj.putAll(super.toJSON());
		return obj;
	}
}
