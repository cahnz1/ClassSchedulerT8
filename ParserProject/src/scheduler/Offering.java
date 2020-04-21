package scheduler;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import scheduler.solver.OfferingDifficultyComparator;

@PlanningEntity (difficultyComparatorClass = OfferingDifficultyComparator.class)
public class Offering {

  private int sectionNumber;
  private String instructorName;
  private Course course;
  private Room room;
  private TimeSlot suggestedTime;
  private TimeSlot timeSlot;
  
  private int capacity;

  public Offering() {
	  this.capacity = 0;
  }
  
  public Offering(int sectionNumber, String instructorName, Room room,
    Course course, TimeSlot timeSlot, int capacity) {
      this.sectionNumber = sectionNumber;
      this.instructorName = instructorName;
      this.room = room;
      this.course = course;
      this.capacity = capacity;
      this.suggestedTime = timeSlot;
      this.timeSlot = null;
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

  @PlanningVariable(valueRangeProviderRefs = {"roomRange"}, nullable=false)
  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
	  this.room = room;
  }

  
  @PlanningVariable(valueRangeProviderRefs = {"timeSlotRange"}, nullable=false)
  public TimeSlot getTimeSlot() {
    return timeSlot;
  }
  
  // restricts possible timeslot values to its suggested timeslot and null
  // might want to implement this as a soft constraint instead, and just
  // manually not schedule a class if timeSlot != suggestedTime
  // (in that case, we shouldn't make timeslot nullable)
  // not currently using since we have to suggest alternate times anyway
  // if we want to use, uncomment below line 
  //@ValueRangeProvider(id = "timeSlotRange")
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
  
//--------zippy code---------------
  public int getPriority() {
	  int E = 1;
	  int IT = 0;
	  int IL = 7;
	  int JWS = 3;
	  int MC = 5;
	  int SH = 2;
	  int BS = 6;
	  int MP = 4;
	  int PP = 8;
	  
	  int priority = 9; //initialized here so compiler doesn't yell at me
	  
	    
	  if(room.getBuilding() == "Engineering") {
	   	priority =  E; 
	   }
	  else if(room.getBuilding() == "Public Policy") {
		   	priority =  PP; 
	  }
	  else if(room.getBuilding() == "Information Technology") {
		  priority =  IT;
	  }
	  else if(room.getBuilding() == "Interdisciplinary Life S") {
		  priority =  IL;
	    }
	  else if(room.getBuilding() == "Janet & Walter Sondheim") {
		  priority =  JWS;
	  }
	  else if(room.getBuilding() == "Meyerhoff Chemistry") {
		  priority =  MC;
	  }
	  else if(room.getBuilding() == "Sherman Hall") {
		  priority =  SH;
	  }
	  else if(room.getBuilding() == "Biological Sciences") {
		  priority =  BS;
	  }
	  else if (room.getBuilding() == "Math & Psychology") {
		  priority =  MP;
	  }
	  
	   
	  return priority;
  }
  
  //------------end Zippy code-------------
  
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
