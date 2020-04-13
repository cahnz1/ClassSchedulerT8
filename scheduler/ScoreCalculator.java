package scheduler;

import java.util.HashSet;
import java.util.Set;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

public class ScoreCalculator 
  implements EasyScoreCalculator<CourseSchedule> {
 
    @Override
    public Score calculateScore(CourseSchedule courseSchedule) {
        int hardScore = 0;
        int softScore = 0;
 
        // hard constraint; two classes should not have the same 
        // room at the same time
        Set<String> occupiedRooms = new HashSet<>();
        for(Offering offering : courseSchedule.getOfferingList()) {
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
        
        // hard constraint; an offering should not be in a room with
        // less space than the offering's capacity
        for(Offering offering : courseSchedule.getOfferingList()) {
            if (offering.getCapacity() > offering.getRoom().getCapacity()) {
            	hardScore += -1; //arbitrary for now but should have higher weight than room one
            }
        }
        
        // programming times vs. suggested times in as a soft constraint; may
        // change to a hard constraint depending on what is said about
        // alternate times
        for(Offering offering : courseSchedule.getOfferingList()) {
            if (offering.getTimeSlot().getDays() == offering.getSuggestedTime().getDays()
            		&& offering.getTimeSlot().getTime() == offering.getSuggestedTime().getTime()) {
            	softScore += -4; //arbitrary for now but should have higher weight than room one
            }
        }
        
        
 
        return HardSoftScore.of(hardScore, softScore);
    }
}