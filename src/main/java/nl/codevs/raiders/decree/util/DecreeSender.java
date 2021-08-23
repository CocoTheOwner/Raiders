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

package nl.codevs.raiders.decree.util;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import nl.codevs.raiders.decree.DecreeParameter;
import nl.codevs.raiders.decree.virtual.VirtualDecreeCommand;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Represents a volume sender. A command sender with extra crap in it
 *
 * @author cyberpwn
 */
public class DecreeSender implements CommandSender {
    private final CommandSender s;
    private String tag;
    int spinh = -20;
    int spins = 7;
    int spinb = 8;

    @Getter
    @Setter
    private String command;

    /**
     * Wrap a command sender
     *
     * @param s the command sender
     */
    public DecreeSender(CommandSender s) {
        tag = "";
        this.s = s;
    }

    public DecreeSender(CommandSender s, String tag) {
        this.tag = tag;
        this.s = s;
    }

    /**
     * Set a command tag (prefix for sendMessage)
     *
     * @param tag the tag
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Get the command tag
     *
     * @return the command tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Is this sender a player?
     *
     * @return true if it is
     */
    public boolean isPlayer() {
        return getS() instanceof Player;
    }

    /**
     * Force cast to player (be sure to check first)
     *
     * @return a casted player
     */
    public Player player() {
        return (Player) getS();
    }

    /**
     * Get the origin sender this object is wrapping
     *
     * @return the command sender
     */
    public CommandSender getS() {
        return s;
    }

    @Override
    public boolean isPermissionSet(String name) {
        return s.isPermissionSet(name);
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        return s.isPermissionSet(perm);
    }

    @Override
    public boolean hasPermission(String name) {
        return s.hasPermission(name);
    }

