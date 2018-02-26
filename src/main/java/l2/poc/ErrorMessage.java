package l2.poc;

public class ErrorMessage {
	private String errorId;
	private String message;

	public ErrorMessage() {
	}

	public ErrorMessage(String errorId, String message) {
		this.setErrorId(errorId);
		this.setMessage(message);
	}

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
