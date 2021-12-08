package team.unnamed.seating.adapt.v1_12_R1.track;

import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityTrackerEntry;
import org.bukkit.Location;

import java.util.List;

public abstract class AbstractEntityTrackerEntry
        extends EntityTrackerEntry {

    private static final Entity MARKER = new EmptyEntity();
    private static final boolean PAPER;

    static {
        boolean paper;

        try {
            Class.forName("com.destroystokyo.paper.Title");
            paper = true;
        } catch (ClassNotFoundException ignored) {
            paper = false;
        }

        PAPER = paper;
    }

    public AbstractEntityTrackerEntry() {
        super(MARKER, -1, -1, 0, false);
    }

    protected void addToTrackedPlayers(EntityPlayer player) {
        if (PAPER) {
            trackedPlayerMap.put(player, true);
        } else {
            trackedPlayers.add(player);
        }
    }

    @Override
    public void track(List<EntityHuman> list) {
        entityTick();
    }

    @Override
    public void updatePlayer(EntityPlayer player) {
        Location location = getLocation();

        if (!player.x().getPlayerChunkMap().a(
                player, location.getBlockX() >> 4, location.getBlockZ() >> 4
        )) {
            if (trackedPlayers.remove(player)) {
                hide(player);
            }
            return;
        }

        if (!trackedPlayers.contains(player)) {
            addToTrackedPlayers(player);
            show(player);
        }
    }

    @Override
    public void a() {
        trackedPlayers.removeIf(player -> {
            hide(player);
            return true;
        });
    }

    @Override
    public void a(EntityPlayer player) {
        if (trackedPlayers.remove(player)) {
            hide(player);
        }
    }

    protected abstract Location getLocation();

    protected abstract void entityTick();

    protected abstract void show(EntityPlayer player);

    protected abstract void hide(EntityPlayer player);

}
