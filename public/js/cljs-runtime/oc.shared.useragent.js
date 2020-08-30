goog.provide('oc.shared.useragent');
oc.shared.useragent.mac_QMARK_ = goog.userAgent.MAC;
oc.shared.useragent.windows_QMARK_ = goog.userAgent.WINDOWS;
oc.shared.useragent.linux_QMARK_ = goog.userAgent.LINUX;
oc.shared.useragent.android_QMARK_ = goog.userAgent.ANDROID;
oc.shared.useragent.ios_QMARK_ = goog.userAgent.IOS;
oc.shared.useragent.edge_QMARK_ = goog.userAgent.EDGE;
oc.shared.useragent.ie_or_edge_QMARK_ = goog.userAgent.EDGE_OR_IE;
oc.shared.useragent.gecko_QMARK_ = goog.userAgent.GECKO;
oc.shared.useragent.ie_QMARK_ = goog.userAgent.IE;
oc.shared.useragent.opera_QMARK_ = goog.userAgent.OPERA;
oc.shared.useragent.chrome_QMARK_ = goog.userAgent.product.CHROME;
oc.shared.useragent.safari_QMARK_ = goog.userAgent.product.SAFARI;
oc.shared.useragent.firefox_QMARK_ = goog.userAgent.product.FIREFOX;
oc.shared.useragent.ipad_QMARK_ = goog.userAgent.IPAD;
oc.shared.useragent.iphone_QMARK_ = goog.userAgent.IPHONE;
oc.shared.useragent.ipod_QMARK_ = goog.userAgent.IPOD;
oc.shared.useragent.mobile_QMARK_ = goog.userAgent.MOBILE;
oc.shared.useragent.webkit_QMARK_ = goog.userAgent.WEBKIT;
oc.shared.useragent.x11_QMARK_ = goog.userAgent.X11;
/**
 * The string that describes the version number of the user agent.
 */
oc.shared.useragent.browser_version = goog.userAgent.VERSION;
/**
 * 
 * Detects the version of the OS/platform the browser is running in.
 * Not supported for Linux, where an empty string is returned.
 */
oc.shared.useragent.os_version = goog.userAgent.platform.VERSION;
/**
 * Returns true if the browser version is v or higher.
 */
oc.shared.useragent.browser_version_or_higher_QMARK_ = (function oc$shared$useragent$browser_version_or_higher_QMARK_(v){
return goog.userAgent.isVersionOrHigher(v);
});
/**
 * Returns true if the OS/platform is v or higher.
 */
oc.shared.useragent.os_version_or_higher_QMARK_ = (function oc$shared$useragent$os_version_or_higher_QMARK_(v){
return goog.userAgent.platform.isVersion(v);
});
/**
 * Whether we're running in the desktop application
 */
oc.shared.useragent.desktop_app_QMARK_ = (!((window.OCCarrotDesktop == null)));
/**
 * Whether we're running in the mobile application
 */
oc.shared.useragent.mobile_app_QMARK_ = (!((window.ReactNativeWebView == null)));
/**
 * Whether we're running in a web-in-native wrapper (e.g. electron, expo, etc)
 */
oc.shared.useragent.pseudo_native_QMARK_ = ((oc.shared.useragent.desktop_app_QMARK_) || (oc.shared.useragent.mobile_app_QMARK_));

//# sourceMappingURL=oc.shared.useragent.js.map
