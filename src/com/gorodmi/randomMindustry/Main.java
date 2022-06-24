package com.gorodmi.randomMindustry;

import arc.Events;
import arc.util.Log;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import mindustry.type.Item;

public class Main extends Mod{
    public Main(){
        Events.on(EventType.ClientLoadEvent.class, (e) -> {
            ResourceMapper.init();
            BlockMapper.init();
            for (ItemPack pack : ResourceMapper.itemMap.values()) {
                Log.info(pack.tag + " " + pack.tier + " locked:");
                for (Item item : pack.locked) Log.info(item + item.emoji());
            }
        });
    }
}
