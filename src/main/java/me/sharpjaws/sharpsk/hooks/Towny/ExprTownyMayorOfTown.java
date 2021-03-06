package me.sharpjaws.sharpsk.hooks.Towny;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprTownyMayorOfTown extends SimpleExpression<OfflinePlayer> {

    private Expression<String> town;

    @Override
    public Class<? extends OfflinePlayer> getReturnType() {
        return OfflinePlayer.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean,
                        SkriptParser.ParseResult Result) {
        town = (Expression<String>) expr[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean paramBoolean) {
        return "[sharpsk] [towny] mayor of town %string%";
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    @Nullable
    protected OfflinePlayer[] get(Event e) {
        try {
            return new OfflinePlayer[]{Bukkit.getOfflinePlayer(TownyUniverse
                    .getPlayer(TownyUniverse.getDataSource().getTown(town.getSingle(e)).getMayor()).getUniqueId())};
        } catch (TownyException e1) {
            return new OfflinePlayer[]{};
        }

    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            try {
                TownyUniverse.getDataSource().getTown(town.getSingle(e))
                        .setMayor(TownyUniverse.getDataSource().getResident(((OfflinePlayer) delta[0]).getName()));
            } catch (TownyException ignored) {
            }
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            return CollectionUtils.array(OfflinePlayer.class);
        }
        return null;
    }

}
