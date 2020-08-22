package assignment3final;

import org.json.simple.JSONObject;

public class GetRequest extends Request {
	private int afterTime;
	
	public GetRequest(int when, String identity) {
		super(GetRequest.class.getSimpleName(), identity);
		afterTime = when;
	}
	
	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		
		JSONObject obj = new JSONObject();
		obj.put("after", afterTime);
		obj.putAll(super.toJSON());
		return obj;
	}
}
