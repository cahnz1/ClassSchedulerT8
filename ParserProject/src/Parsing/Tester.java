package Parsing;

import java.util.List;
import java.util.Map;

public class Tester {

	public static void main(String[] args) {
		
		String roomsFileName = "ClassRoom.csv";
		String courseFileName = "Spring 2020 Schedule.csv";
		
		Parser csvParser = new Parser();
		List<Map<String, String>> parsedCourseFile = csvParser.ParseCSV(courseFileName);
		
		List<Room> rooms = csvParser.GetRoomObjects(roomsFileName);
		
		List<Course> courses =  csvParser.GetCourseObjects(parsedCourseFile);
		
		List<TimeSlot> times = csvParser.GetTimeSlotObjects(parsedCourseFile);
		
		List<Offering> offerings = csvParser.GetOfferingObjects(parsedCourseFile, courses, times);
		
		//csvParser.PrintObjects(parsedCourseFile);
		//printRooms(rooms);
		//printCourses(courses);
		//printTimes(times);
		//printOfferings(offerings);
		
	}
	
	public static void printRooms(List<Room> rooms) {
		int i = 1;
		System.out.println();
		for(Room room : rooms) {
			System.out.println(i + ":\tBuilding: " + room.getBuilding() + "\t\t\tRoom(s): " + room.getNumber() + "\t\t\tCapacity: " + room.getCapacity());
			i++;
		}
	}
	
	public static void printCourses(List<Course> courses) {
		int i = 1;
		System.out.println();
		for(Course course : courses) {
			System.out.println(i + ":\tTitle: " + course.getCourseTitle() + "\t\t\tCode: " + course.getCourseCode());
			i++;
		}
	}
	
	public static void printTimes(List<TimeSlot> times) {
		int i = 1;
		System.out.println();
		for(TimeSlot time : times) {
			System.out.println(i + ":\tDays: " + time.getDays() + "\t\t\tTime: " + time.getTime());
			i++;
		}
	}
	public static void printOfferings(List<Offering> offerings) {
		int i = 1;
		System.out.println();
		for(Offering offering : offerings) {
			System.out.println(i + ":");
			System.out.println("\tClass: " + offering.getCourse().getCourseTitle() + ", " + offering.getCourse().getCourseCode() + ", sec. " + offering.getSectionNumber());
			System.out.println("\tOn: " + offering.getTimeSlot().getDays() + " At: " + offering.getTimeSlot().getTime());
			System.out.println("\tInstructor: " + offering.getInstructorName());
			System.out.println("\tRoom: null");
			i++;
		}
	}

}
