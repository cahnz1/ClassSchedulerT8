package scheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import com.github.javaparser.utils.Pair;


public class ScoreCalculator 
  implements EasyScoreCalculator<CourseSchedule> {
	
	private int myCacheSize = 500; // Default value
	static double LENGTH_OF_PERIOD_HOURS;

    @SuppressWarnings("unused")
    public void setMyCacheSize(int myCacheSize) {
        this.myCacheSize = myCacheSize;
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
    
    @Override
    public Score calculateScore(CourseSchedule courseSchedule) {
        int hardScore = 0;
        int mediumScore = 0;
        int softScore = 0;
 
        // hard constraint; two classes should not have the same 
        // room at the same time
        HashMap<String, ArrayList<Pair<String,String>>> roomTimes = new HashMap<>();
        for(Offering offering : courseSchedule.getOfferingList()) {
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
    							hardScore += -1;
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
        
        // hard constraint; an offering should not be in a room with
        // less space than the offering's capacity
        for(Offering offering : courseSchedule.getOfferingList()) {
        	if (offering.getCapacity() != 0 && offering.getRoom() != null && offering.getRoom().getCapacity() != 0) {
        		if (offering.getCapacity() > offering.getRoom().getCapacity()) {
        			hardScore += -1; //arbitrary for now but should have higher weight than room one
        		}
        	}
        }
        
        
        // hard constraint: an instructor should not be teaching two
        // classes at the same time
        HashMap<String, ArrayList<Pair<String,String>>> instructorTimes = new HashMap<>();
        for(Offering offering : courseSchedule.getOfferingList()) {
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
    								hardScore += -1;
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
        
        // medium constraint; unless it cannot be avoided, an offering's time
        // should not differ from its suggested time
        for(Offering offering : courseSchedule.getOfferingList()) {
        	if (offering.getTimeSlot() != null && offering.getSuggestedTime() != null) {
        		if(offering.getTimeSlot().getDays() != offering.getSuggestedTime().getDays() || 
        				offering.getTimeSlot().getTime() != offering.getSuggestedTime().getTime()) {
        			mediumScore -= 1;
        		}
        	}
        }
        
        
        // soft constraint: the class should ideally be located in a building
        // closer to ITE (location of the professor's office hours)
        for(Offering offering : courseSchedule.getOfferingList()) {
        	if (offering.getRoom() != null) {
        		softScore -= offering.getPriority(); //subtracts priority of building where offering takes place
        	}
        }
 
        return HardMediumSoftScore.of(hardScore, mediumScore, softScore);
    }
}