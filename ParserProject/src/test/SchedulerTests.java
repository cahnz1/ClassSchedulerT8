package test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import Parsing.Parser;
import scheduler.Course;
import scheduler.CourseSchedulerApp;
import scheduler.Offering;
import scheduler.Room;
import scheduler.TimeSlot;


public class SchedulerTests {
	
	static Parser csvParser = new Parser();
	static List<Map<String, String>> parsedCourseFile;
	static List<Room> rooms;
	static List<Course> courses;
	static List<TimeSlot> times;
	static List<Offering> offerings;
	private File output;
	
	@BeforeAll 
	public void createOutputFile() throws IOException{
		// send this file to the parser	
		String courseFileName = "Spring 2020 Schedule.csv";
		String roomsFileName = "ClassRoom.csv";
		 	
		// Parse file
		csvParser = new Parser();
		parsedCourseFile = csvParser.ParseCSV(courseFileName);
		rooms = csvParser.GetRoomObjects(roomsFileName);
		courses =  csvParser.GetCourseObjects(parsedCourseFile);
		times = csvParser.GetTimeSlotObjects(parsedCourseFile);
		offerings = csvParser.GetOfferingObjects(parsedCourseFile, courses, times);
		
		// Run solver and output solution
		CourseSchedulerApp scheduler = new CourseSchedulerApp(rooms, courses, times, offerings);
		scheduler.generateSolution();
		scheduler.Output("schedule.csv");
		
		output = new File("schedule.csv");
	}
	
	@Test
	public void TestsOfferingCapacityIsNotGreaterThanRoomCapacity() {
		
	}
	
	@Test
	public void TestsNoTeacherIsAssignedToMultipleClassesAtTheSameTime() {
		
	}
	
	@Test
	public void TestsNoRoomIsAssignedToMultipleClassseAtTheSameTime() {
		
	}
}
