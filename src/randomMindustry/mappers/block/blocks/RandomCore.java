package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomCore extends CoreBlock implements RandomBlock{
    public final int id;
    public static RandomCore last = null;
    public static final ObjectMap<Integer, Seq<UnitType>> types = ObjectMap.of(
        1, Seq.with(UnitTypes.alpha, UnitTypes.beta),
        2, Seq.with(UnitTypes.poly, UnitTypes.gamma),
        3, Seq.with(UnitTypes.mega),
        4, Seq.with(UnitTypes.oct)
    );
    
    public RandomCore(String name, int id){
        super(name + id);
        this.id = id;
        generate();
    }

    @Override
    public TechTree.TechNode generateNode() {
        if (id == 0) return null; // no need to generate, root
        techNode = new TechTree.TechNode(
                last == null ? TechTree.context() : last.techNode,
                this,
                researchRequirements()
        );
        last = this;
        return techNode;
    }

    @Override
    public int getTier(){
        return id * 3 + 1;
    }
    
    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }
    
    public void generate(){
        if (id == 0) {
            isFirstTier = alwaysUnlocked = true;
            last = null;
        }
        size = id + 3;
        requirements(Category.effect, ItemMapper.getItemStacks(getTier() - 1, r.random(3, 5) + id, () -> (1000 + (getTier() * 200)) + r.random(80, 300), r));
        unitType = types.get(getTier(), Seq.with(UnitTypes.oct)).random(r);
        health = (2000 + (getTier() * 1500)) + r.random(150, 550);
        armor = id * 2;
        itemCapacity = (2500 * getTier()) + r.random(150, 550);
        unitCapModifier = 15 * getTier();
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
    }

    private TextureRegion pixmapRegion;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, Color.gold);
        pixmapRegion = TextureManager.alloc(from);
        pixmapLoaded = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(wallSprites.get(2).random(packer, 64, r));
    }

    public void reloadIcons() {
        createSprites(wallSprites.get(2).random(64, r));
        applyIcons();
    }
}
