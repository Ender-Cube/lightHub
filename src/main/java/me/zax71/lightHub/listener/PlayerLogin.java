package me.zax71.lightHub.listener;

import me.zax71.lightHub.instances.DefaultInstance;
import me.zax71.lightHub.inventories.gameMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class PlayerLogin implements EventListener<PlayerLoginEvent> {

    /*
    Use to pass variables in


    private final Instance lobby;
    public PlayerLogin(Instance lobby){
        this.playerManager = manager;
        this.lobby = lobby;
    }
     */


    @Override
    public @NotNull Class<PlayerLoginEvent> eventType() {
        return PlayerLoginEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull PlayerLoginEvent event) {
        // When event is run
        final Player player = event.getPlayer();
        event.setSpawningInstance(DefaultInstance.INSTANCE);
        player.setRespawnPoint(new Pos(0, -60, 0));
        player.setGameMode(GameMode.CREATIVE);

        PlayerInventory inventory = player.getInventory();


        ItemStack COMPASS = ItemStack.builder(Material.COMPASS).displayName(Component.text("Navigator").decoration(TextDecoration.ITALIC, false)).build();

        inventory.setItemStack(4, COMPASS);



        return Result.SUCCESS;
    }
}
