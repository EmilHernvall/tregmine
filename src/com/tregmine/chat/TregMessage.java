package com.tregmine.chat;

import info.tregmine.api.TregminePlayer;
import net.minecraft.server.v1_7_R1.ChatSerializer;
import net.minecraft.server.v1_7_R1.NBTTagCompound;
import net.minecraft.server.v1_7_R1.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class TregMessage {

    private final List<MessagePart> messageParts;
    private String jsonString;
    private boolean dirty;

    public TregMessage(final String firstPartText)
    {
        messageParts = new ArrayList<MessagePart>();
        messageParts.add(new MessagePart(firstPartText));
        jsonString = null;
        dirty = false;
    }

    public TregMessage color(final ChatColor color)
    {
        if (!color.isColor()) {
            throw new IllegalArgumentException(color.name() + " is not a color");
        }

        latest().color = color;
        dirty = true;

        return this;
    }

    public TregMessage style(final ChatColor... styles)
    {
        for (final ChatColor style : styles) {
            if (!style.isFormat()) {
                throw new IllegalArgumentException(style.name() + " is not a style");
            }
        }

        latest().styles = styles;
        dirty = true;

        return this;
    }

    public TregMessage file(final String path) {
        onClick("open_file", path);
        return this;
    }

    public TregMessage link(final String url) {
        onClick("open_url", url);
        return this;
    }

    public TregMessage suggest(final String command) {
        onClick("suggest_command", command);
        return this;
    }

    public TregMessage command(final String command) {
        onClick("run_command", command);
        return this;
    }

    public TregMessage itemTooltip(final String itemJSON) {
        onHover("show_item", itemJSON);
        return this;
    }

    public TregMessage itemTooltip(final ItemStack itemStack)
    {
        return itemTooltip(CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).toString());
    }

    public TregMessage tooltip(final String text)
    {
        final String[] lines = text.split("\\n");

        if (lines.length <= 1) {
            onHover("show_text", text);
        } else {
            itemTooltip(makeMultilineTooltip(lines));
        }

        return this;
    }

    public TregMessage then(final Object obj)
    {
        messageParts.add(new MessagePart(obj.toString()));
        dirty = true;
        return this;
    }

    public String toJSONString()
    {
        if (!dirty && jsonString != null) {
            return jsonString;
        }

        final JSONStringer json = new JSONStringer();

        try {
            if (messageParts.size() == 1) {
                latest().writeJson(json);
            } else {
                json.object().key("text").value("").key("extra").array();
                for (final MessagePart part : messageParts) {
                    part.writeJson(json);
                }
                json.endArray().endObject();
            }

        } catch (final JSONException e) {
            throw new RuntimeException("invalid message");
        }

        jsonString = json.toString();
        dirty = false;

        return jsonString;
    }

    public void send(Player player)
    {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(toJSONString())));
    }

    public void send(TregminePlayer player)
    {
        this.send(player.getPlayer());
    }

    private MessagePart latest()
    {
        return messageParts.get(messageParts.size() - 1);
    }

    private String makeMultilineTooltip(final String[] lines)
    {
        final JSONStringer json = new JSONStringer();

        try {

            json.object().key("id").value(1);
            json.key("tag").object().key("display").object();
            json.key("Name").value("\\u00A7f" + lines[0].replace("\"", "\\\""));
            json.key("Lore").array();

            for (int i = 1; i < lines.length; i++) {
                final String line = lines[i];
                json.value(line.isEmpty() ? " " : line.replace("\"", "\\\""));
            }

            json.endArray().endObject().endObject().endObject();

        } catch (final JSONException e) {
            throw new RuntimeException("invalid tooltip");
        }

        return json.toString();
    }

    private void onClick(final String name, final String data)
    {
        final MessagePart latest = latest();
        latest.clickActionName = name;
        latest.clickActionData = data;
        dirty = true;
    }

    private void onHover(final String name, final String data)
    {
        final MessagePart latest = latest();
        latest.hoverActionName = name;
        latest.hoverActionData = data;
        dirty = true;
    }

}
