package scheduler;

import java.util.List;

import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;


@PlanningSolution
public class CourseSchedule{

  private List<Course> courseList; //not entirely sure if this list is needed; will remove if not
  private List<TimeSlot> timeSlotList;
  private List<Room> roomList;
  private List<Offering> offeringList;

  // HardSoftScore class used in Course Scheduler Optaplanner example
  private HardSoftScore score;

  public CourseSchedule(List<Course> courseList, List<TimeSlot> timeSlotList,
  List<Room> roomList, List<Offering> offeringList) {
      this.courseList = courseList;
      this.roomList = roomList;
      this.timeSlotList = timeSlotList;
      this.offeringList = offeringList;
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

  public void setOfferingList(List<Room> roomList) {
    this.roomList = roomList;
  }

  @PlanningScore
  public HardSoftScore getScore() {
    return score;
  }

  public void setScore(HardSoftScore score) {
    this.score = score;
  }
}
