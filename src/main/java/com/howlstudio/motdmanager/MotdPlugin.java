package com.howlstudio.motdmanager;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
/** MotdManager — Customizable join MOTD with color codes, player name, and online count. */
public final class MotdPlugin extends JavaPlugin {
    private MotdManager mgr;
    public MotdPlugin(JavaPluginInit init){super(init);}
    @Override protected void setup(){
        System.out.println("[MOTD] Loading...");
        mgr=new MotdManager(getDataDirectory());
        new MotdListener(mgr).register();
        CommandManager.get().register(mgr.getMotdCommand());
        System.out.println("[MOTD] Ready. "+mgr.getLineCount()+" MOTD lines.");
    }
    @Override protected void shutdown(){if(mgr!=null)mgr.save();System.out.println("[MOTD] Stopped.");}
}
