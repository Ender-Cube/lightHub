package me.zax71.lightHub.listener;

import me.zax71.lightHub.instances.DefaultInstance;
import me.zax71.lightHub.inventories.gameMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import static me.zax71.lightHub.Main.config;

public class PlayerBlockBreak implements EventListener<PlayerBlockBreakEvent> {



    @Override
    public @NotNull Class<PlayerBlockBreakEvent> eventType() {
        return PlayerBlockBreakEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerBlockBreakEvent event) {

        Instance instance = DefaultInstance.INSTANCE;
        Player player = event.getPlayer();

        // https://github.com/Minestom/Minestom/discussions/1596
        event.setCancelled(true);


        Component message = MiniMessage.miniMessage().deserialize(config.getOrSetDefault("protectionError", "<bold><red>Hey!</bold> <grey>Sorry, but you can't break that block here"));
        player.sendMessage(message);

        gameMenu.open(player);
        return Result.SUCCESS;
    }
}
