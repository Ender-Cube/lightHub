package me.zax71.lightHub.listeners

import me.zax71.lightHub.instances.DefaultInstance
import me.zax71.lightHub.inventories.GameMenu
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

class PlayerLogin : EventListener<PlayerLoginEvent> {
    /*
    Use to pass variables in


    private final Instance lobby;
    public PlayerLogin(Instance lobby){
        this.playerManager = manager;
        this.lobby = lobby;
    }
     */
    override fun eventType(): Class<PlayerLoginEvent> {
        return PlayerLoginEvent::class.java
    }

    override fun run(event: PlayerLoginEvent): EventListener.Result {
        // When event is run
        val player = event.player

        // Spawning
        event.setSpawningInstance(DefaultInstance.INSTANCE!!)
        player.respawnPoint = Pos(0.0, -60.0, 0.0)
        player.gameMode = GameMode.CREATIVE

        // Adding compass to inventory
        val inventory = player.inventory
        val compass = ItemStack.builder(Material.COMPASS)
            .displayName(Component.text("Navigator").decoration(TextDecoration.ITALIC, false)).build()
        inventory.setItemStack(4, compass)

        player.inventory.addInventoryCondition { playerInventory, slot, _, inventoryConditionResult ->
            // Moving items is bad
            inventoryConditionResult.isCancel = true

            if (slot == 4) {
                playerInventory.openInventory(GameMenu.inventory!!)
            }

        }
        return EventListener.Result.SUCCESS
    }
}