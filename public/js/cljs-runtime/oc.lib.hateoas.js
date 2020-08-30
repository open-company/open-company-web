goog.provide('oc.lib.hateoas');
oc.lib.hateoas.OPTIONS = "OPTIONS";
oc.lib.hateoas.HEAD = "HEAD";
oc.lib.hateoas.GET = "GET";
oc.lib.hateoas.POST = "POST";
oc.lib.hateoas.PUT = "PUT";
oc.lib.hateoas.PATCH = "PATCH";
oc.lib.hateoas.DELETE = "DELETE";
oc.lib.hateoas.http_methods = cljs.core.PersistentHashSet.createAsIfByAssoc([oc.lib.hateoas.PUT,oc.lib.hateoas.POST,oc.lib.hateoas.DELETE,oc.lib.hateoas.GET,oc.lib.hateoas.HEAD,oc.lib.hateoas.PATCH,oc.lib.hateoas.OPTIONS]);
oc.lib.hateoas.json_collection_version = "1.0";
/**
 * Ensure media types is either a map with :accept and/or :content-type keys, or nothing
 */
oc.lib.hateoas.media_types_QMARK_ = (function oc$lib$hateoas$media_types_QMARK_(media_types){
return (((((media_types == null)) || (cljs.core.map_QMARK_(media_types)))) && (clojure.set.subset_QMARK_(cljs.core.keys(media_types),new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"content-type","content-type",-508222634),null,new cljs.core.Keyword(null,"accept","accept",1874130431),null], null), null))));
});
/**
 * 
 *   Create a HATEOAS link for the specified relation, HTTP method, URL, and media-type.
 * 
 *   Any additional key/values will be included as additional properties of the link.
 *   
 */
oc.lib.hateoas.link_map = (function oc$lib$hateoas$link_map(var_args){
var G__41442 = arguments.length;
switch (G__41442) {
case 4:
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
case 5:
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$4 = (function (rel,method,url,media_types){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5(rel,method,url,media_types,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5 = (function (rel,method,url,media_types,others){
if(typeof rel === 'string'){
} else {
throw (new Error("Assert failed: (string? rel)"));
}

if(cljs.core.truth_((oc.lib.hateoas.http_methods.cljs$core$IFn$_invoke$arity$1 ? oc.lib.hateoas.http_methods.cljs$core$IFn$_invoke$arity$1(method) : oc.lib.hateoas.http_methods.call(null,method)))){
} else {
throw (new Error("Assert failed: (http-methods method)"));
}

if(typeof url === 'string'){
} else {
throw (new Error("Assert failed: (string? url)"));
}

if(oc.lib.hateoas.media_types_QMARK_(media_types)){
} else {
throw (new Error("Assert failed: (media-types? media-types)"));
}

if(cljs.core.map_QMARK_(others)){
} else {
throw (new Error("Assert failed: (map? others)"));
}

var link_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"rel","rel",1378823488),rel,new cljs.core.Keyword(null,"method","method",55703592),method,new cljs.core.Keyword(null,"href","href",-793805698),url], null),others], 0));
var accept = new cljs.core.Keyword(null,"accept","accept",1874130431).cljs$core$IFn$_invoke$arity$1(media_types);
var accept_link_map = (cljs.core.truth_(accept)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(link_map,new cljs.core.Keyword(null,"accept","accept",1874130431),accept):link_map);
var content_type = new cljs.core.Keyword(null,"content-type","content-type",-508222634).cljs$core$IFn$_invoke$arity$1(media_types);
if(cljs.core.truth_(content_type)){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(accept_link_map,new cljs.core.Keyword(null,"content-type","content-type",-508222634),content_type);
} else {
return accept_link_map;
}
}));

(oc.lib.hateoas.link_map.cljs$lang$maxFixedArity = 5);

/**
 * Link that points back to the resource itself.
 */
