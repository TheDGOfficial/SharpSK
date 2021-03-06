package me.sharpjaws.sharpsk.hooks.Kingdoms;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.kingdoms.constants.kingdom.Kingdom;
import org.kingdoms.manager.game.GameManagement;

import javax.annotation.Nullable;

public class ExprKingdomsLoreOfKingdom extends SimpleExpression<String> {

    private Expression<String> kingdom;

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int arg1, Kleenean arg2, ParseResult arg3) {
        kingdom = (Expression<String>) expr[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "[sharpsk] [kingdoms] lore of kingdom %string%";
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        return new String[]{
                GameManagement.getKingdomManager().getOrLoadKingdom(kingdom.getSingle(e)).getKingdomLore()};
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            try {
                Kingdom kdm = GameManagement.getKingdomManager().getOrLoadKingdom(kingdom.getSingle(e));
                kdm.setKingdomLore((String) delta[0]);

            } catch (NullPointerException ignored) {
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(String.class);
        }
        return null;
    }
}
