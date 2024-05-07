package me.joaomanoel.d4rkk.dev.skywars.game.title;

import me.joaomanoel.d4rkk.dev.nms.NMS;
import me.joaomanoel.d4rkk.dev.skywars.Language;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TitleAnimation extends BukkitRunnable {
    private final Player player;
    private final String[] animation;
    private int currentIndex = 0;
    private boolean animationFinished = false;

    public TitleAnimation(Player player, String[] animation) {
        this.player = player;
        this.animation = animation;
    }

    @Override
    public void run() {
        if (!animationFinished) {
            NMS.sendTitle(player, animation[currentIndex], Language.ingame$titles$win$footer, 6, 40, 14); // Aumentamos o tempo de fade in e fade out para 5 ticks

            currentIndex++;
            if (currentIndex >= animation.length) {
                animationFinished = true;
            }
        }
}
}