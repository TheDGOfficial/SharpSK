package me.sharpjaws.sharpsk.hooks.AuthmeReloaded;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import fr.xephi.authme.api.NewAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class EffAuthForceRegister extends Effect {
    private Expression<Player> player;
    private Expression<String> pass;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean, SkriptParser.ParseResult paramParseResult) {
        player = (Expression<Player>) expr[0];
        pass = (Expression<String>) expr[1];

        return true;
    }

    @Override
    public String toString(@Nullable Event paramEvent, boolean paramBoolean) {
        return "force %player% to register with password %pass%";
    }

    @Override
    protected void execute(Event e) {
        NewAPI.getInstance().forceRegister(player.getSingle(e), pass.getSingle(e));
    }
}
