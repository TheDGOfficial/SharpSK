package me.sharpjaws.sharpsk.hooks.mcMMO;

import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.gmail.nossr50.api.PartyAPI;
import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.party.PartyLeader;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprmcMMOPartyLeader extends SimpleExpression<String> {
    private Expression<String> s;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean,
                        SkriptParser.ParseResult paramParseResult) {
        s = (Expression<String>) expr[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean paramBoolean) {
        return "party leader of [party] %string%";
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        for (Party a : PartyAPI.getParties()) {
            if (a.getName().equalsIgnoreCase(s.getSingle(e))) {
                return new String[]{a.getLeader().getPlayerName()};
            }
        }

        return new String[]{null};
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) {
            Player p = (Player) delta[0];
            PartyLeader p2 = new PartyLeader(p.getUniqueId(), p.getName());

            com.gmail.nossr50.party.PartyManager.getParty(s.getSingle(e)).setLeader(p2);
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET)
            return CollectionUtils.array(Player.class);
        return null;
    }
}
