package test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	static List<Map<String, String>> parsedSchedule;
	private List<Room> rooms;
	private List<Course> courses;
	private List<TimeSlot> times;
	private List<Offering> offerings;
	
	@BeforeAll 
	public void createOutputFile() throws IOException{
		// send this file to the parser	
		String courseFileName = "Spring 2020 Schedule.csv";
		String roomsFileName = "ClassRoom.csv";
		 	
		// Parse file
		csvParser = new Parser();
		List<Map<String, String>> parsedCourseFile = csvParser.ParseCSV(courseFileName);
		List<Room> inputRooms = csvParser.GetRoomObjects(roomsFileName);
		List<Course> inputCourses =  csvParser.GetCourseObjects(parsedCourseFile);
		List<TimeSlot> inputTimes = csvParser.GetTimeSlotObjects(parsedCourseFile);
		List<Offering> inputOfferings = csvParser.GetOfferingObjects(parsedCourseFile, courses, times);
		
		// Run solver and output solution
		CourseSchedulerApp scheduler = new CourseSchedulerApp(rooms, courses, times, offerings);
		scheduler.generateSolution();
		this.rooms = scheduler.getSchedule().getRoomList();
		this.courses = scheduler.getSchedule().getCourseList();
		this.times = scheduler.getSchedule().getTimeSlotList();
		this.offerings = scheduler.getSchedule().getOfferingList();
	}
	
	@Test
	public void TestOfferingsListNotEmpty() {
		assertNotEquals(offerings.size(), 0);
	}
	
	@Test
	public void TestsOfferingCapacityIsNotGreaterThanRoomCapacity() {
		
		boolean noRoomOverCapacity = true;
		
        for(Offering offering : offerings) {
        	if (offering.getCapacity() != 0 && offering.getRoom() != null && offering.getRoom().getCapacity() != 0) {
        		if (offering.getCapacity() > offering.getRoom().getCapacity()) {
        			noRoomOverCapacity = false; //arbitrary for now but should have higher weight than room one
        		}
        	}
        }
        
        assertTrue(noRoomOverCapacity);
	}
	
	@Test
	public void TestsNoTeacherIsAssignedToMultipleClassesAtTheSameTime() {
		
		boolean noTeacherAssignedConcurrentClasses = true;
		
		Set<String> instructorTimes = new HashSet<>();
        for(Offering offering : offerings) {
        	if (offering.getTimeSlot() != null) {
        		String instructorTimeSlot = offering.getInstructorName() +
    				offering.getTimeSlot().getDays() +
    				offering.getTimeSlot().getTime();
        	
        		if (offering.getInstructorName().compareTo("Staff") != 0) { //if instructor is not Staff
        			if (instructorTimes.contains(instructorTimeSlot)) {
        				noTeacherAssignedConcurrentClasses = false;
        			}
        			else {
        				instructorTimes.add(instructorTimeSlot);
        			}
        		}
        	}
        }
        
        assertTrue(noTeacherAssignedConcurrentClasses);
	}
	
	@Test
	public void TestsNoRoomIsAssignedToMultipleClassseAtTheSameTime() {
		
		boolean noRoomAssignedToConcurrentClasses = true;
		
		Set<String> occupiedRooms = new HashSet<>();
        for(Offering offering : offerings) {
        	if (offering.getRoom() != null && offering.getTimeSlot() != null) {
        		//System.out.print("enters this statement");
        		String roomInUse = offering.getRoom().getBuilding() +
        				offering.getRoom().getNumber() +
        				offering.getTimeSlot().getDays() +
        				offering.getTimeSlot().getTime();
        		if(occupiedRooms.contains(roomInUse)){
        			noRoomAssignedToConcurrentClasses = false;
        		} else {
        			occupiedRooms.add(roomInUse);
        		}
        	}
        }
        
        assertTrue(noRoomAssignedToConcurrentClasses);
	}
}
