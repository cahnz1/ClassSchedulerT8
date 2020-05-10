package test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import Parsing.IncorrectFileFormatException;
import Parsing.MissingInformationException;
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
	public void createOutputFile() throws IOException, IncorrectFileFormatException, MissingInformationException {
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
	public void TestsNoRoomIsAssignedToMultipleClassesAtTheSameTime() {
		
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
	
	public boolean checkOverlapping(String time1, String time2) {
    	DateFormat dateFormat24 = new SimpleDateFormat("HH:mm"); // 24 hour format
    	
    	Date start1 = convertTimeString(time1);
	    Date end1 = Date.from(start1.toInstant().plus(Duration.ofMinutes(89)));
	    
	    String strTime2 = time2;
		Date start2 = convertTimeString(time2);
	    Date end2 = Date.from(start2.toInstant().plus(Duration.ofMinutes(89)));
	    
	    if (!start1.after(end2) && !start2.after(end1)) {
	    	return true;
	    }
	    
	    return false;
    }
    
    public Date convertTimeString(String timeInput) {
    	DateFormat dateFormat24 = new SimpleDateFormat("HH:mm");
    	
    	String strTime1 = timeInput;
		Date timeObj = null;
		
		try {
			timeObj = dateFormat24.parse(strTime1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Date convertToPMBound = null;
		
		try {
			convertToPMBound = dateFormat24.parse("10:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!timeObj.after(convertToPMBound)) {
	    	timeObj = Date.from(timeObj.toInstant().plus(Duration.ofHours(12)));
	    }
		
		return timeObj;
    }
}
