package Parsing;

public class Offering {

	  private int sectionNumber;
	  private String instructorName;
	  private Course course;
	  private Room room;
	  private TimeSlot timeSlot;
	  private int capacity;

	  public Offering(int sectionNumber, String instructorName, Room room,
	    Course course, TimeSlot timeSlot) {
	      this.sectionNumber = sectionNumber;
	      this.instructorName = instructorName;
	      this.room = room;
	      this.course = course;
	      this.timeSlot = timeSlot;
	      this.capacity = capacity;
	    }

	  public int getSectionNumber() {
	    return sectionNumber;
	  }

	  public void setSectionNumber(int sectionNumber) {
	    this.sectionNumber = sectionNumber;
	  }

	  public String getInstructorName() {
	    return instructorName;
	  }

	  public void setInstructorName(String instructorName) {
	    this.instructorName = instructorName;
	  }

	  public Course getCourse() {
	    return course;
	  }

	  public void setCourse(Course course) {
	    this.course = course;
	  }

	  public Room getRoom() {
	    return room;
	  }

	  public void setRoom(Room room) {
	    this.room = room;
	  }

	  public TimeSlot getTimeSlot() {
	    return timeSlot;
	  }

	  public void setTimeSlot(TimeSlot timeSlot) {
	    this.timeSlot = timeSlot;
	  }
	  
	  public int getCapacity() {
		return capacity;
	  }
	  
	  public void setCapacity(int capacity) {
		this.capacity = capacity;
	  }
	}
