goog.provide('oc.lib.user');
oc.lib.user.author_logo = (32);
/**
 * Return an on the fly url of the image circle and resized.
 */
oc.lib.user.circle_image = (function oc$lib$user$circle_image(filestack_api_key,image_url,size){
var filestack_static_url = "https://cdn.filestackcontent.com/";
var is_filestack_resource_QMARK_ = clojure.string.starts_with_QMARK_(image_url,filestack_static_url);
var filestack_resource = ((is_filestack_resource_QMARK_)?cljs.core.subs.cljs$core$IFn$_invoke$arity$2(image_url,((filestack_static_url).length)):image_url);
return ["https://process.filestackapi.com/",((is_filestack_resource_QMARK_)?null:[cljs.core.str.cljs$core$IFn$_invoke$arity$1(filestack_api_key),"/"].join('')),"resize=w:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(size),",h:",cljs.core.str.cljs$core$IFn$_invoke$arity$1(size),",fit:crop,align:faces/","circle/",cljs.core.str.cljs$core$IFn$_invoke$arity$1(filestack_resource)].join('');
});
/**
 * 
 *   First it fixes relative URLs, it prepends our production CDN domain to it if it's relative.
 *   Then if the url is pointing to one of our happy faces, it replaces the SVG extension with PNG
 *   to have it resizable. If it's not one of our happy faces, it uses the on-the-fly resize url.
 *   
 */
oc.lib.user.fix_avatar_url = (function oc$lib$user$fix_avatar_url(var_args){
var G__43171 = arguments.length;
switch (G__43171) {
case 3:
return oc.lib.user.fix_avatar_url.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 2:
return oc.lib.user.fix_avatar_url.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.user.fix_avatar_url.cljs$core$IFn$_invoke$arity$3 = (function (filestack_api_key,avatar_url,avatar_size){
var absolute_avatar_url = ((clojure.string.starts_with_QMARK_(avatar_url,"/img"))?["https://d1wc0stj82keig.cloudfront.net",cljs.core.str.cljs$core$IFn$_invoke$arity$1(avatar_url)].join(''):avatar_url);
if(cljs.core.truth_(cljs.core.re_seq(/happy_face_(red|green|blue|purple|yellow).svg$/,absolute_avatar_url))){
return [cljs.core.subs.cljs$core$IFn$_invoke$arity$3(absolute_avatar_url,(0),(cljs.core.count(absolute_avatar_url) - (3))),"png"].join('');
} else {
return oc.lib.user.circle_image(filestack_api_key,absolute_avatar_url,avatar_size);
}
}));

(oc.lib.user.fix_avatar_url.cljs$core$IFn$_invoke$arity$2 = (function (filestack_api_key,avatar_url){
return oc.lib.user.fix_avatar_url.cljs$core$IFn$_invoke$arity$3(filestack_api_key,avatar_url,oc.lib.user.author_logo);
}));

(oc.lib.user.fix_avatar_url.cljs$lang$maxFixedArity = 3);

/**
 * 
 *   Make a single `name` field from `first-name` and/or `last-name`.
 * 
 *   Use email as the name if the entire user is provided and there's no first or last name.
 *   
 * @param {...*} var_args
 */
oc.lib.user.name_for = (function() { 
var oc$lib$user$name_for__delegate = function (args__41272__auto__){
var ocr_43174 = cljs.core.vec(args__41272__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_43174)) && ((cljs.core.count(ocr_43174) === 1)))){
try{var ocr_43174_0__43176 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
if((function (p1__43172_SHARP_){
return ((clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(p1__43172_SHARP_))) && (clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(p1__43172_SHARP_))) && (clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(p1__43172_SHARP_))));
})(ocr_43174_0__43176)){
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
var G__43194 = new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user);
var G__43195 = "";
return (oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$2 ? oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$2(G__43194,G__43195) : oc.lib.user.name_for.call(null,G__43194,G__43195));
} else {
throw cljs.core.match.backtrack;

}
}catch (e43184){if((e43184 instanceof Error)){
var e__40179__auto__ = e43184;
if((e__40179__auto__ === cljs.core.match.backtrack)){
try{var ocr_43174_0__43176 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
if((function (p1__43173_SHARP_){
return (!(clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(p1__43173_SHARP_))));
})(ocr_43174_0__43176)){
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
var G__43192 = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user);
var G__43193 = "";
return (oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$2 ? oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$2(G__43192,G__43193) : oc.lib.user.name_for.call(null,G__43192,G__43193));
} else {
throw cljs.core.match.backtrack;

}
}catch (e43185){if((e43185 instanceof Error)){
var e__40179__auto____$1 = e43185;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
var G__43187 = new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user);
var G__43188 = new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user);
return (oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$2 ? oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$2(G__43187,G__43188) : oc.lib.user.name_for.call(null,G__43187,G__43188));
} else {
throw e__40179__auto____$1;
}
} else {
throw e43185;

}
}} else {
throw e__40179__auto__;
}
} else {
throw e43184;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e43179){if((e43179 instanceof Error)){
var e__40179__auto__ = e43179;
if((e__40179__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_43174)) && ((cljs.core.count(ocr_43174) === 2)))){
try{var ocr_43174_1__43178 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(1));
if(clojure.string.blank_QMARK_(ocr_43174_1__43178)){
try{var ocr_43174_0__43177 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
if(clojure.string.blank_QMARK_(ocr_43174_0__43177)){
var _first_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
var _last_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(1));
return "";
} else {
throw cljs.core.match.backtrack;

}
}catch (e43183){if((e43183 instanceof Error)){
var e__40179__auto____$1 = e43183;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
var first_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
var _last_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(1));
return first_name;
} else {
throw e__40179__auto____$1;
}
} else {
throw e43183;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e43181){if((e43181 instanceof Error)){
var e__40179__auto____$1 = e43181;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_43174_0__43177 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
if(clojure.string.blank_QMARK_(ocr_43174_0__43177)){
var _first_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
var last_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(1));
return last_name;
} else {
throw cljs.core.match.backtrack;

}
}catch (e43182){if((e43182 instanceof Error)){
var e__40179__auto____$2 = e43182;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
var first_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(0));
var last_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43174,(1));
return clojure.string.trim([cljs.core.str.cljs$core$IFn$_invoke$arity$1(first_name)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(last_name)].join(''));
} else {
throw e__40179__auto____$2;
}
} else {
throw e43182;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e43181;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e43180){if((e43180 instanceof Error)){
var e__40179__auto____$1 = e43180;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_43174)].join('')));
} else {
throw e__40179__auto____$1;
}
} else {
throw e43180;

}
}} else {
throw e__40179__auto__;
}
} else {
throw e43179;

}
}};
var oc$lib$user$name_for = function (var_args){
var args__41272__auto__ = null;
if (arguments.length > 0) {
var G__43233__i = 0, G__43233__a = new Array(arguments.length -  0);
while (G__43233__i < G__43233__a.length) {G__43233__a[G__43233__i] = arguments[G__43233__i + 0]; ++G__43233__i;}
  args__41272__auto__ = new cljs.core.IndexedSeq(G__43233__a,0,null);
} 
return oc$lib$user$name_for__delegate.call(this,args__41272__auto__);};
oc$lib$user$name_for.cljs$lang$maxFixedArity = 0;
oc$lib$user$name_for.cljs$lang$applyTo = (function (arglist__43234){
var args__41272__auto__ = cljs.core.seq(arglist__43234);
return oc$lib$user$name_for__delegate(args__41272__auto__);
});
oc$lib$user$name_for.cljs$core$IFn$_invoke$arity$variadic = oc$lib$user$name_for__delegate;
return oc$lib$user$name_for;
})()
;
/**
 * 
 *   Select the first available between: `first-name`, `last-name` or `name`.
 * 
 *   Fallback to `email` if none are available.
 *   
 * @param {...*} var_args
 */
