package me.zax71.lightHub.inventories

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.minestom.server.inventory.Inventory
import net.minestom.server.inventory.InventoryType
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material

object GameMenu {

    var inventoryTitle: Component? = null
    var inventory: Inventory? = null

    init {
        inventoryTitle = Component.text("Game Menu", NamedTextColor.GRAY)
        inventory = Inventory(InventoryType.CHEST_3_ROW, inventoryTitle!!)

        val grayGlass = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE)
            .displayName(Component.text("").decoration(TextDecoration.ITALIC, false)).build()

        // Create list for items
        val itemStacks = Array(inventory!!.size) { grayGlass }

        itemStacks[22] = ItemStack.builder(Material.BARRIER)
            .displayName(
                Component.text("Close", NamedTextColor.RED).decoration(TextDecoration.ITALIC, false)
            )
            .build()

        // Add items to inv
        inventory!!.copyContents(itemStacks)
    }
}