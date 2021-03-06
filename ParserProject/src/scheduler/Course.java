package scheduler;

import java.io.Serializable;

//used "implements Serializable" because it was recommended
//by OptaPlanner for problem facts (things that don't change
//during problem solving)
public class Course implements Serializable {

  private String courseCode;
  private String courseTitle;

  public Course() {}
  
  public Course(String courseCode, String courseTitle) {
    this.courseCode = courseCode;
    this.courseTitle = courseTitle;
  }

  public String getCourseCode() {
    return courseCode;
  }

  public String getCourseTitle() {
    return courseTitle;
  }
}
