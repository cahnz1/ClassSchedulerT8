package scheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
	
	public String getAlternateTimes() {
		String alternateTimes = "This schedule could not be made with the suggested times, so alternates were given. Alternate Times Scheduled:\n";
		boolean alternateTimesNeeded = false;
		for (Offering offering: schedule.getOfferingList()) {
			if (offering.getTimeSlot() != null && offering.getSuggestedTime() != null) {
			if (offering.getTimeSlot().getDays() != offering.getSuggestedTime().getDays() || 
    				offering.getTimeSlot().getTime() != offering.getSuggestedTime().getTime()) {
				alternateTimes += offering.getCourse().getCourseCode() + "," + 
						  offering.getCourse().getCourseTitle() + "," +
						  Integer.toString(offering.getSectionNumber()) + "," +
						  offering.getInstructorName() + "," + 
						  offering.getTimeSlot().getDays() + "," +
						  offering.getTimeSlot().getTime() + "," +
						  offering.getRoom().getBuilding() + "," +
						  offering.getRoom().getNumber() + "\n";
				alternateTimesNeeded = true;
			}
		}
		}
		
		if (alternateTimesNeeded == true) {
			return alternateTimes;
		} else {
			return "";
		}
	}
	
	private String getExtension(String fileName) {
		int extensionStart = fileName.lastIndexOf('.');
		if (extensionStart == -1) {
			return "";
		}
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
		
		String nameOfFile = toWrite.getAbsolutePath();
		String currentExtension = getExtension(nameOfFile);
		String newFileName = "";
		String newExtension = "csv";
		
		
		if(!validFile) { //if extension is not ".csv"
			if (currentExtension.equals("")){
				newFileName = nameOfFile + "." + newExtension;
			} else {
				newFileName = nameOfFile.replaceAll("." + currentExtension, newExtension);
			}
		}
		else {
			newFileName = nameOfFile;
		}
		File validatedFile = new File(newFileName);
		FileWriter outputFile = new FileWriter(validatedFile); 
		
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