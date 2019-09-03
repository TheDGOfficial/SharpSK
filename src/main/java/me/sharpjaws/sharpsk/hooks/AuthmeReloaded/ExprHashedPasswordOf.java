package me.sharpjaws.sharpsk.hooks.AuthmeReloaded;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import fr.xephi.authme.datasource.DataSource;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

public class ExprHashedPasswordOf extends SimpleExpression<String> {
    private Expression<Player> a;
    private DataSource dataSource;

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
    public boolean init(Expression<?>[] expr, int matchedPattern, Kleenean paramKleenean,
                        SkriptParser.ParseResult paramParseResult) {
        a = (Expression<Player>) expr[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "[authme] hashed password of %player%";
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        String pass = fr.xephi.authme.data.auth.PlayerCache.getInstance().getAuth(a.getSingle(e).getName()).getPassword().getHash();
        return new String[] { pass };
    }

}
