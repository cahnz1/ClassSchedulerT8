
@PlanningSolution
public class CourseSchedule{

  private List<Course> courseList;
  private List<TimeSlot> timeslotList;
  private List<Room> roomList;
  private List<Offering> offeringList;

  // HardSoftScore class used in Course Scheduler Optaplanner example
  private HardSoftScore score;

  public Offering(List<Course> courseList, List<TimeSlot> timeSlotList,
  List<Room> roomList, List<Offering> offeringList) {
      this.courseList = courseList;
      this.roomList = roomList;
      this.timeSlotList = timeSlotList;
    }

  public List<Course> getCourseList() {
    return courseList;
  }

  public void setCourseList(List<Course> courseList) {
    this.courseList = courseList;
  }

  public List<Room> getRoomList() {
    return roomList;
  }

  public void setRoomList(List<Room> roomList) {
    this.roomList = roomList;
  }

  public List<TimeSlot> getTimeSlotList() {
    return timeSlotList;
  }

  public void setTimeSlotList(List<TimeSlot> timeSlotList) {
    this.timeSlotList = timeSlotList;
  }

  public List<Offering> getOfferingList() {
    return offeringList;
  }

  public void setOfferingList(List<Room> roomList) {
    this.roomList = roomList;
  }

  public HardSoftScore getScore() {
    return score;
  }

  public void setScore(HardSoftScore score) {
    this.score = score;
  }

  public Output(String outputFileName) {
    
  }
}
