package com.shortcut.manager;

public class ExtraData {
    private static String extraData ="";
    private static Class<?> tClass;

    /**
     * 获取扩展数据
     * @return 菜单扩展数据
     */
    public static String getExtraData() {
        return extraData;
    }

    /**
     * 设置扩展数据
     * @param extraData 菜单扩展数据
     */
    public static void setExtraData(String extraData) {
        ExtraData.extraData = extraData;
    }

    /**
     * 获取 MainActivity.class
     * @return MainActivity.class
     */
    public static Class<?> getActivityClass() {
        return tClass;
    }

    /**
     * 设置 MainActivity.class
     * @param tClass MainActivity.class
     */
    public static void  setActivityClass(Class<?> tClass) {
        ExtraData.tClass = tClass;
    }
}
