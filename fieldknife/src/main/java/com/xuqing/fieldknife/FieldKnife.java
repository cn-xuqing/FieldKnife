package com.xuqing.fieldknife;

/**
 * @author xuqing
 */
public class FieldKnife {
    public static void bind(Object obj) {
        try {
            Class.forName(obj.getClass().getName() + "_FieldKnife").getConstructor(Object.class).newInstance(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}