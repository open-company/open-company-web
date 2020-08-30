goog.provide('oc.web.images');
oc.web.images.is_filestack_QMARK_ = (function oc$web$images$is_filestack_QMARK_(uri){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(uri.getDomain(),"cdn.filestackcontent.com");
});
oc.web.images.optimize_filestack_image_url = (function oc$web$images$optimize_filestack_image_url(url,preferred_height){
var uri = (new goog.Uri(url));
if((!(oc.web.images.is_filestack_QMARK_(uri)))){
return url;
} else {
var cur_path = uri.getPath();
var resize_frag = ["resize=height:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(preferred_height)].join('');
var new_path = [resize_frag,cljs.core.str.cljs$core$IFn$_invoke$arity$1(cur_path)].join('');
var new_uri = uri.setPath(new_path);
return cljs.core.str.cljs$core$IFn$_invoke$arity$1(new_uri);
}
});
oc.web.images.is_slack_QMARK_ = (function oc$web$images$is_slack_QMARK_(uri){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(uri.getDomain(),"avatars.slack-edge.com");
});
oc.web.images.approximate_slack_org_height = (function oc$web$images$approximate_slack_org_height(pref_height){
if((pref_height <= (34))){
return (34);
} else {
if((pref_height <= (44))){
return (44);
} else {
if((pref_height <= (68))){
return (68);
} else {
if((pref_height <= (88))){
return (88);
} else {
if((pref_height <= (102))){
return (102);
} else {
if((pref_height <= (132))){
return (132);
} else {
return null;

}
}
}
}
}
}
});
oc.web.images.approximate_slack_user_height = (function oc$web$images$approximate_slack_user_height(pref_height){
if((pref_height <= (24))){
return (24);
} else {
if((pref_height <= (32))){
return (32);
} else {
if((pref_height <= (48))){
return (48);
} else {
if((pref_height <= (72))){
return (72);
} else {
if((pref_height <= (192))){
return (192);
} else {
if((pref_height <= (512))){
return (512);
} else {
return null;

}
}
}
}
}
}
});
oc.web.images.optimize_slack_avatar = (function oc$web$images$optimize_slack_avatar(url,approx_height){
var uri = (new goog.Uri(url));
if((!(oc.web.images.is_slack_QMARK_(uri)))){
return url;
} else {
var cur_path = uri.getPath();
var re = /_(\d{2,3})\.([a-z]+)$/;
var template = ["_",cljs.core.str.cljs$core$IFn$_invoke$arity$1(approx_height),".$2"].join('');
var new_path = clojure.string.replace(cur_path,re,template);
var new_uri = uri.setPath(new_path);
return cljs.core.str.cljs$core$IFn$_invoke$arity$1(new_uri);
}
});
oc.web.images.optimize_slack_org_avatar = (function oc$web$images$optimize_slack_org_avatar(url,preferred_height){
var temp__5733__auto__ = oc.web.images.approximate_slack_org_height(preferred_height);
if(cljs.core.truth_(temp__5733__auto__)){
var approx_height = temp__5733__auto__;
return oc.web.images.optimize_slack_avatar(url,approx_height);
} else {
return url;
}
});
oc.web.images.optimize_slack_user_avatar = (function oc$web$images$optimize_slack_user_avatar(url,preferred_height){
var temp__5733__auto__ = oc.web.images.approximate_slack_user_height(preferred_height);
if(cljs.core.truth_(temp__5733__auto__)){
var approx_height = temp__5733__auto__;
return oc.web.images.optimize_slack_avatar(url,approx_height);
} else {
return url;
}
});
oc.web.images.optimize_image_url = (function oc$web$images$optimize_image_url(url,preferred_height){
return oc.web.images.optimize_filestack_image_url(url,preferred_height);
});
oc.web.images.optimize_user_avatar_url = (function oc$web$images$optimize_user_avatar_url(url,preferred_height){
return oc.web.images.optimize_filestack_image_url(oc.web.images.optimize_slack_user_avatar(url,preferred_height),preferred_height);
});
oc.web.images.optimize_org_avatar_url = (function oc$web$images$optimize_org_avatar_url(url,preferred_height){
return oc.web.images.optimize_filestack_image_url(oc.web.images.optimize_slack_org_avatar(url,preferred_height),preferred_height);
});

//# sourceMappingURL=oc.web.images.js.map
