package me.zax71.lightHub;

import de.leonhard.storage.Config;
import de.leonhard.storage.LightningBuilder;
import de.leonhard.storage.Yaml;

import de.leonhard.storage.internal.settings.ConfigSettings;
import me.zax71.lightHub.blocks.Sign;
import me.zax71.lightHub.instances.DefaultInstance;
import me.zax71.lightHub.listener.PlayerBlockBreak;
import me.zax71.lightHub.listener.PlayerLogin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static de.leonhard.storage.internal.FileType.YAML;

public class Main {

    // Set up config
    public static Yaml config = new Yaml("config.yml", System.getProperty("user.dir"), Main.class.getResourceAsStream("/config.yml"));

    public static void main(String[] args) {
        // Initialisation
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Register events
        {
            GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();

            EventNode<Event> entityNode = EventNode.type("listeners", EventFilter.ALL);
            entityNode
                    .addListener(new PlayerLogin())
                    .addListener(new PlayerBlockBreak());

            globalEventHandler.addChild(entityNode);
        }

        // Register block handler for signs
                MinecraftServer.getBlockManager().registerHandler(NamespaceID.from("minecraft:sign"), Sign::new);

        // Piracy = bad
        MojangAuth.init();



        // Start the server
        minecraftServer.start("0.0.0.0", config.getOrSetDefault("port", 25565));
    }
}