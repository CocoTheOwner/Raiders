/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2021 Arcane Arts (Volmit Software)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package nl.codevs.raiders.decree;

import nl.codevs.raiders.decree.exceptions.DecreeException;
import nl.codevs.raiders.decree.exceptions.DecreeWhichException;
import nl.codevs.raiders.decree.handlers.*;
import nl.codevs.raiders.decree.objects.*;
import nl.codevs.raiders.decree.util.AtomicCache;
import nl.codevs.raiders.decree.util.C;
import nl.codevs.raiders.decree.util.KList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public interface DecreeSystem extends CommandExecutor, TabCompleter, Plugin {
    AtomicCache<DecreeVirtualCommand> commandCache = new AtomicCache<>();
    ConcurrentHashMap<String, CompletableFuture<String>> futures = new ConcurrentHashMap<>();
    KList<DecreeParameterHandler<?>> handlers = new KList<>(
            new BlockVectorHandler(),
            new BooleanHandler(),
            new ByteHandler(),
            new DoubleHandler(),
            new FloatHandler(),
            new IntegerHandler(),
            new LongHandler(),
            new PlayerHandler(),
            new ShortHandler(),
            new StringHandler(),
            new VectorHandler(),
            new WorldHandler()
    );

    /**
     * The root class to start command searching from
     */
    DecreeCommandExecutor getRootInstance();

    /**
     * Before you fill out these functions. Read the README.md file in the decree directory.
     *
     * @return The instance of the plugin that is running Decree (literal 'this')
     */
    Plugin instance();

    /**
     * Whether the command system should send sounds
     */
    boolean doCommandSound();

    @EventHandler
    default void on(PlayerCommandPreprocessEvent e)
    {
        String msg = e.getMessage().startsWith("/") ? e.getMessage().substring(1) : e.getMessage();

        if(msg.startsWith("decreefuture "))
        {
            String[] args = msg.split("\\Q \\E");
            CompletableFuture<String> future = futures.get(args[1]);

            if(future != null)
            {
                future.complete(args[2]);
                e.setCancelled(true);
            }
        }
    }

    /**
     * Post a future which assists in figuring out {@link DecreeWhichException}s
     * @param password The password to access this future (appended to the onclick)
     * @param future The future to fulfill
     */
    default void postFuture(String password, CompletableFuture<String> future) {
        futures.put(password, future);
    }

    /**
     * What to do with debug messages
     * @param message The debug message
     */
    default void debug(String message) {
        Bukkit.getConsoleSender().sendMessage(message);
    }

    /**
     * Get the root {@link DecreeVirtualCommand}
     */
    default DecreeVirtualCommand getRoot() {
        return commandCache.aquire(() -> {
            try {
                //return new DecreeCategory(null, getRootInstance(), getRootInstance().getClass().getDeclaredAnnotation(Decree.class));
                return DecreeVirtualCommand.createOrigin(getRootInstance(), getRootInstance().getClass().getDeclaredAnnotation(Decree.class), this);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    default List<String> decreeTabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        KList<String> v = getRoot().invokeTabComplete(new KList<>(args), new DecreeSender(sender, instance(), this));
        v.removeDuplicates();
        return v;
    }

    default boolean decreeCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(instance(), () -> {
            DecreeSender decreeSender = new DecreeSender(sender, instance(), this);
            DecreeContext.touch(decreeSender);

            if (!getRoot().invoke(decreeSender, new KList<>(args), new KList<>())) {
                sender.sendMessage(C.RED + "Unknown Decree Command");
            }
        });
        return true;
    }

    /**
     * Get the handler for the specified type
     *
     * @param type The type to handle
     * @return The corresponding {@link DecreeParameterHandler}, or null
     */
    static DecreeParameterHandler<?> getHandler(Class<?> type) throws DecreeException {
        for (DecreeParameterHandler<?> i : handlers) {
            if (i.supports(type)) {
                return i;
            }
        }
        throw new DecreeException("Unhandled type in Decree Parameter: " + type.getName() + ". This is bad! Contact your admin! (Remove param or add handler)");
    }
}