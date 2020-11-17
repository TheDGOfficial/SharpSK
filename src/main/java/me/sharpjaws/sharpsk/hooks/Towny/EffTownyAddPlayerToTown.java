package me.sharpjaws.sharpsk.hooks.Towny;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.palmergames.bukkit.towny.exceptions.AlreadyRegisteredException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import me.sharpjaws.sharpsk.SharpSK;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffTownyAddPlayerToTown extends Effect {
    private Expression<String> s;
    private Expression<OfflinePlayer> p;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean,
                        SkriptParser.ParseResult paramParseResult) {
        p = (Expression<OfflinePlayer>) expr[0];
        s = (Expression<String>) expr[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event paramEvent, boolean paramBoolean) {
        return "[towny] Add %offlineplayer% to [town] %string%";
    }

    @Override
    protected void execute(Event e) {
        SharpSK core = SharpSK.instance;

        try {
            try {
                TownyUniverse.getDataSource().getTown(s.getSingle(e))
                        .addResident(TownyUniverse.getDataSource().getResident(p.getSingle(e).getName()));

            } catch (NotRegisteredException ex2) {
                core.getLogger().warning("Could not add resident: " + "\"" + p.getSingle(e).getName() + "\""
                        + " to town " + "\"" + s.getSingle(e) + "\"");
            }
        } catch (AlreadyRegisteredException ex3) {
            core.getLogger().warning("Could not add resident: " + "\"" + p.getSingle(e).getName() + "\"" + " to town "
                    + "\"" + s.getSingle(e) + "\"");
            core.getLogger().warning("Resident is already in town: " + "\"" + s.getSingle(e) + "\"");
        }

    }
}