oc.lib.user.short_name_for = (function() { 
var oc$lib$user$short_name_for__delegate = function (args__41272__auto__){
var ocr_43199 = cljs.core.vec(args__41272__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_43199)) && ((cljs.core.count(ocr_43199) === 1)))){
try{var ocr_43199_0__43201 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43199,(0));
if((function (p1__43196_SHARP_){
return (!(clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(p1__43196_SHARP_))));
})(ocr_43199_0__43201)){
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43199,(0));
return new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(user);
} else {
throw cljs.core.match.backtrack;

}
}catch (e43207){if((e43207 instanceof Error)){
var e__40179__auto__ = e43207;
if((e__40179__auto__ === cljs.core.match.backtrack)){
try{var ocr_43199_0__43201 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43199,(0));
if((function (p1__43197_SHARP_){
return (!(clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(p1__43197_SHARP_))));
})(ocr_43199_0__43201)){
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43199,(0));
return new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(user);
} else {
throw cljs.core.match.backtrack;

}
}catch (e43209){if((e43209 instanceof Error)){
var e__40179__auto____$1 = e43209;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{var ocr_43199_0__43201 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43199,(0));
if((function (p1__43198_SHARP_){
return (!(clojure.string.blank_QMARK_(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(p1__43198_SHARP_))));
})(ocr_43199_0__43201)){
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43199,(0));
return new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(user);
} else {
throw cljs.core.match.backtrack;

}
}catch (e43210){if((e43210 instanceof Error)){
var e__40179__auto____$2 = e43210;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
var user = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_43199,(0));
return oc.lib.user.name_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(user),""], 0));
} else {
throw e__40179__auto____$2;
}
} else {
throw e43210;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e43209;

}
}} else {
throw e__40179__auto__;
}
} else {
throw e43207;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e43203){if((e43203 instanceof Error)){
var e__40179__auto__ = e43203;
if((e__40179__auto__ === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_43199)].join('')));
} else {
throw e__40179__auto__;
}
} else {
throw e43203;

}
}};
var oc$lib$user$short_name_for = function (var_args){
var args__41272__auto__ = null;
if (arguments.length > 0) {
var G__43237__i = 0, G__43237__a = new Array(arguments.length -  0);
while (G__43237__i < G__43237__a.length) {G__43237__a[G__43237__i] = arguments[G__43237__i + 0]; ++G__43237__i;}
  args__41272__auto__ = new cljs.core.IndexedSeq(G__43237__a,0,null);
} 
return oc$lib$user$short_name_for__delegate.call(this,args__41272__auto__);};
oc$lib$user$short_name_for.cljs$lang$maxFixedArity = 0;
oc$lib$user$short_name_for.cljs$lang$applyTo = (function (arglist__43238){
var args__41272__auto__ = cljs.core.seq(arglist__43238);
return oc$lib$user$short_name_for__delegate(args__41272__auto__);
});
oc$lib$user$short_name_for.cljs$core$IFn$_invoke$arity$variadic = oc$lib$user$short_name_for__delegate;
return oc$lib$user$short_name_for;
})()
;

//# sourceMappingURL=oc.lib.user.js.map
