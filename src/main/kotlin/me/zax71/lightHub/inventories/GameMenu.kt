package me.zax71.lightHub.inventories

import me.zax71.lightHub.config
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.tag.Tag
import org.slf4j.Logger

object GameMenu {

    var inventory: Inventory? = null

    init {
        val inventoryTitle = Component.text("Game Menu", NamedTextColor.GRAY)
        inventory = Inventory(InventoryType.CHEST_3_ROW, inventoryTitle)

        val grayGlass = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
            .displayName(Component.text("").decoration(TextDecoration.ITALIC, false)).build()

        // Create list for items
        val itemStacks = Array(inventory!!.size) { grayGlass }

        itemStacks[22] = ItemStack.builder(Material.BARRIER)
            .displayName(
                Component.text("Close", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
            )
            .build()

        // Get config values and put them in to lists
        for (i in 0..8) {
            val typeConfig = config.getString("gameMenu.items.$i.type")
            val serverConfig = config.getString("gameMenu.items.$i.server")
            val nameConfig = config.getString("gameMenu.items.$i.name")

            // Stay as glass when null
            if (typeConfig == null || typeConfig == "") {
                continue
            }

            // String -> Material
            val typeMaterial = Material.fromNamespaceId(typeConfig.lowercase())
            if (typeMaterial == null) {
                org.tinylog.kotlin.Logger.error {"gameMenu.items.$i.name is $typeConfig and that is not a valid Material"}
                break
            }

            val item = ItemStack.builder(typeMaterial)
                .displayName(MiniMessage.miniMessage().deserialize(nameConfig))
                .set(Tag.String("server"), serverConfig)
                .build()

            itemStacks[i+9] = item

        }

        // Add items to inv
        inventory!!.copyContents(itemStacks)

        inventory!!.addInventoryCondition { player, slot, _, inventoryConditionResult ->
            // Moving items is bad
            inventoryConditionResult.isCancel = true


            if (slot == 22) {
                player.closeInventory()
            }
        }
    }
}