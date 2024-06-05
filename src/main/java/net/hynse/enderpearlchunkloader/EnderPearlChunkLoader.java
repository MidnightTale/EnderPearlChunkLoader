package net.hynse.enderpearlchunkloader;

import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin;
import me.nahu.scheduler.wrapper.runnable.WrappedRunnable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class EnderPearlChunkLoader extends FoliaWrappedJavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getLogger().info("EnderPearlChunkLoader has been enabled!");
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        getLogger().info("EnderPearlChunkLoader has been disabled!");
    }

    @EventHandler
    public void onEnderPearlThrow(ProjectileLaunchEvent event) {
        Entity projectile = (Entity) event.getEntity();
        if (projectile.getType() == EntityType.ENDER_PEARL) {
            new WrappedRunnable() {
                @Override
                public void run() {
                    if (!projectile.isRemoved() && !projectile.getCommandSenderWorld().getChunkAt(projectile.blockPosition()).getStatus().isEmptyLoadStatus()) {
                        loadChunksAroundLocation(projectile.getCommandSenderWorld(), projectile.getX(), projectile.getZ());
                    } else if (projectile.isRemoved()) {
                        cancel();
                    }
                }
            }.runTaskTimerAtEntity(this, event.getEntity(),1, 1);
        }
    }

    private void loadChunksAroundLocation(Level world, double x, double z) {
        int chunkX = (int) x >> 4;
        int chunkZ = (int) z >> 4;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                world.getChunk(chunkX + dx, chunkZ + dz).setLoaded(true); // Load chunk
            }
        }
    }
}
