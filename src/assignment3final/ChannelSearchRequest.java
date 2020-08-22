package assignment3final;

import org.json.simple.JSONObject;

public class ChannelSearchRequest extends Request {
	private String searchText;

	public ChannelSearchRequest(String searchText, String identity) {
		super(ChannelSearchRequest.class.getSimpleName(), identity);
		this.searchText = searchText;
	}

	@SuppressWarnings({ "exports", "unchecked" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("search", searchText);
		obj.putAll(super.toJSON());
		return obj;
	}
}
