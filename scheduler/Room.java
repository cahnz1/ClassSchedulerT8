package scheduler;

public class Room {

  private String building;
  private int number;
  private int capacity;

  public Room(String building, int number, int capacity) {
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

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
}
