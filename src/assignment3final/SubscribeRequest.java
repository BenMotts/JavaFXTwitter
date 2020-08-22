package assignment3final;
import org.json.simple.JSONObject;

public class SubscribeRequest extends Request {
	
	private String channelName;
	public SubscribeRequest(String channelName, String identity) {
		super(SubscribeRequest.class.getSimpleName(), identity);
		this.channelName = channelName;
	}
	
	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		
		JSONObject obj = new JSONObject();
		obj.put("channel", channelName);
		obj.putAll(super.toJSON());
		return obj;
	}

}
