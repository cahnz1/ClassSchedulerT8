package scheduler.solver;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.heuristic.selector.move.generic.SwapMove;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import scheduler.CourseSchedule;
import scheduler.Offering;

public class DifferentCourseSwapMoveFilter implements SelectionFilter<CourseSchedule, SwapMove> {

    @Override
    public boolean accept(ScoreDirector<CourseSchedule> scoreDirector, SwapMove move) {
        Offering leftOffering = (Offering) move.getLeftEntity();
        Offering rightOffering = (Offering) move.getRightEntity();
        return !leftOffering.getCourse().equals(rightOffering.getCourse());
    }
}