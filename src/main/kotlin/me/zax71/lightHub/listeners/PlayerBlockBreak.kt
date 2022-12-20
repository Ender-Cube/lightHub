package me.zax71.lightHub.listener


import me.zax71.lightHub.config
import me.zax71.lightHub.instances.DefaultInstance
import me.zax71.lightHub.inventories.GameMenu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.event.EventListener
import net.minestom.server.event.player.PlayerBlockBreakEvent
import net.minestom.server.instance.Instance

class PlayerBlockBreak : EventListener<PlayerBlockBreakEvent> {

    override fun eventType(): Class<PlayerBlockBreakEvent> {
        return PlayerBlockBreakEvent::class.java
    }

    override fun run(event: PlayerBlockBreakEvent): EventListener.Result {
        val player = event.player

        // https://github.com/Minestom/Minestom/discussions/1596
        event.isCancelled = true
        val message: Component = MiniMessage.miniMessage().deserialize(
            config.getOrSetDefault(
                "protectionError",
                "<bold><red>Hey!</bold> <grey>Sorry, but you can't break that block here"
            )
        )
        player.sendMessage(message)


        player.openInventory(GameMenu.inventory!!)

        return EventListener.Result.SUCCESS
    }
}