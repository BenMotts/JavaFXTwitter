package assignment3final;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import org.json.simple.JSONObject;
import javafx.scene.image.Image;

public class ImageMessage extends Message {

	private String imgStr;

	public ImageMessage(String body, Integer timeStamp, String fromChannel, File img) {
		super(body, timeStamp, fromChannel);
		try {
			imgStr = convertToString(img);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ImageMessage(String body, Integer timeStamp, String fromChannel, String imgBytes) {
		super(body, timeStamp, fromChannel);
		this.imgStr = imgBytes;
	}

	public ImageMessage(JSONObject msgJSON) {
		super((String) msgJSON.get("body"), (int) (long) msgJSON.get("when"), (String) msgJSON.get("from"));
		this.imgStr = (String) msgJSON.get("image");
	}

	@SuppressWarnings({ "unchecked", "exports" })
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("image", imgStr);
		obj.putAll(super.toJSON());
		return obj;
	}

	private String convertToString(File img) throws IOException {
		FileInputStream imgStream = new FileInputStream(img.toString());
		byte data[] = imgStream.readAllBytes();
		return Base64.getEncoder().encodeToString(data);
	}

	@SuppressWarnings("exports")
	public Image getImageFromString() throws IOException {
		byte data[] = Base64.getDecoder().decode(imgStr);
		return new Image(new ByteArrayInputStream(data));
	}
}
