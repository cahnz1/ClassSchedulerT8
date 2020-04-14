package scheduler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import Parsing.Parser;

public class Tester {
	
	static Parser csvParser = new Parser();
	static List<Map<String, String>> parsedCourseFile;
	static List<Room> rooms;
	static List<Course> courses;
	static List<TimeSlot> times;
	static List<Offering> offerings;
	
	public static void main(String[] args) throws IOException {
		// send this file to the parser	
		String courseFileName = "Spring 2020 Schedule.csv";
		String roomsFileName = "ClassRoom.csv";
 	
		// Print what was parsed
		parseFile(courseFileName, roomsFileName);
		
		CourseSchedulerApp scheduler = new CourseSchedulerApp(rooms, courses, times, offerings);
		scheduler.generateSolution();
		scheduler.Output("schedule.csv");
		
	}
	
	public static void parseFile(String courseFileName, String roomsFileName) {
		csvParser = new Parser();
		parsedCourseFile = csvParser.ParseCSV(courseFileName);
		rooms = csvParser.GetRoomObjects(roomsFileName);
		courses =  csvParser.GetCourseObjects(parsedCourseFile);
		times = csvParser.GetTimeSlotObjects(parsedCourseFile);
		offerings = csvParser.GetOfferingObjects(parsedCourseFile, courses, times);
	}
}
