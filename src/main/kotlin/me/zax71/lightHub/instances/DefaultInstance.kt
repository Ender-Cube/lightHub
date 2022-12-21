package me.zax71.lightHub.instances

import me.zax71.lightHub.config
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.Instance
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType
import org.tinylog.Logger
import java.io.File

object DefaultInstance {
    var DEFAULT_DIM: DimensionType? = null
    var INSTANCE: Instance? = null

    init {
        val namespaceID = NamespaceID.from("zax71:dimension")
        DEFAULT_DIM = DimensionType.builder(namespaceID)
            .ambientLight(2f)
            .fixedTime(6000L)
            .minY(-64)
            .height(384)
            .logicalHeight(384)
            .natural(false)
            .skylightEnabled(false)
            .build()

        MinecraftServer.getDimensionTypeManager().addDimension(DEFAULT_DIM!!)

        // Error if world is not found
        val worldPath = config.getOrSetDefault("world.path", "worlds/hub")
        if (!File(worldPath).isDirectory) {
            Logger.error { "Failed to load world at $worldPath" }
        }

        INSTANCE = MinecraftServer
            .getInstanceManager()
            .createInstanceContainer(
                DEFAULT_DIM!!,
                AnvilLoader(worldPath)
            )
        INSTANCE!!.enableAutoChunkLoad(true)
    }
}