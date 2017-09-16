package me.sharpjaws.sharpSK.hooks.GroupManager;

import javax.annotation.Nullable;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class EffGroupManagerSetGroupOfPlayer extends Effect{
	private Expression<OfflinePlayer> player;
	private Expression<String> group;
	private Expression<World> world;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expr, int arg1, Kleenean arg2, ParseResult arg3) {
		player = (Expression<OfflinePlayer>) expr[0];
		group = (Expression<String>) expr[1];
		world = (Expression<World>) expr[2];
		return true;
	}

	@Override
	public String toString(@Nullable Event arg0, boolean arg1) {
		return "[sharpsk] (gm|group[ ]manager set group of [player] %offlineplayer% to %string%";
	}

	@Override
	protected void execute(Event e) {
		final Plugin GMplugin = Bukkit.getPluginManager().getPlugin("GroupManager");
		GroupManager groupManager = (GroupManager)GMplugin;	
		OverloadedWorldHolder handler = groupManager.getWorldsHolder().getDefaultWorld();
		
		if (world != null){
			handler = groupManager.getWorldsHolder().getWorldData(world.getSingle(e).getName());
		}
		for (User u : handler.getUserList()){
		if (u.getUUID().equals(player.getSingle(e).getUniqueId().toString())){
			System.out.println("MATCH");	
			u.setGroup(new Group(group.getSingle(e)));
		}
		}
		handler.reloadUsers();
		
		
		
		
	}

}