package me.zax71.lightHub.listeners;

import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static me.zax71.lightHub.Main.config;
import static me.zax71.lightHub.Main.configUtils;

public class PlayerMove implements EventListener<PlayerMoveEvent> {
    @Override
    public @NotNull Class<PlayerMoveEvent> eventType() {
        return PlayerMoveEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Teleport the player back up if they go below the death point
        if (player.getPosition().y() < Double.parseDouble(configUtils.getOrSetDefault(config.node("deathY"), "10"))) {
            player.teleport(Objects.requireNonNull(configUtils.getPosFromConfig(config.node("spawnPoint"))));
        }
        return Result.SUCCESS;
    }
}
