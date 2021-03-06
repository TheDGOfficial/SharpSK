package me.sharpjaws.sharpsk.expressions;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import javax.annotation.Nullable;

public class ExprEventWorldLoc extends SimpleExpression<Location> {

    private int mark;

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean paramBoolean) {
        return "[the] [(-1¦past|1¦future) state of] event-location";
    }

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean arg2, ParseResult result) {
        if (!ScriptLoader.isCurrentEvent(PlayerChangedWorldEvent.class)) {
            Skript.error("This expression can only be used in an \"on world change\" event.",
                    ErrorQuality.SEMANTIC_ERROR);
            return false;
        }
        mark = result.mark;
        return true;
    }

    @Override
    @Nullable
    protected Location[] get(Event e) {
        if (mark == 0) {
            return new Location[]{((PlayerChangedWorldEvent) e).getPlayer().getLocation()};
        } else if (mark == 1) {
            return new Location[]{((PlayerChangedWorldEvent) e).getPlayer().getLocation()};
        } else if (mark == -1) {
            return new Location[]{((PlayerChangedWorldEvent) e).getFrom().getSpawnLocation()};
        }
        return null;
    }

}
