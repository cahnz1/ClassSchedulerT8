package Parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {
	
		private final String CSV_EXTENSION = ".csv";
		private boolean debug = false;

		public Parser(boolean debugMode){
			debug = debugMode;
		}
		public Parser() {
			debug = false;
		}
		
		//Specific To Project
		
		//Rooms_________________________________________________________________________
		public List<Room> GetRoomObjects(List<Map<String, String>> inputRooms) {
			List<Room> rooms = new ArrayList<Room>();
			
			for (Map<String,String> inputRoom : inputRooms) {
				int capacity = Integer.parseInt(inputRoom.get("capacity"));
				
				String classRoom = inputRoom.get("class room");
				classRoom = classRoom.replace(",", "");
				String[] pieces = classRoom.split(" ");
				String building = "";
				String roomNumber = "";
				for(String piece : pieces) {
					if(piece.matches(".*\\d.*")) {
						roomNumber = roomNumber + piece + ", ";
					}else {
						if(!building.contains(piece))
							building = building + piece + " ";
					}
				}
				roomNumber = roomNumber.substring(0,roomNumber.length()-2);
				building = building.substring(0,building.length()-1);
				
				boolean duplicate = false;
				for (Room r : rooms) {
					if(r.getBuilding().equals(building) && r.getNumber().equals(roomNumber) && r.getCapacity() == capacity) {
						duplicate = true;
					}
				}
				
				if(!duplicate) {
					Room newRoom = new Room(building, roomNumber, capacity);
					rooms.add(newRoom);
				}
				
			}
			
			return rooms;
		}
		
		public List<Room> GetRoomObjects(String fileName){
			return GetRoomObjects(ParseCSV(fileName));
		}
		
		//Courses_________________________________________________________________________
		public List<Course> GetCourseObjects(List<Map<String, String>> inputCourses) {
			List<Course> courses = new ArrayList<Course>();
			
			for (Map<String,String> inputCourse : inputCourses) {
				
				String title = inputCourse.get("course title");
				String code = inputCourse.get("subject") +" " + inputCourse.get("course #");

				boolean duplicate = false;
				for (Course c : courses) {
					if(c.getCourseTitle().equals(title) && c.getCourseCode().equals(code)) {
						duplicate = true;
					}
				}
				
				if(!duplicate) {
					Course newCourse = new Course(code,title);
					courses.add(newCourse);
				}
			}
			
			return courses;
		}
		
		//TimeSlots_________________________________________________________________________
		public List<TimeSlot> GetTimeSlotObjects(List<Map<String, String>> inputTimeSlots) {
			List<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
					
			for (Map<String,String> inputTimeSlot : inputTimeSlots) {
						
				String timeString = inputTimeSlot.get("time").toLowerCase();
				String days = "";
							
				if(timeString.contains("wmf")) {
					days = "mwf";
				}else if(timeString.contains("tt")) {
					days = "tt";
				}else if(timeString.contains("mw")) {
					days = "mw";
				}
				
				timeString = timeString.replaceAll("[^\\d.]", "");
				if(timeString.length() == 1) {
					timeString = timeString + ":00";
				}else if(timeString.length() == 2) {
					timeString = timeString + ":00";
				}else if(timeString.length() == 3) {
					timeString = timeString.charAt(0) + ":" + timeString.substring(1);
				}else if(timeString.length() == 4) {
					timeString = timeString.substring(0,1) + ":" + timeString.substring(2);
				}
				
				
				boolean duplicate = false;
				for (TimeSlot t : timeSlots) {
					if(t.getDays().equals(days) && t.getTime().equals(timeString)) {
						duplicate = true;
					}
				}
							
				if(!duplicate) {
					TimeSlot newTime = new TimeSlot(timeString,days);
					timeSlots.add(newTime);
				}
			}
		
			return timeSlots;
		}
		
		//Offerings___________________________________________________________________________________________
		public List<Offering> GetOfferingObjects(List<Map<String, String>> inputCourses, List<Course> courseObjs, List<TimeSlot> timeObjs) {
			List<Offering> offerings = new ArrayList<Offering>();
			
			for (Map<String,String> inputCourse : inputCourses) {
				
				int section = Integer.parseInt(inputCourse.get("sec."));
				String instructorName = inputCourse.get("instructor real name");
				
				String title = inputCourse.get("course title");
				String code = inputCourse.get("subject") +" "+ inputCourse.get("course #");
				
				Course course = FindCourse(courseObjs, title, code);
				
				String timeString = inputCourse.get("time").toLowerCase();
				String days = "";
							
				if(timeString.contains("wmf")) {
					days = "mwf";
				}else if(timeString.contains("tt")) {
					days = "tt";
				}else if(timeString.contains("mw")) {
					days = "mw";
				}
				
				timeString = timeString.replaceAll("[^\\d.]", "");
				if(timeString.length() == 1) {
					timeString = timeString + ":00";
				}else if(timeString.length() == 2) {
					timeString = timeString + ":00";
				}else if(timeString.length() == 3) {
					timeString = timeString.charAt(0) + ":" + timeString.substring(1);
				}else if(timeString.length() == 4) {
					timeString = timeString.substring(0,1) + ":" + timeString.substring(2);
				}
				
				TimeSlot timeSlot = FindTimeSlot(timeObjs, days, timeString);
				Room room = null;
				
				Offering newOffering = new Offering(section, instructorName, room, course, timeSlot);
				
				offerings.add(newOffering);
				
			}
			
			return offerings;
		}
		
		
		
		
		
		
		
		//Returns A List Of Key Values For Any CSV File, Assuming First Row Is The Name Of The Keys
		
		public List<Map<String, String>> ParseCSV(String fileName) {
			File inputFile = new File(fileName);
			
			boolean validFile = CheckExtension(fileName, CSV_EXTENSION);
			if(!validFile) {
				return null;
			}
			
			
			try {
				debugPrintln("Opening " + fileName);
				Scanner inputStream = new Scanner(inputFile);
				
				String[] keys = GetNextLineValues(inputStream);
				
				for(int i = 0; i < keys.length; i++) {
					keys[i] = keys[i].toLowerCase();
				}
				
				println();
				
				List<Map<String, String>> parsedObjects = new ArrayList<Map<String,String>>();
				
				while (inputStream .hasNext()) {
					
					String[] values = GetNextLineValues(inputStream);
					Map<String, String> parsedObject = new HashMap<String, String>();
					for(int i = 0; i < keys.length; i++) {
						parsedObject.put(keys[i], values[i]);
					}
					parsedObjects.add(parsedObject);
					
				}
				
				debugPrintln("Closing " + fileName);
				debugPrintln();
				inputStream.close();
				
				return parsedObjects;
			}catch(FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		private String[] GetNextLineValues(Scanner inputStream) {
			String data = inputStream.nextLine();
			
			data = data.replace(',', ';');
			while(data.indexOf('"') != -1) {
				int i1 = data.indexOf('"');
				int i2 = data.substring(i1+1).indexOf('"') + i1 + 1;
				String quoted = data.substring(i1,i2+1);
				String unquoted = quoted.replace(';', ',');
				unquoted = unquoted.substring(1, unquoted.length()-1);

				data = data.replace(quoted, unquoted);
			}
			
			
			return data.split(";");
		}
		
		//Utility
		
		public void PrintObjects(List<Map<String, String>> inputObjects) {
			println();
			int i = 1;
			for(Map<String, String> obj : inputObjects) {
				print(i + ":\t");
				
				for (String key: obj.keySet()){
		            String value = obj.get(key);  
		            print(key + ": " + value + "\t\t\t");  
				} 
				
				
				i++;
				println();
			}
		}
		
		private boolean CheckExtension(String fileName, String expectedExtension) {
			String extension = getExtension(fileName);
			if(!extension.equals(expectedExtension)) {
				println("Incorrect File Format!");
				println("Input Format:\t\t" + extension);
				println("Expected Format:\t" + expectedExtension);
				println();
				return false;
			}
			debugPrintln();
			return true;
		}
		
		private String getExtension(String fileName) {
			int extensionStart = fileName.lastIndexOf('.');
			return(fileName.substring(extensionStart));
		}
		
		private Course FindCourse(List<Course> courses,String title, String code) {
			for(Course c : courses) {
				if(c.getCourseTitle().equals(title) && c.getCourseCode().equals(code)){
					return c;
				}
			}
			return null;
		}
		
		private TimeSlot FindTimeSlot(List<TimeSlot> timeSlots,String days, String time) {
			for(TimeSlot t : timeSlots) {
				if(t.getDays().equals(days) && t.getTime().equals(time)){
					return t;
				}
			}
			return null;
		}
		
		//System.out
		
		private void print(String inputString) {
			System.out.print(inputString);
		}
		
		private void println(String inputString) {
			System.out.println(inputString);
		}
		
		private void println() {
			System.out.println();
		}
		
		private void debugPrint(String inputString) {
			if(!debug)return;
			System.out.print(inputString);
		}
		
		private void debugPrintln(String inputString) {
			if(!debug)return;
			System.out.println(inputString);
		}
		
		private void debugPrintln() {
			if(!debug)return;
			System.out.println();
		}
	
}
