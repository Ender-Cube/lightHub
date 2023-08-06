package me.zax71.lightHub.listeners;

import me.zax71.lightHub.utils.ConfigUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static me.zax71.lightHub.Main.CONFIG;
import static me.zax71.lightHub.utils.ConfigUtils.getOrSetDefault;

public class PlayerMove implements EventListener<PlayerMoveEvent> {
    @Override
    public @NotNull Class<PlayerMoveEvent> eventType() {
        return PlayerMoveEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Teleport the player back up if they go below the death point
        if (player.getPosition().y() < Double.parseDouble(getOrSetDefault(CONFIG.node("deathY"), "10"))) {
            player.teleport(Objects.requireNonNull(ConfigUtils.getPosFromConfig(CONFIG.node("spawnPoint"))));
        }
        return Result.SUCCESS;
    }
}
