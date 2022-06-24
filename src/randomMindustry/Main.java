package randomMindustry;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.type.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class Main extends Mod{
    public Main(){
        Events.on(ClientLoadEvent.class, (e) -> {
            ResourceMapper.init();
            BlockMapper.init();
            for (ItemPack pack : ResourceMapper.itemMap.values()) {
                Log.info(pack.tag + " " + pack.tier + " locked:");
                for (Item item : pack.locked) Log.info(item + item.emoji());
            }
        });
    }
}
