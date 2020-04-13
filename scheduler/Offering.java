package scheduler;

import java.util.List;
import java.util.ArrayList;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Offering {

  private int sectionNumber;
  private String instructorName;
  private Course course;
  private Room room;
  private TimeSlot suggestedTime;
  private TimeSlot timeSlot;
  
  private int capacity;

  public Offering(int sectionNumber, String instructorName, Room room,
    Course course, TimeSlot timeSlot, int capacity) {
      this.sectionNumber = sectionNumber;
      this.instructorName = instructorName;
      this.room = room;
      this.course = course;
      this.timeSlot = timeSlot;
      this.capacity = capacity;
      this.suggestedTime = timeSlot;
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

  public void setInstructorName(int getInstructorName) {
    this.instructorName = instructorName;
  }

  @PlanningVariable(valueRangeProviderRefs = {"roomRange"})
  public Course getCourse() {
    return course;
  }

  public void setCourse(String Course) {
    this.course = course;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(String Room) {
    this.room = room;
  }

//made nullable equal to true in order to make it so that a course will
 // not be scheduled as opposed to being scheduled in a different time slot
 @PlanningVariable(valueRangeProviderRefs = {"timeSlotRange"}, nullable=true)
  public TimeSlot getTimeSlot() {
    return timeSlot;
  }
  
  // restricts possible timeslot values to its suggested timeslot and null
  // might want to implement this as a soft constraint instead, and just
  // manually not schedule a class if timeSlot != suggestedTime
  // (in that case, we shouldn't make timeslot nullable)
  @ValueRangeProvider(id = "timeSlotRange")
  public List<TimeSlot> getPossibleTimeSlots() {
	List<TimeSlot> possibleTimes = new ArrayList<TimeSlot>();
	possibleTimes.add(suggestedTime);
	possibleTimes.add(null);
	return possibleTimes;
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
  
  public String createLine() {
	  String newLine = "";
	  newLine.concat(course.getCourseTitle() + "," + 
			  course.getCourseTitle() + "," +
			  Integer.toString(sectionNumber) + "," +
			  instructorName + "," + 
			  timeSlot.getDays() + "," +
			  timeSlot.getTime() + "," +
			  room.getBuilding() + Integer.toString(room.getNumber()) + "/n");
	  return newLine;
  }
}
