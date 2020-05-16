package Parsing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import scheduler.Course;
import scheduler.CourseSchedulerApp;
import scheduler.Offering;
import scheduler.Room;
import scheduler.TimeSlot;


public class Tester {
	static Parser csvParser = new Parser();
	static List<Map<String, String>> parsedCourseFile;
	static List<Room> rooms;
	static List<Course> courses;
	static List<TimeSlot> times;
	static List<Offering> offerings;
	static String courseFileName = null;
	static String roomsFileName = null;
	

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Course Scheduler");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300, 200);
	       
	    JMenuBar mb = new JMenuBar();
	    /*JMenu fm = new JMenu("File");
	    mb.add(fm);*/
	    
	    JPanel mainMenu = new JPanel(new GridLayout(0, 1, 10, 10));
	       
	    //JMenuItem open = new JMenuItem("Open...");
	    /*JFileChooser fc = new JFileChooser();
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
	    });*/
	    //fm.add(open); // Adds Button to content pane of frame
	       
	    /*JMenuItem save = new JMenuItem("Save As...");
	    fm.add(save);*/
	       
	    JButton openCoursesFile = new JButton("Open Courses File");
	    JButton openRoomsFile = new JButton("Open Rooms File");
	    JButton run = new JButton("Plan Schedule");
	    
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV sheet", "csv");
	    
	    JFileChooser fcCourses = new JFileChooser();
	    fcCourses.setFileFilter(filter);
	    openCoursesFile.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	             int rv = fcCourses.showOpenDialog(frame);
	                
	             if (rv == JFileChooser.APPROVE_OPTION) {
	             	File file = fcCourses.getSelectedFile();
	             	
	              	// send this file to the parser
	             	courseFileName = file.getName();
	             	openCoursesFile.setText("Open Courses File: " + courseFileName);
	             	//String roomsFileName = "Spring 2020 Schedule.csv";
	             	
	             	// Print what was parsed
	        		//parseFile(courseFileName, roomsFileName);
	        		//printRooms(rooms);
	        		//csvParser.PrintObjects(parsedCourseFile);
	        		
	             }
	         }
	    });
	    JFileChooser fcRooms = new JFileChooser();
	    fcRooms.setFileFilter(filter);
	    openRoomsFile.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	             int rv = fcRooms.showOpenDialog(frame);
	                
	             if (rv == JFileChooser.APPROVE_OPTION) {
	             	File file = fcRooms.getSelectedFile();
	             	
	              	// send this file to the parser
	             	//String courseFileName = "Spring 2020 Schedule.csv";
	             	roomsFileName = file.getName();
	             	openRoomsFile.setText("Open Room File: " + roomsFileName);
	             	
	             	// Print what was parsed
	        		//parseFile(courseFileName, roomsFileName);
	        		//printRooms(rooms);
	        		//csvParser.PrintObjects(parsedCourseFile);
	        		
	             }
	         }
	    });
	    JFileChooser fcSave = new JFileChooser();
	    fcSave.setFileFilter(filter);
	    run.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	// Print what was parsed
	        	if (courseFileName != null && roomsFileName != null) {
	        		try {
	        			parseFile(courseFileName, roomsFileName);
	        		} catch (MissingInformationException ex) {
	        			JOptionPane.showMessageDialog(frame, ex.getMessage() + ". Please ensure the file is formatted correctly.");
	        		} catch (IncorrectFileFormatException ex) {
	        			JOptionPane.showMessageDialog(frame, ex.getMessage() + ". Please ensure the file is a .csv file.");
	        		}
		        	//printRooms(rooms);
		        	//csvParser.PrintObjects(parsedCourseFile);
		        	//String courseFileName = "Spring 2020 Schedule.csv";
		    		//String roomsFileName = "ClassRoom.csv";
		     	
		    		// Print what was parsed
		    		//parseFile(courseFileName, roomsFileName);
		    		CourseSchedulerApp scheduler = new CourseSchedulerApp(rooms, courses, times, offerings);
		    		scheduler.generateSolution();
		    		int rv = fcSave.showSaveDialog(frame);
	                
		            if (rv == JFileChooser.APPROVE_OPTION) {
		             	File toWrite = fcSave.getSelectedFile();
		            
			    		try {
							scheduler.Output(toWrite);
						} catch (IOException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
		            }
	        	}
	        	else {
	        		JOptionPane.showMessageDialog(frame, "You must load a Courses file and a Rooms file to do this.");
	        	}
	         }
	    });
	    
	    mainMenu.add(openCoursesFile);
	    mainMenu.add(openRoomsFile);
	    mainMenu.add(run);
	    
	    frame.getContentPane().add(BorderLayout.NORTH, mb);
	    frame.add(mainMenu);
	    //frame.pack();
	    frame.setVisible(true);
		
	}
	
	public static void parseFile(String courseFileName, String roomsFileName) throws IncorrectFileFormatException, MissingInformationException {
		csvParser = new Parser();
		try {
			parsedCourseFile = csvParser.ParseCSV(courseFileName);
			String[] requiredKeys = new String[]{"subject", "course #", "course title", "ver.", "sec.", "instructor real name", "time", "capacity"};
			csvParser.CheckForRequiredKeys(parsedCourseFile, requiredKeys);
			rooms = csvParser.GetRoomObjects(roomsFileName);
		}catch(IncorrectFileFormatException e) {
			throw e;
		}catch(MissingInformationException e) {
			throw e;
		}
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
