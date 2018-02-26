package l2.poc.utils;

import org.bson.codecs.pojo.annotations.BsonId;

public class Data {
	private String value = "";
	private int id = -1;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@BsonId
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
