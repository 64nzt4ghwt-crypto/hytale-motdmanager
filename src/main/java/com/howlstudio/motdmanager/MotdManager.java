package com.howlstudio.motdmanager;
import com.hypixel.hytale.component.Ref; import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.nio.file.*; import java.util.*;
public class MotdManager {
    private final Path dataDir;
    private final List<String> lines=new ArrayList<>();
    public MotdManager(Path d){this.dataDir=d;try{Files.createDirectories(d);}catch(Exception e){}load();if(lines.isEmpty())setDefault();}
    private void setDefault(){lines.add("§6Welcome to the server, §e{player}§6!");lines.add("§rThere are §b{online}§r players online.");lines.add("§7Type §f/help§7 to get started.");}
    public int getLineCount(){return lines.size();}
    public void sendTo(PlayerRef ref){
        int online=Universe.get().getPlayers().size();
        for(String l:lines){String line=l.replace("{player}",ref.getUsername()).replace("{online}",String.valueOf(online));ref.sendMessage(Message.raw(line));}
    }
    public void save(){try{Files.writeString(dataDir.resolve("motd.txt"),String.join("\n",lines));}catch(Exception e){}}
    private void load(){try{Path f=dataDir.resolve("motd.txt");if(!Files.exists(f))return;lines.clear();lines.addAll(Files.readAllLines(f));}catch(Exception e){}}
    private String translate(String s){return s.replace("&0","§0").replace("&1","§1").replace("&2","§2").replace("&3","§3").replace("&4","§4").replace("&5","§5").replace("&6","§6").replace("&7","§7").replace("&8","§8").replace("&9","§9").replace("&a","§a").replace("&b","§b").replace("&c","§c").replace("&d","§d").replace("&e","§e").replace("&f","§f").replace("&l","§l").replace("&o","§o").replace("&r","§r");}
    public AbstractPlayerCommand getMotdCommand(){
        return new AbstractPlayerCommand("motd","View or edit MOTD. /motd | /motd set <line_n> <text> | /motd add <text> | /motd clear"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                String[]args=ctx.getInputString().trim().split("\\s+",3); String sub=args.length>0?args[0].toLowerCase():"show";
                switch(sub){
                    case"","show"->sendTo(playerRef);
                    case"add"->{if(args.length<2)break;lines.add(translate(args[1]+(args.length>2?" "+args[2]:"")));save();playerRef.sendMessage(Message.raw("[MOTD] Added line "+lines.size()+".")); }
                    case"set"->{if(args.length<3)break;try{int n=Integer.parseInt(args[1])-1;if(n<0||n>=lines.size()){playerRef.sendMessage(Message.raw("[MOTD] Line "+args[1]+" not found."));break;}lines.set(n,translate(args[2]));save();playerRef.sendMessage(Message.raw("[MOTD] Line "+(n+1)+" updated."));}catch(Exception e){playerRef.sendMessage(Message.raw("Usage: /motd set <line_number> <text>"));}}
                    case"list"->{playerRef.sendMessage(Message.raw("[MOTD] Lines:"));for(int i=0;i<lines.size();i++)playerRef.sendMessage(Message.raw("  "+(i+1)+". "+lines.get(i)));}
                    case"clear"->{lines.clear();setDefault();save();playerRef.sendMessage(Message.raw("[MOTD] Reset to default."));}
                    default->playerRef.sendMessage(Message.raw("Usage: /motd | /motd list | /motd add <text> | /motd set <n> <text> | /motd clear"));
                }
            }
        };
    }
}
