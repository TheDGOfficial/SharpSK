package me.sharpjaws.sharpsk.hooks.AuthmeReloaded;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import fr.xephi.authme.events.*;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public class AuthmeRegistry {

    public static void registerAuthMe() {
        Skript.registerEvent("Authme debug", SimpleEvent.class, CustomEvent.class, "authme debug");
        Skript.registerEvent("Authme login", SimpleEvent.class, LoginEvent.class, "authme login");
        EventValues.registerEventValue(LoginEvent.class, Player.class, new Getter<Player, LoginEvent>() {
            @Override
            @Nullable
            public Player get(LoginEvent e) {
                return e.getPlayer();
            }
        }, 0);
        Skript.registerEvent("Authme logout", SimpleEvent.class, LogoutEvent.class, "authme logout");
        EventValues.registerEventValue(LogoutEvent.class, Player.class, new Getter<Player, LogoutEvent>() {
            @Override
            @Nullable
            public Player get(LogoutEvent e) {

                return e.getPlayer();
            }
        }, 0);
        Skript.registerEvent("Authme teleport", SimpleEvent.class, AuthMeTeleportEvent.class, "authme teleport");
        EventValues.registerEventValue(AuthMeTeleportEvent.class, Player.class,
                new Getter<Player, AuthMeTeleportEvent>() {
                    @Override
                    @Nullable
                    public Player get(AuthMeTeleportEvent e) {
                        return e.getPlayer();
                    }
                }, 0);
        Skript.registerEvent("Authme inventory restore", SimpleEvent.class, RestoreInventoryEvent.class, "authme inventory restore");
        EventValues.registerEventValue(
            RestoreInventoryEvent.class, Player.class,
            new Getter<Player, RestoreInventoryEvent>() {
                @Override
                @Nullable
                public Player get(RestoreInventoryEvent e) {
                    return e.getPlayer();
                }
            }, 0);
        Skript.registerExpression(ExprHashedPasswordOf.class, String.class, ExpressionType.PROPERTY, "[authme] hashed password of %player%");
        Skript.registerEffect(EffAuthForceLogout.class, "[authme] force %player% to log[]out");
        Skript.registerEffect(EffAuthForceLogin.class, "[authme] force %player% to log[]in");
        Skript.registerEffect(EffAuthForceRegister.class, "[authme] force %player% to register with pass[word] %string%");
        Skript.registerEffect(EffAuthForceUnregister.class, "[authme] force %player% to unregister");
        Skript.registerCondition(CondAuthIsRegistered.class, "[authme] %player% is registered");
        Skript.registerCondition(CondAuthIsNotRegistered.class, "[authme] %player% is not registered");
        Skript.registerCondition(CondAuthIsAuth.class, "[authme] %player% is (authenticated|logged [in])");
        Skript.registerCondition(CondAuthIsNotAuth.class, "[authme] %player% is not (authenticated|logged [in])");
    }

}
