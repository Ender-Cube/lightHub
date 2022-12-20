package me.zax71.lightHub.blocks

import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID

// SignHandler Class
class Sign : BlockHandler {
    override fun getNamespaceId(): NamespaceID {
        return NamespaceID.from("minecraft", "sign")
    }

    override fun getBlockEntityTags(): Collection<Tag<*>> {
        return listOf<Tag<*>>(
            Tag.Byte("GlowingText"),
            Tag.String("Color"),
            Tag.String("Text1"),
            Tag.String("Text2"),
            Tag.String("Text3"),
            Tag.String("Text4")
        )
    }
}