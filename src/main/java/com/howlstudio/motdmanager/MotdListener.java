package com.howlstudio.motdmanager;
import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.universe.PlayerRef;
public class MotdListener {
    private final MotdManager mgr;
    public MotdListener(MotdManager m){this.mgr=m;}
    public void register(){
        HytaleServer.get().getEventBus().registerGlobal(PlayerReadyEvent.class,e->{Player p=e.getPlayer();if(p==null)return;PlayerRef ref=p.getPlayerRef();if(ref!=null)mgr.sendTo(ref);});
    }
}
