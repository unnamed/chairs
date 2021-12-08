package team.unnamed.seating.adapt.v1_12_R1.seat;

import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class PacketMountSerializer extends PacketDataSerializer {

    private final int entityId;
    private final int ownerId;

    public PacketMountSerializer(int entityId, int ownerId) {
        super(null);
        this.entityId = entityId;
        this.ownerId = ownerId;
    }

    @Override
    public int g() {
        return entityId;
    }

    @Override
    public int[] b() {
        return new int[] { ownerId };
    }

}
