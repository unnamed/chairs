package team.unnamed.chairs.adapt.intercept;

import org.bukkit.entity.Player;

public interface PacketInterceptorAssigner {

    void assignToPlayer(Player player, String channelName);

}
