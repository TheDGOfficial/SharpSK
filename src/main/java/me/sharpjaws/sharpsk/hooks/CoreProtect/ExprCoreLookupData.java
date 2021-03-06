package me.sharpjaws.sharpsk.hooks.CoreProtect;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.util.Timespan;
import ch.njol.util.Kleenean;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExprCoreLookupData extends SimpleExpression<ItemStack> {
    private int mark;
    private Expression<Location> l;
    private Expression<Number> n;
    private Expression<Timespan> times;
    private Expression<ItemStack> exblocks;
    private Expression<OfflinePlayer> players;

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expr, int arg1, Kleenean arg2, ParseResult result) {
        players = (Expression<OfflinePlayer>) expr[0];
        l = (Expression<Location>) expr[1];
        n = (Expression<Number>) expr[2];
        times = (Expression<Timespan>) expr[3];
        exblocks = (Expression<ItemStack>) expr[4];
        mark = result.mark;
        return true;

    }

    @Override
    public String toString(@Nullable Event arg0, boolean arg1) {
        return "[(coreprotect|cp)] [(0¦removal|1¦placement|2¦interaction)] lookup data [from by %-offlineplayers%] from %location% in radius %number% from %timespan% ago";
    }

    @Override
    @Nullable
    protected ItemStack[] get(final Event e) {

        final List<String> users = new ArrayList<>();
        if (users != null && players != null) {
            for (OfflinePlayer b : players.getAll(e)) {

                users.add(b.getName());
            }
        }
        final List<Object> exclude = new ArrayList<>();
        if (exblocks != null) {
            for (ItemStack b : exblocks.getAll(e)) {

                exclude.add(b.getType());
            }
        }

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");
        CoreProtectAPI api = ((CoreProtect) plugin).getAPI();

        List<String[]> lookup = null;
        List<ItemStack> parsedlookup = new ArrayList<>();
        lookup = api.performLookup((int) times.getSingle(e).getTicks_i() / 20, users, null, null, exclude,
                Collections.singletonList(mark), n.getSingle(e).intValue(), l.getSingle(e));

        for (String[] value : lookup) {

            if (lookup != null) {
                parsedlookup.add(new ItemStack(api.parseResult(value).getType()));
            }
        }

        return parsedlookup.toArray(new ItemStack[0]);

    }

}
