package application.model.game.entity;

public class Message {

	private String protocol = null ;
	private Object message = null ;
	
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Message(String protocol, String message) {
		super();
		this.protocol = protocol;
		this.message = message;
	}
	
	public Message(String protocol, Object message) {
		super();
		this.protocol = protocol;
		this.message = message;
	}
	
	public Message(String protocol) {
		this.protocol = protocol ;
	}

	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	
	
	@Override
	public String toString() {
		if(this.getMessage() != null)
			return "Protocol: [" + this.getProtocol()+"] by: "+this.getMessage();
		else 
			return "Protocol: [" + this.getProtocol() +"]" ;
	}
	
}
