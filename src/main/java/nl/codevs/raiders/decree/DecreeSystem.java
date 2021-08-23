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



import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import nl.codevs.raiders.Raiders;
import nl.codevs.raiders.decree.util.JarScanner;
import nl.codevs.raiders.decree.util.KList;
import nl.codevs.raiders.decree.util.DecreeSender;
import nl.codevs.raiders.decree.virtual.VirtualDecreeCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public interface DecreeSystem extends CommandExecutor, TabCompleter {
    KList<DecreeParameterHandler<?>> handlers = Iris.initialize("com.volmit.iris.util.decree.handlers", null).convert((i) -> (DecreeParameterHandler<?>) i);

    /**
     * The root class to start command searching from
     *
     * @return
     */
    VirtualDecreeCommand getRoot();

    /**
     * The jar file, common to use getFile() in {@link org.bukkit.plugin.java.JavaPlugin}
     */
    File getJarFile();

    /**
     * The bukkit audiences, commonly {@link BukkitAudiences#create(Plugin)}
     * The entered plugin is the main class as an instance (this)
     */
    BukkitAudiences getAudiences();

    void error(String string);

    void debug(String string);

    void info(String string);

    default boolean call(DecreeSender sender, String[] args) {
        DecreeContext.touch(sender);
        return getRoot().invoke(sender, enhanceArgs(args));
    }

    @Nullable
    @Override
    default List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        KList<String> enhanced = new KList<>(args);
        KList<String> v = getRoot().tabComplete(enhanced, enhanced.toString(" "));
        v.removeDuplicates();
        return v;
    }


    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        J.aBukkit(() -> {
            if (!call(new DecreeSender(sender), args)) {
                sender.sendMessage(C.RED + "Unknown Iris Command");
            }
        });
        return true;
    }

    static KList<String> enhanceArgs(String[] args) {
        return enhanceArgs(args, true);
    }

    static KList<String> enhanceArgs(String[] args, boolean trim) {
        KList<String> a = new KList<>();

        if (args.length == 0) {
            return a;
        }

        StringBuilder flat = new StringBuilder();
        for (String i : args) {
            if (trim) {
                if (i.trim().isEmpty()) {
                    continue;
                }

                flat.append(" ").append(i.trim());
            } else {
                if (i.endsWith(" ")) {
                    flat.append(" ").append(i.trim()).append(" ");
                }
            }
        }

        flat = new StringBuilder(flat.length() > 0 ? trim ? flat.toString().trim().length() > 0 ? flat.substring(1).trim() : flat.toString().trim() : flat.substring(1) : flat);
        StringBuilder arg = new StringBuilder();
        boolean quoting = false;

        for (int x = 0; x < flat.length(); x++) {
            char i = flat.charAt(x);
            char j = x < flat.length() - 1 ? flat.charAt(x + 1) : i;
            boolean hasNext = x < flat.length();

            if (i == ' ' && !quoting) {
                if (!arg.toString().trim().isEmpty() && trim) {
                    a.add(arg.toString().trim());
                    arg = new StringBuilder();
                }
            } else if (i == '"') {
                if (!quoting && (arg.length() == 0)) {
                    quoting = true;
                } else if (quoting) {
                    quoting = false;

                    if (hasNext && j == ' ') {
                        if (!arg.toString().trim().isEmpty() && trim) {
                            a.add(arg.toString().trim());
                            arg = new StringBuilder();
                        }
                    } else if (!hasNext) {
                        if (!arg.toString().trim().isEmpty() && trim) {
                            a.add(arg.toString().trim());
                            arg = new StringBuilder();
                        }
                    }
                }
            } else {
                arg.append(i);
            }
        }

        if (!arg.toString().trim().isEmpty() && trim) {
            a.add(arg.toString().trim());
        }

        return a;
    }

    /**
     * Get the handler for the specified type
     *
     * @param type The type to handle
     * @return The corresponding {@link DecreeParameterHandler}, or null
     */
    static DecreeParameterHandler<?> getHandler(Class<?> type) {
        for (DecreeParameterHandler<?> i : handlers) {
            if (i.supports(type)) {
                return i;
            }
        }
        Iris.error("Unhandled type in Decree Parameter: " + type.getName() + ". This is bad!");
        return null;
    }
}
