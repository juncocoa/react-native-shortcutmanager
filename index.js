import { NativeModules, NativeEventEmitter } from 'react-native';
const { ShortcutManagerModule } = NativeModules; var temp_shortcut_callback;

const _eventShortcutClick = new NativeEventEmitter(ShortcutManagerModule);
var _shortcutEvent = _eventShortcutClick.addListener('ShortcutClick', (data) => {
  if(temp_shortcut_callback != undefined){
    temp_shortcut_callback(data);
  }else {
    console.log("ShortcutClick: " + data);
  }
})

//全局事件 Hook
export function getExtraDataHook (callback) {
  temp_shortcut_callback = callback;
}
//注销全局事件
export function removeShortcutEvent () {
    _shortcutEvent.remove();
};
//添加动态图标菜单
export function addShortcut (label, icon, extra) {
    return ShortcutManagerModule.addShortcut(label, icon, extra)
}
//移除指定项
export function removeItem (menuId) {
    return ShortcutManagerModule.removeItem(menuId)
}
//删除所有动态图标
export function removeAllItem () {
    return ShortcutManagerModule.removeAllItem()
}
//获取菜单图标支持宽高
export function getIconWeightAndHeight () {
    return ShortcutManagerModule.getIconWeightAndHeight()
}
//获取菜单列表
export function getShortcutList () {
    return ShortcutManagerModule.getShortcutList()
}
//更新指定菜单
export function updateShortcut (menuId, label, icon, extra) {
    return ShortcutManagerModule.updateShortcut(menuId, label, icon, extra)
}
//异步获取参数
export function getExtraData (callback) {
    return ShortcutManagerModule.getExtraData(callback)
}
//添加桌面菜单，需要权限
export function addPinShortcut (label, icon, extra) {
    return ShortcutManagerModule.addPinShortcut(label, icon, extra)
}
//获取添加（成功）桌面菜单
export function getPinShortcutList () {
    return ShortcutManagerModule.getPinShortcutList()
}
