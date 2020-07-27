package dev.bloodstone.bugs.paperblockstatesnapshotbug;

import javafx.util.Pair;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class PaperBlockStateSnapshotBug extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.PLAYER_HEAD) return;
        PersistentDataContainer snapshotPDC = checkTileState(event.getBlock(), true);
        PersistentDataContainer livePDC = checkTileState(event.getBlock(), false);
        if (snapshotPDC.equals(livePDC)) {
            getServer().broadcastMessage("PDCs are not equal!");
        } else getServer().broadcastMessage("PDCs are equal!");
        for (Pair<String, PersistentDataContainer> p: Arrays.asList(
                new Pair<>("Snapshot", snapshotPDC),
                new Pair<>("Live", livePDC)
        )) {
            String name = p.getKey();
            PersistentDataContainer pdc = p.getValue();
            if (pdc == null) {
                getServer().broadcastMessage(name + " PDC is NULL!");
            } else if (pdc.isEmpty()) {
                getServer().broadcastMessage(name + " is empty!");
            } else {
                getServer().broadcastMessage(name + " keys:");
                pdc.getKeys().forEach( it ->getServer().broadcastMessage(it.toString()));
            }
        }
    }

    private PersistentDataContainer checkTileState(Block block, boolean useSnapshot) {
        TileState state = (TileState) block.getState(useSnapshot);
        getServer().broadcastMessage(String.format("Checking %s state...", (useSnapshot ? "snapshot" : "live")));
        PersistentDataContainer pdc = state.getPersistentDataContainer();
        if (pdc == null) {
            getServer().broadcastMessage("PDC is NULL");
        } else {
            getServer().broadcastMessage("PDC is NOT NULL");
        }
        return pdc;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
