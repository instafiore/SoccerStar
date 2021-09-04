package application.model.game.entity;

public class DataMatch {
	
	private String date = null ;
	private String result = null ;
	private String field = null ;
	private String home = null ;
	private String guest = null ;
	
	public DataMatch(String date, String result, String field, String home, String guest) {
		super();
		this.date = date;
		this.result = result;
		this.field = field;
		this.home = home;
		this.guest = guest;
	}
	
	public DataMatch() {}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	
}
