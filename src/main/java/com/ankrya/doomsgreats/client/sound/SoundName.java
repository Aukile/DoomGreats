package com.ankrya.doomsgreats.client.sound;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class SoundName {

    public static final String GUN = "gun";
    public static final String BLADE = "blade";
    public static final String REVOLVE_ON = "revolve_on";
    public static final String DESIRE_DRIVER = "desire_driver";
    public static final String BUCKLE_OPEN_ALL = "doomsgeatsbuckleopen";
    public static final String BUCKLE_OPEN = "buckle_open";
    public static final String BUCKLE_OPEN_WAIT = "buckle_open_wait";
    public static final String HENSHIN = "doomsgeatshenshinfull";
    public static final String BUCKLE_SET = "doomsgeatssetjudgment";

    public static String[] getAll(){
        try {
            Field[] fields = SoundName.class.getDeclaredFields();
            List<String> soundNames = new ArrayList<>();

            for (Field field : fields) {
                if (field.getType() == String.class && Modifier.isStatic(field.getModifiers())) {
                    String string = (String) field.get(null);
                    if (string != null)
                        soundNames.add(string);
                }
            }

            return soundNames.toArray(new String[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}
