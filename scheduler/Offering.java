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

  public void setInstructorName(String getInstructorName) {
    this.instructorName = instructorName;
  }

  @PlanningVariable(valueRangeProviderRefs = {"roomRange"})
  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
  }

  public Room getRoom() {
    return room;
  }
//--------zippy code---------------
  public getPriority() {
	  int priority;
	  int E = 1;
	  int IT = 0;
	  int IL = 7;
	  int JWS = 3;
	  int MC = 5;
	  int SH = 2;
	  int BS = 6;
	  int MP = 4;
	  int MP = 8;
	    
	  this.room.getBuiling();
	  if("Engineering") {
	   	priority =  E; 
	   }
	  else if("Public Policy") {
		   	priority =  PP; 
	  }
	  else if("Information Technology") {
		  priority =  IT;
	  }
	  else if("Interdisciplinary Life S") {
		  priority =  IL;
	    }
	  else if("Janet & Walter Sondheim") {
		  priority =  JWS;
	  }
	  else if("Meyerhoff Chemistry") {
		  priority =  MC;
	  }
	  else if("Sherman Hall") {
		  priority =  SH;
	  }
	  else if("Biological Sciences") {
		  priority =  BS;
	  }
	  else("Math & Psychology") {
		  priority =  MP;
	  }
	   
	  return priority;
  }
  
  //------------end Zippy code-------------
  public void setRoom(Room room) {
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
  
  public TimeSlot getSuggestedTime() {
	return suggestedTime;
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
			  room.getBuilding() + room.getNumber() + "/n");
	  return newLine;
  }
}
