package me.zax71.lightHub.inventories;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.*;
import net.minestom.server.sound.SoundEvent;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.*;

import static me.zax71.lightHub.Main.REDIS;
import static me.zax71.lightHub.Main.logger;

public class MapInventory {

    // private static MapInventory INSTANCE;
    private final Inventory inventory;
    private final int[] mapSlots = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    public MapInventory() {
        Inventory inventory = new Inventory(InventoryType.CHEST_5_ROW, "Select a map");


        // Create an ItemStack the size of the Inventory and fill it with black stained glass panes
        ItemStack[] itemStacks = new ItemStack[inventory.getSize()];
        Arrays.fill(itemStacks, ItemStack.builder(Material.BLACK_STAINED_GLASS_PANE).build());

        // Add the top buttons
        itemStacks[3] = ItemStack.of(Material.GREEN_CONCRETE)
                .withDisplayName(
                        Component.text("Easy Maps")
                                .color(NamedTextColor.GREEN)
                );

        itemStacks[4] = ItemStack.of(Material.ORANGE_CONCRETE)
                .withDisplayName(
                        Component.text("Medium Maps")
                                .color(NamedTextColor.GREEN)
                );

        itemStacks[5] = ItemStack.of(Material.RED_CONCRETE)
                .withDisplayName(
                        Component.text("Hard Maps")
                                .color(NamedTextColor.GREEN)
                );


        // put the contents of the itemStack in to the inventory
        inventory.copyContents(itemStacks);

        // Add the inventory condition for all the events of this inventory
        inventory.addInventoryCondition(this::inventoryCondition);

        this.inventory = inventory;

        // Set the state to easy by default
        setState("easy", null);
    }

    private void inventoryCondition(Player player, int slot, ClickType clickType, InventoryConditionResult inventoryConditionResult) {
        // Stop items from being moved around
        inventoryConditionResult.setCancel(true);

        if (inventoryConditionResult.getClickedItem() == ItemStack.AIR) return;

        // Deal with sending a player to a map
        if (Arrays.stream(mapSlots).anyMatch(i -> i == slot)) {
            String map = inventory.getItemStack(slot).getTag(Tag.String("map"));
            sendToMap(player, map);
            player.sendMessage("Sending you to " + map);

        }

        switch (slot) {
            case 3 -> setState("easy", player);
            case 4 -> setState("medium", player);
            case 5 -> setState("hard", player);
        }
    }

    private ItemStack easyMap(int i) {
        List<ItemStack> maps = getParkourMapsFromRedis()
                .stream()
                .filter(obj -> Objects.equals(obj.get("difficulty"), "easy"))
                .map(obj -> ItemStack
                        .of(Objects.requireNonNull(Material.fromNamespaceId(obj.get("materialType"))))
                        .withDisplayName(MiniMessage.miniMessage().deserialize(obj.get("materialDisplayName")))
                        .withTag(Tag.String("map"), obj.get("name"))
                )
                .toList();

        // Return the ItemStack if i is in bounds, else return AIR
        if (i < maps.size()) {
            return maps.get(i);
        } else {
            return ItemStack.AIR;
        }
    }

    private ItemStack mediumMap(int i) {
        List<ItemStack> maps = getParkourMapsFromRedis()
                .stream()
                .filter(obj -> Objects.equals(obj.get("difficulty"), "medium"))
                .map(obj -> ItemStack
                        .of(Objects.requireNonNull(Material.fromNamespaceId(obj.get("materialType"))))
                        .withDisplayName(MiniMessage.miniMessage().deserialize(obj.get("materialDisplayName")))
                        .withTag(Tag.String("map"), obj.get("name"))
                )
                .toList();

        // Return the ItemStack if i is in bounds, else return AIR
        if (i < maps.size()) {
            return maps.get(i);
        } else {
            return ItemStack.AIR;
        }
    }

    private ItemStack hardMap(int i) {
        List<ItemStack> maps = getParkourMapsFromRedis()
                .stream()
                .filter(obj -> Objects.equals(obj.get("difficulty"), "hard"))
                .map(obj -> ItemStack
                        .of(Objects.requireNonNull(Material.fromNamespaceId(obj.get("materialType"))))
                        .withDisplayName(MiniMessage.miniMessage().deserialize(obj.get("materialDisplayName")))
                        .withTag(Tag.String("map"), obj.get("name"))
                )
                .toList();

        // Return the ItemStack if i is in bounds, else return AIR
        if (i < maps.size()) {
            return maps.get(i);
        } else {
            return ItemStack.AIR;
        }
    }

    private void setGlowing(int slot, boolean state) {
        if (state) {
            inventory.setItemStack(slot,
                    inventory
                            .getItemStack(slot)
                            .withMeta(builder -> builder
                                    .enchantment(Enchantment.KNOCKBACK, (short) 1)
                                    .hideFlag(ItemHideFlag.HIDE_ENCHANTS)
                            )
            );
        } else {
            inventory.setItemStack(slot,
                    inventory
                            .getItemStack(slot)
                            .withMeta(ItemMeta.Builder::clearEnchantment)
            );
        }

    }

    private void setState(String state, @Nullable Player player) {


        switch (state) {
            case "easy" -> {
                // Set the buttons to glow appropriately
                setGlowing(3, true);
                setGlowing(4, false);
                setGlowing(5, false);

                // Add the maps to the slots they have a space in
                int mapI = 0;
                for (int slot : mapSlots) {
                    inventory.setItemStack(slot, easyMap(mapI));
                    mapI++;
                }
            }
            case "medium" -> {
                // Set the buttons to glow appropriately
                setGlowing(3, false);
                setGlowing(4, true);
                setGlowing(5, false);

                // Add the maps to the slots they have a space in
                int mapI = 0;
                for (int slot : mapSlots) {
                    inventory.setItemStack(slot, mediumMap(mapI));
                    mapI++;
                }
            }
            case "hard" -> {
                // Set the buttons to glow appropriately
                setGlowing(3, false);
                setGlowing(4, false);
                setGlowing(5, true);

                // Add the maps to the slots they have a space in
                int mapI = 0;
                for (int slot : mapSlots) {
                    inventory.setItemStack(slot, hardMap(mapI));
                    mapI++;
                }
            }
            default -> logger.error("State: " + state + " is not allowed in MapInventory#setState()");
        }

        if (player != null) {
            player.playSound(Sound.sound(
                    SoundEvent.UI_BUTTON_CLICK,
                    Sound.Source.PLAYER,
                    1f,
                    1f)
            );
        }

    }


    private List<HashMap<String, String>> getParkourMapsFromRedis() {
        Type typeToken = new TypeToken<ArrayList<HashMap<String, String>>>() {
        }.getType();

        return new Gson().fromJson(REDIS.get("parkourMaps"), typeToken);
    }

    private void sendToMap(Player player, String mapName) {

        player.playSound(Sound.sound(
                SoundEvent.BLOCK_NOTE_BLOCK_PLING,
                Sound.Source.PLAYER,
                1f,
                1f)
        );

        String channel = "endercube/proxy/map/switch";
        Map<String, String> payload = new HashMap<>();
        payload.put("player", player.getUuid().toString());
        payload.put("map", mapName);

        REDIS.publish(channel, new JSONObject(payload).toString());

        logger.info("Sent" + new JSONObject(payload));
        logger.info("Sent " + player.getUsername() + " to " + mapName);
    }

    public static Inventory getInventory() {
        return new MapInventory().inventory;
    }
}