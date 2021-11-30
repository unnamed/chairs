package team.unnamed.seating.adapt.entity;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import team.unnamed.seating.data.ChairSeatingData;

public interface SeatingEntityHandler {

    void calculateBaseLocation(Player owner, Block block, ChairSeatingData.Builder builder);

    void sit(Player player, ChairSeatingData seatingData);

    void destroySit(ChairSeatingData seatingData);

    void sendDismountActionbar(Player player);

    void lay(Player player);

}
