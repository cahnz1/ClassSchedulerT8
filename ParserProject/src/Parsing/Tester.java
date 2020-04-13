package Parsing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class Tester {
	static Parser csvParser = new Parser();
	static List<Map<String, String>> parsedCourseFile;
	static List<Room> rooms;
	static List<Course> courses;
	static List<TimeSlot> times;
	static List<Offering> offerings;
	

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Course Scheduler");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300,300);
	       
	    JMenuBar mb = new JMenuBar();
	    JMenu fm = new JMenu("File");
	    mb.add(fm);
	       
	    JMenuItem open = new JMenuItem("Open...");
	    JFileChooser fc = new JFileChooser();
	    open.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	             int rv = fc.showOpenDialog(frame);
	                
	             if (rv == JFileChooser.APPROVE_OPTION) {
	             	File file = fc.getSelectedFile();
	             	
	              	// send this file to the parser
	             	String courseFileName = "Spring 2020 Schedule.csv";
	             	String roomsFileName = file.getName();
	             	
	             	// Print what was parsed
	        		parseFile(courseFileName, roomsFileName);
	        		printRooms(rooms);
	        		csvParser.PrintObjects(parsedCourseFile);
	        		
	             }
	         }
	    });
	    fm.add(open); // Adds Button to content pane of frame
	       
	    JMenuItem save = new JMenuItem("Save As...");
	    fm.add(save);
	       
	    JButton run = new JButton("Plan Schedule");
	       
	    frame.getContentPane().add(BorderLayout.NORTH, mb);
	    frame.getContentPane().add(run);
	       
	    frame.setVisible(true);
		
	}
	
	public static void parseFile(String courseFileName, String roomsFileName) {
		csvParser = new Parser();
		parsedCourseFile = csvParser.ParseCSV(courseFileName);
		rooms = csvParser.GetRoomObjects(roomsFileName);
		courses =  csvParser.GetCourseObjects(parsedCourseFile);
		times = csvParser.GetTimeSlotObjects(parsedCourseFile);
		offerings = csvParser.GetOfferingObjects(parsedCourseFile, courses, times);
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
