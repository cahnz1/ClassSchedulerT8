package scheduler;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;


@PlanningSolution
public class CourseSchedule { 

  private List<Course> courseList; //not entirely sure if this list is needed; will remove if not
  private List<TimeSlot> timeSlotList;
  private List<Room> roomList;
  private List<Offering> offeringList;

  // HardSoftScore class used in Course Scheduler Optaplanner example
  private HardSoftScore score;

  public CourseSchedule() {
	  this.courseList = new ArrayList<Course>();
      this.roomList = new ArrayList<Room>();
      this.timeSlotList = new ArrayList<TimeSlot>();
      this.offeringList = new ArrayList<Offering>();
  }
  
  public CourseSchedule(List<Course> courseList, List<TimeSlot> timeSlotList,
  List<Room> roomList, List<Offering> offeringList) {
      this.courseList = new ArrayList<Course>(courseList);
      this.roomList = new ArrayList<Room>(roomList);
      this.timeSlotList = new ArrayList<TimeSlot>(timeSlotList);
      this.offeringList = new ArrayList<Offering>(offeringList);
    }

  @ProblemFactCollectionProperty
  public List<Course> getCourseList() {
    return courseList;
  }

  public void setCourseList(List<Course> courseList) {
    this.courseList = courseList;
  }


  @ValueRangeProvider(id = "roomRange")
  @ProblemFactCollectionProperty
  public List<Room> getRoomList() {
    return roomList;
  }

  public void setRoomList(List<Room> roomList) {
    this.roomList = roomList;
  }

  @ValueRangeProvider(id = "timeSlotRange")
  @ProblemFactCollectionProperty
  public List<TimeSlot> getTimeSlotList() {
    return timeSlotList;
  }

  public void setTimeSlotList(List<TimeSlot> timeSlotList) {
    this.timeSlotList = timeSlotList;
  }

  @PlanningEntityCollectionProperty
  public List<Offering> getOfferingList() {
    return offeringList;
  }

  public void setOfferingList(List<Offering> offeringList) {
    this.offeringList = offeringList;
  }

  @PlanningScore
  public HardSoftScore getScore() {
    return score;
  }

  public void setScore(HardSoftScore score) {
    this.score = score;
  }
  
  // testing function; prints all the offerings and their assigned times,
  // as well as the available rooms and their capacities
  public void printElements() {
	System.out.print("Offerings: \n");
	for (Offering offering : offeringList) {
		System.out.print(offering.getCourse().getCourseCode() + ": "
				+ offering.getTimeSlot().getDays() + offering.getTimeSlot().getTime() 
				+ "- " + offering.getCapacity() + "\n");
		
	}
	System.out.print("Rooms: \n");
	for (Room room: roomList) {
		System.out.print(room.getBuilding() + room.getNumber() + ": " + room.getCapacity() + "\n");
	}
	System.out.print("Times: \n");
	for (TimeSlot timeSlot: timeSlotList) {
		System.out.print(timeSlot.getDays() + timeSlot.getTime() + "\n");
	}
  }
}
