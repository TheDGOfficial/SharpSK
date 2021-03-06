package me.sharpjaws.sharpsk;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAPIException;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.skript.util.Timespan;
import com.codingforcookies.armorequip.ArmorEquipEvent;
import com.codingforcookies.armorequip.ArmorEquipListener;
import com.codingforcookies.armorequip.ArmorunEquipEvent;
import com.codingforcookies.armorequip.ArmorunEquipListener;
import me.sharpjaws.sharpsk.conditions.*;
import me.sharpjaws.sharpsk.effects.*;
import me.sharpjaws.sharpsk.events.EvtExpChange;
import me.sharpjaws.sharpsk.events.EvtTimerComplete;
import me.sharpjaws.sharpsk.events.EvtTimerTick;
import me.sharpjaws.sharpsk.expressions.*;
import me.sharpjaws.sharpsk.threads.CTickTimerThread;
import me.sharpjaws.sharpsk.threads.CTimerThread;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderDragon.Phase;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EnderDragonChangePhaseEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.FireworkExplodeEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO re-add updater
// TODO re-add bStats
// TODO re-add Travis CI
// TODO re-add readme and contributing files
// TODO maybe also re-add PermissionsEx support
public final class SharpSK extends JavaPlugin implements Listener {

    public static JavaPlugin plugin;
    public static SharpSK instance;

    public SharpSK() {
        SharpSK.instance = this;
    }

    /**
     * @param args command line arguments
     * @deprecated just for intellij
     */
    @SuppressWarnings("EmptyMethod")
    @Deprecated
    public static final void main(final String[] args) {
        // Just for IntelliJ to not show "Invalid main class" in MANIFEST.MF
    }

