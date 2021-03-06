package me.sharpjaws.sharpsk.hooks.mcMMO;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.gmail.nossr50.datatypes.skills.AbilityType;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityDeactivateEvent;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprEvtAbilityType extends SimpleExpression<AbilityType> {

    @Override
    public Class<? extends AbilityType> getReturnType() {
        return AbilityType.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean paramBoolean) {
        return "event-abilitytype";
    }

    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean arg2, ParseResult result) {
        return ScriptLoader.isCurrentEvent(McMMOPlayerAbilityActivateEvent.class)
                || ScriptLoader.isCurrentEvent(McMMOPlayerAbilityDeactivateEvent.class);
    }

    @Override
    @Nullable
    protected AbilityType[] get(Event e) {
        if (e.getEventName().equals("McMMOPlayerAbilityActivateEvent")) {
            return new AbilityType[]{((McMMOPlayerAbilityActivateEvent) e).getAbility()};
        } else if (e.getEventName().equals("McMMOPlayerAbilityDeactivateEvent")) {
            return new AbilityType[]{((McMMOPlayerAbilityDeactivateEvent) e).getAbility()};
        }
        return null;
    }

}
