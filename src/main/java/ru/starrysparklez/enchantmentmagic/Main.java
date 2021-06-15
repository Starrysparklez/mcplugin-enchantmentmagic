package ru.starrysparklez.enchantmentmagic;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    Logger logger;

    public void onEnable() {
        logger = Bukkit.getLogger();
        Bukkit.getPluginManager().registerEvents(new MagicEvents(this), this);
    }
}
