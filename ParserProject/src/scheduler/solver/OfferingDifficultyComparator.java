package scheduler.solver;

import java.util.Comparator;
import scheduler.Offering;

public class OfferingDifficultyComparator implements Comparator<Offering> {
    public int compare(Offering a, Offering b) {
        return a.getCapacity() - b.getCapacity();
    }
}