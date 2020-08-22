package assignment3final;

import org.json.simple.JSONObject;

public class Response {

	private String _class;
	
	public Response(String _class) {
		this._class = _class;
	}
	
	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("_class", _class);
		return obj;
	}
}
