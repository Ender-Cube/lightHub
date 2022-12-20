package me.zax71.lightHub.inventories;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.TransactionOption;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class gameMenu {



    public static void open(Player player){

        TextComponent inventoryTitle = Component.text("Music Discs", NamedTextColor.WHITE);
        Inventory inventory = new Inventory(InventoryType.CHEST_3_ROW, inventoryTitle);

        // Create list for items
        List<ItemStack> itemStacks = new

        ItemStack GRAY_GLASS = ItemStack.builder(Material.GRAY_STAINED_GLASS_PANE).displayName(Component.text("").decoration(TextDecoration.ITALIC, false)).build();


        // Add dark gray glass to list
        for (int i = 0; i < inventory.getSize(); i++) {
            itemStacks.add(GRAY_GLASS);
        }

        // inventory.addItemStacks(itemStacks, TransactionOption.ALL);

        // Add list to inventory with each item representing one slot
        for (int i = 0; i < itemStacks.size(); i++) {
            inventory.setItemStack(i, itemStacks.get(i));
        }

        inventory.copyContents(itemStacks);

        player.openInventory(inventory);
    }


}
