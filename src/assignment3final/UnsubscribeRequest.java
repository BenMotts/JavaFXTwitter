package assignment3final;
import org.json.simple.JSONObject;

public class UnsubscribeRequest extends Request {
	
	private String channelName;
	
	public UnsubscribeRequest(String channelName, String identity) {
		super(UnsubscribeRequest.class.getSimpleName(), identity);
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
