package assignment3final;
import org.json.simple.JSONObject;

public class PublishRequest extends Request {

	private Message msg;

	public PublishRequest(Message msg, String identity) {
		super(PublishRequest.class.getSimpleName(), identity);
		this.msg = msg;
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.putAll(super.toJSON());
		obj.put("message", msg.toJSON());
		return obj;
	}

}
