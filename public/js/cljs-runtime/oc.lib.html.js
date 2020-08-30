goog.provide('oc.lib.html');
oc.lib.html.thumbnail_elements = (function oc$lib$html$thumbnail_elements(body){
var thumbnail_selector = "img:not(.emojione):not([data-media-type='image/gif']), iframe";
var $body = $(["<div>",cljs.core.str.cljs$core$IFn$_invoke$arity$1(body),"</div>"].join(''));
var els = cljs.core.js__GT_clj.cljs$core$IFn$_invoke$arity$1($(thumbnail_selector,$body));
return new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"elements","elements",657646735),els,new cljs.core.Keyword(null,"count","count",2139924085),els.length], null);
});
oc.lib.html.$el = (function oc$lib$html$$el(el){
return $(el);
});
oc.lib.html.tag_name = (function oc$lib$html$tag_name(el){
return el.tagName;
});
oc.lib.html.read_size = (function oc$lib$html$read_size(size){
return size;
});
/**
 * 
 *   Given an entry body get the first thumbnail available.
 *   Thumbnail type: image, video or chart.
 *   This rely on the similitudes between jQuery and soup parsed objects like the attr function.
 *   
 */
oc.lib.html.first_body_thumbnail = (function oc$lib$html$first_body_thumbnail(html_body){
var map__48159 = oc.lib.html.thumbnail_elements(html_body);
var map__48159__$1 = (((((!((map__48159 == null))))?(((((map__48159.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__48159.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__48159):map__48159);
var els_count = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__48159__$1,new cljs.core.Keyword(null,"count","count",2139924085));
var thumb_els = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__48159__$1,new cljs.core.Keyword(null,"elements","elements",657646735));
var found = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null);
var n__4613__auto___48167 = els_count;
var el_num_48168 = (0);
while(true){
if((el_num_48168 < n__4613__auto___48167)){
var el_48169 = (thumb_els[el_num_48168]);
var $el_48170 = oc.lib.html.$el(el_48169);
if(cljs.core.truth_(cljs.core.deref(found))){
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(cuerdas.core.lower(oc.lib.html.tag_name(el_48169)),"img")){
var width_48173 = oc.lib.html.read_size($el_48170.attr("width"));
var height_48174 = oc.lib.html.read_size($el_48170.attr("height"));
if(((cljs.core.not(cljs.core.deref(found))) && ((((width_48173 <= (height_48174 * (2)))) || ((height_48174 <= (width_48173 * (2)))))))){
cljs.core.reset_BANG_(found,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),"image",new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798),(cljs.core.truth_($el_48170.attr("data-thumbnail"))?$el_48170.attr("data-thumbnail"):$el_48170.attr("src"))], null));
} else {
}
} else {
cljs.core.reset_BANG_(found,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"type","type",1174270348),$el_48170.attr("data-media-type"),new cljs.core.Keyword(null,"thumbnail","thumbnail",-867906798),$el_48170.attr("data-thumbnail")], null));
}
}

var G__48175 = (el_num_48168 + (1));
el_num_48168 = G__48175;
continue;
} else {
}
break;
}

return cljs.core.deref(found);
});
oc.lib.html.allowed_block_elements = new cljs.core.PersistentVector(null, 8, 5, cljs.core.PersistentVector.EMPTY_NODE, ["span","img","a","iframe","pre","code","div","mark"], null);

//# sourceMappingURL=oc.lib.html.js.map
