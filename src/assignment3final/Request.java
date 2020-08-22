package assignment3final;

import org.json.simple.*;
// Class to be transfered across network
public class Request {

	// class name to be used as tag in JSON representation
	private String _class;
	private String identity;

	public Request(String _class, String identity) {
		this._class = _class;
		this.identity = identity;
	}

	// Serializes this object into a JSONObject
	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {

		// create JSON Object
		JSONObject obj = new JSONObject();
		obj.put("identity", identity);
		obj.put("_class", _class);

		return obj;

	}

}