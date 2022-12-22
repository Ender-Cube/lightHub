package me.zax71.lightHub.listeners

import me.zax71.lightHub.inventories.GameMenu
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerUseItemEvent

class PlayerUseItem : EventListener<PlayerUseItemEvent> {
    override fun eventType(): Class<PlayerUseItemEvent> {
        return PlayerUseItemEvent::class.java
    }

    override fun run(event: PlayerUseItemEvent): EventListener.Result {
        val player = event.player
        player.openInventory(GameMenu.inventory!!)

        return EventListener.Result.SUCCESS
    }
}