    @Override
    public boolean hasPermission(Permission perm) {
        return s.hasPermission(perm);
    }


    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return s.addAttachment(plugin, name, value);
    }


    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        return s.addAttachment(plugin);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return s.addAttachment(plugin, name, value, ticks);
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return s.addAttachment(plugin, ticks);
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        s.removeAttachment(attachment);
    }

    @Override
    public void recalculatePermissions() {
        s.recalculatePermissions();
    }


    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return s.getEffectivePermissions();
    }

    @Override
    public boolean isOp() {
        return s.isOp();
    }

    @Override
    public void setOp(boolean value) {
        s.setOp(value);
    }

    public void hr() {
        s.sendMessage("========================================================");
    }

    public void sendTitle(String title, String subtitle, int i, int s, int o) {
        audiences.player(player()).showTitle(Title.title(
                createComponent(title),
                createComponent(subtitle),
                Title.Times.of(Duration.ofMillis(i), Duration.ofMillis(s), Duration.ofMillis(o))));
    }

    public static long getTick() {
        return System.currentTimeMillis() / 16;
    }

    public void sendProgress(double percent, String thing) {
        if (percent < 0) {
            int l = 44;
            int g = (int) (1D * l);
            sendTitle(C.IRIS + thing + " ", 0, 500, 250);
            sendActionNoProcessing("" + "" + pulse("#00ff80", "#00373d", 1D) + "<underlined> " + Form.repeat(" ", g) + "<reset>" + Form.repeat(" ", l - g));
        } else {
            int l = 44;
            int g = (int) (percent * l);
            sendTitle(C.IRIS + thing + " " + C.BLUE + "<font:minecraft:uniform>" + Form.pc(percent, 0), 0, 500, 250);
            sendActionNoProcessing("" + "" + pulse("#00ff80", "#00373d", 1D) + "<underlined> " + Form.repeat(" ", g) + "<reset>" + Form.repeat(" ", l - g));
        }
    }

    public static String pulse(String colorA, String colorB, double speed) {
        return "<gradient:" + colorA + ":" + colorB + ":" + pulse(speed) + ">";
    }

    public static String pulse(double speed) {
        return Form.f(invertSpread((((getTick() * 15D * speed) % 1000D) / 1000D)), 3).replaceAll("\\Q,\\E", ".").replaceAll("\\Q?\\E", "-");
    }

    public static double invertSpread(double v) {
        return ((1D - v) * 2D) - 1D;
    }

    public void sendAction(String action) {
        Iris.audiences.player(player()).sendActionBar(createNoPrefixComponent(action));
    }

    public void sendActionNoProcessing(String action) {
        Iris.audiences.player(player()).sendActionBar(createNoPrefixComponentNoProcessing(action));
    }

    public void sendTitle(String subtitle, int i, int s, int o) {
        Iris.audiences.player(player()).showTitle(Title.title(
                createNoPrefixComponent(" "),
                createNoPrefixComponent(subtitle),
                Title.Times.of(Duration.ofMillis(i), Duration.ofMillis(s), Duration.ofMillis(o))));
    }

    private Component createNoPrefixComponent(String message) {
        String t = C.translateAlternateColorCodes('&', message);
        String a = C.aura(t, spinh, spins, spinb, 0.36);
        return MiniMessage.get().parse(a);
    }

    private Component createNoPrefixComponentNoProcessing(String message) {
        return MiniMessage.get().parse(message);
    }

    private Component createComponent(String message) {
        String t = C.translateAlternateColorCodes('&', getTag() + message);
        String a = C.aura(t, spinh, spins spinb);
        return MiniMessage.get().parse(a);
    }

    private Component createComponentRaw(String message) {
        String t = C.translateAlternateColorCodes('&', getTag() + message);
        return MiniMessage.get().parse(t);
    }

    public <T> void showWaiting(String passive, CompletableFuture<T> f) {
        AtomicInteger v = new AtomicInteger();
        AtomicReference<T> g = new AtomicReference<>();
        v.set(J.ar(() -> {
            if (f.isDone() && g.get() != null) {
                J.car(v.get());
                sendAction(" ");
                return;
            }

            sendProgress(-1, passive);
        }, 0));
        J.a(() -> {
            try {
                g.set(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void sendMessage(String message) {
        if (message.contains("<NOMINI>")) {
            s.sendMessage(message.replaceAll("\\Q<NOMINI>\\E", ""));
            return;
        }

        try {
            Iris.audiences.sender(s).sendMessage(createComponent(message));
        } catch (Throwable e) {
            String t = C.translateAlternateColorCodes('&', getTag() + message);
            String a = C.aura(t, spinh, spins spinb;

            Iris.debug("<NOMINI>Failure to parse " + a);
            s.sendMessage(C.translateAlternateColorCodes('&', getTag() + message));
        }
    }


    public void sendMessageRaw(String message) {
        if (message.contains("<NOMINI>")) {
            s.sendMessage(message.replaceAll("\\Q<NOMINI>\\E", ""));
            return;
        }

        try {
            Iris.audiences.sender(s).sendMessage(createComponentRaw(message));
        } catch (Throwable e) {
            String t = C.translateAlternateColorCodes('&', getTag() + message);
            String a = C.aura(t, spinh, spins, spinb);

            Iris.debug("<NOMINI>Failure to parse " + a);
            s.sendMessage(C.translateAlternateColorCodes('&', getTag() + message));
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String str : messages)
            sendMessage(str);
    }

    @Override
    public void sendMessage(UUID uuid, String message) {
        sendMessage(message);
    }

    @Override
    public void sendMessage(UUID uuid, String[] messages) {
        sendMessage(messages);
    }


    @Override
    public Server getServer() {
        return s.getServer();
    }


    @Override
    public String getName() {
        return s.getName();
    }


    @Override
    public Spigot spigot() {
        return s.spigot();
    }

    private String pickRandoms(int max, VirtualDecreeCommand i) {
        KList<String> m = new KList<>();
        for (int ix = 0; ix < max; ix++) {
            m.add((i.isNode()
                    ? (i.getNode().getParameters().isNotEmpty())
                    ? "<#aebef2>✦ <#5ef288>"
                    + i.getParentPath()
                    + " <#42ecf5>"
                    + i.getName() + " "
                    + i.getNode().getParameters().shuffleCopy(RNG.r).convert((f)
                            -> (f.isRequired() || RNG.r.b(0.5)
                            ? "<#f2e15e>" + f.getNames().getRandom() + "="
                            + "<#d665f0>" + f.example()
                            : ""))
                    .toString(" ")
                    : ""
                    : ""));
        }

        return m.removeDuplicates().convert((iff) -> iff.replaceAll("\\Q  \\E", " ")).toString("\n");
    }


    public void sendHeader(String name, int overrideLength) {
        int len = overrideLength;
        int h = name.length() + 2;
        String s = Form.repeat(" ", len - h - 4);
        String si = Form.repeat("(", 3);
        String so = Form.repeat(")", 3);
        String sf = "[";
        String se = "]";

        if (name.trim().isEmpty()) {
            sendMessageRaw("<font:minecraft:uniform><strikethrough><gradient:#34eb6b:#32bfad>" + sf + s + "<reset><font:minecraft:uniform><strikethrough><gradient:#32bfad:#34eb6b>" + s + se);
        } else {
            sendMessageRaw("<font:minecraft:uniform><strikethrough><gradient:#34eb6b:#32bfad>" + sf + s + si + "<reset> <gradient:#3299bf:#323bbf>" + name + "<reset> <font:minecraft:uniform><strikethrough><gradient:#32bfad:#34eb6b>" + so + s + se);
        }
    }

    public void sendHeader(String name) {
        sendHeader(name, 46);
    }

    public void sendDecreeHelp(VirtualDecreeCommand v) {
        int m = v.getNodes().size();

        if (v.getNodes().isNotEmpty()) {
            sendHeader(Form.capitalize(v.getName()) + " Help");
            if (isPlayer() && v.getParent() != null) {
                sendMessageRaw("<hover:show_text:'" + "<#b54b38>Click to go back to <#3299bf>" + Form.capitalize(v.getParent().getName()) + " Help" + "'><click:run_command:" + v.getParent().getPath() + "><font:minecraft:uniform><#f58571>〈 Back</click></hover>");
            }

            for (VirtualDecreeCommand i : v.getNodes()) {
                sendDecreeHelpNode(i);
            }
        } else {
            sendMessage(C.RED + "There are no subcommands in this group! Contact support, this is a command design issue!");
        }
    }

    public void sendDecreeHelpNode(VirtualDecreeCommand i){
        if (isPlayer()) {

            String newline = "<reset>\n";

            /// Command
            // Contains main command & aliases
            String realText = i.getPath() + " >" + "<#46826a>⇀<gradient:#42ecf5:#428df5> " + i.getName();
            String hoverTitle = i.getNames().copy().reverse().convert((f) -> "<#42ecf5>" + f).toString(", ");
            String description = "<#3fe05a>✎ <#6ad97d><font:minecraft:uniform>" + i.getDescription();
            String usage = "<#bbe03f>✒ <#a8e0a2><font:minecraft:uniform>";
            String onClick;
            if (i.isNode()) {
                if (i.getNode().getParameters().isEmpty()){
                    usage += "There are no parameters. Click to run.";
                    onClick = "run_command";
                } else {
                    usage += "Hover over all of the parameters to learn more.";
                    onClick = "suggest_command";
                }
            } else {
                usage += "This is a command category. Click to run.";
                onClick = "run_command";
            }

            String suggestion = "";
            String suggestions = "";
            if (i.isNode() && i.getNode().getParameters().isNotEmpty()) {
                suggestion += newline + "<#aebef2>✦ <#5ef288><font:minecraft:uniform>" + i.getParentPath() + " <#42ecf5>" + i.getName() + " "
                        + i.getNode().getParameters().convert((f) -> "<#d665f0>" + f.example()).toString(" ");
                suggestions += newline + "<font:minecraft:uniform>" + pickRandoms(Math.min(i.getNode().getParameters().size() + 1, 5), i);
            }

            /// Params
            StringBuilder nodes = new StringBuilder();
            if (i.isNode()){
                for (DecreeParameter p : i.getNode().getParameters()) {

                    String nTitle = "<gradient:#d665f0:#a37feb>" + p.getName();
                    String nHoverTitle = p.getNames().convert((ff) -> "<#d665f0>" + ff).toString(", ");
                    String nDescription = "<#3fe05a>✎ <#6ad97d><font:minecraft:uniform>" + p.getDescription();
                    String nUsage;
                    String context = "";
                    if (p.isRequired()){
                        nUsage = "<#db4321>⚠ <#faa796><font:minecraft:uniform>This parameter is required.";
                    } else if (p.hasDefault()) {
                        nUsage = "<#2181db>✔ <#78dcf0><font:minecraft:uniform>Defaults to \""+ p.getParam().defaultValue()+"\" if undefined.";
                    } else {
                        nUsage = "<#a73abd>✔ <#78dcf0><font:minecraft:uniform>This parameter is optional.";
                    }
                    if (p.isContextual()){
                        context = "<#ff9900>➱ <#ffcc00><font:minecraft:uniform>The value may be derived from environment context" + newline;
                    }
                    String type = "<#cc00ff>✢ <#ff33cc><font:minecraft:uniform>This parameter is of type " + p.getType().getSimpleName();
                    String fullTitle;
                    if (p.isRequired()){
                        fullTitle = "<red>[" + nTitle + "<red>] ";
                    } else {
                        fullTitle = "<#4f4f4f>⊰" + nTitle + "<#4f4f4f>⊱";
                    }

                    nodes
                            .append("<hover:show_text:'")
                            .append(nHoverTitle).append(newline)
                            .append(nDescription).append(newline)
                            .append(context)
                            .append(nUsage).append(newline)
                            .append(type)
                            .append("'>")
                            .append(fullTitle)
                            .append("</hover>");
                }
            } else {
                nodes = new StringBuilder("<gradient:#afe3d3:#a2dae0> - Category of Commands");
            }

            /// Wrapper
            String wrapper =
                    "<hover:show_text:'" +
                        hoverTitle + newline +
                        description + newline +
                        usage +
                        suggestion + //Newlines for suggestions are added when they're built, to prevent blanklines.
                        suggestions + // ^
                    "'>" +
                    "<click:" +
                        onClick +
                        ":" +
                        realText +
                    "</click>" +
                    "</hover>" +
                    " " +
                    nodes;

            sendMessageRaw(wrapper);
        } else {
            sendMessage(i.getPath());
        }
    }

    public void playSound(Sound sound, float volume, float pitch) {
        if (isPlayer()) {
            player().playSound(player().getLocation(), sound, volume, pitch);
        }
    }
}