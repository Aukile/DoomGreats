package com.ankrya.doomsgreats.help.json;

import com.ankrya.doomsgreats.DoomsGreats;
import com.ankrya.doomsgreats.client.sound.SoundName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonCreator {
    private static String ID = DoomsGreats.MODID;
    private static Path path;
    public static void main(String[] args) {

            //自主修改此处方法
            createSoundsFile();

    }

    /**
     * 自动获取{@link SoundName}中全部的String静态变量<br>
     * 创建 sounds.json
     */
    public static void createSoundsFile(){
        JsonObject root = createSounds(SoundName.getAll());
        try {
            createStart("sounds", GatherType.SOUND, root);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public enum GatherType {
        ITEM,
        GEO_ITEM,
        BLOCK,
        SOUND,
        PARTICLE,
        TROPHIES;
    }

    /**
     * 创建json文件
     * @param name 文件名
     * @param type 文件类型
     */
    public static void createStart(String name, GatherType type, JsonObject root) throws IOException {
        switch (type){
            case ITEM -> path = Path.of("src/main/resources/assets/dooms_greats/models/item");
            case GEO_ITEM -> path = Path.of("src/main/resources/assets/dooms_greats/models/item/geo");
            case BLOCK -> path = Path.of("src/main/resources/assets/dooms_greats/models/block");
            case SOUND -> path = Path.of("src/main/resources/assets/dooms_greats");
            case PARTICLE -> path = Path.of("src/main/resources/assets/dooms_greats/particles");
            case TROPHIES -> path = Path.of("src/main/resources/data/dooms_greats/trophies");
        }
        path = path.resolve(name + ".json");
        Files.createDirectories(path.getParent());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.write(path, gson.toJson(root).getBytes());
    }

    public static JsonObject createItemJson(String texture){
        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();

        root.addProperty("parent", "item/generated");
        textures.addProperty("layer0", ID + ":items/" + texture);
        root.add("textures", textures);

        return root;
    }

    public static JsonObject createGeoItemJson(String display, String texture){
        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();

        root.addProperty("parent", ID + ":displaysettings/" +display);
        textures.addProperty("layer0", ID + ":items/" + texture);
        root.add("textures", textures);

        return root;
    }

    public static JsonObject createBlockStateJson(Path path, String texture){
        JsonObject root = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject all = new JsonObject();
        all.addProperty("model", ID + ":block/" + texture);
        variants.add("all", all);
        root.add("variants", variants);
        return root;
    }

    public static JsonObject createBlockJson(Path path, String texture, String renderType){
        try {
            Path blockstate = path.getParent().resolve("blockstate");
            Files.createDirectories(path);

            JsonObject root = new JsonObject();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.write(path, gson.toJson(root).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonObject root = new JsonObject();
        JsonObject textures = new JsonObject();

        root.addProperty("parent", ID + "block/cube_all");
        textures.addProperty("all", ID + ":blocks/" + texture);
        textures.addProperty("particle", ID + ":blocks/" + texture);
        root.add("textures", textures);

        root.addProperty("render_type", renderType);
        return root;
    }

    public static JsonObject createSounds(boolean subtitle, String... sounds) {
        JsonObject root = new JsonObject();

        for (String soundName : sounds) {

            JsonObject soundEntry = new JsonObject();
            JsonArray soundsArray = new JsonArray();
            JsonObject soundObject = new JsonObject();

            soundObject.addProperty("name", ID + ":" + soundName);

            soundObject.addProperty("stream", false);
            soundsArray.add(soundObject);

            soundEntry.addProperty("category", "player");
            if (subtitle) soundEntry.addProperty("subtitle", "subtitles." + soundName);
            soundEntry.add("sounds", soundsArray);
            root.add(soundName, soundEntry);
        }
        return root;
    }

    public static JsonObject createSounds(String... sounds){
        return createSounds(true, sounds);
    }

    public static JsonObject createParticle(String... textures) {
        JsonObject root = new JsonObject();
        JsonArray texture = new JsonArray();
        for(String t : textures)
            texture.add(ID + ":" + t);
        root.add("textures", texture);
        return root;
    }

    public enum TrophyType {
        BLOCK,
        ENTITY;

        public static String get(TrophyType type){
            return switch (type) {
                case BLOCK -> "block";
                case ENTITY -> "entity";
            };
        }

        public static String getSequence(TrophyType type){
            return switch (type) {
                case BLOCK -> "blocks";
                case ENTITY -> "entities";
            };
        }
    }



    public static JsonObject createTrophy(String name, TrophyType type, TrophyPools[] trophyPools) {
        JsonObject root = new JsonObject();
        JsonArray pools = new JsonArray();

        root.addProperty("type", "minecraft:" + TrophyType.get(type));

        for (TrophyPools trophyPool : trophyPools) {
            JsonObject pool = new JsonObject();

            // 处理rolls
            int[] rollsValue = trophyPool.getRolls();
            if (rollsValue.length == 1) {
                pool.addProperty("rolls", rollsValue[0]);
            } else {
                JsonObject rollsRange = new JsonObject();
                rollsRange.addProperty("min", rollsValue[0]);
                rollsRange.addProperty("max", rollsValue[1]);
                pool.add("rolls", rollsRange);
            }

            // 处理bonus_rolls
            int[] bonusRollsValue = trophyPool.getBonusRolls();
            if (bonusRollsValue.length > 0) {
                if (bonusRollsValue.length == 1) {
                    pool.addProperty("bonus_rolls", bonusRollsValue[0]);
                } else {
                    JsonObject bonusRange = new JsonObject();
                    bonusRange.addProperty("min", bonusRollsValue[0]);
                    bonusRange.addProperty("max", bonusRollsValue[1]);
                    pool.add("bonus_rolls", bonusRange);
                }
            }

            // 处理entries
            JsonArray entriesArray = new JsonArray();
            for (TrophyPools.Entry entry : trophyPool.entries()) {
                JsonObject entryJson = new JsonObject();
                entryJson.addProperty("type", "minecraft:item");
                entryJson.addProperty("name", entry.name);
                entryJson.addProperty("weight", entry.weight);

                // 处理条件（conditions）
                JsonArray conditionsArray = new JsonArray();
                if (entry.match()) {
                    JsonObject condition = new JsonObject();
                    condition.addProperty("condition", "minecraft:match_tool");

                    JsonObject predicate = new JsonObject();
                    JsonObject enchantments = new JsonObject();
                    JsonArray enchantArray = new JsonArray();

                    JsonObject enchantObj = new JsonObject();
                    enchantObj.addProperty("enchantment", "minecraft:silk_touch");

                    JsonObject levels = new JsonObject();
                    levels.addProperty("min", 1);
                    enchantObj.add("levels", levels);

                    enchantArray.add(enchantObj);
                    enchantments.add("enchantments", enchantArray);
                    predicate.add("predicates", enchantments);

                    condition.add("predicate", predicate);
                    conditionsArray.add(condition);
                }

                if (!conditionsArray.isEmpty()) {
                    entryJson.add("conditions", conditionsArray);
                }

                // 处理函数（functions）
                JsonArray functionsArray = new JsonArray();

                // set_count 函数
                if (entry.countMin != entry.countMax) {
                    JsonObject setCount = new JsonObject();
                    setCount.addProperty("function", "minecraft:set_count");

                    JsonObject countObj = new JsonObject();
                    countObj.addProperty("min", entry.countMin);
                    countObj.addProperty("max", entry.countMax);
                    setCount.add("count", countObj);

                    functionsArray.add(setCount);
                }

                // explosion_decay 函数
                if (entry.explosion_decay()) {
                    JsonObject explosionDecay = new JsonObject();
                    explosionDecay.addProperty("function", "minecraft:explosion_decay");
                    functionsArray.add(explosionDecay);
                }

                // 时运效果
                if (entry.fortune) {
                    JsonObject applyBonus = new JsonObject();
                    applyBonus.addProperty("function", "minecraft:apply_bonus");
                    applyBonus.addProperty("enchantment", "minecraft:fortune");
                    applyBonus.addProperty("formula", "minecraft:ore_drops");
                    functionsArray.add(applyBonus);
                }

                // enchant_with_levels 函数
                if (entry.hasEnchantLevel()) {
                    JsonObject enchantWith = new JsonObject();
                    enchantWith.addProperty("function", "minecraft:enchant_with_levels");

                    JsonObject levelsObj = new JsonObject();
                    levelsObj.addProperty("min", entry.enchantLevelMin);
                    levelsObj.addProperty("max", entry.enchantLevelMax);
                    enchantWith.add("levels", levelsObj);

                    enchantWith.addProperty("treasure", true);
                    functionsArray.add(enchantWith);
                }

                if (!functionsArray.isEmpty()) {
                    entryJson.add("functions", functionsArray);
                }

                entriesArray.add(entryJson);
            }

            pool.add("entries", entriesArray);
            pools.add(pool);
        }

        root.add("pools", pools);
        root.addProperty("random_sequence", ID + ":" + TrophyType.get(type) + "/" + name);
        return root;
    }


    /**
     *
     * @param rollsMin 基础抽取最小
     * @param rollsMax 基础抽取最大
     * @param bonusMin 额外抽取最小
     * @param bonusMax 额外抽取最大
     * @param entries 条目
     */
    public record TrophyPools(int rollsMin, int rollsMax, int bonusMin, int bonusMax, Entry[] entries){

        public int[] getRolls() {
            return new int[]{rollsMin, rollsMax};
        }

        public int[] getBonusRolls() {
            return bonusMin == bonusMax ? new int[]{bonusMax} : new int[]{bonusMin, bonusMax};
        }

        /**
         *
         * @param name 物品注册名
         * @param weight 权重
         * @param countMin 最小掉落数量
         * @param countMax 最大掉落数量
         * @param enchantLevelMin 附魔等级影响最小值
         * @param enchantLevelMax 附魔等级影响最大值
         * @param fortune 时运等级影响
         * @param explosion_decay 爆炸衰减
         * @param match 匹配工具模式
         */
        public record Entry(String name, int weight, int countMin, int countMax, int enchantLevelMin, int enchantLevelMax, boolean fortune, boolean explosion_decay, boolean match) {
            public boolean hasEnchantLevel() {
                return enchantLevelMin > 0 || enchantLevelMax > 0;
            }
        }
    }
}
