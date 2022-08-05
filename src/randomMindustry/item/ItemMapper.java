package randomMindustry.item;

import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;

public class ItemMapper {
    public static final Seq<CustomItem> generatedItems = new Seq<>();
    public static final Seq<ItemPack> packs = new Seq<>();
    public static final int itemCount = 18;
    public static final Rand rand = new Rand();

    public static void editContent() {
        rand.setSeed(SeedManager.getSeed());
        generatedItems.each(CustomItem::edit);

        ItemPack all = new ItemPack(0, 0, "all", generatedItems.toArray(Item.class));
        for (int i = 0; i < itemCount / 3; i++) {
            packs.add(new ItemPack(i / 2, i, i % 2 == 0 ? "drill" : "craft",
                    all.random(true), all.random(true), all.random(true)
            ));
        }
    }

    public static void generateContent() {
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = new CustomItem("random-item-" + i, Color.red);
            generatedItems.add(item);
        }
    }

    public static int getTier(Item item) {
        Seq<ItemPack> tierPacks = packs.select((p) -> p.globalTier >= 0);
        for (ItemPack pack : tierPacks)
            if (pack.in(item)) return pack.globalTier;
        return -1;
    }

    public static ItemPack combine(Seq<ItemPack> itemPacks) {
        return combine(itemPacks.toArray(ItemPack.class));
    }

    public static ItemPack combine(ItemPack... itemPacks) {
        ItemPack pack = new ItemPack(-1, -1, "all");
        for (ItemPack p : itemPacks) pack.addFrom(p);
        return pack;
    }

    public static Seq<ItemPack> getPacksWithItem(Item item, boolean locked) {
        return getPacksBy((pack) -> (locked ? pack.locked : pack.all).contains(item));
    }

    public static Seq<ItemPack> getPacksInGlobalTierRange(int min, int max) {
        return getPacksBy((pack) -> pack.globalTier >= min && pack.globalTier <= max);
    }

    public static Seq<ItemPack> getPacksByTier(String tier) {
        return getPacksBy((pack) -> pack.tier.equalsIgnoreCase(tier));
    }

    public static Seq<ItemPack> getLockedPacksByTier(String tier) {
        return getPacksByTier(tier).select((pack) -> pack.locked() != 0);
    }

    public static ItemPack getPackByGlobalTier(int globalTier) {
        return getFirstPackBy((pack) -> pack.globalTier == globalTier);
    }

    public static void lock(Item item) {
        getPacksWithItem(item, true).select(p -> p.globalTier >= 0).each(p -> p.lock(item));
    }

    public static ItemPack getFirstPackBy(Func<ItemPack, Boolean> func) {
        for (ItemPack pack : packs)
            if (func.get(pack)) return pack;
        return null;
    }

    public static Seq<ItemPack> getPacksBy(Func<ItemPack, Boolean> func) {
        Seq<ItemPack> itemPacks = new Seq<>();
        for (ItemPack pack : packs)
            if (func.get(pack)) itemPacks.add(pack);
        return itemPacks;
    }
}
