package assignment3final;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SubsListResponse extends Response {

	private List<String> channelNames;
	
	public SubsListResponse(List<String> channelNames) {
		super(SubsListResponse.class.getSimpleName());
		this.channelNames = channelNames;
	}
	
	@SuppressWarnings({"unchecked", "exports"})
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		JSONArray channelList = new JSONArray();
		
		obj.putAll(super.toJSON());
		for (String s : channelNames) {
			channelList.add(s);
		}
		obj.put("channels", channelList);
		return obj;
	}
}
