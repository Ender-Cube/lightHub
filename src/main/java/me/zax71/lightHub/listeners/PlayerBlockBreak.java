package me.zax71.lightHub.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationNode;

import static me.zax71.lightHub.Main.config;
import static me.zax71.lightHub.Main.configUtils;

/**
 * Stops players from breaking blocks in all worlds
 */
public class PlayerBlockBreak implements EventListener<PlayerBlockBreakEvent> {
    @Override
    public @NotNull Class<PlayerBlockBreakEvent> eventType() {
        return PlayerBlockBreakEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerBlockBreakEvent event) {

        final ConfigurationNode protectionErrorNode = config.node("messages", "protectionError");
        Player player = event.getPlayer();

        // https://github.com/Minestom/Minestom/discussions/1596
        event.setCancelled(true);

        // Send an error message
        Component message = MiniMessage.miniMessage().deserialize(
                configUtils.getOrSetDefault(protectionErrorNode, "<bold><red>Hey!</bold> <grey>Sorry, but you can't break that block here")
        );
        player.sendMessage(message);

        return Result.SUCCESS;
    }
}
