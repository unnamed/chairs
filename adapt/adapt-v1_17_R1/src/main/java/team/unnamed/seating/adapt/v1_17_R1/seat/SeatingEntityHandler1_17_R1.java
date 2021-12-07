package team.unnamed.seating.adapt.v1_17_R1.seat;

import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerChunkMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import team.unnamed.seating.adapt.seat.SeatingEntityHandler;
import team.unnamed.seating.data.ChairSeatingData;
import team.unnamed.seating.message.MessageHandler;

import static team.unnamed.seating.adapt.v1_17_R1.track.EntityTrackerAccessor.addEntry;
import static team.unnamed.seating.adapt.v1_17_R1.track.EntityTrackerAccessor.removeEntry;

public record SeatingEntityHandler1_17_R1(
        MessageHandler messageHandler) implements SeatingEntityHandler {

    @Override
    public void calculateBaseLocation(Player owner, Block block, ChairSeatingData.Builder builder) {
        Material material = block.getType();
        Location location = block.getLocation();
        float yaw = owner.getLocation().getYaw();
        double incrementX = 0.5;
        double incrementZ = 0.5;

        BlockData blockData = block.getBlockData();
        ChairSeatingData.ChairType chairType;

        if (blockData instanceof Stairs stairs) {
            if (stairs.getHalf() == Bisected.Half.TOP) {
                return;
            }

            chairType = ChairSeatingData.ChairType.STAIR;
            switch (stairs.getFacing().getOppositeFace()) {
                case EAST -> {
                    yaw = -90;
                    incrementX = 0.8;
                }
                case WEST -> {
                    yaw = 90;
                    incrementX = 0.2;
                }
                case NORTH -> {
                    yaw = -180;
                    incrementZ = 0.2;
                }
                case SOUTH -> {
                    yaw = 0;
                    incrementZ = 0.8;
                }
            }
        } else if (blockData instanceof Slab slab) {
            if (slab.getType() == Slab.Type.TOP) {
                return;
            }
            chairType = ChairSeatingData.ChairType.SLAB;
        } else if (material.name().contains("CARPET")) {
            chairType = ChairSeatingData.ChairType.CARPET;
        } else {
            chairType = ChairSeatingData.ChairType.BLOCK;
        }

        Location ownerLocation = owner.getLocation();
        ownerLocation.setYaw(yaw);
        owner.teleport(ownerLocation);
        location.add(incrementX, 0, incrementZ);
        location.setYaw(yaw);
        builder.setLocation(location)
                .setBlockType(material)
                .setChairType(chairType);
    }

    @Override
    public void sit(Player player, ChairSeatingData seatingData) {
        int entityId = SeatUtils.generateId(seatingData);
        seatingData.setSpigotId(entityId);

        Location seatLocation = seatingData.getLocation();
        PlayerChunkMap.EntityTracker entityTracker = addEntry(
                seatLocation, entityId,
                (worldServer, entity, players) -> new ChairEntityTrackerEntry(
                        worldServer, entity, players, seatingData
                )
        );

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityTracker.updatePlayer(entityPlayer);
        messageHandler.sendDismountMessage(player);
    }

    @Override
    public void destroySit(ChairSeatingData seatingData) {
        removeEntry(seatingData.getLocation(), seatingData.getSpigotId());
    }

    @Override
    public void lay(Player player) {
//        Location location = player.getLocation();
//        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
//
//        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), entityPlayer.displayName);
//        PropertyMap propertyMap = gameProfile.getProperties();
//        propertyMap.removeAll("textures");
//        propertyMap.putAll("textures", entityPlayer.getProfile().getProperties().get("textures"));
//
//        WorldServer worldServer = entityPlayer.getWorldServer();
//        EntityPlayer fakePlayer = new EntityPlayer(
//                entityPlayer.c, worldServer, gameProfile
//        );
//
//        fakePlayer.setLocation(location.getX(), location.getY(), location.getZ(), 180, 0);
//        PacketPlayOutNamedEntitySpawn namedEntitySpawnPacket
//                = new PacketPlayOutNamedEntitySpawn(fakePlayer);
//
//        PlayerConnection playerConnection = entityPlayer.b;
//        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
//                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, fakePlayer
//        ));
//        playerConnection.sendPacket(namedEntitySpawnPacket);
//
//        fakePlayer.setPose(EntityPose.c);
//        playerConnection.sendPacket(new PacketPlayOutEntityMetadata(
//                fakePlayer.getId(), fakePlayer.getDataWatcher(), false));
    }

}
