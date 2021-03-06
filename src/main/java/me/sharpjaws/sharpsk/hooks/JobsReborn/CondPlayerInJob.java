package me.sharpjaws.sharpsk.hooks.JobsReborn;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class CondPlayerInJob extends Condition {

    private Expression<Player> p;
    private Expression<Job> j;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean arg2, SkriptParser.ParseResult arg3) {
        p = (Expression<Player>) expr[0];
        j = (Expression<Job>) expr[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "%player% is in job %job%";
    }

    @Override
    public boolean check(Event e) {

        return Jobs.getPlayerManager().getJobsPlayer(p.getSingle(e)).isInJob(j.getSingle(e));
    }
}
