package Parsing;

public class Room {

	  private String building;
	  private String number;
	  private int capacity;

	  public Room(String building, String number, int capacity) {
	    this.building = building;
	    this.number = number;
	    this.capacity = capacity;
	  }

	  public String getBuilding() {
	    return building;
	  }

	  public void setBuilding(String building) {
	    this.building = building;
	  }

	  public String getNumber() {
	    return number;
	  }

	  public void setNumber(String number) {
	    this.number = number;
	  }

	  public int getCapacity() {
	    return capacity;
	  }

	  public void setCapacity(int capacity) {
	    this.capacity = capacity;
	  }
	}
