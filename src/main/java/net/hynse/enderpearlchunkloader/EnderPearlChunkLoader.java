package net.hynse.enderpearlchunkloader;

import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin;
import me.nahu.scheduler.wrapper.runnable.WrappedRunnable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class EnderPearlChunkLoader extends FoliaWrappedJavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    @EventHandler
    public void onEnderPearlThrow(ProjectileLaunchEvent event) {
        Entity projectile = event.getEntity();
        if (projectile.getType() == EntityType.ENDER_PEARL) {
            new WrappedRunnable() {
                @Override
                public void run() {
                    if (!projectile.isDead() && !projectile.getLocation().getChunk().isLoaded()) {
                        loadChunksAroundLocation(projectile.getLocation());
                    } else if (projectile.isDead()) {
                        cancel();
                    }
                }
            }.runTaskTimerAtEntity(this, event.getEntity(), 1, 1);
        }
    }

    private void loadChunksAroundLocation(Location location) {
        World world = location.getWorld();
        int chunkX = location.getBlockX() >> 4;
        int chunkZ = location.getBlockZ() >> 4;
        for (int x = chunkX - 1; x <= chunkX + 1; x++) {
            for (int z = chunkZ - 1; z <= chunkZ + 1; z++) {
                world.getChunkAt(x, z).load(true); // Load chunk with force option
            }
        }
    }
}
