package Parsing;

//note: added setters for this class b/c its variables may
//change throughout but if unneeded we can delete them

public class TimeSlot {

	private String time;
	private String days;
	
	public TimeSlot(String time, String days) {
	  this.time = time;
	  this.days = days;
	}
	
	public String getTime() {
	  return time;
	}
	
	public void setTime(String time) {
	  this.time = time;
	}
	
	public String getDays() {
	  return days;
	}
	
	public void setDays(String days) {
	  this.days = days;
	}
}