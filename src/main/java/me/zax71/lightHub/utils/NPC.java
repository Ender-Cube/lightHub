package me.zax71.lightHub.utils;

import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.metadata.PlayerMeta;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.sound.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static me.zax71.lightHub.Main.REDIS;
import static me.zax71.lightHub.Main.logger;

// https://gist.github.com/iam4722202468/36630043ca89e786bb6318e296f822f8
public final class NPC extends EntityCreature {
    private final String name;
    private final PlayerSkin skin;
    private final Consumer<Player> onClick;

    NPC(@NotNull String name, @NotNull PlayerSkin skin, @NotNull Instance instance,
        @NotNull Point spawn, @NotNull Consumer<Player> onClick) {

        super(EntityType.PLAYER);
        this.name = name;
        this.skin = skin;
        this.onClick = onClick;

        final PlayerMeta meta = (PlayerMeta) getEntityMeta();
        meta.setNotifyAboutChanges(false);
        meta.setCapeEnabled(false);
        meta.setJacketEnabled(true);
        meta.setLeftSleeveEnabled(true);
        meta.setRightSleeveEnabled(true);
        meta.setLeftLegEnabled(true);
        meta.setRightLegEnabled(true);
        meta.setHatEnabled(true);
        meta.setNotifyAboutChanges(true);

        addAIGroup(
                List.of(new LookAtPlayerGoal(this)),
                List.of(new ClosestEntityTarget(this, 15, entity -> entity instanceof Player))
        );

        setInstance(instance, spawn);
    }

    public void handle(@NotNull EntityAttackEvent event) {
        if (event.getTarget() != this) return;
        if (!(event.getEntity() instanceof Player player)) return;

        player.playSound(Sound.sound()
                .type(SoundEvent.BLOCK_NOTE_BLOCK_PLING)
                .pitch(2)
                .build(), event.getTarget());
        onClick.accept(player);
    }

    public void handle(@NotNull PlayerEntityInteractEvent event) {
        if (event.getTarget() != this) return;
        if (event.getHand() != Player.Hand.MAIN) return; // Prevent duplicating event

        event.getEntity().playSound(Sound.sound()
                .type(SoundEvent.BLOCK_NOTE_BLOCK_PLING)
                .pitch(2)
                .build(), event.getTarget());
        onClick.accept(event.getEntity());
    }

    @Override
    public void updateNewViewer(@NotNull Player player) {
        // Required to spawn player
        final List<PlayerInfoUpdatePacket.Property> properties = List.of(
                new PlayerInfoUpdatePacket.Property("textures", skin.textures(), skin.signature())
        );
        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER,
                        new PlayerInfoUpdatePacket.Entry(
                                getUuid(), name, properties, false, 0, GameMode.SURVIVAL, null,
                                null)
                )
        );

        super.updateNewViewer(player);
    }

    private static final class LookAtPlayerGoal extends GoalSelector {
        private Entity target;

        public LookAtPlayerGoal(EntityCreature entityCreature) {
            super(entityCreature);
        }

        @Override
        public boolean shouldStart() {
            target = findTarget();
            return target != null;
        }

        @Override
        public void start() {
        }

        @Override
        public void tick(long time) {
            if (entityCreature.getDistanceSquared(target) > 225 ||
                    entityCreature.getInstance() != target.getInstance()) {
                target = null;
                return;
            }

            entityCreature.lookAt(target);
        }

        @Override
        public boolean shouldEnd() {
            return target == null;
        }

        @Override
        public void end() {
        }
    }

    private static void sendToMap(Player player, String map) {

        String channel = "endercube/proxy/map/switch";
        Map<String, String> payload = new HashMap<>();
        payload.put("player", player.getUuid().toString());
        payload.put("map", map);

        REDIS.publish(channel, new JSONObject(payload).toString());
        
        logger.info("Sent" + new JSONObject(payload));
        logger.info("Sent " + player.getUsername() + " to " + map);
    }

    public static List<NPC> spawnNPCs(@NotNull Instance instance) {
        return List.of(
                new NPC("Parkour", PlayerSkin.fromUsername("CrimsonIsAkai"), instance, new Pos(0.5, 71, -5.5),
                        player -> sendToMap(player, "easy-1")),

                new NPC("Spleef", PlayerSkin.fromUsername("Notch"), instance, new Pos(-2.5, 71, -5.5),
                        player -> sendToMap(player, "spleef"))
        );
    }
}