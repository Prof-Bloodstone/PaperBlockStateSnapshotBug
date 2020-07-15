package dev.bloodstone.bugs.paperblockstatesnapshotbug;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

public final class PaperBlockStateSnapshotBug extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.PLAYER_HEAD) return;
        getServer().broadcastMessage("Checking snapshot...");
        checkTileState(event.getBlock(), true);
        getServer().broadcastMessage("Checking live state...");
        checkTileState(event.getBlock(), false);
    }

    private void checkTileState(Block block, boolean useSnapshot) {
        TileState state = (TileState) block.getState(useSnapshot);
        PersistentDataContainer pdc = state.getPersistentDataContainer();
        if (pdc == null) {
            getServer().broadcastMessage("PDC is NULL");
        } else {
            getServer().broadcastMessage("PDC is NOT NULL");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
