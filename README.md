## ShortcutManager 管理 API(Reat Native 0.63 上测试开发)
[![React Native Version](https://img.shields.io/badge/react--native-latest-blue.svg?style=flat-square)](http://facebook.github.io/react-native/releases)
[![Version](https://img.shields.io/npm/v/react-native-shortcutmanager.svg)](https://www.npmjs.com/package/react-native-shortcutmanager)
[![NPM](https://img.shields.io/npm/dm/react-native-shortcutmanager.svg)](https://www.npmjs.com/package/react-native-shortcutmanager)

### 捷径管理 API：v 1.0.4 ，效果如图所示
![waCPTP](https://s1.ax1x.com/2020/09/12/waCFFf.jpg)
![waCFFf](https://s1.ax1x.com/2020/09/12/waCPTP.jpg)

```js
第一步安装
npm install react-native-shortcutmanager --save

RN 0.60 以下需要
react-native link react-native-shortcutmanager(或)
react-native unlink react-native-shortcutmanager
```

```js
第二步，配置代码（注意导包）
没有请创建 MyReactPackage -> createNativeModules 并，注册
MainApplication -> packages.add(new MyReactPackage())

在代码 MyNativeModule.java 下，导包：import com.shortcut.manager.ExtraData; 添加如下代码
public MyNativeModule(ReactApplicationContext reactContext){
    ...
    //把 MainActivity.class 添加到连接库里 lib
    ExtraData.setActivityClass(MainActivity.class);
}

在代码 MainActivity.java 下，导包：import com.shortcut.manager.ShortcutManagerModule;
在生命周期 onCreate 和 onNewIntent 分别添加如下代码
@Override
public void onCreate(Bundle savedInstanceState) {
  ...
  //--添加这段代码
  ShortcutManagerModule.AppUninitiIntent(getIntent());
}
@Override
public void onNewIntent(final Intent intent) {
  ...
  //--添加这段代码
  ShortcutManagerModule.newIntent(intent);
}
```

```js
第三步，拷贝图标
菜单图标（android\app\src\main\res\drawable）用 png 格式，放到此目录下，注意先调用 getIconWeightAndHeight
查看图标尺寸，单位 px
```

### 使用方法
```js
import * as shortcutManager from 'react-native-shortcutmanager'

shortcutManager.addShortcut("这是菜单名1", "telephone", "这儿传递数据1")
.then((res)=>{
    alert("menuId：" + res);
    that.setState({menuId: res});
})
.catch((err)=>{
    alert("对不起，Android 版本不支持。")
});

shortcutManager.removeAllItem().then(()=>{
  alert('删除成功');
})
```

```js
以下9个 API 用于菜单图标管理(9 API)
addShortcut -> 添加动态图标菜单
removeItem -> 移除指定项
removeAllItem -> 删除所有动态图标
getIconWeightAndHeight -> 获取菜单图标支持宽高
getShortcutList -> 获取菜单列表
updateShortcut -> 更新指定菜单
addPinShortcut -> 添加桌面菜单，需要权限
getPinShortcutList -> 获取添加（成功）桌面菜单
getExtraData -> 异步获取参数
getExtraDataHook -> 指定全局事件（Hook），回调方法（callback）
removeShortcutEvent -> 注销全局事件
```
```js
参数除了通过 getExtraData 获取外，还可通过异步回调事件，事件名：ShortcutClick，参数：extra
因 componentDidMount 生命周期原因，APP 启动中，可考虑异步回调事件方式，改变路由等等。

代码如下：
//导入 react-native-shortcutmanager，就会注册，全局事件 Hook
componentDidMount(){
  shortcutManager.getExtraDataHook((data)=>{
    alert(data)
  })
}
//注销全局事件
shortcutManager.removeShortcutEvent()
```

### 菜单管理 APIs 详解
```js
添加动态图标菜单
addShortcut(label, icon, extra)

参数说明
  label：菜单标题，长短标签都是此值（setShortLabel、setLongLabel）（必填）
  icon：菜单图标，无扩展名：因为 Android 带深色、浅色，图标最好带底色（Icon）（必填）
  extra：菜单传值（JSON，最好用 JSON.stringify（Object)，序列成字符串）（必填）

返回值(通过 Promise)
  成功（then）返回菜单ID（menuId，是一个 UUID）
  失败（catch）返回错误对象（err） Tip: 9523 版本不支持，请使用（API 25 以上）
```

```js
移除指定菜单项（仅限动态菜单，桌面图标自行移除）
removeItem(menuId)

参数说明
  menuId：菜单Id（添加菜单返回的 UUID，必填）

返回值(通过 Promise)
  成功（then）返回成功（true）
  失败（catch）返回错误对象（err）
    * code: 9523 版本不支持，请使用（API 25 以上）
    * code: -1 menuId 不能为空
```

```js
移除所有菜单项（仅限动态菜单，桌面图标自行移除）
removeAllItem()

参数说明
  无参数

返回值(通过 Promise)
  成功（then）返回成功（true）
  失败（catch）返回错误对象（err） Tip: 9523 版本不支持，请使用（API 25 以上）
```

```js
获取系统支持的图标宽高（设计图标前，先获取查看一下宽高 px）
getIconWeightAndHeight()

参数说明
  无参数

返回值(通过 Promise)
  成功（then）返回系统支持的 Icon （width、height），单位 px
  失败（catch）返回错误对象（err） Tip: 9523 版本不支持，请使用（API 25 以上）
```

```js
获取所有菜单列表
getShortcutList()

参数说明
  无参数

返回值(通过 Promise)
  成功（then）返回菜单列表（size:0,result:[{id, label}])
  失败（catch）返回错误对象（err） Tip: 9523 版本不支持，请使用（API 25 以上）
```

```js
根据菜单Id，更新指定菜单项
updateShortcut(menuId, label, icon, extra)

参数说明
  menuId：需要更新的菜单ID（必填）
  label：菜单标题（必填）
  icon：菜单图标（必填）
  extra：菜单传值（必填，不要传空字符串）

返回值(通过 Promise)
  成功（then）返回菜单ID（menuId，是一个 UUID）
  失败（catch）返回错误对象（err）
    * code: 9523 版本不支持，请使用（API 25 以上）
    * code: -1 menuId 不能为空
```

```js
添加桌面图标（需要权限执行）
addPinShortcut(label, icon, extra)

参数说明
  label：图标的标题（必填）
  icon：图标（用上面的路径）（必填）
  extra：传递参数（JSON，最好用 JSON.stringify(Object)，序列成字符串）（必填）

返回值(通过 Promise)
  成功（then）返回图标ID（iconId，是一个 UUID，可能因权限而失败，图标 ID 不一定有效）
  失败（catch）返回错误对象（err） Tip: 9523 版本不支持，请使用（API 25 以上）
    Tip：不知道是否成功，添加后需手工删除。
```

```js
获取添加成功的桌面图标
getPinShortcutList()

参数说明
  无参数

返回值(通过 Promise)
  成功（then）返回图标ID（size:0,result:[{id, label}])
  失败（catch）返回错误对象（err） Tip: 9523 版本不支持，请使用（API 25 以上）
```

```js
异步获取参数对象（可在 componentDidMount 里获取，以判断 APP 启动类型，做相应的事情）
getExtraData(callback)

参数说明
  callback：异步方法回调（extra）

例如（e.g)：
componentDidMount(){
  var that = this;
  getExtraData((extra)=>{
    ...判断后做你的事情，如跳转路由传参，等等
  })
}
注意：参数只能获取一次，阅后即焚。
```

```js
导入 react-native-shortcutmanager，就会注册，全局事件 Hook
getExtraDataHook(callback)

参数说明
  callback：异步方法回调（extra）

例如（e.g)：
componentDidMount(){
  shortcutManager.getExtraDataHook((data)=>{
    alert(data)
  })
}
```

```js
注销全局事件（Hook）
removeShortcutEvent()

参数说明
  无参数

例如（e.g)：
componentWillUnmount(){
  shortcutManager.removeShortcutEvent()
}
```

### 捐赠（Donate）
<img src="https://camo.githubusercontent.com/61ea2757c07e7f367707f297b06022bf0e6724e8efb86b6aab0b9cc0946951a9/68747470733a2f2f73312e617831782e636f6d2f323032322f30392f31362f767a775944672e6a7067" title="支付宝扫一扫" data-canonical-src="https://s1.ax1x.com/2022/09/16/vzwYDg.jpg" style="max-width: 360px;">
