package assignment3final;

import org.json.simple.JSONObject;

public class ErrorResponse extends Response {

	private String error;
	
	public ErrorResponse(String error) {
		super(ErrorResponse.class.getSimpleName());
		this.error = error;
	}
	
	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.putAll(super.toJSON());
		obj.put("error", error);
		
		return obj;
	}
	
	
}
