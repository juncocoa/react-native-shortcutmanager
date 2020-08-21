package com.shortcut.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ShortcutManager;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.content.pm.ShortcutInfo;

import java.util.*;

public class ShortcutManagerModule extends ReactContextBaseJavaModule {
    private static ReactApplicationContext mContext;
    private ShortcutManager mShortcutManager;

    public ShortcutManagerModule(ReactApplicationContext reactContext){
        super(reactContext);
        mContext = reactContext;
    }

    @Override
    public String getName() {
        return "ShortcutManagerModule";
    }

     /**
     * 获取图标菜单，所支持的最大宽高（px）（Ok）
     * @param promise 回调函数
     */
    @ReactMethod
    private void getIconWeightAndHeight(final Promise promise){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mShortcutManager = mContext.getSystemService(ShortcutManager.class);

            WritableMap arr = Arguments.createMap();
            arr.putString("tip", "因为 Android 带深色、浅色，图标最好带底色（Icon）");
            arr.putInt("width", mShortcutManager.getIconMaxWidth());
            arr.putInt("height", mShortcutManager.getIconMaxHeight());
            promise.resolve(arr);
        }else {
            promise.reject("9523", "版本不支持");
        }
    }

    /**
     * 获取所有图标菜单（Ok）
     * @param promise
     */
    @ReactMethod
    private void getShortcutList(final Promise promise){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mShortcutManager = mContext.getSystemService(ShortcutManager.class);
            WritableMap map = Arguments.createMap();
            List<ShortcutInfo> list = mShortcutManager.getDynamicShortcuts();

            if(list.size() > 0){
                map.putInt("size", list.size());
                WritableArray arr = Arguments.createArray();

                for(ShortcutInfo info : list){
                    WritableMap subMap = Arguments.createMap();

                    String label = info.getShortLabel().toString();
                    String menuId = info.getId();
                    subMap.putString("id", menuId);
                    subMap.putString("label", label);

                    arr.pushMap(subMap);
                }
                map.putArray("result", arr);
            }else {
                map.putInt("size", 0);
            }
            promise.resolve(map);
        }else {
            promise.reject("9523", "版本不支持");
        }
    }

    /**
     *  添加图标菜单（Ok）
     * @param label 菜单名
     * @param icon 菜单图标，来自于（android\app\src\main\res\drawable）
     * @param extra 需要传递的值 (必须）
     * @param promise 回调函数
     */
    @ReactMethod
    private void addShortcut(String label, String icon, String extra, final Promise promise){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mShortcutManager = mContext.getSystemService(ShortcutManager.class);
            List<ShortcutInfo> infos = new ArrayList();
            Intent intent = new Intent(mContext, ExtraData.getActivityClass());
            intent.setAction(Intent.ACTION_VIEW);
            //带参数传递
            intent.putExtra("data", extra);
            int id = mContext.getResources().getIdentifier(icon, "drawable", "com.awesomeproject");
            String menuId = UUID.randomUUID().toString().replace("-","");

            ShortcutInfo info = new ShortcutInfo.Builder(mContext, menuId)
                    .setShortLabel(label)
                    .setLongLabel(label)
                    .setIcon(Icon.createWithResource(mContext, id))
                    .setIntent(intent)
                    .build();
            infos.add(info);
            mShortcutManager.addDynamicShortcuts(infos);
            promise.resolve(menuId);
        }else {
            promise.reject("9523", "版本不支持");
        }
    }

    /**
     * 移除单个图标菜单（Ok）
     * @param menuId  菜单ID
     * @param promise 回调函数
     */
    @ReactMethod
    private void removeItem(String menuId, final Promise promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if (menuId == null || menuId == "") {
                promise.reject("-1", "menuId 不能为空");
            }else {
                mShortcutManager = mContext.getSystemService(ShortcutManager.class);
                mShortcutManager.removeDynamicShortcuts(Arrays.asList(menuId));
                promise.resolve(true);
            }
        }else {
            promise.reject("9523", "版本不支持");
        }
    }

    /**
     * 移除所有图标菜单（Ok）
     * @param promise 回调函数
     */
    @ReactMethod
    private void removeAllItem(final Promise promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mShortcutManager = mContext.getSystemService(ShortcutManager.class);
            mShortcutManager.removeAllDynamicShortcuts();
            promise.resolve(true);
        }else {
            promise.reject("9523", "版本不支持");
        }
    }

    /**
     * 更新菜单信息 和 （Extra：传值）(Ok)
     * @param menuId  菜单ID（必须）
     * @param label 菜单名（必须）
     * @param icon 菜单图标，来自于（android\app\src\main\res\drawable）（必须）
     * @param extra 需要传递的值 (必须）
     * @param promise 回调函数
     */
    @ReactMethod
    private void updateShortcut(String menuId, String label, String icon, String extra,final Promise promise) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if (menuId == null || menuId == "") {
                promise.reject("-1", "menuId 不能为空");
            }else {
                Intent intent = new Intent(mContext, ExtraData.getActivityClass());
                intent.setAction(Intent.ACTION_VIEW);
                intent.putExtra("data", extra);
                int id = mContext.getResources().getIdentifier(icon, "drawable", "com.awesomeproject");

                ShortcutInfo info = new ShortcutInfo.Builder(mContext, menuId)
                        .setShortLabel(label)
                        .setLongLabel(label)
                        .setIcon(Icon.createWithResource(mContext, id))
                        .setIntent(intent)

                        .build();

                mShortcutManager.updateShortcuts(Arrays.asList(info));

                promise.resolve(menuId);
            }
        }else {
            promise.reject("9523", "版本不支持");
        }
    }

    /**
     * 这只一个可以固定到桌面的 icon（Ok）
     * @param label 桌面菜单名
     * @param icon 桌面菜单图标，来自于（android\app\src\main\res\drawable）
     * @param extra 需要传递的值 (必须）
     * @param promise 回调函数
     */
    @ReactMethod
    private void  addPinShortcut(String label, String icon, String extra, final Promise promise){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mShortcutManager = mContext.getSystemService(ShortcutManager.class);

            if(mShortcutManager.isRequestPinShortcutSupported() == true){
                Intent intent = new Intent(mContext, ExtraData.getActivityClass());
                intent.setAction(Intent.ACTION_VIEW);
                //带参数传递
                intent.putExtra("data", extra);
                int id = mContext.getResources().getIdentifier(icon, "drawable", "com.awesomeproject");
                String menuId = UUID.randomUUID().toString().replace("-","");

                ShortcutInfo info = new ShortcutInfo.Builder(mContext, menuId)
                        .setShortLabel(label)
                        .setLongLabel(label)
                        .setIcon(Icon.createWithResource(mContext, id))
                        .setIntent(intent)
                        .build();

                Intent pinnedShortcutCallbackIntent = mShortcutManager.createShortcutResultIntent(info);
                PendingIntent successCallback = PendingIntent.getBroadcast(mContext, 0, pinnedShortcutCallbackIntent, 0);
                mShortcutManager.requestPinShortcut(info, successCallback.getIntentSender());
                promise.resolve(menuId);
            }else {
                addShortcut(label, icon, extra, promise);
            }
        }else {
            promise.reject("9523", "版本不支持");
        }
    }

    /**
     * 获取桌面所有桌面图标（Ok）
     * @param promise 回调函数
     */
    @ReactMethod
    private void getPinShortcutList(final Promise promise){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            mShortcutManager = mContext.getSystemService(ShortcutManager.class);
            WritableMap map = Arguments.createMap();
            List<ShortcutInfo> list = mShortcutManager.getPinnedShortcuts();

            if(list.size() > 0){
                map.putInt("size", list.size());
                WritableArray arr = Arguments.createArray();

                for(ShortcutInfo info : list){
                    WritableMap subMap = Arguments.createMap();

                    String label = info.getShortLabel().toString();
                    String menuId = info.getId();
                    subMap.putString("id", menuId);
                    subMap.putString("label", label);

                    arr.pushMap(subMap);
                }
                map.putArray("result", arr);
            }else {
                map.putInt("size", 0);
            }
            promise.resolve(map);
        }else {
            promise.reject("9523", "版本不支持");
        }
    }

    /**
     * js bundle 加载完成， React Native 异步主动取值
     * @param callback 回调函数
     */
    @ReactMethod
    private void getExtraData(Callback callback){
        callback.invoke(ExtraData.getExtraData());
        ExtraData.setExtraData("");  //阅后即焚，参数丢失
    }

    /**
     * APP 运行中，点击菜单
     * @param intent APP 调用对象
     */
    public static void newIntent(Intent intent){
        if(intent.getExtras().getCharSequence("data") != null){
            String msg = intent.getExtras().getCharSequence("data").toString();
            ExtraData.setExtraData(msg);
            //异步事件回调
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("ShortcutClick", msg);
        }else {
            ExtraData.setExtraData("");
        }
    }

    /**
     * APP 未运行，首次调用。
     * @param intent APP 调用对象
     */
    public static void AppUninitiIntent(Intent intent){
        if(intent.getStringExtra("data") != null){
            String msg = intent.getStringExtra("data");
            ExtraData.setExtraData(msg);
        }else {
            ExtraData.setExtraData("");
        }
    }
}