    private static final Block getBlock(final InventoryHolder iH) {
        Block b3 = null;

        if (iH instanceof Chest) {
            Chest b = (Chest) iH;
            BlockState b2 = b.getBlock().getState();
            b3 = b2.getBlock();
        } else if (iH instanceof Hopper) {
            Hopper b = (Hopper) iH;
            BlockState b2 = b.getBlock().getState();
            b3 = b2.getBlock();
        } else if (iH instanceof Dispenser) {
            Dispenser b = (Dispenser) iH;
            BlockState b2 = b.getBlock().getState();
            b3 = b2.getBlock();
        } else if (iH instanceof Dropper) {
            Dropper b = (Dropper) iH;
            BlockState b2 = b.getBlock().getState();
            b3 = b2.getBlock();
        } else if (iH instanceof DoubleChest) {
            DoubleChest b = (DoubleChest) iH;
            b3 = b.getLocation().getBlock();
        } else if (iH instanceof Furnace) {
            Furnace b = (Furnace) iH;
            b3 = b.getLocation().getBlock();
        }

        return b3;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sharpsk")) {
            if (sender.hasPermission("sharpsk.admin")) {
                if (args.length == 0) {
                    sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK]" + org.bukkit.ChatColor.GREEN + " Use "
                            + org.bukkit.ChatColor.YELLOW + "/sharpsk help" + org.bukkit.ChatColor.GREEN
                            + " to see all the available commands");
                    return false;
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("help")) {
                        sender.sendMessage("");
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.RED
                                + "====== SharpSK Commands =====");
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.YELLOW
                                + "/sharpsk help" + org.bukkit.ChatColor.GREEN + " // " + "All of the plugin commands");
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.YELLOW
                                + "/sharpsk version " + org.bukkit.ChatColor.GREEN + " // "
                                + "Current version of SharpSK");
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.YELLOW
                                + "/sharpsk check" + org.bukkit.ChatColor.GREEN + " // "
                                + "Checks for any new versions");
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.YELLOW
                                + "/sharpsk timers" + org.bukkit.ChatColor.GREEN + " // "
                                + "Count of all running timers");
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.YELLOW
                                + "/sharpsk timers list" + org.bukkit.ChatColor.GREEN + " // "
                                + "list of all running timers");
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.RED
                                + "====== SharpSK Commands =====");
                        sender.sendMessage("");
                    } else if (args[0].equalsIgnoreCase("version")) {
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.GREEN
                                + "Running on version " + org.bukkit.ChatColor.YELLOW + "v"
                                + this.getDescription().getVersion());
                    } else if (args[0].equalsIgnoreCase("check")) {
                        // Using this character instead of chat color is faster to write :
                        // We are already using UTF-8 for compilation so this should work.
                        sender.sendMessage("§cUpdater is disabled for now, sorry!");
                    } else if (args[0].equalsIgnoreCase("timers")) {

                        int activetimers = 0;
                        for (Thread t : Thread.getAllStackTraces().keySet()) {
                            if (t instanceof CTimerThread || t instanceof CTickTimerThread) {
                                activetimers++;
                            }
                        }
                        if (activetimers > 1) {
                            sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.GREEN
                                    + "There are " + org.bukkit.ChatColor.YELLOW + activetimers
                                    + org.bukkit.ChatColor.GREEN + " Timers active.");
                        } else if (activetimers > 0) {
                            sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.GREEN
                                    + "There is " + org.bukkit.ChatColor.YELLOW + activetimers
                                    + org.bukkit.ChatColor.GREEN + " Timer active.");
                        } else {
                            sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.GREEN
                                    + "There are no running timers active");
                        }
                    } else
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK]" + org.bukkit.ChatColor.GREEN
                                + " Use " + org.bukkit.ChatColor.YELLOW + "/sharpsk help" + org.bukkit.ChatColor.GREEN
                                + " to see all the available commands");

                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("timers") && args[1].equalsIgnoreCase("list")) {

                        ArrayList<String> timers = new ArrayList<>();
                        for (Thread t : Thread.getAllStackTraces().keySet()) {
                            if (t instanceof CTimerThread || t instanceof CTickTimerThread) {
                                timers.add(t.getName());
                            }
                        }
                        if (timers.size() > 0) {
                            sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.RED
                                    + "====== SharpSK Timers =====");
                            for (String timer : timers) {
                                sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + ChatColor.YELLOW + timer);
                            }
                            sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.RED
                                    + "====== SharpSK Timers =====");
                        } else if (timers.size() == 0) {
                            sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK] " + org.bukkit.ChatColor.GREEN
                                    + "There are no running timers active");
                        }
                    } else
                        sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK]" + org.bukkit.ChatColor.GREEN
                                + " Use " + org.bukkit.ChatColor.YELLOW + "/sharpsk help" + org.bukkit.ChatColor.GREEN
                                + " to see all the available commands");
                }
            }

        }
        if (!sender.hasPermission("sharpsk.admin")) {
            sender.sendMessage(org.bukkit.ChatColor.AQUA + "[SharpSK]" + org.bukkit.ChatColor.RED
                    + " You do not have permission to run this command");
            return false;
        }
        return false;
    }

    @Override
    public void onEnable() {
        plugin = this;
        HookManager hman = new HookManager();
        File configfile = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configfile);

        try {
            if (!config.getString("cfgver").contains(this.getDescription().getVersion())) {
                getLogger().info("Old config Detected. It has been updated.");
                configfile.renameTo(new File(getDataFolder(), "config_" + System.currentTimeMillis() + ".yml.old"));
                saveDefaultConfig();
            }
        } catch (NullPointerException ex2) {
            if (!configfile.exists()) {
                getLogger().info("Generating config...");
                saveDefaultConfig();
            } else {
                getLogger().info("Old config Detected. It has been updated.");
                configfile.renameTo(new File(getDataFolder(), "config-" + System.currentTimeMillis() + ".yml.old"));
                saveDefaultConfig();
            }
        }

        Plugin skript = Bukkit.getPluginManager().getPlugin("Skript");

        if (Bukkit.getPluginManager().isPluginEnabled(skript)) {
            try {
                getLogger().info("Attempting to register Addon...");
                Skript.checkAcceptRegistrations();
                Skript.registerAddon(this);
                getLogger().info("Attempting to register stuff...");

                try {
                    if (Skript.classExists("org.bukkit.event.entity.FireworkExplodeEvent")) {
                        Skript.registerEvent("Firework Explode", SimpleEvent.class, FireworkExplodeEvent.class,
                                "firework explode");
                    }
                } catch (final Throwable tw) {
                    getLogger().info("An error occurred while trying to register Firework Explode event");
                }
                Skript.registerEvent("Shear", SimpleEvent.class, PlayerShearEntityEvent.class, "[on] shear");
                Skript.registerEvent("Transfer", SimpleEvent.class, InventoryMoveItemEvent.class, "[on] transfer");
                EventValues.registerEventValue(InventoryMoveItemEvent.class, ItemStack.class,
                        new Getter<ItemStack, InventoryMoveItemEvent>() {
                            @Override
                            public ItemStack get(InventoryMoveItemEvent e) {
                                return e.getItem();
                            }
                        }, 0);
                EventValues.registerEventValue(InventoryMoveItemEvent.class, Block.class,
                        new Getter<Block, InventoryMoveItemEvent>() {
                            @Override
                            public Block get(InventoryMoveItemEvent e) {
                                InventoryHolder iH = e.getSource().getHolder();

                                return getBlock(iH);
                            }
                        }, 0);
                EventValues.registerEventValue(InventoryMoveItemEvent.class, Entity.class,
                        new Getter<Entity, InventoryMoveItemEvent>() {
                            @Override
                            public Entity get(InventoryMoveItemEvent e) {
                                // TODO find out all NullPointerException catch clauses and replace them with proper null-checks.
                                try {
                                    if (e.getSource().getHolder() instanceof Entity) {
                                        return (Entity) e.getSource().getHolder();
                                    }
                                } catch (NullPointerException ex) {
                                    return null;
                                }
                                return null;

                            }
                        }, 0);
                EventValues.registerEventValue(InventoryMoveItemEvent.class, Location.class,
                        new Getter<Location, InventoryMoveItemEvent>() {
                            @Override
                            public Location get(InventoryMoveItemEvent e) {
                                InventoryHolder iH = e.getDestination().getHolder();
                                Block b3 = getBlock(iH);

                                return b3.getLocation();
                            }
                        }, 0);

                Skript.registerEvent("Pickup", SimpleEvent.class, InventoryPickupItemEvent.class, "[on] hopper pickup");
                EventValues.registerEventValue(InventoryPickupItemEvent.class, Inventory.class,
                        new Getter<Inventory, InventoryPickupItemEvent>() {
                            @Override
                            public Inventory get(InventoryPickupItemEvent e) {
                                return e.getInventory();
                            }
                        }, 0);
                EventValues.registerEventValue(InventoryPickupItemEvent.class, ItemStack.class,
                        new Getter<ItemStack, InventoryPickupItemEvent>() {
                            @Override
                            public ItemStack get(InventoryPickupItemEvent e) {
                                return e.getItem().getItemStack();
                            }
                        }, 0);
                EventValues.registerEventValue(InventoryPickupItemEvent.class, Location.class,
                        new Getter<Location, InventoryPickupItemEvent>() {
                            @Override
                            public Location get(InventoryPickupItemEvent e) {
                                InventoryHolder inv = e.getInventory().getHolder();
                                Location loc = null;
                                if (inv instanceof Hopper) {
                                    loc = ((Hopper) inv).getLocation();
                                } else if (inv instanceof HopperMinecart) {
                                    loc = ((HopperMinecart) inv).getLocation();
                                }
                                return loc;
                            }
                        }, 0);
                if (Bukkit.getPluginManager().getPlugin("Umbaska") != null) {
                    Skript.registerEvent("Armor Equip", SimpleEvent.class, ArmorEquipEvent.class,
                            "sharpsk [on] (armor|armour) equip");
                    EventValues.registerEventValue(ArmorEquipEvent.class, ItemStack.class,
                            new Getter<ItemStack, ArmorEquipEvent>() {
                                @Override
                                public ItemStack get(ArmorEquipEvent e) {
                                    return e.getItem();
                                }
                            }, 0);
                    Skript.registerEvent("Armor unEquip", SimpleEvent.class, ArmorunEquipEvent.class,
                            "sharpsk [on] (armor|armour) unequip");
                    EventValues.registerEventValue(ArmorunEquipEvent.class, ItemStack.class,
                            new Getter<ItemStack, ArmorunEquipEvent>() {
                                @Override
                                public ItemStack get(ArmorunEquipEvent e) {
                                    return e.getItem();
                                }
                            }, 0);
                } else {
                    Skript.registerEvent("Armor Equip", SimpleEvent.class, ArmorEquipEvent.class,
                            "[sharpsk] [on] (armor|armour) equip");
                    EventValues.registerEventValue(ArmorEquipEvent.class, ItemStack.class,
                            new Getter<ItemStack, ArmorEquipEvent>() {
                                @Override
                                public ItemStack get(ArmorEquipEvent e) {
                                    return e.getItem();
                                }
                            }, 0);
                    Skript.registerEvent("Armor unEquip", SimpleEvent.class, ArmorunEquipEvent.class,
                            "[sharpsk] [on] (armor|armour) unequip");
                    EventValues.registerEventValue(ArmorunEquipEvent.class, ItemStack.class,
                            new Getter<ItemStack, ArmorunEquipEvent>() {
                                @Override
                                public ItemStack get(ArmorunEquipEvent e) {
                                    return e.getItem();
                                }
                            }, 0);
                }
                Skript.registerEvent("Enderman Teleport", SimpleEvent.class, EntityTeleportEvent.class,
                        "[on] enderman teleport");
                Skript.registerEvent("Extract", SimpleEvent.class, FurnaceExtractEvent.class, "extract");
                EventValues.registerEventValue(FurnaceExtractEvent.class, ItemStack.class,
                        new Getter<ItemStack, FurnaceExtractEvent>() {
                            @Override
                            public ItemStack get(FurnaceExtractEvent e) {
                                return new ItemStack(e.getItemType(), e.getItemAmount());
                            }
                        }, 0);
                Bukkit.getPluginManager().registerEvents(new ArmorEquipListener(), this);
                Bukkit.getPluginManager().registerEvents(new ArmorunEquipListener(), this);
                Skript.registerEvent("Server Command", SimpleEvent.class, ServerCommandEvent.class,
                        "[on] (server|console) command");
                EventValues.registerEventValue(ServerCommandEvent.class, String.class,
                        new Getter<String, ServerCommandEvent>() {
                            @Override
                            public String get(ServerCommandEvent e) {
                                return e.getCommand();
                            }
                        }, 0);
                Skript.registerEvent("Remote Server Command", SimpleEvent.class, RemoteServerCommandEvent.class,
                        "[on] (remote|rcon) (server|console) command");
                EventValues.registerEventValue(RemoteServerCommandEvent.class, String.class,
                        new Getter<String, RemoteServerCommandEvent>() {
                            @Override
                            public String get(RemoteServerCommandEvent e) {
                                return e.getCommand();
                            }
                        }, 0);
                new ExpChangeListener(this);
                Skript.registerEvent("Experience Change", SimpleEvent.class, EvtExpChange.class,
                        "[on] exp[erience] change");
                EventValues.registerEventValue(EvtExpChange.class, Player.class, new Getter<Player, EvtExpChange>() {
                    @Override
                    public Player get(EvtExpChange e) {
                        return e.getPlayer();
                    }
                }, 0);
                EventValues.registerEventValue(EvtExpChange.class, Number.class, new Getter<Number, EvtExpChange>() {
                    @Override
                    public Number get(EvtExpChange e) {
                        return e.getExp();
                    }
                }, 0);
                Skript.registerEffect(EffBrewerInv.class, "open brewer inventory to %player% [with name %-string%]");
                Skript.registerEffect(EffHopperInv.class, "open hopper inventory to %player% [with name %-string%]");
                Skript.registerCondition(CondPlayerIsStandingOn.class, "%entity% is standing on %itemstack%");
                Skript.registerCondition(CondNotPlayerStandingOn.class, "%entity% is not standing on %itemstack%");
                Skript.registerCondition(CondIsLeashed.class, "%entity% is leashed");
                Skript.registerCondition(CondNotLeashed.class, "%entity% is not leashed");
                // These are already included Skript, also in Skript it's not possible to listen cancelled events. (unless you use sk-mirror)
                //Skript.registerCondition(CondEventCancelled.class, "[the] event (is|was) cancelled");
                //Skript.registerCondition(CondEventNotCancelled.class, "[the] event (is|was) not cancelled");
                Skript.registerExpression(ExprInvType.class, String.class, ExpressionType.SIMPLE,
                        "%player%['s] [current] inventory type");
                Skript.registerEvent("World Change", SimpleEvent.class, PlayerChangedWorldEvent.class, "world change");
                EventValues.registerEventValue(PlayerChangedWorldEvent.class, Player.class,
                        new Getter<Player, PlayerChangedWorldEvent>() {
                            @Override
                            public Player get(PlayerChangedWorldEvent e) {
                                return e.getPlayer();
                            }
                        }, 0);
                Skript.registerExpression(ExprEventWorld.class, World.class, ExpressionType.SIMPLE,
                        "[the] [(-1¦(past|former)|1¦future)] [event-]world");
                Skript.registerExpression(ExprEventWorldLoc.class, Location.class, ExpressionType.SIMPLE,
                        "[the] [(-1¦(past|former)|1¦future)] event-location");

                try {
                    if (Skript.isRunningMinecraft(1, 9)) {
                        getLogger().info("MC 1.9+ (MC " + Skript.getMinecraftVersion().getMajor() + "." + Skript.getMinecraftVersion().getMinor() + ") Server detected! Registering some MC 1.9 related stuff..");

                        Skript.registerEvent("Anvil Prepare", SimpleEvent.class, PrepareAnvilEvent.class, "anvil prepare");
                        EventValues.registerEventValue(PrepareAnvilEvent.class, Player.class,
                                new Getter<Player, PrepareAnvilEvent>() {
                                    @Override
                                    @Nullable
                                    public Player get(PrepareAnvilEvent e) {

                                        return Bukkit.getPlayer(e.getView().getPlayer().getName());
                                    }
                                }, 0);
                        EventValues.registerEventValue(PrepareAnvilEvent.class, ItemStack.class,
                                new Getter<ItemStack, PrepareAnvilEvent>() {
                                    @Override
                                    @Nullable
                                    public ItemStack get(PrepareAnvilEvent e) {
                                        return e.getInventory().getItem(0);
                                    }
                                }, 0);
                        Classes.registerClass(new ClassInfo<>(Phase.class, "phase").parser(new Parser<Phase>() {
                            @Override
                            public String getVariableNamePattern() {
                                return ".+";
                            }

                            @Override
                            @Nullable
                            public Phase parse(String s, ParseContext cont) {
                                try {
                                    return Phase.valueOf(s.replace(" ", "_").trim().toUpperCase());
                                } catch (IllegalArgumentException e) {
                                    return null;
                                }
                            }

                            @Override
                            public String toString(Phase eff, int i) {
                                return eff.name().replace("_", " ").toUpperCase();
                            }

                            @Override
                            public String toVariableNameString(Phase eff) {
                                return eff.name().replace("_", " ").toUpperCase();
                            }

                        }));
                        try {
                            Skript.registerExpression(ExprEventAnvilCost.class, Number.class, ExpressionType.SIMPLE,
                                    "(anvil[]cost|event-[anvil]cost)");
                        } catch (Exception ignored) {

                        }
                        Skript.registerExpression(ExprPhaseOf.class, Phase.class, ExpressionType.SIMPLE,
                                "phase of [ender]dragon in %world%");
                        Skript.registerEvent("Dragon Phase Change", SimpleEvent.class, EnderDragonChangePhaseEvent.class,
                                "[ender]dragon phase change");
                        EventValues.registerEventValue(EnderDragonChangePhaseEvent.class, Phase.class,
                                new Getter<Phase, EnderDragonChangePhaseEvent>() {
                                    @Override
                                    @Nullable
                                    public Phase get(EnderDragonChangePhaseEvent e) {
                                        return e.getNewPhase();
                                    }
                                }, 0);
                        Skript.registerExpression(ExprGlowingStateEntity.class, Boolean.class, ExpressionType.SIMPLE,
                                "[sharpsk] glowing state of %entity%");
                        Skript.registerExpression(ExprOffhandItem.class, ItemStack.class, ExpressionType.PROPERTY,
                                "[sharpsk] [item in] %player%'s offhand");
                    }
                } catch (final NoSuchMethodError e) {
                    // Skript < 2.2, assume no 1.9+
                }

                // PirateSK Syntaxes
                // -------------------
                if (Bukkit.getServer().getPluginManager().getPlugin("PirateSK") != null) {
                    Skript.registerEffect(EffLoadPlugin.class, "[sharpsk] load plugin %string%");
                    Skript.registerEffect(EffEnablePlugin.class, "[sharpsk] enable plugin %string%");
                    Skript.registerEffect(EffDisablePlugin.class, "[sharpsk] disable plugin %string%");

                    Skript.registerEffect(EffSaveWorlds.class, "[sharpsk] save %worlds%");
                } else {
                    Skript.registerEffect(EffLoadPlugin.class, "load plugin %string%");
                    Skript.registerEffect(EffEnablePlugin.class, "enable plugin %string%");
                    Skript.registerEffect(EffDisablePlugin.class, "disable plugin %string%");

                    Skript.registerEffect(EffSaveWorlds.class, "save %worlds%");
                }

                // -------------------

                // Plugin Hook registration

                hman.RegisterHooks();

                // Timers
                Skript.registerEffect(EffTimerCreate.class,
                        "create (-1¦timer|1¦timer in ticks) %string% for %timespan% [keep active %-boolean%] [[with] (interval|delay) %-timespan% [between ticks]]");
                Skript.registerEffect(EffTimerStop.class, "stop timer %string%");
                Skript.registerEffect(EffTimerPause.class, "pause timer %string%");
                Skript.registerEffect(EffTimerResume.class, "resume timer %string%");
                Skript.registerExpression(ExprTimerTime.class, Integer.class, ExpressionType.SIMPLE,
                        "time of timer %string%");
                Skript.registerExpression(ExprAllTimers.class, String.class, ExpressionType.SIMPLE,
                        "[(the|all)] [of] [the] [running] timers");
                Skript.registerCondition(CondTimerActive.class, "timer %string% is active");
                Skript.registerCondition(CondTimerNotActive.class, "timer %string% is not active");
                Skript.registerEvent("Timer Tick", SimpleEvent.class, EvtTimerTick.class, "timer tick");
                EventValues.registerEventValue(EvtTimerTick.class, String.class, new Getter<String, EvtTimerTick>() {
                    @Override
                    @Nullable
                    public String get(EvtTimerTick e) {
                        return e.getTimer();
                    }
                }, 0);
                EventValues.registerEventValue(EvtTimerTick.class, Timespan.class,
                        new Getter<Timespan, EvtTimerTick>() {
                            @Override
                            @Nullable
                            public Timespan get(EvtTimerTick e) {
                                if (e.getTimerType() == 1) {
                                    return Timespan.fromTicks_i(e.getTimeLeft() * 20);
                                } else if (e.getTimerType() == 2) {
                                    return Timespan.fromTicks_i(e.getTimeLeft());
                                }
                                return new Timespan();
                            }
                        }, 0);

                Skript.registerEvent("Timer Complete", SimpleEvent.class, EvtTimerComplete.class, "timer complete");
                EventValues.registerEventValue(EvtTimerComplete.class, String.class,
                        new Getter<String, EvtTimerComplete>() {
                            @Override
                            @Nullable
                            public String get(EvtTimerComplete e) {
                                return e.getTimer();
                            }
                        }, 0);
                Skript.registerExpression(ExprEventTimeLeft.class, Number.class, ExpressionType.SIMPLE,
                        "event-time[left]");

                // --------------------------
                File cache = new File(getDataFolder(), "Tcache.yml");
                File Tickcache = new File(getDataFolder(), "TTickcache.yml");
                if (cache.exists()) {
                    getLogger().info("Resuming active timers from cache...");
                    try {

                        YamlConfiguration Tcache = YamlConfiguration.loadConfiguration(cache);
                        Map<String, Object> b = Tcache.getConfigurationSection("timers").getValues(false);
                        for (Map.Entry<?, Object> a : b.entrySet()) {
                            if ((int) a.getValue() > 0) {
                                CTimerThread th = new CTimerThread((String) a.getKey(), (int) a.getValue(), true, 0);
                                th.instance().start();
                            }
                        }
                        cache.delete();
                    } catch (NullPointerException ignored) {

                    }
                }
                if (Tickcache.exists()) {
                    getLogger().info("Resuming active tick timers...");
                    try {

                        YamlConfiguration TTickcache1 = YamlConfiguration.loadConfiguration(Tickcache);
                        Map<String, Object> b = TTickcache1.getConfigurationSection("timers").getValues(false);
                        for (Map.Entry<?, Object> a : b.entrySet()) {
                            if ((int) a.getValue() > 0) {
                                CTickTimerThread th = new CTickTimerThread((String) a.getKey(), (int) a.getValue(),
                                        true, 0);
                                th.instance().start();
                            }
                        }
                        Tickcache.delete();
                    } catch (NullPointerException ignored) {

                    }
                }
                getLogger().info("Loading Complete!");

            } catch (SkriptAPIException ex) {
                getLogger().warning("Error: Unable to register the addon and the features");
                getLogger().warning("Error: Skript is not allowing registerations.");
                Bukkit.getPluginManager().disablePlugin(this);
            }
        } else if (!Bukkit.getPluginManager().isPluginEnabled(skript)) {
            getLogger().info("Error Skript was not found or enabled. Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        ArrayList<String> atimers = new ArrayList<>();
        for (Thread t1 : Thread.getAllStackTraces().keySet()) {
            if (t1 instanceof CTimerThread) {
                if (((CTimerThread) t1).instance().isActive()) {
                    atimers.add(t1.getName());
                }

            }
        }

        if (!atimers.isEmpty()) {

            getLogger().info("Saving data for active timers...");
            File cache = new File(getDataFolder(), "Tcache.yml");

            if (!cache.exists()) {
                try {
                    cache.createNewFile();
                } catch (IOException ignored) {

                }
            }
            YamlConfiguration Tcache = YamlConfiguration.loadConfiguration(cache);
            Map<String, Integer> timers = new HashMap<>();

            for (Thread t2 : Thread.getAllStackTraces().keySet()) {
                if (t2 instanceof CTimerThread) {
                    if (((CTimerThread) t2).instance().isActive()) {
                        timers.put(t2.getName(), ((CTimerThread) t2).getTime());
                    }

                }
            }

            Tcache.createSection("timers", timers);
            Tcache.getMapList("timers").add(timers);
            try {
                Tcache.save(cache);
            } catch (IOException ignored) {

            }
        }
        getLogger().info("Successfully disabled.");
    }
}