package scheduler;

import java.util.HashSet;
import java.util.Set;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

public class ScoreCalculator 
  implements EasyScoreCalculator<CourseSchedule> {
	
	private int myCacheSize = 500; // Default value

    @SuppressWarnings("unused")
    public void setMyCacheSize(int myCacheSize) {
        this.myCacheSize = myCacheSize;
    }
 
    @Override
    public Score calculateScore(CourseSchedule courseSchedule) {
        int hardScore = 0;
        int mediumScore = 0;
        int softScore = 0;
 
        // hard constraint; two classes should not have the same 
        // room at the same time
        Set<String> occupiedRooms = new HashSet<>();
        for(Offering offering : courseSchedule.getOfferingList()) {
        	if (offering.getRoom() != null && offering.getTimeSlot() != null) {
        		//System.out.print("enters this statement");
        		String roomInUse = offering.getRoom().getBuilding() +
        				offering.getRoom().getNumber() +
        				offering.getTimeSlot().getDays() +
        				offering.getTimeSlot().getTime();
        		if(occupiedRooms.contains(roomInUse)){
        			hardScore += -1;
        		} else {
        			occupiedRooms.add(roomInUse);
        		}
        	}
        	if (offering.getRoom() == null) {
        		hardScore += -1;
        	}
        	if (offering.getTimeSlot() == null){
        		hardScore += -1;
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
        
        // medium constraint; unless it cannot be avoided, an offering's time
        // should not differ from its suggested time
        for(Offering offering : courseSchedule.getOfferingList()) {
        	if(offering.getTimeSlot().getDays() != offering.getSuggestedTime().getDays() || 
        			offering.getTimeSlot().getTime() != offering.getSuggestedTime().getTime()) {
        		mediumScore -= 1;
        	}
        }
        
        
        //add soft constraint for room distance/priority
        for(Offering offering : courseSchedule.getOfferingList()) {
        	if (offering.getRoom() != null) {
        		softScore -= offering.getPriority(); //subtracts priority of building where offering takes place
        	}
        }
 
        return HardMediumSoftScore.of(hardScore, mediumScore, softScore);
    }
}