oc.lib.hateoas.self_link = (function oc$lib$hateoas$self_link(var_args){
var G__41448 = arguments.length;
switch (G__41448) {
case 2:
return oc.lib.hateoas.self_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.lib.hateoas.self_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.self_link.cljs$core$IFn$_invoke$arity$2 = (function (url,media_type){
return oc.lib.hateoas.self_link.cljs$core$IFn$_invoke$arity$3(url,media_type,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.self_link.cljs$core$IFn$_invoke$arity$3 = (function (url,media_type,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("self",oc.lib.hateoas.GET,url,media_type,others);
}));

(oc.lib.hateoas.self_link.cljs$lang$maxFixedArity = 3);

/**
 * Link that points to a collection (list) of items.
 */
oc.lib.hateoas.collection_link = (function oc$lib$hateoas$collection_link(var_args){
var G__41452 = arguments.length;
switch (G__41452) {
case 2:
return oc.lib.hateoas.collection_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.lib.hateoas.collection_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.collection_link.cljs$core$IFn$_invoke$arity$2 = (function (url,media_type){
return oc.lib.hateoas.collection_link.cljs$core$IFn$_invoke$arity$3(url,media_type,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.collection_link.cljs$core$IFn$_invoke$arity$3 = (function (url,media_type,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("collection",oc.lib.hateoas.GET,url,media_type,others);
}));

(oc.lib.hateoas.collection_link.cljs$lang$maxFixedArity = 3);

/**
 * Link that points to an individual item in a collection.
 */
oc.lib.hateoas.item_link = (function oc$lib$hateoas$item_link(var_args){
var G__41454 = arguments.length;
switch (G__41454) {
case 2:
return oc.lib.hateoas.item_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.lib.hateoas.item_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.item_link.cljs$core$IFn$_invoke$arity$2 = (function (url,media_type){
return oc.lib.hateoas.item_link.cljs$core$IFn$_invoke$arity$3(url,media_type,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.item_link.cljs$core$IFn$_invoke$arity$3 = (function (url,media_type,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$4("item",oc.lib.hateoas.GET,url,media_type);
}));

(oc.lib.hateoas.item_link.cljs$lang$maxFixedArity = 3);

/**
 * Link that points to the parent collection that contains this item.
 */
oc.lib.hateoas.up_link = (function oc$lib$hateoas$up_link(var_args){
var G__41458 = arguments.length;
switch (G__41458) {
case 2:
return oc.lib.hateoas.up_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.lib.hateoas.up_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.up_link.cljs$core$IFn$_invoke$arity$2 = (function (url,media_type){
return oc.lib.hateoas.up_link.cljs$core$IFn$_invoke$arity$3(url,media_type,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.up_link.cljs$core$IFn$_invoke$arity$3 = (function (url,media_type,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("up",oc.lib.hateoas.GET,url,media_type,others);
}));

(oc.lib.hateoas.up_link.cljs$lang$maxFixedArity = 3);

/**
 * Link to add an existing item to a collection.
 */
oc.lib.hateoas.add_link = (function oc$lib$hateoas$add_link(var_args){
var G__41460 = arguments.length;
switch (G__41460) {
case 3:
return oc.lib.hateoas.add_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return oc.lib.hateoas.add_link.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.add_link.cljs$core$IFn$_invoke$arity$3 = (function (method,url,media_type){
return oc.lib.hateoas.add_link.cljs$core$IFn$_invoke$arity$4(method,url,media_type,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.add_link.cljs$core$IFn$_invoke$arity$4 = (function (method,url,media_type,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("add",method,url,media_type,others);
}));

(oc.lib.hateoas.add_link.cljs$lang$maxFixedArity = 4);

/**
 * Link to remove an item from a collection.
 */
oc.lib.hateoas.remove_link = (function oc$lib$hateoas$remove_link(var_args){
var G__41462 = arguments.length;
switch (G__41462) {
case 1:
return oc.lib.hateoas.remove_link.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.lib.hateoas.remove_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.lib.hateoas.remove_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.remove_link.cljs$core$IFn$_invoke$arity$1 = (function (url){
return oc.lib.hateoas.remove_link.cljs$core$IFn$_invoke$arity$3(url,cljs.core.PersistentArrayMap.EMPTY,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.remove_link.cljs$core$IFn$_invoke$arity$2 = (function (url,media_type){
return oc.lib.hateoas.remove_link.cljs$core$IFn$_invoke$arity$3(url,media_type,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.remove_link.cljs$core$IFn$_invoke$arity$3 = (function (url,media_type,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("remove",oc.lib.hateoas.DELETE,url,media_type,others);
}));

(oc.lib.hateoas.remove_link.cljs$lang$maxFixedArity = 3);

/**
 * Link to create a new resource.
 */
oc.lib.hateoas.create_link = (function oc$lib$hateoas$create_link(var_args){
var G__41466 = arguments.length;
switch (G__41466) {
case 2:
return oc.lib.hateoas.create_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.lib.hateoas.create_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.create_link.cljs$core$IFn$_invoke$arity$2 = (function (url,media_type){
return oc.lib.hateoas.create_link.cljs$core$IFn$_invoke$arity$3(url,media_type,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.create_link.cljs$core$IFn$_invoke$arity$3 = (function (url,media_type,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("create",oc.lib.hateoas.POST,url,media_type,others);
}));

(oc.lib.hateoas.create_link.cljs$lang$maxFixedArity = 3);

/**
 * Link to replace an existing resource with new content.
 */
oc.lib.hateoas.update_link = (function oc$lib$hateoas$update_link(var_args){
var G__41468 = arguments.length;
switch (G__41468) {
case 2:
return oc.lib.hateoas.update_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.lib.hateoas.update_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.update_link.cljs$core$IFn$_invoke$arity$2 = (function (url,media_types){
return oc.lib.hateoas.update_link.cljs$core$IFn$_invoke$arity$3(url,media_types,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.update_link.cljs$core$IFn$_invoke$arity$3 = (function (url,media_types,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("update",oc.lib.hateoas.PUT,url,media_types,others);
}));

(oc.lib.hateoas.update_link.cljs$lang$maxFixedArity = 3);

/**
 * Link to update an existing resource with a fragment of content that's merged into the existing content.
 */
oc.lib.hateoas.partial_update_link = (function oc$lib$hateoas$partial_update_link(var_args){
var G__41470 = arguments.length;
switch (G__41470) {
case 2:
return oc.lib.hateoas.partial_update_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return oc.lib.hateoas.partial_update_link.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.partial_update_link.cljs$core$IFn$_invoke$arity$2 = (function (url,media_types){
return oc.lib.hateoas.partial_update_link.cljs$core$IFn$_invoke$arity$3(url,media_types,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.partial_update_link.cljs$core$IFn$_invoke$arity$3 = (function (url,media_types,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("partial-update",oc.lib.hateoas.PATCH,url,media_types,others);
}));

(oc.lib.hateoas.partial_update_link.cljs$lang$maxFixedArity = 3);

/**
 * Link to delete an existing resource.
 */
oc.lib.hateoas.delete_link = (function oc$lib$hateoas$delete_link(var_args){
var G__41472 = arguments.length;
switch (G__41472) {
case 1:
return oc.lib.hateoas.delete_link.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.lib.hateoas.delete_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.delete_link.cljs$core$IFn$_invoke$arity$1 = (function (url){
return oc.lib.hateoas.delete_link.cljs$core$IFn$_invoke$arity$2(url,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.delete_link.cljs$core$IFn$_invoke$arity$2 = (function (url,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("delete",oc.lib.hateoas.DELETE,url,cljs.core.PersistentArrayMap.EMPTY,others);
}));

(oc.lib.hateoas.delete_link.cljs$lang$maxFixedArity = 2);

/**
 * Link to archive an existing resource.
 */
oc.lib.hateoas.archive_link = (function oc$lib$hateoas$archive_link(var_args){
var G__41474 = arguments.length;
switch (G__41474) {
case 1:
return oc.lib.hateoas.archive_link.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return oc.lib.hateoas.archive_link.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(oc.lib.hateoas.archive_link.cljs$core$IFn$_invoke$arity$1 = (function (url){
return oc.lib.hateoas.archive_link.cljs$core$IFn$_invoke$arity$2(url,cljs.core.PersistentArrayMap.EMPTY);
}));

(oc.lib.hateoas.archive_link.cljs$core$IFn$_invoke$arity$2 = (function (url,others){
return oc.lib.hateoas.link_map.cljs$core$IFn$_invoke$arity$5("archive",oc.lib.hateoas.DELETE,url,cljs.core.PersistentArrayMap.EMPTY,others);
}));

(oc.lib.hateoas.archive_link.cljs$lang$maxFixedArity = 2);

/**
 * Given a link with an :href and a :replace map,
 * and given a replacements map, apply the replacements to the link.
 */
oc.lib.hateoas.link_replace_href = (function oc$lib$hateoas$link_replace_href(link,replacements){
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(link,new cljs.core.Keyword(null,"href","href",-793805698),(function (p1__41475_SHARP_){
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (href,p__41476){
var vec__41477 = p__41476;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41477,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__41477,(1),null);
return cuerdas.core.replace(href,v,cljs.core.get.cljs$core$IFn$_invoke$arity$2(replacements,k));
}),p1__41475_SHARP_,new cljs.core.Keyword(null,"replace","replace",-786587770).cljs$core$IFn$_invoke$arity$1(link));
}));
});
/**
 * Truthy if the provided value is a string or a keyword.
 */
oc.lib.hateoas.s_or_k_QMARK_ = (function oc$lib$hateoas$s_or_k_QMARK_(value){
return ((typeof value === 'string') || ((value instanceof cljs.core.Keyword)));
});
oc.lib.hateoas.nil_or_map_QMARK_ = (function oc$lib$hateoas$nil_or_map_QMARK_(v){
return (((v == null)) || (cljs.core.map_QMARK_(v)));
});
oc.lib.hateoas.check_params = (function oc$lib$hateoas$check_params(link,params){
return (((params == null)) || (cljs.core.every_QMARK_((function (p1__41480_SHARP_){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(p1__41480_SHARP_,new cljs.core.Keyword(null,"replace","replace",-786587770))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cljs.core.get.cljs$core$IFn$_invoke$arity$2(link,p1__41480_SHARP_),cljs.core.get.cljs$core$IFn$_invoke$arity$2(params,p1__41480_SHARP_))));
}),cljs.core.keys(params))));
});
/**
 * @param {...*} var_args
 */
oc.lib.hateoas.link_for = (function() { 
var oc$lib$hateoas$link_for__delegate = function (args__41272__auto__){
var ocr_41488 = cljs.core.vec(args__41272__auto__);
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) >= (1))))){
try{var ocr_41488_left__41500 = cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(ocr_41488,(0),(1));
if(((cljs.core.vector_QMARK_(ocr_41488_left__41500)) && ((cljs.core.count(ocr_41488_left__41500) === (1))))){
try{var ocr_41488_left__41500_0__41502 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488_left__41500,(0));
if((ocr_41488_left__41500_0__41502 === null)){
var rest = cljs.core.subvec.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
return false;
} else {
throw cljs.core.match.backtrack;

}
}catch (e41658){if((e41658 instanceof Error)){
var e__40179__auto__ = e41658;
if((e__40179__auto__ === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto__;
}
} else {
throw e41658;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41656){if((e41656 instanceof Error)){
var e__40179__auto__ = e41656;
if((e__40179__auto__ === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto__;
}
} else {
throw e41656;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41557){if((e41557 instanceof Error)){
var e__40179__auto__ = e41557;
if((e__40179__auto__ === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 3)))){
try{var ocr_41488_0__41503 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41503)){
try{var ocr_41488_1__41504 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(cljs.core.sequential_QMARK_(ocr_41488_1__41504)){
try{var ocr_41488_2__41505 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(cljs.core.sequential_QMARK_(ocr_41488_2__41505)){
var methods$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rels = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (rel){
return (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$3 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$3(links,rel,methods$) : oc.lib.hateoas.link_for.call(null,links,rel,methods$));
}),rels);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41653){if((e41653 instanceof Error)){
var e__40179__auto____$1 = e41653;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$1;
}
} else {
throw e41653;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41652){if((e41652 instanceof Error)){
var e__40179__auto____$1 = e41652;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$1;
}
} else {
throw e41652;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41649){if((e41649 instanceof Error)){
var e__40179__auto____$1 = e41649;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$1;
}
} else {
throw e41649;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41558){if((e41558 instanceof Error)){
var e__40179__auto____$1 = e41558;
if((e__40179__auto____$1 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 5)))){
try{var ocr_41488_0__41506 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41506)){
try{var ocr_41488_1__41507 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(cljs.core.sequential_QMARK_(ocr_41488_1__41507)){
try{var ocr_41488_2__41508 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(cljs.core.sequential_QMARK_(ocr_41488_2__41508)){
try{var ocr_41488_3__41509 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_3__41509)){
try{var ocr_41488_4__41510 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(4));
if(cljs.core.map_QMARK_(ocr_41488_4__41510)){
var replacements = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(4));
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var methods$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rels = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
var link = (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4(links,rels,methods$,params) : oc.lib.hateoas.link_for.call(null,links,rels,methods$,params));
return oc.lib.hateoas.link_replace_href(link,replacements);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41646){if((e41646 instanceof Error)){
var e__40179__auto____$2 = e41646;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41646;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41644){if((e41644 instanceof Error)){
var e__40179__auto____$2 = e41644;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41644;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41642){if((e41642 instanceof Error)){
var e__40179__auto____$2 = e41642;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41642;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41641){if((e41641 instanceof Error)){
var e__40179__auto____$2 = e41641;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41641;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41639){if((e41639 instanceof Error)){
var e__40179__auto____$2 = e41639;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$2;
}
} else {
throw e41639;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41559){if((e41559 instanceof Error)){
var e__40179__auto____$2 = e41559;
if((e__40179__auto____$2 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 4)))){
try{var ocr_41488_0__41511 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41511)){
try{var ocr_41488_1__41512 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(cljs.core.sequential_QMARK_(ocr_41488_1__41512)){
try{var ocr_41488_2__41513 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(cljs.core.sequential_QMARK_(ocr_41488_2__41513)){
try{var ocr_41488_3__41514 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_3__41514)){
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var methods$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rels = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (rel){
return (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4(links,rel,methods$,params) : oc.lib.hateoas.link_for.call(null,links,rel,methods$,params));
}),rels);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41635){if((e41635 instanceof Error)){
var e__40179__auto____$3 = e41635;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41635;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41634){if((e41634 instanceof Error)){
var e__40179__auto____$3 = e41634;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41634;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41632){if((e41632 instanceof Error)){
var e__40179__auto____$3 = e41632;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41632;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41630){if((e41630 instanceof Error)){
var e__40179__auto____$3 = e41630;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$3;
}
} else {
throw e41630;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41560){if((e41560 instanceof Error)){
var e__40179__auto____$3 = e41560;
if((e__40179__auto____$3 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 3)))){
try{var ocr_41488_0__41515 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41515)){
try{var ocr_41488_1__41516 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(cljs.core.sequential_QMARK_(ocr_41488_1__41516)){
try{var ocr_41488_2__41517 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(typeof ocr_41488_2__41517 === 'string'){
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rels = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (p1__41481_SHARP_){
return (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$3 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$3(links,p1__41481_SHARP_,method) : oc.lib.hateoas.link_for.call(null,links,p1__41481_SHARP_,method));
}),rels);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41627){if((e41627 instanceof Error)){
var e__40179__auto____$4 = e41627;
if((e__40179__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$4;
}
} else {
throw e41627;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41626){if((e41626 instanceof Error)){
var e__40179__auto____$4 = e41626;
if((e__40179__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$4;
}
} else {
throw e41626;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41623){if((e41623 instanceof Error)){
var e__40179__auto____$4 = e41623;
if((e__40179__auto____$4 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$4;
}
} else {
throw e41623;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41561){if((e41561 instanceof Error)){
var e__40179__auto____$4 = e41561;
if((e__40179__auto____$4 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 5)))){
try{var ocr_41488_0__41518 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41518)){
try{var ocr_41488_1__41519 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(cljs.core.sequential_QMARK_(ocr_41488_1__41519)){
try{var ocr_41488_2__41520 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(typeof ocr_41488_2__41520 === 'string'){
try{var ocr_41488_3__41521 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_3__41521)){
try{var ocr_41488_4__41522 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(4));
if(cljs.core.map_QMARK_(ocr_41488_4__41522)){
var replacements = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(4));
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rels = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
var link = (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4(links,rels,method,params) : oc.lib.hateoas.link_for.call(null,links,rels,method,params));
return oc.lib.hateoas.link_replace_href(link,replacements);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41620){if((e41620 instanceof Error)){
var e__40179__auto____$5 = e41620;
if((e__40179__auto____$5 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$5;
}
} else {
throw e41620;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41619){if((e41619 instanceof Error)){
var e__40179__auto____$5 = e41619;
if((e__40179__auto____$5 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$5;
}
} else {
throw e41619;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41618){if((e41618 instanceof Error)){
var e__40179__auto____$5 = e41618;
if((e__40179__auto____$5 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$5;
}
} else {
throw e41618;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41616){if((e41616 instanceof Error)){
var e__40179__auto____$5 = e41616;
if((e__40179__auto____$5 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$5;
}
} else {
throw e41616;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41614){if((e41614 instanceof Error)){
var e__40179__auto____$5 = e41614;
if((e__40179__auto____$5 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$5;
}
} else {
throw e41614;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41562){if((e41562 instanceof Error)){
var e__40179__auto____$5 = e41562;
if((e__40179__auto____$5 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 4)))){
try{var ocr_41488_0__41523 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41523)){
try{var ocr_41488_1__41524 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(cljs.core.sequential_QMARK_(ocr_41488_1__41524)){
try{var ocr_41488_2__41525 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(typeof ocr_41488_2__41525 === 'string'){
try{var ocr_41488_3__41526 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_3__41526)){
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rels = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (p1__41482_SHARP_){
return (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4(links,p1__41482_SHARP_,method,params) : oc.lib.hateoas.link_for.call(null,links,p1__41482_SHARP_,method,params));
}),rels);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41611){if((e41611 instanceof Error)){
var e__40179__auto____$6 = e41611;
if((e__40179__auto____$6 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$6;
}
} else {
throw e41611;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41610){if((e41610 instanceof Error)){
var e__40179__auto____$6 = e41610;
if((e__40179__auto____$6 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$6;
}
} else {
throw e41610;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41609){if((e41609 instanceof Error)){
var e__40179__auto____$6 = e41609;
if((e__40179__auto____$6 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$6;
}
} else {
throw e41609;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41608){if((e41608 instanceof Error)){
var e__40179__auto____$6 = e41608;
if((e__40179__auto____$6 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$6;
}
} else {
throw e41608;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41563){if((e41563 instanceof Error)){
var e__40179__auto____$6 = e41563;
if((e__40179__auto____$6 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 3)))){
try{var ocr_41488_0__41527 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41527)){
try{var ocr_41488_1__41528 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(typeof ocr_41488_1__41528 === 'string'){
try{var ocr_41488_2__41529 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(cljs.core.sequential_QMARK_(ocr_41488_2__41529)){
var methods$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (p1__41483_SHARP_){
return (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$3 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$3(links,rel,p1__41483_SHARP_) : oc.lib.hateoas.link_for.call(null,links,rel,p1__41483_SHARP_));
}),methods$);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41602){if((e41602 instanceof Error)){
var e__40179__auto____$7 = e41602;
if((e__40179__auto____$7 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$7;
}
} else {
throw e41602;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41601){if((e41601 instanceof Error)){
var e__40179__auto____$7 = e41601;
if((e__40179__auto____$7 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$7;
}
} else {
throw e41601;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41600){if((e41600 instanceof Error)){
var e__40179__auto____$7 = e41600;
if((e__40179__auto____$7 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$7;
}
} else {
throw e41600;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41564){if((e41564 instanceof Error)){
var e__40179__auto____$7 = e41564;
if((e__40179__auto____$7 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 5)))){
try{var ocr_41488_0__41530 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41530)){
try{var ocr_41488_1__41531 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(typeof ocr_41488_1__41531 === 'string'){
try{var ocr_41488_2__41532 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(cljs.core.sequential_QMARK_(ocr_41488_2__41532)){
try{var ocr_41488_3__41533 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_3__41533)){
try{var ocr_41488_4__41534 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(4));
if(cljs.core.map_QMARK_(ocr_41488_4__41534)){
var replacements = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(4));
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var methods$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
var link = (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4(links,rel,methods$,params) : oc.lib.hateoas.link_for.call(null,links,rel,methods$,params));
return oc.lib.hateoas.link_replace_href(link,replacements);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41599){if((e41599 instanceof Error)){
var e__40179__auto____$8 = e41599;
if((e__40179__auto____$8 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$8;
}
} else {
throw e41599;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41598){if((e41598 instanceof Error)){
var e__40179__auto____$8 = e41598;
if((e__40179__auto____$8 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$8;
}
} else {
throw e41598;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41597){if((e41597 instanceof Error)){
var e__40179__auto____$8 = e41597;
if((e__40179__auto____$8 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$8;
}
} else {
throw e41597;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41596){if((e41596 instanceof Error)){
var e__40179__auto____$8 = e41596;
if((e__40179__auto____$8 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$8;
}
} else {
throw e41596;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41595){if((e41595 instanceof Error)){
var e__40179__auto____$8 = e41595;
if((e__40179__auto____$8 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$8;
}
} else {
throw e41595;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41565){if((e41565 instanceof Error)){
var e__40179__auto____$8 = e41565;
if((e__40179__auto____$8 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 4)))){
try{var ocr_41488_0__41535 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41535)){
try{var ocr_41488_1__41536 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(typeof ocr_41488_1__41536 === 'string'){
try{var ocr_41488_2__41537 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(cljs.core.sequential_QMARK_(ocr_41488_2__41537)){
try{var ocr_41488_3__41538 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_3__41538)){
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var methods$ = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (p1__41484_SHARP_){
return (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4(links,rel,p1__41484_SHARP_,params) : oc.lib.hateoas.link_for.call(null,links,rel,p1__41484_SHARP_,params));
}),methods$);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41594){if((e41594 instanceof Error)){
var e__40179__auto____$9 = e41594;
if((e__40179__auto____$9 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$9;
}
} else {
throw e41594;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41593){if((e41593 instanceof Error)){
var e__40179__auto____$9 = e41593;
if((e__40179__auto____$9 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$9;
}
} else {
throw e41593;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41592){if((e41592 instanceof Error)){
var e__40179__auto____$9 = e41592;
if((e__40179__auto____$9 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$9;
}
} else {
throw e41592;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41591){if((e41591 instanceof Error)){
var e__40179__auto____$9 = e41591;
if((e__40179__auto____$9 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$9;
}
} else {
throw e41591;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41566){if((e41566 instanceof Error)){
var e__40179__auto____$9 = e41566;
if((e__40179__auto____$9 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 2)))){
try{var ocr_41488_0__41539 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41539)){
try{var ocr_41488_1__41540 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(typeof ocr_41488_1__41540 === 'string'){
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (p1__41485_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(p1__41485_SHARP_),rel)){
return p1__41485_SHARP_;
} else {
return null;
}
}),links);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41590){if((e41590 instanceof Error)){
var e__40179__auto____$10 = e41590;
if((e__40179__auto____$10 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$10;
}
} else {
throw e41590;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41589){if((e41589 instanceof Error)){
var e__40179__auto____$10 = e41589;
if((e__40179__auto____$10 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$10;
}
} else {
throw e41589;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41567){if((e41567 instanceof Error)){
var e__40179__auto____$10 = e41567;
if((e__40179__auto____$10 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 4)))){
try{var ocr_41488_0__41541 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41541)){
try{var ocr_41488_1__41542 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(typeof ocr_41488_1__41542 === 'string'){
try{var ocr_41488_2__41543 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_2__41543)){
try{var ocr_41488_3__41544 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(cljs.core.map_QMARK_(ocr_41488_3__41544)){
var replacements = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
var link = (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$3 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$3(links,rel,params) : oc.lib.hateoas.link_for.call(null,links,rel,params));
return oc.lib.hateoas.link_replace_href(link,replacements);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41588){if((e41588 instanceof Error)){
var e__40179__auto____$11 = e41588;
if((e__40179__auto____$11 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$11;
}
} else {
throw e41588;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41587){if((e41587 instanceof Error)){
var e__40179__auto____$11 = e41587;
if((e__40179__auto____$11 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$11;
}
} else {
throw e41587;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41586){if((e41586 instanceof Error)){
var e__40179__auto____$11 = e41586;
if((e__40179__auto____$11 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$11;
}
} else {
throw e41586;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41585){if((e41585 instanceof Error)){
var e__40179__auto____$11 = e41585;
if((e__40179__auto____$11 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$11;
}
} else {
throw e41585;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41568){if((e41568 instanceof Error)){
var e__40179__auto____$11 = e41568;
if((e__40179__auto____$11 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 3)))){
try{var ocr_41488_0__41545 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41545)){
try{var ocr_41488_1__41546 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(typeof ocr_41488_1__41546 === 'string'){
try{var ocr_41488_2__41547 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_2__41547)){
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (link){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(link),rel)) && (oc.lib.hateoas.check_params(link,params)))){
return link;
} else {
return null;
}
}),links);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41583){if((e41583 instanceof Error)){
var e__40179__auto____$12 = e41583;
if((e__40179__auto____$12 === cljs.core.match.backtrack)){
try{var ocr_41488_2__41547 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(typeof ocr_41488_2__41547 === 'string'){
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (link){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(link),rel)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"method","method",55703592).cljs$core$IFn$_invoke$arity$1(link),method)))){
return link;
} else {
return null;
}
}),links);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41584){if((e41584 instanceof Error)){
var e__40179__auto____$13 = e41584;
if((e__40179__auto____$13 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$13;
}
} else {
throw e41584;

}
}} else {
throw e__40179__auto____$12;
}
} else {
throw e41583;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41582){if((e41582 instanceof Error)){
var e__40179__auto____$12 = e41582;
if((e__40179__auto____$12 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$12;
}
} else {
throw e41582;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41581){if((e41581 instanceof Error)){
var e__40179__auto____$12 = e41581;
if((e__40179__auto____$12 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$12;
}
} else {
throw e41581;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41569){if((e41569 instanceof Error)){
var e__40179__auto____$12 = e41569;
if((e__40179__auto____$12 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 5)))){
try{var ocr_41488_0__41548 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41548)){
try{var ocr_41488_1__41549 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(typeof ocr_41488_1__41549 === 'string'){
try{var ocr_41488_2__41550 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(typeof ocr_41488_2__41550 === 'string'){
try{var ocr_41488_3__41551 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_3__41551)){
try{var ocr_41488_4__41552 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(4));
if(cljs.core.map_QMARK_(ocr_41488_4__41552)){
var replacements = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(4));
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
var link = (oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4 ? oc.lib.hateoas.link_for.cljs$core$IFn$_invoke$arity$4(links,rel,method,params) : oc.lib.hateoas.link_for.call(null,links,rel,method,params));
return oc.lib.hateoas.link_replace_href(link,replacements);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41580){if((e41580 instanceof Error)){
var e__40179__auto____$13 = e41580;
if((e__40179__auto____$13 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$13;
}
} else {
throw e41580;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41579){if((e41579 instanceof Error)){
var e__40179__auto____$13 = e41579;
if((e__40179__auto____$13 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$13;
}
} else {
throw e41579;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41578){if((e41578 instanceof Error)){
var e__40179__auto____$13 = e41578;
if((e__40179__auto____$13 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$13;
}
} else {
throw e41578;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41577){if((e41577 instanceof Error)){
var e__40179__auto____$13 = e41577;
if((e__40179__auto____$13 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$13;
}
} else {
throw e41577;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41576){if((e41576 instanceof Error)){
var e__40179__auto____$13 = e41576;
if((e__40179__auto____$13 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$13;
}
} else {
throw e41576;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41570){if((e41570 instanceof Error)){
var e__40179__auto____$13 = e41570;
if((e__40179__auto____$13 === cljs.core.match.backtrack)){
try{if(((cljs.core.vector_QMARK_(ocr_41488)) && ((cljs.core.count(ocr_41488) === 4)))){
try{var ocr_41488_0__41553 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
if(cljs.core.sequential_QMARK_(ocr_41488_0__41553)){
try{var ocr_41488_1__41554 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
if(typeof ocr_41488_1__41554 === 'string'){
try{var ocr_41488_2__41555 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
if(typeof ocr_41488_2__41555 === 'string'){
try{var ocr_41488_3__41556 = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
if(oc.lib.hateoas.nil_or_map_QMARK_(ocr_41488_3__41556)){
var params = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(3));
var method = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(2));
var rel = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(1));
var links = cljs.core.nth.cljs$core$IFn$_invoke$arity$2(ocr_41488,(0));
return cljs.core.some((function (link){
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(link),rel)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"method","method",55703592).cljs$core$IFn$_invoke$arity$1(link),method)) && (oc.lib.hateoas.check_params(link,params)))){
return link;
} else {
return null;
}
}),links);
} else {
throw cljs.core.match.backtrack;

}
}catch (e41575){if((e41575 instanceof Error)){
var e__40179__auto____$14 = e41575;
if((e__40179__auto____$14 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$14;
}
} else {
throw e41575;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41574){if((e41574 instanceof Error)){
var e__40179__auto____$14 = e41574;
if((e__40179__auto____$14 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$14;
}
} else {
throw e41574;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41573){if((e41573 instanceof Error)){
var e__40179__auto____$14 = e41573;
if((e__40179__auto____$14 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$14;
}
} else {
throw e41573;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41572){if((e41572 instanceof Error)){
var e__40179__auto____$14 = e41572;
if((e__40179__auto____$14 === cljs.core.match.backtrack)){
throw cljs.core.match.backtrack;
} else {
throw e__40179__auto____$14;
}
} else {
throw e41572;

}
}} else {
throw cljs.core.match.backtrack;

}
}catch (e41571){if((e41571 instanceof Error)){
var e__40179__auto____$14 = e41571;
if((e__40179__auto____$14 === cljs.core.match.backtrack)){
throw (new Error(["No matching clause: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ocr_41488)].join('')));
} else {
throw e__40179__auto____$14;
}
} else {
throw e41571;

}
}} else {
throw e__40179__auto____$13;
}
} else {
throw e41570;

}
}} else {
throw e__40179__auto____$12;
}
} else {
throw e41569;

}
}} else {
throw e__40179__auto____$11;
}
} else {
throw e41568;

}
}} else {
throw e__40179__auto____$10;
}
} else {
throw e41567;

}
}} else {
throw e__40179__auto____$9;
}
} else {
throw e41566;

}
}} else {
throw e__40179__auto____$8;
}
} else {
throw e41565;

}
}} else {
throw e__40179__auto____$7;
}
} else {
throw e41564;

}
}} else {
throw e__40179__auto____$6;
}
} else {
throw e41563;

}
}} else {
throw e__40179__auto____$5;
}
} else {
throw e41562;

}
}} else {
throw e__40179__auto____$4;
}
} else {
throw e41561;

}
}} else {
throw e__40179__auto____$3;
}
} else {
throw e41560;

}
}} else {
throw e__40179__auto____$2;
}
} else {
throw e41559;

}
}} else {
throw e__40179__auto____$1;
}
} else {
throw e41558;

}
}} else {
throw e__40179__auto__;
}
} else {
throw e41557;

}
}};
var oc$lib$hateoas$link_for = function (var_args){
var args__41272__auto__ = null;
if (arguments.length > 0) {
var G__41789__i = 0, G__41789__a = new Array(arguments.length -  0);
while (G__41789__i < G__41789__a.length) {G__41789__a[G__41789__i] = arguments[G__41789__i + 0]; ++G__41789__i;}
  args__41272__auto__ = new cljs.core.IndexedSeq(G__41789__a,0,null);
} 
return oc$lib$hateoas$link_for__delegate.call(this,args__41272__auto__);};
oc$lib$hateoas$link_for.cljs$lang$maxFixedArity = 0;
oc$lib$hateoas$link_for.cljs$lang$applyTo = (function (arglist__41790){
var args__41272__auto__ = cljs.core.seq(arglist__41790);
return oc$lib$hateoas$link_for__delegate(args__41272__auto__);
});
oc$lib$hateoas$link_for.cljs$core$IFn$_invoke$arity$variadic = oc$lib$hateoas$link_for__delegate;
return oc$lib$hateoas$link_for;
})()
;

//# sourceMappingURL=oc.lib.hateoas.js.map
