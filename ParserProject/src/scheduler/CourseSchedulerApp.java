package scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;


public class CourseSchedulerApp {
	
	private final String CSV_EXTENSION = ".csv";
	private CourseSchedule schedule;
	
	public CourseSchedulerApp(List<Room> rooms, List<Course> courses, List<TimeSlot> times, List<Offering> offerings) {
		this.schedule = new CourseSchedule(courses, times, rooms, offerings);
	}
	
	public CourseSchedule getSchedule() {
		return schedule;
	}

	public void generateSolution() {
        SolverFactory<CourseSchedule> solverFactory = 
        		SolverFactory.createFromXmlResource("main/resources/courseScheduleSolverConfig.xml", getClass().getClassLoader());
        Solver<CourseSchedule>  solver = solverFactory.buildSolver();
        CourseSchedule newSchedule = solver.solve(schedule);
        this.schedule = newSchedule;
    }
	
	private String getExtension(String fileName) {
		int extensionStart = fileName.lastIndexOf('.');
		return(fileName.substring(extensionStart));
	}
	
	private void println(String inputString) {
		System.out.println(inputString);
	}
	
	private void println() {
		System.out.println();
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
		return true;
	}
	
	public void printResults(){
		
		for (Offering offering: schedule.getOfferingList()) {
			System.out.print (offering.getCourse().getCourseTitle() + "," + 
					  offering.getCourse().getCourseTitle() + "," +
					  Integer.toString(offering.getSectionNumber()) + "," +
					  offering.getInstructorName() + "," + 
					  offering.getTimeSlot().getDays() + "," +
					  offering.getTimeSlot().getTime() + "," +
					  offering.getRoom().getBuilding() + "," +
					  offering.getRoom().getNumber() + "\n");
		}
		
	}
	
	public void Output(File toWrite) throws IOException {
		//File toWrite = new File(fileName);
		
		boolean validFile = CheckExtension(toWrite.getName(), CSV_EXTENSION);
		if(!validFile) {
			return;
		}
		
		FileWriter outputFile = new FileWriter(toWrite); 
		
		outputFile.write("Subject,Course #,Course Title,Sec.,Instructor Real Name,Time,Capacity, Building, Room Number\n");
		for(Offering offering: schedule.getOfferingList()) {
			String[] courseCodeInfo = offering.getCourse().getCourseCode().split("\\s+");
			String nextLine = courseCodeInfo[0] + "," 
					+ courseCodeInfo[1] + "," +
					offering.getCourse().getCourseTitle() + "," +
					Integer.toString(offering.getSectionNumber()) + "," + "'" + 
					offering.getInstructorName() + "'" + "," + 
					offering.getTimeSlot().getDays() + offering.getTimeSlot().getTime() + "," +
					Integer.toString(offering.getCapacity()) + "," +
					offering.getRoom().getBuilding() + "," +
					offering.getRoom().getNumber() + "\n";
			outputFile.write(nextLine);
		}
		outputFile.close();
		
	}
}