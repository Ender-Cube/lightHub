package me.zax71.EndercubeTemplate;

import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.minestom.LiteMinestomFactory;
import me.zax71.EndercubeTemplate.blocks.Sign;
import me.zax71.EndercubeTemplate.blocks.Skull;
import me.zax71.EndercubeTemplate.commands.arguments.PlayerArgument;
import me.zax71.EndercubeTemplate.listeners.PlayerBlockBreak;
import me.zax71.EndercubeTemplate.listeners.PlayerLogin;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.*;
import net.minestom.server.utils.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static me.zax71.EndercubeTemplate.utils.ConfigUtils.*;

public class Main {
    public static CommentedConfigurationNode CONFIG;

    public static HoconConfigurationLoader LOADER;
    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static LiteCommands<CommandSender> liteCommands;


    public static void main(String[] args) {
        initConfig();

        // Server Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Register events
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        EventNode<Event> entityNode = EventNode.type("listeners", EventFilter.ALL);
        entityNode
                .addListener(new PlayerLogin())
                .addListener(new PlayerBlockBreak());
        globalEventHandler.addChild(entityNode);

        // Register block handlers
        MinecraftServer.getBlockManager().registerHandler(NamespaceID.from("minecraft:sign"), Sign::new);
        MinecraftServer.getBlockManager().registerHandler(NamespaceID.from("minecraft:skull"), Skull::new);

        // Offline mode bad
        MojangAuth.init();

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
        initCommands();

    }

    public static Path getPath(String path) {
        try {
            return Path.of(new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getPath()).getParent().resolve(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initCommands() {
        liteCommands = LiteMinestomFactory.builder(MinecraftServer.getServer(), MinecraftServer.getCommandManager())
                // .commandInstance(new Command())
                .argument(Player.class, new PlayerArgument(MinecraftServer.getServer()))
                .register();
    }

}