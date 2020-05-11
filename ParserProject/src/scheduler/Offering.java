package scheduler;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
  
  public void ErrorMsg() {
      String message = "Error: Invalid building name in room info file. " + room.getBuilding() + " is invalid or doesn't host computer science classes";
      JOptionPane.showMessageDialog(new JFrame(), message, "Dialog",
          JOptionPane.ERROR_MESSAGE);
    }
  
  public int getPriority() {
	  int E = 2;
	  int IT = 0;
	  int IL = 10;
	  int JWS = 4;
	  int MC = 7;
	  int SH = 1;
	  int BS = 9;
	  int MP = 6;
	  int PP = 12;
	  int FA = 5;
	  int PAH = 8;
	  int P = 11;
	  int UC = 3;
	  
	  int priority = 13; //initialized here so compiler doesn't yell at me
	  
	    
	  if(room.getBuilding().compareTo("Engineering") == 0) {
	   	priority =  E; 
	   }
	  else if(room.getBuilding().compareTo("Public Policy") == 0) {
		   	priority =  PP; 
	  }
	  else if(room.getBuilding().compareTo("Information Technology") == 0) {
		  priority =  IT;
	  }
	  else if(room.getBuilding().compareTo("Interdisciplinary Life S") == 0) {
		  priority =  IL;
	    }
	  else if(room.getBuilding().compareTo("Janet & Walter Sondheim") == 0) {
		  priority =  JWS;
	  }
	  else if(room.getBuilding().compareTo("Meyerhoff Chemistry") == 0) {
		  priority =  MC;
	  }
	  else if(room.getBuilding().compareTo("Sherman Hall") == 0) {
		  priority =  SH;
	  }
	  else if(room.getBuilding().compareTo("Biological Sciences") == 0) {
		  priority =  BS;
	  }
	  else if (room.getBuilding().compareTo("Math & Psychology") == 0) {
		  priority =  MP;
	  }
	  else if (room.getBuilding().compareTo("Fine Arts") == 0) {
		  priority =  FA;
	  }
	  else if (room.getBuilding().compareTo("Performing Arts & Humanities") == 0) {
		  priority =  PAH;
	  }
	  else if (room.getBuilding().compareTo("Physics") == 0) {
		  priority =  P;
	  }
	  else if (room.getBuilding().compareTo("University Center") == 0) {
		  priority =  UC;
	  }
	   
	  else {
		  //there is no building
		  System.out.print("Error: Invalid building name in room info file: " + room.getBuilding() + " is invalid or doesn't host computer science classes");
		  ErrorMsg();
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
