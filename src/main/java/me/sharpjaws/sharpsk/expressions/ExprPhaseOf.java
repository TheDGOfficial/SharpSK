package me.sharpjaws.sharpsk.expressions;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.List;

public class ExprPhaseOf extends SimpleExpression<Phase> {
    private Expression<World> w;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Phase> getReturnType() {
        return Phase.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean,
                        SkriptParser.ParseResult paramParseResult) {
        w = (Expression<World>) expr[0];
        return true;
    }

    @Override
    public String toString(Event e, boolean paramBoolean) {
        return "phase of dragon in %world%";
    }

    private EnderDragon.Phase GetPhase(Event e) {
        World w1 = w.getSingle(e);
        List<Entity> entities = w1.getEntities();
        EnderDragon enderDragon = null;
        Phase p = null;
        for (Entity entity : entities) {
            if (entity instanceof EnderDragon) {
                enderDragon = (EnderDragon) entity;
                p = enderDragon.getPhase();
            }
        }
        return p;
    }

    private EnderDragon GetDragon(Event e) {
        World w1 = w.getSingle(e);
        List<Entity> entities = w1.getEntities();
        EnderDragon enderDragon = null;
        for (Entity entity : entities) {
            if (entity instanceof EnderDragon) {
                enderDragon = (EnderDragon) entity;
            }
        }
        return enderDragon;
    }

    @Override
    @Nullable
    protected Phase[] get(Event e) {
        return new Phase[]{GetPhase(e)};
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        EnderDragon dragon = GetDragon(e);
        dragon.setPhase((Phase) delta[0]);

    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return (new Class[]{Phase.class});
        return null;
    }
}
