package test;



import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.github.javaparser.utils.Pair;

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
	
	@Before 
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
		List<Offering> inputOfferings = csvParser.GetOfferingObjects(parsedCourseFile, inputCourses, inputTimes);
		
		// Run solver and output solution
		CourseSchedulerApp scheduler = new CourseSchedulerApp(inputRooms, inputCourses, inputTimes, inputOfferings);
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
		
		HashMap<String, ArrayList<Pair<String,String>>> instructorTimes = new HashMap<>();
        for(Offering offering : offerings) {
        	if (offering.getTimeSlot() != null) {
        		
        		String instructorName = offering.getInstructorName();
    			String strDay1 = offering.getTimeSlot().getDays();
    			String strTime1 = offering.getTimeSlot().getTime();
    			Pair<String,String> timeValue = new Pair<String,String>(strDay1, strTime1);
    			
    			if (instructorTimes.get(instructorName) != null) {
    				
    				boolean foundInList = false;
    				for (int i=0; i < instructorTimes.get(instructorName).size(); i++) {
    					
    					if (instructorName.compareTo("Staff") != 0) { //if instructor is not Staff
    						String strDay2 = instructorTimes.get(instructorName).get(i).a;
    						
    						if (strDay1.contains(strDay2) || strDay2.contains(strDay1)) {
    							String strTime2 = instructorTimes.get(instructorName).get(i).b;
    							    							
    							if (checkOverlapping(strTime1, strTime2) == true) {
    								noTeacherAssignedConcurrentClasses = false;
    							}
    							
    							if (strDay2.compareTo(strDay1) == 0 && strTime2.compareTo(strTime1) == 0) {
									foundInList = true;
								}
    							
    						}
    					}
    				}
    				
    				if (foundInList == false) {
						instructorTimes.get(offering.getInstructorName()).add(timeValue);
					}
    			} else {
    				ArrayList<Pair<String,String>> newList = new ArrayList<Pair<String,String>>();
    				newList.add(timeValue);
    				instructorTimes.put(instructorName,newList);
    			}
        	}
        }
        
        assertTrue(noTeacherAssignedConcurrentClasses);
	}
	
	@Test
	public void TestsNoRoomIsAssignedToMultipleClassesAtTheSameTime() {
		
		boolean noRoomAssignedToConcurrentClasses = true;
		
		HashMap<String, ArrayList<Pair<String,String>>> roomTimes = new HashMap<>();
		for(Offering offering : offerings) {
        	if (offering.getRoom() != null && offering.getTimeSlot() != null) {
        		String roomString = offering.getRoom().getBuilding() + 
        				offering.getRoom().getNumber();
    			String strDay1 = offering.getTimeSlot().getDays();
    			String strTime1 = offering.getTimeSlot().getTime();
    			Pair<String,String> timeValue = new Pair<String,String>(strDay1, strTime1);
    			
    			if (roomTimes.get(roomString) != null) {
    				
    				boolean foundInList = false;
    				for (int i=0; i < roomTimes.get(roomString).size(); i++) {
    					
    					String strDay2 = roomTimes.get(roomString).get(i).a;
    						
    					if (strDay1.contains(strDay2) || strDay2.contains(strDay1)) {
    						String strTime2 = roomTimes.get(roomString).get(i).b;
    							    							
    						if (checkOverlapping(strTime1, strTime2) == true) {
    							noRoomAssignedToConcurrentClasses = false;
    						}
    						if (strDay2.compareTo(strDay1) == 0 && strTime2.compareTo(strTime1) == 0) {
								foundInList = true;
							}
    					}
    				}
    				
    				if (foundInList == false) {
						roomTimes.get(roomString).add(timeValue);
					}
    			} else {
    				ArrayList<Pair<String,String>> newList = new ArrayList<Pair<String,String>>();
    				newList.add(timeValue);
    				roomTimes.put(roomString,newList);
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
