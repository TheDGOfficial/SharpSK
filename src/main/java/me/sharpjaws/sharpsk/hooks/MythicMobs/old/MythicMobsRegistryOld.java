package me.sharpjaws.sharpsk.hooks.MythicMobs.old;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class MythicMobsRegistryOld {
    public static void RegisterMythicMobsOld() {
        Skript.registerEvent("Mythicmob death", SimpleEvent.class,
                net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent.class, "([mythicmob|mm]) death");
        EventValues.registerEventValue(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent.class,
                Location.class,
                new Getter<Location, net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent>() {
                    @Override
                    @Nullable
                    public Location get(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent e) {
                        return e.getEntity().getLocation();
                    }
                }, 0);
        EventValues.registerEventValue(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent.class,
                String.class,
                new Getter<String, net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent>() {
                    @Override
                    @Nullable
                    public String get(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobDeathEvent e) {
                        return e.getMobType().getInternalName();
                    }
                }, 0);
        Skript.registerEvent("Mythicmob spawn", SimpleEvent.class,
                net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent.class, "([mythicmob|mm]) spawn");
        EventValues.registerEventValue(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent.class,
                Entity.class,
                new Getter<Entity, net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent>() {
                    @Override
                    @Nullable
                    public Entity get(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent e) {
                        return e.getEntity();
                    }
                }, 0);
        EventValues.registerEventValue(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent.class,
                String.class,
                new Getter<String, net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent>() {
                    @Override
                    @Nullable
                    public String get(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSpawnEvent e) {
                        return e.getMobType().getInternalName();
                    }
                }, 0);
        Skript.registerExpression(ExprEvtMMDropsOld.class, ItemStack.class, ExpressionType.SIMPLE,
                "[all] [event-]mmdrops");
        Skript.registerCondition(CondIsMythicMobOld.class, "%entity% is a mythicmob");
        Skript.registerCondition(CondNotMythicMobOld.class, "%entity% is not a mythicmob");
        Skript.registerEffect(EffSpawnMMOld.class,
                "[sharpsk] spawn [a] mythicmob %string% at [the] %location% with level %integer%");
        Skript.registerEvent("Mythicmob Skill", SimpleEvent.class,
                net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSkillEvent.class,
                "([mythicmob|mm]) skill [cast]");
        EventValues.registerEventValue(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSkillEvent.class,
                Location.class,
                new Getter<Location, net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSkillEvent>() {
                    @Override
                    @Nullable
                    public Location get(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSkillEvent e) {
                        return e.getEntity().getLocation();
                    }
                }, 0);
        EventValues.registerEventValue(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSkillEvent.class,
                String.class,
                new Getter<String, net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSkillEvent>() {
                    @Override
                    @Nullable
                    public String get(net.elseland.xikage.MythicMobs.API.Bukkit.Events.MythicMobSkillEvent e) {
                        return e.getSkillName();
                    }
                }, 0);
    }

}
