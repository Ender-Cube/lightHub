package me.zax71.lightHub

import de.leonhard.storage.Yaml
import me.zax71.lightHub.blocks.Sign
import me.zax71.lightHub.listener.PlayerBlockBreak
import me.zax71.lightHub.listeners.PlayerLogin
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.extras.MojangAuth
import net.minestom.server.utils.NamespaceID


var config: Yaml = Yaml(
    "config.yml",
    System.getProperty("user.dir"),
    // main()::class.java.getResourceAsStream("/config.yml")
)
fun main() {
    // Initialisation
    val minecraftServer = MinecraftServer.init()

    // Register events
        val globalEventHandler = MinecraftServer.getGlobalEventHandler()
        val entityNode = EventNode.type("listeners", EventFilter.ALL)
        entityNode
            .addListener(PlayerLogin())
            .addListener(PlayerBlockBreak())
        globalEventHandler.addChild(entityNode)


    // Register block handler for signs
    MinecraftServer.getBlockManager().registerHandler(NamespaceID.from("minecraft:sign")) { Sign() }


    // Piracy = bad
    MojangAuth.init()

    // Start the server
    minecraftServer.start("0.0.0.0", config.getOrSetDefault("port", 25565))
}