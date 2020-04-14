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

	public void generateSolution() {
        SolverFactory<CourseSchedule> solverFactory = 
        		SolverFactory.createFromXmlResource("scheduler/solver/CourseScheduleSolverConfig.xml");
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
	
	public void Output(String fileName) throws IOException {
		File toWrite = new File(fileName);
		
		boolean validFile = CheckExtension(fileName, CSV_EXTENSION);
		if(!validFile) {
			return;
		}
		
		FileWriter outputFile = new FileWriter(toWrite); 
		
		ArrayList<Offering> offerings = new ArrayList<Offering>(schedule.getOfferingList());
		for(int i=0; i < offerings.size(); i++) {
			Offering currentOffering = offerings.get(i);
			String nextLine = currentOffering.createLine();
			outputFile.write(nextLine);
		}
		outputFile.close();
		
	}
}
