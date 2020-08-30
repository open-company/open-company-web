goog.provide('shadow.dom');
shadow.dom.transition_supported_QMARK_ = (((typeof window !== 'undefined'))?goog.style.transition.isSupported():null);

/**
 * @interface
 */
shadow.dom.IElement = function(){};

var shadow$dom$IElement$_to_dom$dyn_34211 = (function (this$){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (shadow.dom._to_dom[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4429__auto__.call(null,this$));
} else {
var m__4426__auto__ = (shadow.dom._to_dom["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4426__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("IElement.-to-dom",this$);
}
}
});
shadow.dom._to_dom = (function shadow$dom$_to_dom(this$){
if((((!((this$ == null)))) && ((!((this$.shadow$dom$IElement$_to_dom$arity$1 == null)))))){
return this$.shadow$dom$IElement$_to_dom$arity$1(this$);
} else {
return shadow$dom$IElement$_to_dom$dyn_34211(this$);
}
});


/**
 * @interface
 */
shadow.dom.SVGElement = function(){};

var shadow$dom$SVGElement$_to_svg$dyn_34212 = (function (this$){
var x__4428__auto__ = (((this$ == null))?null:this$);
var m__4429__auto__ = (shadow.dom._to_svg[goog.typeOf(x__4428__auto__)]);
if((!((m__4429__auto__ == null)))){
return (m__4429__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4429__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4429__auto__.call(null,this$));
} else {
var m__4426__auto__ = (shadow.dom._to_svg["_"]);
if((!((m__4426__auto__ == null)))){
return (m__4426__auto__.cljs$core$IFn$_invoke$arity$1 ? m__4426__auto__.cljs$core$IFn$_invoke$arity$1(this$) : m__4426__auto__.call(null,this$));
} else {
throw cljs.core.missing_protocol("SVGElement.-to-svg",this$);
}
}
});
shadow.dom._to_svg = (function shadow$dom$_to_svg(this$){
if((((!((this$ == null)))) && ((!((this$.shadow$dom$SVGElement$_to_svg$arity$1 == null)))))){
return this$.shadow$dom$SVGElement$_to_svg$arity$1(this$);
} else {
return shadow$dom$SVGElement$_to_svg$dyn_34212(this$);
}
});

shadow.dom.lazy_native_coll_seq = (function shadow$dom$lazy_native_coll_seq(coll,idx){
if((idx < coll.length)){
return (new cljs.core.LazySeq(null,(function (){
return cljs.core.cons((coll[idx]),(function (){var G__33424 = coll;
var G__33425 = (idx + (1));
return (shadow.dom.lazy_native_coll_seq.cljs$core$IFn$_invoke$arity$2 ? shadow.dom.lazy_native_coll_seq.cljs$core$IFn$_invoke$arity$2(G__33424,G__33425) : shadow.dom.lazy_native_coll_seq.call(null,G__33424,G__33425));
})());
}),null,null));
} else {
return null;
}
});

/**
* @constructor
 * @implements {cljs.core.IIndexed}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IDeref}
 * @implements {shadow.dom.IElement}
*/
shadow.dom.NativeColl = (function (coll){
this.coll = coll;
this.cljs$lang$protocol_mask$partition0$ = 8421394;
this.cljs$lang$protocol_mask$partition1$ = 0;
});
(shadow.dom.NativeColl.prototype.cljs$core$IDeref$_deref$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll;
}));

(shadow.dom.NativeColl.prototype.cljs$core$IIndexed$_nth$arity$2 = (function (this$,n){
var self__ = this;
var this$__$1 = this;
return (self__.coll[n]);
}));

(shadow.dom.NativeColl.prototype.cljs$core$IIndexed$_nth$arity$3 = (function (this$,n,not_found){
var self__ = this;
var this$__$1 = this;
var or__4126__auto__ = (self__.coll[n]);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return not_found;
}
}));

(shadow.dom.NativeColl.prototype.cljs$core$ICounted$_count$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll.length;
}));

(shadow.dom.NativeColl.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return shadow.dom.lazy_native_coll_seq(self__.coll,(0));
}));

(shadow.dom.NativeColl.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(shadow.dom.NativeColl.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var self__ = this;
var this$__$1 = this;
return self__.coll;
}));

(shadow.dom.NativeColl.getBasis = (function (){
return new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"coll","coll",-1006698606,null)], null);
}));

(shadow.dom.NativeColl.cljs$lang$type = true);

(shadow.dom.NativeColl.cljs$lang$ctorStr = "shadow.dom/NativeColl");

(shadow.dom.NativeColl.cljs$lang$ctorPrWriter = (function (this__4369__auto__,writer__4370__auto__,opt__4371__auto__){
return cljs.core._write(writer__4370__auto__,"shadow.dom/NativeColl");
}));

/**
 * Positional factory function for shadow.dom/NativeColl.
 */
shadow.dom.__GT_NativeColl = (function shadow$dom$__GT_NativeColl(coll){
return (new shadow.dom.NativeColl(coll));
});

shadow.dom.native_coll = (function shadow$dom$native_coll(coll){
return (new shadow.dom.NativeColl(coll));
});
shadow.dom.dom_node = (function shadow$dom$dom_node(el){
if((el == null)){
return null;
} else {
if((((!((el == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === el.shadow$dom$IElement$))))?true:false):false)){
return el.shadow$dom$IElement$_to_dom$arity$1(null);
} else {
if(typeof el === 'string'){
return document.createTextNode(el);
} else {
if(typeof el === 'number'){
return document.createTextNode(cljs.core.str.cljs$core$IFn$_invoke$arity$1(el));
} else {
return el;

}
}
}
}
});
shadow.dom.query_one = (function shadow$dom$query_one(var_args){
var G__33434 = arguments.length;
switch (G__33434) {
case 1:
return shadow.dom.query_one.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.query_one.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.query_one.cljs$core$IFn$_invoke$arity$1 = (function (sel){
return document.querySelector(sel);
}));

(shadow.dom.query_one.cljs$core$IFn$_invoke$arity$2 = (function (sel,root){
return shadow.dom.dom_node(root).querySelector(sel);
}));

(shadow.dom.query_one.cljs$lang$maxFixedArity = 2);

shadow.dom.query = (function shadow$dom$query(var_args){
var G__33439 = arguments.length;
switch (G__33439) {
case 1:
return shadow.dom.query.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.query.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.query.cljs$core$IFn$_invoke$arity$1 = (function (sel){
return (new shadow.dom.NativeColl(document.querySelectorAll(sel)));
}));

(shadow.dom.query.cljs$core$IFn$_invoke$arity$2 = (function (sel,root){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(root).querySelectorAll(sel)));
}));

(shadow.dom.query.cljs$lang$maxFixedArity = 2);

shadow.dom.by_id = (function shadow$dom$by_id(var_args){
var G__33441 = arguments.length;
switch (G__33441) {
case 2:
return shadow.dom.by_id.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return shadow.dom.by_id.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.by_id.cljs$core$IFn$_invoke$arity$2 = (function (id,el){
return shadow.dom.dom_node(el).getElementById(id);
}));

(shadow.dom.by_id.cljs$core$IFn$_invoke$arity$1 = (function (id){
return document.getElementById(id);
}));

(shadow.dom.by_id.cljs$lang$maxFixedArity = 2);

shadow.dom.build = shadow.dom.dom_node;
shadow.dom.ev_stop = (function shadow$dom$ev_stop(var_args){
var G__33446 = arguments.length;
switch (G__33446) {
case 1:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 4:
return shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1 = (function (e){
if(cljs.core.truth_(e.stopPropagation)){
e.stopPropagation();

e.preventDefault();
} else {
(e.cancelBubble = true);

(e.returnValue = false);
}

return e;
}));

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$2 = (function (e,el){
shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1(e);

return el;
}));

(shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$4 = (function (e,el,scope,owner){
shadow.dom.ev_stop.cljs$core$IFn$_invoke$arity$1(e);

return el;
}));

(shadow.dom.ev_stop.cljs$lang$maxFixedArity = 4);

/**
 * check wether a parent node (or the document) contains the child
 */
shadow.dom.contains_QMARK_ = (function shadow$dom$contains_QMARK_(var_args){
var G__33448 = arguments.length;
switch (G__33448) {
case 1:
return shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$1 = (function (el){
return goog.dom.contains(document,shadow.dom.dom_node(el));
}));

(shadow.dom.contains_QMARK_.cljs$core$IFn$_invoke$arity$2 = (function (parent,el){
return goog.dom.contains(shadow.dom.dom_node(parent),shadow.dom.dom_node(el));
}));

(shadow.dom.contains_QMARK_.cljs$lang$maxFixedArity = 2);

shadow.dom.add_class = (function shadow$dom$add_class(el,cls){
return goog.dom.classlist.add(shadow.dom.dom_node(el),cls);
});
shadow.dom.remove_class = (function shadow$dom$remove_class(el,cls){
return goog.dom.classlist.remove(shadow.dom.dom_node(el),cls);
});
shadow.dom.toggle_class = (function shadow$dom$toggle_class(var_args){
var G__33454 = arguments.length;
switch (G__33454) {
case 2:
return shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$2 = (function (el,cls){
return goog.dom.classlist.toggle(shadow.dom.dom_node(el),cls);
}));

(shadow.dom.toggle_class.cljs$core$IFn$_invoke$arity$3 = (function (el,cls,v){
if(cljs.core.truth_(v)){
return shadow.dom.add_class(el,cls);
} else {
return shadow.dom.remove_class(el,cls);
}
}));

(shadow.dom.toggle_class.cljs$lang$maxFixedArity = 3);

shadow.dom.dom_listen = (cljs.core.truth_((function (){var or__4126__auto__ = (!((typeof document !== 'undefined')));
if(or__4126__auto__){
return or__4126__auto__;
} else {
return document.addEventListener;
}
})())?(function shadow$dom$dom_listen_good(el,ev,handler){
return el.addEventListener(ev,handler,false);
}):(function shadow$dom$dom_listen_ie(el,ev,handler){
try{return el.attachEvent(["on",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ev)].join(''),(function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
}));
}catch (e33462){if((e33462 instanceof Object)){
var e = e33462;
return console.log("didnt support attachEvent",el,e);
} else {
throw e33462;

}
}}));
shadow.dom.dom_listen_remove = (cljs.core.truth_((function (){var or__4126__auto__ = (!((typeof document !== 'undefined')));
if(or__4126__auto__){
return or__4126__auto__;
} else {
return document.removeEventListener;
}
})())?(function shadow$dom$dom_listen_remove_good(el,ev,handler){
return el.removeEventListener(ev,handler,false);
}):(function shadow$dom$dom_listen_remove_ie(el,ev,handler){
return el.detachEvent(["on",cljs.core.str.cljs$core$IFn$_invoke$arity$1(ev)].join(''),handler);
}));
shadow.dom.on_query = (function shadow$dom$on_query(root_el,ev,selector,handler){
var seq__33472 = cljs.core.seq(shadow.dom.query.cljs$core$IFn$_invoke$arity$2(selector,root_el));
var chunk__33473 = null;
var count__33474 = (0);
var i__33475 = (0);
while(true){
if((i__33475 < count__33474)){
var el = chunk__33473.cljs$core$IIndexed$_nth$arity$2(null,i__33475);
var handler_34230__$1 = ((function (seq__33472,chunk__33473,count__33474,i__33475,el){
return (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});})(seq__33472,chunk__33473,count__33474,i__33475,el))
;
shadow.dom.dom_listen(el,cljs.core.name(ev),handler_34230__$1);


var G__34231 = seq__33472;
var G__34232 = chunk__33473;
var G__34233 = count__33474;
var G__34234 = (i__33475 + (1));
seq__33472 = G__34231;
chunk__33473 = G__34232;
count__33474 = G__34233;
i__33475 = G__34234;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__33472);
if(temp__5735__auto__){
var seq__33472__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__33472__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__33472__$1);
var G__34235 = cljs.core.chunk_rest(seq__33472__$1);
var G__34236 = c__4556__auto__;
var G__34237 = cljs.core.count(c__4556__auto__);
var G__34238 = (0);
seq__33472 = G__34235;
chunk__33473 = G__34236;
count__33474 = G__34237;
i__33475 = G__34238;
continue;
} else {
var el = cljs.core.first(seq__33472__$1);
var handler_34239__$1 = ((function (seq__33472,chunk__33473,count__33474,i__33475,el,seq__33472__$1,temp__5735__auto__){
return (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});})(seq__33472,chunk__33473,count__33474,i__33475,el,seq__33472__$1,temp__5735__auto__))
;
shadow.dom.dom_listen(el,cljs.core.name(ev),handler_34239__$1);


var G__34240 = cljs.core.next(seq__33472__$1);
var G__34241 = null;
var G__34242 = (0);
var G__34243 = (0);
seq__33472 = G__34240;
chunk__33473 = G__34241;
count__33474 = G__34242;
i__33475 = G__34243;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.on = (function shadow$dom$on(var_args){
var G__33488 = arguments.length;
switch (G__33488) {
case 3:
return shadow.dom.on.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return shadow.dom.on.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.on.cljs$core$IFn$_invoke$arity$3 = (function (el,ev,handler){
return shadow.dom.on.cljs$core$IFn$_invoke$arity$4(el,ev,handler,false);
}));

(shadow.dom.on.cljs$core$IFn$_invoke$arity$4 = (function (el,ev,handler,capture){
if(cljs.core.vector_QMARK_(ev)){
return shadow.dom.on_query(el,cljs.core.first(ev),cljs.core.second(ev),handler);
} else {
var handler__$1 = (function (e){
return (handler.cljs$core$IFn$_invoke$arity$2 ? handler.cljs$core$IFn$_invoke$arity$2(e,el) : handler.call(null,e,el));
});
return shadow.dom.dom_listen(shadow.dom.dom_node(el),cljs.core.name(ev),handler__$1);
}
}));

(shadow.dom.on.cljs$lang$maxFixedArity = 4);

shadow.dom.remove_event_handler = (function shadow$dom$remove_event_handler(el,ev,handler){
return shadow.dom.dom_listen_remove(shadow.dom.dom_node(el),cljs.core.name(ev),handler);
});
shadow.dom.add_event_listeners = (function shadow$dom$add_event_listeners(el,events){
var seq__33496 = cljs.core.seq(events);
var chunk__33497 = null;
var count__33498 = (0);
var i__33499 = (0);
while(true){
if((i__33499 < count__33498)){
var vec__33506 = chunk__33497.cljs$core$IIndexed$_nth$arity$2(null,i__33499);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33506,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33506,(1),null);
shadow.dom.on.cljs$core$IFn$_invoke$arity$3(el,k,v);


var G__34245 = seq__33496;
var G__34246 = chunk__33497;
var G__34247 = count__33498;
var G__34248 = (i__33499 + (1));
seq__33496 = G__34245;
chunk__33497 = G__34246;
count__33498 = G__34247;
i__33499 = G__34248;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__33496);
if(temp__5735__auto__){
var seq__33496__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__33496__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__33496__$1);
var G__34250 = cljs.core.chunk_rest(seq__33496__$1);
var G__34251 = c__4556__auto__;
var G__34252 = cljs.core.count(c__4556__auto__);
var G__34253 = (0);
seq__33496 = G__34250;
chunk__33497 = G__34251;
count__33498 = G__34252;
i__33499 = G__34253;
continue;
} else {
var vec__33509 = cljs.core.first(seq__33496__$1);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33509,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33509,(1),null);
shadow.dom.on.cljs$core$IFn$_invoke$arity$3(el,k,v);


var G__34265 = cljs.core.next(seq__33496__$1);
var G__34266 = null;
var G__34267 = (0);
var G__34268 = (0);
seq__33496 = G__34265;
chunk__33497 = G__34266;
count__33498 = G__34267;
i__33499 = G__34268;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.set_style = (function shadow$dom$set_style(el,styles){
var dom = shadow.dom.dom_node(el);
var seq__33518 = cljs.core.seq(styles);
var chunk__33519 = null;
var count__33520 = (0);
var i__33521 = (0);
while(true){
if((i__33521 < count__33520)){
var vec__33531 = chunk__33519.cljs$core$IIndexed$_nth$arity$2(null,i__33521);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33531,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33531,(1),null);
goog.style.setStyle(dom,cljs.core.name(k),(((v == null))?"":v));


var G__34271 = seq__33518;
var G__34272 = chunk__33519;
var G__34273 = count__33520;
var G__34274 = (i__33521 + (1));
seq__33518 = G__34271;
chunk__33519 = G__34272;
count__33520 = G__34273;
i__33521 = G__34274;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__33518);
if(temp__5735__auto__){
var seq__33518__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__33518__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__33518__$1);
var G__34275 = cljs.core.chunk_rest(seq__33518__$1);
var G__34276 = c__4556__auto__;
var G__34277 = cljs.core.count(c__4556__auto__);
var G__34278 = (0);
seq__33518 = G__34275;
chunk__33519 = G__34276;
count__33520 = G__34277;
i__33521 = G__34278;
continue;
} else {
var vec__33537 = cljs.core.first(seq__33518__$1);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33537,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33537,(1),null);
goog.style.setStyle(dom,cljs.core.name(k),(((v == null))?"":v));


var G__34279 = cljs.core.next(seq__33518__$1);
var G__34280 = null;
var G__34281 = (0);
var G__34282 = (0);
seq__33518 = G__34279;
chunk__33519 = G__34280;
count__33520 = G__34281;
i__33521 = G__34282;
continue;
}
} else {
return null;
}
}
break;
}
});
shadow.dom.set_attr_STAR_ = (function shadow$dom$set_attr_STAR_(el,key,value){
var G__33543_34283 = key;
var G__33543_34284__$1 = (((G__33543_34283 instanceof cljs.core.Keyword))?G__33543_34283.fqn:null);
switch (G__33543_34284__$1) {
case "id":
(el.id = cljs.core.str.cljs$core$IFn$_invoke$arity$1(value));

break;
case "class":
(el.className = cljs.core.str.cljs$core$IFn$_invoke$arity$1(value));

break;
case "for":
(el.htmlFor = value);

break;
case "cellpadding":
el.setAttribute("cellPadding",value);

break;
case "cellspacing":
el.setAttribute("cellSpacing",value);

break;
case "colspan":
el.setAttribute("colSpan",value);

break;
case "frameborder":
el.setAttribute("frameBorder",value);

break;
case "height":
el.setAttribute("height",value);

break;
case "maxlength":
el.setAttribute("maxLength",value);

break;
case "role":
el.setAttribute("role",value);

break;
case "rowspan":
el.setAttribute("rowSpan",value);

break;
case "type":
el.setAttribute("type",value);

break;
case "usemap":
el.setAttribute("useMap",value);

break;
case "valign":
el.setAttribute("vAlign",value);

break;
case "width":
el.setAttribute("width",value);

break;
case "on":
shadow.dom.add_event_listeners(el,value);

break;
case "style":
if((value == null)){
} else {
if(typeof value === 'string'){
el.setAttribute("style",value);
} else {
if(cljs.core.map_QMARK_(value)){
shadow.dom.set_style(el,value);
} else {
goog.style.setStyle(el,value);

}
}
}

break;
default:
var ks_34286 = cljs.core.name(key);
if(cljs.core.truth_((function (){var or__4126__auto__ = goog.string.startsWith(ks_34286,"data-");
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return goog.string.startsWith(ks_34286,"aria-");
}
})())){
el.setAttribute(ks_34286,value);
} else {
(el[ks_34286] = value);
}

}

return el;
});
shadow.dom.set_attrs = (function shadow$dom$set_attrs(el,attrs){
return cljs.core.reduce_kv((function (el__$1,key,value){
shadow.dom.set_attr_STAR_(el__$1,key,value);

return el__$1;
}),shadow.dom.dom_node(el),attrs);
});
shadow.dom.set_attr = (function shadow$dom$set_attr(el,key,value){
return shadow.dom.set_attr_STAR_(shadow.dom.dom_node(el),key,value);
});
shadow.dom.has_class_QMARK_ = (function shadow$dom$has_class_QMARK_(el,cls){
return goog.dom.classlist.contains(shadow.dom.dom_node(el),cls);
});
shadow.dom.merge_class_string = (function shadow$dom$merge_class_string(current,extra_class){
if(cljs.core.seq(current)){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(current)," ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(extra_class)].join('');
} else {
return extra_class;
}
});
shadow.dom.parse_tag = (function shadow$dom$parse_tag(spec){
var spec__$1 = cljs.core.name(spec);
var fdot = spec__$1.indexOf(".");
var fhash = spec__$1.indexOf("#");
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fdot)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fhash)))){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1,null,null], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fhash)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fdot),null,clojure.string.replace(spec__$1.substring((fdot + (1))),/\./," ")], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2((-1),fdot)){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fhash),spec__$1.substring((fhash + (1))),null], null);
} else {
if((fhash > fdot)){
throw ["cant have id after class?",spec__$1].join('');
} else {
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [spec__$1.substring((0),fhash),spec__$1.substring((fhash + (1)),fdot),clojure.string.replace(spec__$1.substring((fdot + (1))),/\./," ")], null);

}
}
}
}
});
shadow.dom.create_dom_node = (function shadow$dom$create_dom_node(tag_def,p__33547){
var map__33548 = p__33547;
var map__33548__$1 = (((((!((map__33548 == null))))?(((((map__33548.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__33548.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__33548):map__33548);
var props = map__33548__$1;
var class$ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__33548__$1,new cljs.core.Keyword(null,"class","class",-2030961996));
var tag_props = ({});
var vec__33552 = shadow.dom.parse_tag(tag_def);
var tag_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33552,(0),null);
var tag_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33552,(1),null);
var tag_classes = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33552,(2),null);
if(cljs.core.truth_(tag_id)){
(tag_props["id"] = tag_id);
} else {
}

if(cljs.core.truth_(tag_classes)){
(tag_props["class"] = shadow.dom.merge_class_string(class$,tag_classes));
} else {
}

var G__33555 = goog.dom.createDom(tag_name,tag_props);
shadow.dom.set_attrs(G__33555,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(props,new cljs.core.Keyword(null,"class","class",-2030961996)));

return G__33555;
});
shadow.dom.append = (function shadow$dom$append(var_args){
var G__33560 = arguments.length;
switch (G__33560) {
case 1:
return shadow.dom.append.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.append.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.append.cljs$core$IFn$_invoke$arity$1 = (function (node){
if(cljs.core.truth_(node)){
var temp__5735__auto__ = shadow.dom.dom_node(node);
if(cljs.core.truth_(temp__5735__auto__)){
var n = temp__5735__auto__;
document.body.appendChild(n);

return n;
} else {
return null;
}
} else {
return null;
}
}));

(shadow.dom.append.cljs$core$IFn$_invoke$arity$2 = (function (el,node){
if(cljs.core.truth_(node)){
var temp__5735__auto__ = shadow.dom.dom_node(node);
if(cljs.core.truth_(temp__5735__auto__)){
var n = temp__5735__auto__;
shadow.dom.dom_node(el).appendChild(n);

return n;
} else {
return null;
}
} else {
return null;
}
}));

(shadow.dom.append.cljs$lang$maxFixedArity = 2);

shadow.dom.destructure_node = (function shadow$dom$destructure_node(create_fn,p__33561){
var vec__33562 = p__33561;
var seq__33563 = cljs.core.seq(vec__33562);
var first__33564 = cljs.core.first(seq__33563);
var seq__33563__$1 = cljs.core.next(seq__33563);
var nn = first__33564;
var first__33564__$1 = cljs.core.first(seq__33563__$1);
var seq__33563__$2 = cljs.core.next(seq__33563__$1);
var np = first__33564__$1;
var nc = seq__33563__$2;
var node = vec__33562;
if((nn instanceof cljs.core.Keyword)){
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("invalid dom node",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"node","node",581201198),node], null));
}

if((((np == null)) && ((nc == null)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__33565 = nn;
var G__33566 = cljs.core.PersistentArrayMap.EMPTY;
return (create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(G__33565,G__33566) : create_fn.call(null,G__33565,G__33566));
})(),cljs.core.List.EMPTY], null);
} else {
if(cljs.core.map_QMARK_(np)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(nn,np) : create_fn.call(null,nn,np)),nc], null);
} else {
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(function (){var G__33567 = nn;
var G__33568 = cljs.core.PersistentArrayMap.EMPTY;
return (create_fn.cljs$core$IFn$_invoke$arity$2 ? create_fn.cljs$core$IFn$_invoke$arity$2(G__33567,G__33568) : create_fn.call(null,G__33567,G__33568));
})(),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(nc,np)], null);

}
}
});
shadow.dom.make_dom_node = (function shadow$dom$make_dom_node(structure){
var vec__33569 = shadow.dom.destructure_node(shadow.dom.create_dom_node,structure);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33569,(0),null);
var node_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33569,(1),null);
var seq__33572_34301 = cljs.core.seq(node_children);
var chunk__33573_34302 = null;
var count__33574_34303 = (0);
var i__33575_34304 = (0);
while(true){
if((i__33575_34304 < count__33574_34303)){
var child_struct_34306 = chunk__33573_34302.cljs$core$IIndexed$_nth$arity$2(null,i__33575_34304);
var children_34307 = shadow.dom.dom_node(child_struct_34306);
if(cljs.core.seq_QMARK_(children_34307)){
var seq__33604_34308 = cljs.core.seq(cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom.dom_node,children_34307));
var chunk__33606_34309 = null;
var count__33607_34310 = (0);
var i__33608_34311 = (0);
while(true){
if((i__33608_34311 < count__33607_34310)){
var child_34315 = chunk__33606_34309.cljs$core$IIndexed$_nth$arity$2(null,i__33608_34311);
if(cljs.core.truth_(child_34315)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_34315);


var G__34316 = seq__33604_34308;
var G__34317 = chunk__33606_34309;
var G__34318 = count__33607_34310;
var G__34319 = (i__33608_34311 + (1));
seq__33604_34308 = G__34316;
chunk__33606_34309 = G__34317;
count__33607_34310 = G__34318;
i__33608_34311 = G__34319;
continue;
} else {
var G__34320 = seq__33604_34308;
var G__34321 = chunk__33606_34309;
var G__34322 = count__33607_34310;
var G__34323 = (i__33608_34311 + (1));
seq__33604_34308 = G__34320;
chunk__33606_34309 = G__34321;
count__33607_34310 = G__34322;
i__33608_34311 = G__34323;
continue;
}
} else {
var temp__5735__auto___34325 = cljs.core.seq(seq__33604_34308);
if(temp__5735__auto___34325){
var seq__33604_34326__$1 = temp__5735__auto___34325;
if(cljs.core.chunked_seq_QMARK_(seq__33604_34326__$1)){
var c__4556__auto___34327 = cljs.core.chunk_first(seq__33604_34326__$1);
var G__34328 = cljs.core.chunk_rest(seq__33604_34326__$1);
var G__34329 = c__4556__auto___34327;
var G__34330 = cljs.core.count(c__4556__auto___34327);
var G__34331 = (0);
seq__33604_34308 = G__34328;
chunk__33606_34309 = G__34329;
count__33607_34310 = G__34330;
i__33608_34311 = G__34331;
continue;
} else {
var child_34333 = cljs.core.first(seq__33604_34326__$1);
if(cljs.core.truth_(child_34333)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_34333);


var G__34334 = cljs.core.next(seq__33604_34326__$1);
var G__34335 = null;
var G__34336 = (0);
var G__34337 = (0);
seq__33604_34308 = G__34334;
chunk__33606_34309 = G__34335;
count__33607_34310 = G__34336;
i__33608_34311 = G__34337;
continue;
} else {
var G__34338 = cljs.core.next(seq__33604_34326__$1);
var G__34339 = null;
var G__34340 = (0);
var G__34341 = (0);
seq__33604_34308 = G__34338;
chunk__33606_34309 = G__34339;
count__33607_34310 = G__34340;
i__33608_34311 = G__34341;
continue;
}
}
} else {
}
}
break;
}
} else {
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,children_34307);
}


var G__34342 = seq__33572_34301;
var G__34343 = chunk__33573_34302;
var G__34344 = count__33574_34303;
var G__34345 = (i__33575_34304 + (1));
seq__33572_34301 = G__34342;
chunk__33573_34302 = G__34343;
count__33574_34303 = G__34344;
i__33575_34304 = G__34345;
continue;
} else {
var temp__5735__auto___34346 = cljs.core.seq(seq__33572_34301);
if(temp__5735__auto___34346){
var seq__33572_34347__$1 = temp__5735__auto___34346;
if(cljs.core.chunked_seq_QMARK_(seq__33572_34347__$1)){
var c__4556__auto___34354 = cljs.core.chunk_first(seq__33572_34347__$1);
var G__34356 = cljs.core.chunk_rest(seq__33572_34347__$1);
var G__34357 = c__4556__auto___34354;
var G__34358 = cljs.core.count(c__4556__auto___34354);
var G__34359 = (0);
seq__33572_34301 = G__34356;
chunk__33573_34302 = G__34357;
count__33574_34303 = G__34358;
i__33575_34304 = G__34359;
continue;
} else {
var child_struct_34360 = cljs.core.first(seq__33572_34347__$1);
var children_34361 = shadow.dom.dom_node(child_struct_34360);
if(cljs.core.seq_QMARK_(children_34361)){
var seq__33614_34362 = cljs.core.seq(cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom.dom_node,children_34361));
var chunk__33616_34363 = null;
var count__33617_34364 = (0);
var i__33618_34365 = (0);
while(true){
if((i__33618_34365 < count__33617_34364)){
var child_34366 = chunk__33616_34363.cljs$core$IIndexed$_nth$arity$2(null,i__33618_34365);
if(cljs.core.truth_(child_34366)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_34366);


var G__34367 = seq__33614_34362;
var G__34368 = chunk__33616_34363;
var G__34369 = count__33617_34364;
var G__34370 = (i__33618_34365 + (1));
seq__33614_34362 = G__34367;
chunk__33616_34363 = G__34368;
count__33617_34364 = G__34369;
i__33618_34365 = G__34370;
continue;
} else {
var G__34371 = seq__33614_34362;
var G__34372 = chunk__33616_34363;
var G__34373 = count__33617_34364;
var G__34374 = (i__33618_34365 + (1));
seq__33614_34362 = G__34371;
chunk__33616_34363 = G__34372;
count__33617_34364 = G__34373;
i__33618_34365 = G__34374;
continue;
}
} else {
var temp__5735__auto___34377__$1 = cljs.core.seq(seq__33614_34362);
if(temp__5735__auto___34377__$1){
var seq__33614_34378__$1 = temp__5735__auto___34377__$1;
if(cljs.core.chunked_seq_QMARK_(seq__33614_34378__$1)){
var c__4556__auto___34379 = cljs.core.chunk_first(seq__33614_34378__$1);
var G__34380 = cljs.core.chunk_rest(seq__33614_34378__$1);
var G__34381 = c__4556__auto___34379;
var G__34382 = cljs.core.count(c__4556__auto___34379);
var G__34383 = (0);
seq__33614_34362 = G__34380;
chunk__33616_34363 = G__34381;
count__33617_34364 = G__34382;
i__33618_34365 = G__34383;
continue;
} else {
var child_34385 = cljs.core.first(seq__33614_34378__$1);
if(cljs.core.truth_(child_34385)){
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,child_34385);


var G__34386 = cljs.core.next(seq__33614_34378__$1);
var G__34387 = null;
var G__34388 = (0);
var G__34389 = (0);
seq__33614_34362 = G__34386;
chunk__33616_34363 = G__34387;
count__33617_34364 = G__34388;
i__33618_34365 = G__34389;
continue;
} else {
var G__34390 = cljs.core.next(seq__33614_34378__$1);
var G__34391 = null;
var G__34392 = (0);
var G__34393 = (0);
seq__33614_34362 = G__34390;
chunk__33616_34363 = G__34391;
count__33617_34364 = G__34392;
i__33618_34365 = G__34393;
continue;
}
}
} else {
}
}
break;
}
} else {
shadow.dom.append.cljs$core$IFn$_invoke$arity$2(node,children_34361);
}


var G__34395 = cljs.core.next(seq__33572_34347__$1);
var G__34396 = null;
var G__34397 = (0);
var G__34398 = (0);
seq__33572_34301 = G__34395;
chunk__33573_34302 = G__34396;
count__33574_34303 = G__34397;
i__33575_34304 = G__34398;
continue;
}
} else {
}
}
break;
}

return node;
});
(cljs.core.Keyword.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.Keyword.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_dom_node(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [this$__$1], null));
}));

(cljs.core.PersistentVector.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_dom_node(this$__$1);
}));

(cljs.core.LazySeq.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.LazySeq.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom._to_dom,this$__$1);
}));
if(cljs.core.truth_(((typeof HTMLElement) != 'undefined'))){
(HTMLElement.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(HTMLElement.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1;
}));
} else {
}
if(cljs.core.truth_(((typeof DocumentFragment) != 'undefined'))){
(DocumentFragment.prototype.shadow$dom$IElement$ = cljs.core.PROTOCOL_SENTINEL);

(DocumentFragment.prototype.shadow$dom$IElement$_to_dom$arity$1 = (function (this$){
var this$__$1 = this;
return this$__$1;
}));
} else {
}
/**
 * clear node children
 */
shadow.dom.reset = (function shadow$dom$reset(node){
return goog.dom.removeChildren(shadow.dom.dom_node(node));
});
shadow.dom.remove = (function shadow$dom$remove(node){
if((((!((node == null))))?(((((node.cljs$lang$protocol_mask$partition0$ & (8388608))) || ((cljs.core.PROTOCOL_SENTINEL === node.cljs$core$ISeqable$))))?true:false):false)){
var seq__33640 = cljs.core.seq(node);
var chunk__33641 = null;
var count__33642 = (0);
var i__33643 = (0);
while(true){
if((i__33643 < count__33642)){
var n = chunk__33641.cljs$core$IIndexed$_nth$arity$2(null,i__33643);
(shadow.dom.remove.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.remove.cljs$core$IFn$_invoke$arity$1(n) : shadow.dom.remove.call(null,n));


var G__34406 = seq__33640;
var G__34407 = chunk__33641;
var G__34408 = count__33642;
var G__34409 = (i__33643 + (1));
seq__33640 = G__34406;
chunk__33641 = G__34407;
count__33642 = G__34408;
i__33643 = G__34409;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__33640);
if(temp__5735__auto__){
var seq__33640__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__33640__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__33640__$1);
var G__34413 = cljs.core.chunk_rest(seq__33640__$1);
var G__34414 = c__4556__auto__;
var G__34415 = cljs.core.count(c__4556__auto__);
var G__34416 = (0);
seq__33640 = G__34413;
chunk__33641 = G__34414;
count__33642 = G__34415;
i__33643 = G__34416;
continue;
} else {
var n = cljs.core.first(seq__33640__$1);
(shadow.dom.remove.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.remove.cljs$core$IFn$_invoke$arity$1(n) : shadow.dom.remove.call(null,n));


var G__34418 = cljs.core.next(seq__33640__$1);
var G__34419 = null;
var G__34420 = (0);
var G__34421 = (0);
seq__33640 = G__34418;
chunk__33641 = G__34419;
count__33642 = G__34420;
i__33643 = G__34421;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return goog.dom.removeNode(node);
}
});
shadow.dom.replace_node = (function shadow$dom$replace_node(old,new$){
return goog.dom.replaceNode(shadow.dom.dom_node(new$),shadow.dom.dom_node(old));
});
shadow.dom.text = (function shadow$dom$text(var_args){
var G__33711 = arguments.length;
switch (G__33711) {
case 2:
return shadow.dom.text.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 1:
return shadow.dom.text.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.text.cljs$core$IFn$_invoke$arity$2 = (function (el,new_text){
return (shadow.dom.dom_node(el).innerText = new_text);
}));

(shadow.dom.text.cljs$core$IFn$_invoke$arity$1 = (function (el){
return shadow.dom.dom_node(el).innerText;
}));

(shadow.dom.text.cljs$lang$maxFixedArity = 2);

shadow.dom.check = (function shadow$dom$check(var_args){
var G__33729 = arguments.length;
switch (G__33729) {
case 1:
return shadow.dom.check.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.check.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.check.cljs$core$IFn$_invoke$arity$1 = (function (el){
return shadow.dom.check.cljs$core$IFn$_invoke$arity$2(el,true);
}));

(shadow.dom.check.cljs$core$IFn$_invoke$arity$2 = (function (el,checked){
return (shadow.dom.dom_node(el).checked = checked);
}));

(shadow.dom.check.cljs$lang$maxFixedArity = 2);

shadow.dom.checked_QMARK_ = (function shadow$dom$checked_QMARK_(el){
return shadow.dom.dom_node(el).checked;
});
shadow.dom.form_elements = (function shadow$dom$form_elements(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).elements));
});
shadow.dom.children = (function shadow$dom$children(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).children));
});
shadow.dom.child_nodes = (function shadow$dom$child_nodes(el){
return (new shadow.dom.NativeColl(shadow.dom.dom_node(el).childNodes));
});
shadow.dom.attr = (function shadow$dom$attr(var_args){
var G__33743 = arguments.length;
switch (G__33743) {
case 2:
return shadow.dom.attr.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.attr.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.attr.cljs$core$IFn$_invoke$arity$2 = (function (el,key){
return shadow.dom.dom_node(el).getAttribute(cljs.core.name(key));
}));

(shadow.dom.attr.cljs$core$IFn$_invoke$arity$3 = (function (el,key,default$){
var or__4126__auto__ = shadow.dom.dom_node(el).getAttribute(cljs.core.name(key));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return default$;
}
}));

(shadow.dom.attr.cljs$lang$maxFixedArity = 3);

shadow.dom.del_attr = (function shadow$dom$del_attr(el,key){
return shadow.dom.dom_node(el).removeAttribute(cljs.core.name(key));
});
shadow.dom.data = (function shadow$dom$data(el,key){
return shadow.dom.dom_node(el).getAttribute(["data-",cljs.core.name(key)].join(''));
});
shadow.dom.set_data = (function shadow$dom$set_data(el,key,value){
return shadow.dom.dom_node(el).setAttribute(["data-",cljs.core.name(key)].join(''),cljs.core.str.cljs$core$IFn$_invoke$arity$1(value));
});
shadow.dom.set_html = (function shadow$dom$set_html(node,text){
return (shadow.dom.dom_node(node).innerHTML = text);
});
shadow.dom.get_html = (function shadow$dom$get_html(node){
return shadow.dom.dom_node(node).innerHTML;
});
shadow.dom.fragment = (function shadow$dom$fragment(var_args){
var args__4742__auto__ = [];
var len__4736__auto___34451 = arguments.length;
var i__4737__auto___34452 = (0);
while(true){
if((i__4737__auto___34452 < len__4736__auto___34451)){
args__4742__auto__.push((arguments[i__4737__auto___34452]));

var G__34453 = (i__4737__auto___34452 + (1));
i__4737__auto___34452 = G__34453;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return shadow.dom.fragment.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(shadow.dom.fragment.cljs$core$IFn$_invoke$arity$variadic = (function (nodes){
var fragment = document.createDocumentFragment();
var seq__33755_34454 = cljs.core.seq(nodes);
var chunk__33756_34455 = null;
var count__33757_34456 = (0);
var i__33758_34457 = (0);
while(true){
if((i__33758_34457 < count__33757_34456)){
var node_34461 = chunk__33756_34455.cljs$core$IIndexed$_nth$arity$2(null,i__33758_34457);
fragment.appendChild(shadow.dom._to_dom(node_34461));


var G__34462 = seq__33755_34454;
var G__34463 = chunk__33756_34455;
var G__34464 = count__33757_34456;
var G__34465 = (i__33758_34457 + (1));
seq__33755_34454 = G__34462;
chunk__33756_34455 = G__34463;
count__33757_34456 = G__34464;
i__33758_34457 = G__34465;
continue;
} else {
var temp__5735__auto___34466 = cljs.core.seq(seq__33755_34454);
if(temp__5735__auto___34466){
var seq__33755_34467__$1 = temp__5735__auto___34466;
if(cljs.core.chunked_seq_QMARK_(seq__33755_34467__$1)){
var c__4556__auto___34468 = cljs.core.chunk_first(seq__33755_34467__$1);
var G__34469 = cljs.core.chunk_rest(seq__33755_34467__$1);
var G__34470 = c__4556__auto___34468;
var G__34471 = cljs.core.count(c__4556__auto___34468);
var G__34472 = (0);
seq__33755_34454 = G__34469;
chunk__33756_34455 = G__34470;
count__33757_34456 = G__34471;
i__33758_34457 = G__34472;
continue;
} else {
var node_34473 = cljs.core.first(seq__33755_34467__$1);
fragment.appendChild(shadow.dom._to_dom(node_34473));


var G__34474 = cljs.core.next(seq__33755_34467__$1);
var G__34475 = null;
var G__34476 = (0);
var G__34477 = (0);
seq__33755_34454 = G__34474;
chunk__33756_34455 = G__34475;
count__33757_34456 = G__34476;
i__33758_34457 = G__34477;
continue;
}
} else {
}
}
break;
}

return (new shadow.dom.NativeColl(fragment));
}));

(shadow.dom.fragment.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(shadow.dom.fragment.cljs$lang$applyTo = (function (seq33753){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq33753));
}));

/**
 * given a html string, eval all <script> tags and return the html without the scripts
 * don't do this for everything, only content you trust.
 */
shadow.dom.eval_scripts = (function shadow$dom$eval_scripts(s){
var scripts = cljs.core.re_seq(/<script[^>]*?>(.+?)<\/script>/,s);
var seq__33769_34478 = cljs.core.seq(scripts);
var chunk__33770_34479 = null;
var count__33771_34480 = (0);
var i__33772_34481 = (0);
while(true){
if((i__33772_34481 < count__33771_34480)){
var vec__33783_34482 = chunk__33770_34479.cljs$core$IIndexed$_nth$arity$2(null,i__33772_34481);
var script_tag_34483 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33783_34482,(0),null);
var script_body_34484 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33783_34482,(1),null);
eval(script_body_34484);


var G__34485 = seq__33769_34478;
var G__34486 = chunk__33770_34479;
var G__34487 = count__33771_34480;
var G__34488 = (i__33772_34481 + (1));
seq__33769_34478 = G__34485;
chunk__33770_34479 = G__34486;
count__33771_34480 = G__34487;
i__33772_34481 = G__34488;
continue;
} else {
var temp__5735__auto___34489 = cljs.core.seq(seq__33769_34478);
if(temp__5735__auto___34489){
var seq__33769_34494__$1 = temp__5735__auto___34489;
if(cljs.core.chunked_seq_QMARK_(seq__33769_34494__$1)){
var c__4556__auto___34495 = cljs.core.chunk_first(seq__33769_34494__$1);
var G__34496 = cljs.core.chunk_rest(seq__33769_34494__$1);
var G__34497 = c__4556__auto___34495;
var G__34498 = cljs.core.count(c__4556__auto___34495);
var G__34499 = (0);
seq__33769_34478 = G__34496;
chunk__33770_34479 = G__34497;
count__33771_34480 = G__34498;
i__33772_34481 = G__34499;
continue;
} else {
var vec__33787_34500 = cljs.core.first(seq__33769_34494__$1);
var script_tag_34501 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33787_34500,(0),null);
var script_body_34502 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33787_34500,(1),null);
eval(script_body_34502);


var G__34503 = cljs.core.next(seq__33769_34494__$1);
var G__34504 = null;
var G__34505 = (0);
var G__34506 = (0);
seq__33769_34478 = G__34503;
chunk__33770_34479 = G__34504;
count__33771_34480 = G__34505;
i__33772_34481 = G__34506;
continue;
}
} else {
}
}
break;
}

return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (s__$1,p__33791){
var vec__33792 = p__33791;
var script_tag = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33792,(0),null);
var script_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33792,(1),null);
return clojure.string.replace(s__$1,script_tag,"");
}),s,scripts);
});
shadow.dom.str__GT_fragment = (function shadow$dom$str__GT_fragment(s){
var el = document.createElement("div");
(el.innerHTML = s);

return (new shadow.dom.NativeColl(goog.dom.childrenToNode_(document,el)));
});
shadow.dom.node_name = (function shadow$dom$node_name(el){
return shadow.dom.dom_node(el).nodeName;
});
shadow.dom.ancestor_by_class = (function shadow$dom$ancestor_by_class(el,cls){
return goog.dom.getAncestorByClass(shadow.dom.dom_node(el),cls);
});
shadow.dom.ancestor_by_tag = (function shadow$dom$ancestor_by_tag(var_args){
var G__33800 = arguments.length;
switch (G__33800) {
case 2:
return shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$2 = (function (el,tag){
return goog.dom.getAncestorByTagNameAndClass(shadow.dom.dom_node(el),cljs.core.name(tag));
}));

(shadow.dom.ancestor_by_tag.cljs$core$IFn$_invoke$arity$3 = (function (el,tag,cls){
return goog.dom.getAncestorByTagNameAndClass(shadow.dom.dom_node(el),cljs.core.name(tag),cljs.core.name(cls));
}));

(shadow.dom.ancestor_by_tag.cljs$lang$maxFixedArity = 3);

shadow.dom.get_value = (function shadow$dom$get_value(dom){
return goog.dom.forms.getValue(shadow.dom.dom_node(dom));
});
shadow.dom.set_value = (function shadow$dom$set_value(dom,value){
return goog.dom.forms.setValue(shadow.dom.dom_node(dom),value);
});
shadow.dom.px = (function shadow$dom$px(value){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1((value | (0))),"px"].join('');
});
shadow.dom.pct = (function shadow$dom$pct(value){
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(value),"%"].join('');
});
shadow.dom.remove_style_STAR_ = (function shadow$dom$remove_style_STAR_(el,style){
return el.style.removeProperty(cljs.core.name(style));
});
shadow.dom.remove_style = (function shadow$dom$remove_style(el,style){
var el__$1 = shadow.dom.dom_node(el);
return shadow.dom.remove_style_STAR_(el__$1,style);
});
shadow.dom.remove_styles = (function shadow$dom$remove_styles(el,style_keys){
var el__$1 = shadow.dom.dom_node(el);
var seq__33805 = cljs.core.seq(style_keys);
var chunk__33806 = null;
var count__33807 = (0);
var i__33808 = (0);
while(true){
if((i__33808 < count__33807)){
var it = chunk__33806.cljs$core$IIndexed$_nth$arity$2(null,i__33808);
shadow.dom.remove_style_STAR_(el__$1,it);


var G__34515 = seq__33805;
var G__34516 = chunk__33806;
var G__34517 = count__33807;
var G__34518 = (i__33808 + (1));
seq__33805 = G__34515;
chunk__33806 = G__34516;
count__33807 = G__34517;
i__33808 = G__34518;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__33805);
if(temp__5735__auto__){
var seq__33805__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__33805__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__33805__$1);
var G__34521 = cljs.core.chunk_rest(seq__33805__$1);
var G__34522 = c__4556__auto__;
var G__34523 = cljs.core.count(c__4556__auto__);
var G__34524 = (0);
seq__33805 = G__34521;
chunk__33806 = G__34522;
count__33807 = G__34523;
i__33808 = G__34524;
continue;
} else {
var it = cljs.core.first(seq__33805__$1);
shadow.dom.remove_style_STAR_(el__$1,it);


var G__34526 = cljs.core.next(seq__33805__$1);
var G__34527 = null;
var G__34528 = (0);
var G__34529 = (0);
seq__33805 = G__34526;
chunk__33806 = G__34527;
count__33807 = G__34528;
i__33808 = G__34529;
continue;
}
} else {
return null;
}
}
break;
}
});

/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
shadow.dom.Coordinate = (function (x,y,__meta,__extmap,__hash){
this.x = x;
this.y = y;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(shadow.dom.Coordinate.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__4380__auto__,k__4381__auto__){
var self__ = this;
var this__4380__auto____$1 = this;
return this__4380__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__4381__auto__,null);
}));

(shadow.dom.Coordinate.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__4382__auto__,k33817,else__4383__auto__){
var self__ = this;
var this__4382__auto____$1 = this;
var G__33844 = k33817;
var G__33844__$1 = (((G__33844 instanceof cljs.core.Keyword))?G__33844.fqn:null);
switch (G__33844__$1) {
case "x":
return self__.x;

break;
case "y":
return self__.y;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k33817,else__4383__auto__);

}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__4399__auto__,f__4400__auto__,init__4401__auto__){
var self__ = this;
var this__4399__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__4402__auto__,p__33845){
var vec__33846 = p__33845;
var k__4403__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33846,(0),null);
var v__4404__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33846,(1),null);
return (f__4400__auto__.cljs$core$IFn$_invoke$arity$3 ? f__4400__auto__.cljs$core$IFn$_invoke$arity$3(ret__4402__auto__,k__4403__auto__,v__4404__auto__) : f__4400__auto__.call(null,ret__4402__auto__,k__4403__auto__,v__4404__auto__));
}),init__4401__auto__,this__4399__auto____$1);
}));

(shadow.dom.Coordinate.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__4394__auto__,writer__4395__auto__,opts__4396__auto__){
var self__ = this;
var this__4394__auto____$1 = this;
var pr_pair__4397__auto__ = (function (keyval__4398__auto__){
return cljs.core.pr_sequential_writer(writer__4395__auto__,cljs.core.pr_writer,""," ","",opts__4396__auto__,keyval__4398__auto__);
});
return cljs.core.pr_sequential_writer(writer__4395__auto__,pr_pair__4397__auto__,"#shadow.dom.Coordinate{",", ","}",opts__4396__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"x","x",2099068185),self__.x],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"y","y",-1757859776),self__.y],null))], null),self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__33816){
var self__ = this;
var G__33816__$1 = this;
return (new cljs.core.RecordIter((0),G__33816__$1,2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"x","x",2099068185),new cljs.core.Keyword(null,"y","y",-1757859776)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__4378__auto__){
var self__ = this;
var this__4378__auto____$1 = this;
return self__.__meta;
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__4375__auto__){
var self__ = this;
var this__4375__auto____$1 = this;
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,self__.__extmap,self__.__hash));
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__4384__auto__){
var self__ = this;
var this__4384__auto____$1 = this;
return (2 + cljs.core.count(self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__4376__auto__){
var self__ = this;
var this__4376__auto____$1 = this;
var h__4238__auto__ = self__.__hash;
if((!((h__4238__auto__ == null)))){
return h__4238__auto__;
} else {
var h__4238__auto____$1 = (function (coll__4377__auto__){
return (145542109 ^ cljs.core.hash_unordered_coll(coll__4377__auto__));
})(this__4376__auto____$1);
(self__.__hash = h__4238__auto____$1);

return h__4238__auto____$1;
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this33818,other33819){
var self__ = this;
var this33818__$1 = this;
return (((!((other33819 == null)))) && ((this33818__$1.constructor === other33819.constructor)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this33818__$1.x,other33819.x)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this33818__$1.y,other33819.y)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this33818__$1.__extmap,other33819.__extmap)));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__4389__auto__,k__4390__auto__){
var self__ = this;
var this__4389__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"y","y",-1757859776),null,new cljs.core.Keyword(null,"x","x",2099068185),null], null), null),k__4390__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__4389__auto____$1),self__.__meta),k__4390__auto__);
} else {
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__4390__auto__)),null));
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__4387__auto__,k__4388__auto__,G__33816){
var self__ = this;
var this__4387__auto____$1 = this;
var pred__33859 = cljs.core.keyword_identical_QMARK_;
var expr__33860 = k__4388__auto__;
if(cljs.core.truth_((pred__33859.cljs$core$IFn$_invoke$arity$2 ? pred__33859.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"x","x",2099068185),expr__33860) : pred__33859.call(null,new cljs.core.Keyword(null,"x","x",2099068185),expr__33860)))){
return (new shadow.dom.Coordinate(G__33816,self__.y,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__33859.cljs$core$IFn$_invoke$arity$2 ? pred__33859.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"y","y",-1757859776),expr__33860) : pred__33859.call(null,new cljs.core.Keyword(null,"y","y",-1757859776),expr__33860)))){
return (new shadow.dom.Coordinate(self__.x,G__33816,self__.__meta,self__.__extmap,null));
} else {
return (new shadow.dom.Coordinate(self__.x,self__.y,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__4388__auto__,G__33816),null));
}
}
}));

(shadow.dom.Coordinate.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__4392__auto__){
var self__ = this;
var this__4392__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"x","x",2099068185),self__.x,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"y","y",-1757859776),self__.y,null))], null),self__.__extmap));
}));

(shadow.dom.Coordinate.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__4379__auto__,G__33816){
var self__ = this;
var this__4379__auto____$1 = this;
return (new shadow.dom.Coordinate(self__.x,self__.y,G__33816,self__.__extmap,self__.__hash));
}));

(shadow.dom.Coordinate.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__4385__auto__,entry__4386__auto__){
var self__ = this;
var this__4385__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__4386__auto__)){
return this__4385__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__4386__auto__,(0)),cljs.core._nth(entry__4386__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__4385__auto____$1,entry__4386__auto__);
}
}));

(shadow.dom.Coordinate.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"x","x",-555367584,null),new cljs.core.Symbol(null,"y","y",-117328249,null)], null);
}));

(shadow.dom.Coordinate.cljs$lang$type = true);

(shadow.dom.Coordinate.cljs$lang$ctorPrSeq = (function (this__4423__auto__){
return (new cljs.core.List(null,"shadow.dom/Coordinate",null,(1),null));
}));

(shadow.dom.Coordinate.cljs$lang$ctorPrWriter = (function (this__4423__auto__,writer__4424__auto__){
return cljs.core._write(writer__4424__auto__,"shadow.dom/Coordinate");
}));

/**
 * Positional factory function for shadow.dom/Coordinate.
 */
shadow.dom.__GT_Coordinate = (function shadow$dom$__GT_Coordinate(x,y){
return (new shadow.dom.Coordinate(x,y,null,null,null));
});

/**
 * Factory function for shadow.dom/Coordinate, taking a map of keywords to field values.
 */
shadow.dom.map__GT_Coordinate = (function shadow$dom$map__GT_Coordinate(G__33820){
var extmap__4419__auto__ = (function (){var G__33889 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__33820,new cljs.core.Keyword(null,"x","x",2099068185),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"y","y",-1757859776)], 0));
if(cljs.core.record_QMARK_(G__33820)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__33889);
} else {
return G__33889;
}
})();
return (new shadow.dom.Coordinate(new cljs.core.Keyword(null,"x","x",2099068185).cljs$core$IFn$_invoke$arity$1(G__33820),new cljs.core.Keyword(null,"y","y",-1757859776).cljs$core$IFn$_invoke$arity$1(G__33820),null,cljs.core.not_empty(extmap__4419__auto__),null));
});

shadow.dom.get_position = (function shadow$dom$get_position(el){
var pos = goog.style.getPosition(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});
shadow.dom.get_client_position = (function shadow$dom$get_client_position(el){
var pos = goog.style.getClientPosition(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});
shadow.dom.get_page_offset = (function shadow$dom$get_page_offset(el){
var pos = goog.style.getPageOffset(shadow.dom.dom_node(el));
return shadow.dom.__GT_Coordinate(pos.x,pos.y);
});

/**
* @constructor
 * @implements {cljs.core.IRecord}
 * @implements {cljs.core.IKVReduce}
 * @implements {cljs.core.IEquiv}
 * @implements {cljs.core.IHash}
 * @implements {cljs.core.ICollection}
 * @implements {cljs.core.ICounted}
 * @implements {cljs.core.ISeqable}
 * @implements {cljs.core.IMeta}
 * @implements {cljs.core.ICloneable}
 * @implements {cljs.core.IPrintWithWriter}
 * @implements {cljs.core.IIterable}
 * @implements {cljs.core.IWithMeta}
 * @implements {cljs.core.IAssociative}
 * @implements {cljs.core.IMap}
 * @implements {cljs.core.ILookup}
*/
shadow.dom.Size = (function (w,h,__meta,__extmap,__hash){
this.w = w;
this.h = h;
this.__meta = __meta;
this.__extmap = __extmap;
this.__hash = __hash;
this.cljs$lang$protocol_mask$partition0$ = 2230716170;
this.cljs$lang$protocol_mask$partition1$ = 139264;
});
(shadow.dom.Size.prototype.cljs$core$ILookup$_lookup$arity$2 = (function (this__4380__auto__,k__4381__auto__){
var self__ = this;
var this__4380__auto____$1 = this;
return this__4380__auto____$1.cljs$core$ILookup$_lookup$arity$3(null,k__4381__auto__,null);
}));

(shadow.dom.Size.prototype.cljs$core$ILookup$_lookup$arity$3 = (function (this__4382__auto__,k33898,else__4383__auto__){
var self__ = this;
var this__4382__auto____$1 = this;
var G__33902 = k33898;
var G__33902__$1 = (((G__33902 instanceof cljs.core.Keyword))?G__33902.fqn:null);
switch (G__33902__$1) {
case "w":
return self__.w;

break;
case "h":
return self__.h;

break;
default:
return cljs.core.get.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k33898,else__4383__auto__);

}
}));

(shadow.dom.Size.prototype.cljs$core$IKVReduce$_kv_reduce$arity$3 = (function (this__4399__auto__,f__4400__auto__,init__4401__auto__){
var self__ = this;
var this__4399__auto____$1 = this;
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (ret__4402__auto__,p__33903){
var vec__33904 = p__33903;
var k__4403__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33904,(0),null);
var v__4404__auto__ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33904,(1),null);
return (f__4400__auto__.cljs$core$IFn$_invoke$arity$3 ? f__4400__auto__.cljs$core$IFn$_invoke$arity$3(ret__4402__auto__,k__4403__auto__,v__4404__auto__) : f__4400__auto__.call(null,ret__4402__auto__,k__4403__auto__,v__4404__auto__));
}),init__4401__auto__,this__4399__auto____$1);
}));

(shadow.dom.Size.prototype.cljs$core$IPrintWithWriter$_pr_writer$arity$3 = (function (this__4394__auto__,writer__4395__auto__,opts__4396__auto__){
var self__ = this;
var this__4394__auto____$1 = this;
var pr_pair__4397__auto__ = (function (keyval__4398__auto__){
return cljs.core.pr_sequential_writer(writer__4395__auto__,cljs.core.pr_writer,""," ","",opts__4396__auto__,keyval__4398__auto__);
});
return cljs.core.pr_sequential_writer(writer__4395__auto__,pr_pair__4397__auto__,"#shadow.dom.Size{",", ","}",opts__4396__auto__,cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"w","w",354169001),self__.w],null)),(new cljs.core.PersistentVector(null,2,(5),cljs.core.PersistentVector.EMPTY_NODE,[new cljs.core.Keyword(null,"h","h",1109658740),self__.h],null))], null),self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IIterable$_iterator$arity$1 = (function (G__33897){
var self__ = this;
var G__33897__$1 = this;
return (new cljs.core.RecordIter((0),G__33897__$1,2,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"w","w",354169001),new cljs.core.Keyword(null,"h","h",1109658740)], null),(cljs.core.truth_(self__.__extmap)?cljs.core._iterator(self__.__extmap):cljs.core.nil_iter())));
}));

(shadow.dom.Size.prototype.cljs$core$IMeta$_meta$arity$1 = (function (this__4378__auto__){
var self__ = this;
var this__4378__auto____$1 = this;
return self__.__meta;
}));

(shadow.dom.Size.prototype.cljs$core$ICloneable$_clone$arity$1 = (function (this__4375__auto__){
var self__ = this;
var this__4375__auto____$1 = this;
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,self__.__extmap,self__.__hash));
}));

(shadow.dom.Size.prototype.cljs$core$ICounted$_count$arity$1 = (function (this__4384__auto__){
var self__ = this;
var this__4384__auto____$1 = this;
return (2 + cljs.core.count(self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IHash$_hash$arity$1 = (function (this__4376__auto__){
var self__ = this;
var this__4376__auto____$1 = this;
var h__4238__auto__ = self__.__hash;
if((!((h__4238__auto__ == null)))){
return h__4238__auto__;
} else {
var h__4238__auto____$1 = (function (coll__4377__auto__){
return (-1228019642 ^ cljs.core.hash_unordered_coll(coll__4377__auto__));
})(this__4376__auto____$1);
(self__.__hash = h__4238__auto____$1);

return h__4238__auto____$1;
}
}));

(shadow.dom.Size.prototype.cljs$core$IEquiv$_equiv$arity$2 = (function (this33899,other33900){
var self__ = this;
var this33899__$1 = this;
return (((!((other33900 == null)))) && ((this33899__$1.constructor === other33900.constructor)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this33899__$1.w,other33900.w)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this33899__$1.h,other33900.h)) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(this33899__$1.__extmap,other33900.__extmap)));
}));

(shadow.dom.Size.prototype.cljs$core$IMap$_dissoc$arity$2 = (function (this__4389__auto__,k__4390__auto__){
var self__ = this;
var this__4389__auto____$1 = this;
if(cljs.core.contains_QMARK_(new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"w","w",354169001),null,new cljs.core.Keyword(null,"h","h",1109658740),null], null), null),k__4390__auto__)){
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core._with_meta(cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,this__4389__auto____$1),self__.__meta),k__4390__auto__);
} else {
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,cljs.core.not_empty(cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(self__.__extmap,k__4390__auto__)),null));
}
}));

(shadow.dom.Size.prototype.cljs$core$IAssociative$_assoc$arity$3 = (function (this__4387__auto__,k__4388__auto__,G__33897){
var self__ = this;
var this__4387__auto____$1 = this;
var pred__33917 = cljs.core.keyword_identical_QMARK_;
var expr__33918 = k__4388__auto__;
if(cljs.core.truth_((pred__33917.cljs$core$IFn$_invoke$arity$2 ? pred__33917.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"w","w",354169001),expr__33918) : pred__33917.call(null,new cljs.core.Keyword(null,"w","w",354169001),expr__33918)))){
return (new shadow.dom.Size(G__33897,self__.h,self__.__meta,self__.__extmap,null));
} else {
if(cljs.core.truth_((pred__33917.cljs$core$IFn$_invoke$arity$2 ? pred__33917.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"h","h",1109658740),expr__33918) : pred__33917.call(null,new cljs.core.Keyword(null,"h","h",1109658740),expr__33918)))){
return (new shadow.dom.Size(self__.w,G__33897,self__.__meta,self__.__extmap,null));
} else {
return (new shadow.dom.Size(self__.w,self__.h,self__.__meta,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(self__.__extmap,k__4388__auto__,G__33897),null));
}
}
}));

(shadow.dom.Size.prototype.cljs$core$ISeqable$_seq$arity$1 = (function (this__4392__auto__){
var self__ = this;
var this__4392__auto____$1 = this;
return cljs.core.seq(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(new cljs.core.MapEntry(new cljs.core.Keyword(null,"w","w",354169001),self__.w,null)),(new cljs.core.MapEntry(new cljs.core.Keyword(null,"h","h",1109658740),self__.h,null))], null),self__.__extmap));
}));

(shadow.dom.Size.prototype.cljs$core$IWithMeta$_with_meta$arity$2 = (function (this__4379__auto__,G__33897){
var self__ = this;
var this__4379__auto____$1 = this;
return (new shadow.dom.Size(self__.w,self__.h,G__33897,self__.__extmap,self__.__hash));
}));

(shadow.dom.Size.prototype.cljs$core$ICollection$_conj$arity$2 = (function (this__4385__auto__,entry__4386__auto__){
var self__ = this;
var this__4385__auto____$1 = this;
if(cljs.core.vector_QMARK_(entry__4386__auto__)){
return this__4385__auto____$1.cljs$core$IAssociative$_assoc$arity$3(null,cljs.core._nth(entry__4386__auto__,(0)),cljs.core._nth(entry__4386__auto__,(1)));
} else {
return cljs.core.reduce.cljs$core$IFn$_invoke$arity$3(cljs.core._conj,this__4385__auto____$1,entry__4386__auto__);
}
}));

(shadow.dom.Size.getBasis = (function (){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Symbol(null,"w","w",1994700528,null),new cljs.core.Symbol(null,"h","h",-1544777029,null)], null);
}));

(shadow.dom.Size.cljs$lang$type = true);

(shadow.dom.Size.cljs$lang$ctorPrSeq = (function (this__4423__auto__){
return (new cljs.core.List(null,"shadow.dom/Size",null,(1),null));
}));

(shadow.dom.Size.cljs$lang$ctorPrWriter = (function (this__4423__auto__,writer__4424__auto__){
return cljs.core._write(writer__4424__auto__,"shadow.dom/Size");
}));

/**
 * Positional factory function for shadow.dom/Size.
 */
shadow.dom.__GT_Size = (function shadow$dom$__GT_Size(w,h){
return (new shadow.dom.Size(w,h,null,null,null));
});

/**
 * Factory function for shadow.dom/Size, taking a map of keywords to field values.
 */
shadow.dom.map__GT_Size = (function shadow$dom$map__GT_Size(G__33901){
var extmap__4419__auto__ = (function (){var G__33933 = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(G__33901,new cljs.core.Keyword(null,"w","w",354169001),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"h","h",1109658740)], 0));
if(cljs.core.record_QMARK_(G__33901)){
return cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,G__33933);
} else {
return G__33933;
}
})();
return (new shadow.dom.Size(new cljs.core.Keyword(null,"w","w",354169001).cljs$core$IFn$_invoke$arity$1(G__33901),new cljs.core.Keyword(null,"h","h",1109658740).cljs$core$IFn$_invoke$arity$1(G__33901),null,cljs.core.not_empty(extmap__4419__auto__),null));
});

shadow.dom.size__GT_clj = (function shadow$dom$size__GT_clj(size){
return (new shadow.dom.Size(size.width,size.height,null,null,null));
});
shadow.dom.get_size = (function shadow$dom$get_size(el){
return shadow.dom.size__GT_clj(goog.style.getSize(shadow.dom.dom_node(el)));
});
shadow.dom.get_height = (function shadow$dom$get_height(el){
return shadow.dom.get_size(el).h;
});
shadow.dom.get_viewport_size = (function shadow$dom$get_viewport_size(){
return shadow.dom.size__GT_clj(goog.dom.getViewportSize());
});
shadow.dom.first_child = (function shadow$dom$first_child(el){
return (shadow.dom.dom_node(el).children[(0)]);
});
shadow.dom.select_option_values = (function shadow$dom$select_option_values(el){
var native$ = shadow.dom.dom_node(el);
var opts = (native$["options"]);
var a__4610__auto__ = opts;
var l__4611__auto__ = a__4610__auto__.length;
var i = (0);
var ret = cljs.core.PersistentVector.EMPTY;
while(true){
if((i < l__4611__auto__)){
var G__34557 = (i + (1));
var G__34558 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(ret,(opts[i]["value"]));
i = G__34557;
ret = G__34558;
continue;
} else {
return ret;
}
break;
}
});
shadow.dom.build_url = (function shadow$dom$build_url(path,query_params){
if(cljs.core.empty_QMARK_(query_params)){
return path;
} else {
return [cljs.core.str.cljs$core$IFn$_invoke$arity$1(path),"?",clojure.string.join.cljs$core$IFn$_invoke$arity$2("&",cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__33953){
var vec__33954 = p__33953;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33954,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__33954,(1),null);
return [cljs.core.name(k),"=",cljs.core.str.cljs$core$IFn$_invoke$arity$1(encodeURIComponent(cljs.core.str.cljs$core$IFn$_invoke$arity$1(v)))].join('');
}),query_params))].join('');
}
});
shadow.dom.redirect = (function shadow$dom$redirect(var_args){
var G__33978 = arguments.length;
switch (G__33978) {
case 1:
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.redirect.cljs$core$IFn$_invoke$arity$1 = (function (path){
return shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2(path,cljs.core.PersistentArrayMap.EMPTY);
}));

(shadow.dom.redirect.cljs$core$IFn$_invoke$arity$2 = (function (path,query_params){
return (document["location"]["href"] = shadow.dom.build_url(path,query_params));
}));

(shadow.dom.redirect.cljs$lang$maxFixedArity = 2);

shadow.dom.reload_BANG_ = (function shadow$dom$reload_BANG_(){
return (document.location.href = document.location.href);
});
shadow.dom.tag_name = (function shadow$dom$tag_name(el){
var dom = shadow.dom.dom_node(el);
return dom.tagName;
});
shadow.dom.insert_after = (function shadow$dom$insert_after(ref,new$){
var new_node = shadow.dom.dom_node(new$);
goog.dom.insertSiblingAfter(new_node,shadow.dom.dom_node(ref));

return new_node;
});
shadow.dom.insert_before = (function shadow$dom$insert_before(ref,new$){
var new_node = shadow.dom.dom_node(new$);
goog.dom.insertSiblingBefore(new_node,shadow.dom.dom_node(ref));

return new_node;
});
shadow.dom.insert_first = (function shadow$dom$insert_first(ref,new$){
var temp__5733__auto__ = shadow.dom.dom_node(ref).firstChild;
if(cljs.core.truth_(temp__5733__auto__)){
var child = temp__5733__auto__;
return shadow.dom.insert_before(child,new$);
} else {
return shadow.dom.append.cljs$core$IFn$_invoke$arity$2(ref,new$);
}
});
shadow.dom.index_of = (function shadow$dom$index_of(el){
var el__$1 = shadow.dom.dom_node(el);
var i = (0);
while(true){
var ps = el__$1.previousSibling;
if((ps == null)){
return i;
} else {
var G__34571 = ps;
var G__34572 = (i + (1));
el__$1 = G__34571;
i = G__34572;
continue;
}
break;
}
});
shadow.dom.get_parent = (function shadow$dom$get_parent(el){
return goog.dom.getParentElement(shadow.dom.dom_node(el));
});
shadow.dom.parents = (function shadow$dom$parents(el){
var parent = shadow.dom.get_parent(el);
if(cljs.core.truth_(parent)){
return cljs.core.cons(parent,(new cljs.core.LazySeq(null,(function (){
return (shadow.dom.parents.cljs$core$IFn$_invoke$arity$1 ? shadow.dom.parents.cljs$core$IFn$_invoke$arity$1(parent) : shadow.dom.parents.call(null,parent));
}),null,null)));
} else {
return null;
}
});
shadow.dom.matches = (function shadow$dom$matches(el,sel){
return shadow.dom.dom_node(el).matches(sel);
});
shadow.dom.get_next_sibling = (function shadow$dom$get_next_sibling(el){
return goog.dom.getNextElementSibling(shadow.dom.dom_node(el));
});
shadow.dom.get_previous_sibling = (function shadow$dom$get_previous_sibling(el){
return goog.dom.getPreviousElementSibling(shadow.dom.dom_node(el));
});
shadow.dom.xmlns = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(new cljs.core.PersistentArrayMap(null, 2, ["svg","http://www.w3.org/2000/svg","xlink","http://www.w3.org/1999/xlink"], null));
shadow.dom.create_svg_node = (function shadow$dom$create_svg_node(tag_def,props){
var vec__34018 = shadow.dom.parse_tag(tag_def);
var tag_name = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34018,(0),null);
var tag_id = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34018,(1),null);
var tag_classes = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34018,(2),null);
var el = document.createElementNS("http://www.w3.org/2000/svg",tag_name);
if(cljs.core.truth_(tag_id)){
el.setAttribute("id",tag_id);
} else {
}

if(cljs.core.truth_(tag_classes)){
el.setAttribute("class",shadow.dom.merge_class_string(new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(props),tag_classes));
} else {
}

var seq__34023_34573 = cljs.core.seq(props);
var chunk__34024_34574 = null;
var count__34025_34575 = (0);
var i__34026_34576 = (0);
while(true){
if((i__34026_34576 < count__34025_34575)){
var vec__34038_34577 = chunk__34024_34574.cljs$core$IIndexed$_nth$arity$2(null,i__34026_34576);
var k_34578 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34038_34577,(0),null);
var v_34579 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34038_34577,(1),null);
el.setAttributeNS((function (){var temp__5735__auto__ = cljs.core.namespace(k_34578);
if(cljs.core.truth_(temp__5735__auto__)){
var ns = temp__5735__auto__;
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(shadow.dom.xmlns),ns);
} else {
return null;
}
})(),cljs.core.name(k_34578),v_34579);


var G__34580 = seq__34023_34573;
var G__34584 = chunk__34024_34574;
var G__34585 = count__34025_34575;
var G__34586 = (i__34026_34576 + (1));
seq__34023_34573 = G__34580;
chunk__34024_34574 = G__34584;
count__34025_34575 = G__34585;
i__34026_34576 = G__34586;
continue;
} else {
var temp__5735__auto___34587 = cljs.core.seq(seq__34023_34573);
if(temp__5735__auto___34587){
var seq__34023_34588__$1 = temp__5735__auto___34587;
if(cljs.core.chunked_seq_QMARK_(seq__34023_34588__$1)){
var c__4556__auto___34589 = cljs.core.chunk_first(seq__34023_34588__$1);
var G__34590 = cljs.core.chunk_rest(seq__34023_34588__$1);
var G__34591 = c__4556__auto___34589;
var G__34592 = cljs.core.count(c__4556__auto___34589);
var G__34593 = (0);
seq__34023_34573 = G__34590;
chunk__34024_34574 = G__34591;
count__34025_34575 = G__34592;
i__34026_34576 = G__34593;
continue;
} else {
var vec__34042_34594 = cljs.core.first(seq__34023_34588__$1);
var k_34595 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34042_34594,(0),null);
var v_34596 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34042_34594,(1),null);
el.setAttributeNS((function (){var temp__5735__auto____$1 = cljs.core.namespace(k_34595);
if(cljs.core.truth_(temp__5735__auto____$1)){
var ns = temp__5735__auto____$1;
return cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(shadow.dom.xmlns),ns);
} else {
return null;
}
})(),cljs.core.name(k_34595),v_34596);


var G__34597 = cljs.core.next(seq__34023_34588__$1);
var G__34598 = null;
var G__34599 = (0);
var G__34600 = (0);
seq__34023_34573 = G__34597;
chunk__34024_34574 = G__34598;
count__34025_34575 = G__34599;
i__34026_34576 = G__34600;
continue;
}
} else {
}
}
break;
}

return el;
});
shadow.dom.svg_node = (function shadow$dom$svg_node(el){
if((el == null)){
return null;
} else {
if((((!((el == null))))?((((false) || ((cljs.core.PROTOCOL_SENTINEL === el.shadow$dom$SVGElement$))))?true:false):false)){
return el.shadow$dom$SVGElement$_to_svg$arity$1(null);
} else {
return el;

}
}
});
shadow.dom.make_svg_node = (function shadow$dom$make_svg_node(structure){
var vec__34059 = shadow.dom.destructure_node(shadow.dom.create_svg_node,structure);
var node = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34059,(0),null);
var node_children = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__34059,(1),null);
var seq__34065_34604 = cljs.core.seq(node_children);
var chunk__34067_34605 = null;
var count__34068_34606 = (0);
var i__34069_34607 = (0);
while(true){
if((i__34069_34607 < count__34068_34606)){
var child_struct_34608 = chunk__34067_34605.cljs$core$IIndexed$_nth$arity$2(null,i__34069_34607);
if((!((child_struct_34608 == null)))){
if(typeof child_struct_34608 === 'string'){
var text_34609 = (node["textContent"]);
(node["textContent"] = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(text_34609),child_struct_34608].join(''));
} else {
var children_34610 = shadow.dom.svg_node(child_struct_34608);
if(cljs.core.seq_QMARK_(children_34610)){
var seq__34121_34611 = cljs.core.seq(children_34610);
var chunk__34123_34612 = null;
var count__34124_34613 = (0);
var i__34125_34614 = (0);
while(true){
if((i__34125_34614 < count__34124_34613)){
var child_34615 = chunk__34123_34612.cljs$core$IIndexed$_nth$arity$2(null,i__34125_34614);
if(cljs.core.truth_(child_34615)){
node.appendChild(child_34615);


var G__34616 = seq__34121_34611;
var G__34617 = chunk__34123_34612;
var G__34618 = count__34124_34613;
var G__34619 = (i__34125_34614 + (1));
seq__34121_34611 = G__34616;
chunk__34123_34612 = G__34617;
count__34124_34613 = G__34618;
i__34125_34614 = G__34619;
continue;
} else {
var G__34620 = seq__34121_34611;
var G__34621 = chunk__34123_34612;
var G__34622 = count__34124_34613;
var G__34623 = (i__34125_34614 + (1));
seq__34121_34611 = G__34620;
chunk__34123_34612 = G__34621;
count__34124_34613 = G__34622;
i__34125_34614 = G__34623;
continue;
}
} else {
var temp__5735__auto___34624 = cljs.core.seq(seq__34121_34611);
if(temp__5735__auto___34624){
var seq__34121_34625__$1 = temp__5735__auto___34624;
if(cljs.core.chunked_seq_QMARK_(seq__34121_34625__$1)){
var c__4556__auto___34626 = cljs.core.chunk_first(seq__34121_34625__$1);
var G__34627 = cljs.core.chunk_rest(seq__34121_34625__$1);
var G__34628 = c__4556__auto___34626;
var G__34629 = cljs.core.count(c__4556__auto___34626);
var G__34630 = (0);
seq__34121_34611 = G__34627;
chunk__34123_34612 = G__34628;
count__34124_34613 = G__34629;
i__34125_34614 = G__34630;
continue;
} else {
var child_34631 = cljs.core.first(seq__34121_34625__$1);
if(cljs.core.truth_(child_34631)){
node.appendChild(child_34631);


var G__34632 = cljs.core.next(seq__34121_34625__$1);
var G__34633 = null;
var G__34634 = (0);
var G__34635 = (0);
seq__34121_34611 = G__34632;
chunk__34123_34612 = G__34633;
count__34124_34613 = G__34634;
i__34125_34614 = G__34635;
continue;
} else {
var G__34636 = cljs.core.next(seq__34121_34625__$1);
var G__34637 = null;
var G__34638 = (0);
var G__34639 = (0);
seq__34121_34611 = G__34636;
chunk__34123_34612 = G__34637;
count__34124_34613 = G__34638;
i__34125_34614 = G__34639;
continue;
}
}
} else {
}
}
break;
}
} else {
node.appendChild(children_34610);
}
}


var G__34640 = seq__34065_34604;
var G__34641 = chunk__34067_34605;
var G__34642 = count__34068_34606;
var G__34643 = (i__34069_34607 + (1));
seq__34065_34604 = G__34640;
chunk__34067_34605 = G__34641;
count__34068_34606 = G__34642;
i__34069_34607 = G__34643;
continue;
} else {
var G__34647 = seq__34065_34604;
var G__34648 = chunk__34067_34605;
var G__34649 = count__34068_34606;
var G__34650 = (i__34069_34607 + (1));
seq__34065_34604 = G__34647;
chunk__34067_34605 = G__34648;
count__34068_34606 = G__34649;
i__34069_34607 = G__34650;
continue;
}
} else {
var temp__5735__auto___34651 = cljs.core.seq(seq__34065_34604);
if(temp__5735__auto___34651){
var seq__34065_34652__$1 = temp__5735__auto___34651;
if(cljs.core.chunked_seq_QMARK_(seq__34065_34652__$1)){
var c__4556__auto___34653 = cljs.core.chunk_first(seq__34065_34652__$1);
var G__34654 = cljs.core.chunk_rest(seq__34065_34652__$1);
var G__34655 = c__4556__auto___34653;
var G__34656 = cljs.core.count(c__4556__auto___34653);
var G__34657 = (0);
seq__34065_34604 = G__34654;
chunk__34067_34605 = G__34655;
count__34068_34606 = G__34656;
i__34069_34607 = G__34657;
continue;
} else {
var child_struct_34658 = cljs.core.first(seq__34065_34652__$1);
if((!((child_struct_34658 == null)))){
if(typeof child_struct_34658 === 'string'){
var text_34659 = (node["textContent"]);
(node["textContent"] = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(text_34659),child_struct_34658].join(''));
} else {
var children_34660 = shadow.dom.svg_node(child_struct_34658);
if(cljs.core.seq_QMARK_(children_34660)){
var seq__34134_34661 = cljs.core.seq(children_34660);
var chunk__34136_34662 = null;
var count__34137_34663 = (0);
var i__34138_34664 = (0);
while(true){
if((i__34138_34664 < count__34137_34663)){
var child_34665 = chunk__34136_34662.cljs$core$IIndexed$_nth$arity$2(null,i__34138_34664);
if(cljs.core.truth_(child_34665)){
node.appendChild(child_34665);


var G__34666 = seq__34134_34661;
var G__34667 = chunk__34136_34662;
var G__34668 = count__34137_34663;
var G__34669 = (i__34138_34664 + (1));
seq__34134_34661 = G__34666;
chunk__34136_34662 = G__34667;
count__34137_34663 = G__34668;
i__34138_34664 = G__34669;
continue;
} else {
var G__34670 = seq__34134_34661;
var G__34671 = chunk__34136_34662;
var G__34672 = count__34137_34663;
var G__34673 = (i__34138_34664 + (1));
seq__34134_34661 = G__34670;
chunk__34136_34662 = G__34671;
count__34137_34663 = G__34672;
i__34138_34664 = G__34673;
continue;
}
} else {
var temp__5735__auto___34674__$1 = cljs.core.seq(seq__34134_34661);
if(temp__5735__auto___34674__$1){
var seq__34134_34675__$1 = temp__5735__auto___34674__$1;
if(cljs.core.chunked_seq_QMARK_(seq__34134_34675__$1)){
var c__4556__auto___34676 = cljs.core.chunk_first(seq__34134_34675__$1);
var G__34677 = cljs.core.chunk_rest(seq__34134_34675__$1);
var G__34678 = c__4556__auto___34676;
var G__34679 = cljs.core.count(c__4556__auto___34676);
var G__34680 = (0);
seq__34134_34661 = G__34677;
chunk__34136_34662 = G__34678;
count__34137_34663 = G__34679;
i__34138_34664 = G__34680;
continue;
} else {
var child_34681 = cljs.core.first(seq__34134_34675__$1);
if(cljs.core.truth_(child_34681)){
node.appendChild(child_34681);


var G__34682 = cljs.core.next(seq__34134_34675__$1);
var G__34683 = null;
var G__34684 = (0);
var G__34685 = (0);
seq__34134_34661 = G__34682;
chunk__34136_34662 = G__34683;
count__34137_34663 = G__34684;
i__34138_34664 = G__34685;
continue;
} else {
var G__34686 = cljs.core.next(seq__34134_34675__$1);
var G__34687 = null;
var G__34688 = (0);
var G__34689 = (0);
seq__34134_34661 = G__34686;
chunk__34136_34662 = G__34687;
count__34137_34663 = G__34688;
i__34138_34664 = G__34689;
continue;
}
}
} else {
}
}
break;
}
} else {
node.appendChild(children_34660);
}
}


var G__34690 = cljs.core.next(seq__34065_34652__$1);
var G__34691 = null;
var G__34692 = (0);
var G__34693 = (0);
seq__34065_34604 = G__34690;
chunk__34067_34605 = G__34691;
count__34068_34606 = G__34692;
i__34069_34607 = G__34693;
continue;
} else {
var G__34694 = cljs.core.next(seq__34065_34652__$1);
var G__34695 = null;
var G__34696 = (0);
var G__34697 = (0);
seq__34065_34604 = G__34694;
chunk__34067_34605 = G__34695;
count__34068_34606 = G__34696;
i__34069_34607 = G__34697;
continue;
}
}
} else {
}
}
break;
}

return node;
});
goog.object.set(shadow.dom.SVGElement,"string",true);

goog.object.set(shadow.dom._to_svg,"string",(function (this$){
if((this$ instanceof cljs.core.Keyword)){
return shadow.dom.make_svg_node(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [this$], null));
} else {
throw cljs.core.ex_info.cljs$core$IFn$_invoke$arity$2("strings cannot be in svgs",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"this","this",-611633625),this$], null));
}
}));

(cljs.core.PersistentVector.prototype.shadow$dom$SVGElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.PersistentVector.prototype.shadow$dom$SVGElement$_to_svg$arity$1 = (function (this$){
var this$__$1 = this;
return shadow.dom.make_svg_node(this$__$1);
}));

(cljs.core.LazySeq.prototype.shadow$dom$SVGElement$ = cljs.core.PROTOCOL_SENTINEL);

(cljs.core.LazySeq.prototype.shadow$dom$SVGElement$_to_svg$arity$1 = (function (this$){
var this$__$1 = this;
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(shadow.dom._to_svg,this$__$1);
}));

goog.object.set(shadow.dom.SVGElement,"null",true);

goog.object.set(shadow.dom._to_svg,"null",(function (_){
return null;
}));
shadow.dom.svg = (function shadow$dom$svg(var_args){
var args__4742__auto__ = [];
var len__4736__auto___34698 = arguments.length;
var i__4737__auto___34699 = (0);
while(true){
if((i__4737__auto___34699 < len__4736__auto___34698)){
args__4742__auto__.push((arguments[i__4737__auto___34699]));

var G__34700 = (i__4737__auto___34699 + (1));
i__4737__auto___34699 = G__34700;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return shadow.dom.svg.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(shadow.dom.svg.cljs$core$IFn$_invoke$arity$variadic = (function (attrs,children){
return shadow.dom._to_svg(cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"svg","svg",856789142),attrs], null),children)));
}));

(shadow.dom.svg.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(shadow.dom.svg.cljs$lang$applyTo = (function (seq34173){
var G__34174 = cljs.core.first(seq34173);
var seq34173__$1 = cljs.core.next(seq34173);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__34174,seq34173__$1);
}));

/**
 * returns a channel for events on el
 * transform-fn should be a (fn [e el] some-val) where some-val will be put on the chan
 * once-or-cleanup handles the removal of the event handler
 * - true: remove after one event
 * - false: never removed
 * - chan: remove on msg/close
 */
shadow.dom.event_chan = (function shadow$dom$event_chan(var_args){
var G__34177 = arguments.length;
switch (G__34177) {
case 2:
return shadow.dom.event_chan.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return shadow.dom.event_chan.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
case 4:
return shadow.dom.event_chan.cljs$core$IFn$_invoke$arity$4((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(shadow.dom.event_chan.cljs$core$IFn$_invoke$arity$2 = (function (el,event){
return shadow.dom.event_chan.cljs$core$IFn$_invoke$arity$4(el,event,null,false);
}));

(shadow.dom.event_chan.cljs$core$IFn$_invoke$arity$3 = (function (el,event,xf){
return shadow.dom.event_chan.cljs$core$IFn$_invoke$arity$4(el,event,xf,false);
}));

(shadow.dom.event_chan.cljs$core$IFn$_invoke$arity$4 = (function (el,event,xf,once_or_cleanup){
var buf = cljs.core.async.sliding_buffer((1));
var chan = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$2(buf,xf);
var event_fn = (function shadow$dom$event_fn(e){
cljs.core.async.put_BANG_.cljs$core$IFn$_invoke$arity$2(chan,e);

if(once_or_cleanup === true){
shadow.dom.remove_event_handler(el,event,shadow$dom$event_fn);

return cljs.core.async.close_BANG_(chan);
} else {
return null;
}
});
shadow.dom.dom_listen(shadow.dom.dom_node(el),cljs.core.name(event),event_fn);

if(cljs.core.truth_((function (){var and__4115__auto__ = once_or_cleanup;
if(cljs.core.truth_(and__4115__auto__)){
return (!(once_or_cleanup === true));
} else {
return and__4115__auto__;
}
})())){
var c__31115__auto___34706 = cljs.core.async.chan.cljs$core$IFn$_invoke$arity$1((1));
cljs.core.async.impl.dispatch.run((function (){
var f__31116__auto__ = (function (){var switch__30837__auto__ = (function (state_34189){
var state_val_34190 = (state_34189[(1)]);
if((state_val_34190 === (1))){
var state_34189__$1 = state_34189;
return cljs.core.async.impl.ioc_helpers.take_BANG_(state_34189__$1,(2),once_or_cleanup);
} else {
if((state_val_34190 === (2))){
var inst_34186 = (state_34189[(2)]);
var inst_34187 = shadow.dom.remove_event_handler(el,event,event_fn);
var state_34189__$1 = (function (){var statearr_34194 = state_34189;
(statearr_34194[(7)] = inst_34186);

return statearr_34194;
})();
return cljs.core.async.impl.ioc_helpers.return_chan(state_34189__$1,inst_34187);
} else {
return null;
}
}
});
return (function() {
var shadow$dom$state_machine__30838__auto__ = null;
var shadow$dom$state_machine__30838__auto____0 = (function (){
var statearr_34195 = [null,null,null,null,null,null,null,null];
(statearr_34195[(0)] = shadow$dom$state_machine__30838__auto__);

(statearr_34195[(1)] = (1));

return statearr_34195;
});
var shadow$dom$state_machine__30838__auto____1 = (function (state_34189){
while(true){
var ret_value__30839__auto__ = (function (){try{while(true){
var result__30840__auto__ = switch__30837__auto__(state_34189);
if(cljs.core.keyword_identical_QMARK_(result__30840__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
continue;
} else {
return result__30840__auto__;
}
break;
}
}catch (e34196){var ex__30841__auto__ = e34196;
var statearr_34197_34707 = state_34189;
(statearr_34197_34707[(2)] = ex__30841__auto__);


if(cljs.core.seq((state_34189[(4)]))){
var statearr_34198_34708 = state_34189;
(statearr_34198_34708[(1)] = cljs.core.first((state_34189[(4)])));

} else {
throw ex__30841__auto__;
}

return new cljs.core.Keyword(null,"recur","recur",-437573268);
}})();
if(cljs.core.keyword_identical_QMARK_(ret_value__30839__auto__,new cljs.core.Keyword(null,"recur","recur",-437573268))){
var G__34709 = state_34189;
state_34189 = G__34709;
continue;
} else {
return ret_value__30839__auto__;
}
break;
}
});
shadow$dom$state_machine__30838__auto__ = function(state_34189){
switch(arguments.length){
case 0:
return shadow$dom$state_machine__30838__auto____0.call(this);
case 1:
return shadow$dom$state_machine__30838__auto____1.call(this,state_34189);
}
throw(new Error('Invalid arity: ' + arguments.length));
};
shadow$dom$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$0 = shadow$dom$state_machine__30838__auto____0;
shadow$dom$state_machine__30838__auto__.cljs$core$IFn$_invoke$arity$1 = shadow$dom$state_machine__30838__auto____1;
return shadow$dom$state_machine__30838__auto__;
})()
})();
var state__31117__auto__ = (function (){var statearr_34199 = f__31116__auto__();
(statearr_34199[(6)] = c__31115__auto___34706);

return statearr_34199;
})();
return cljs.core.async.impl.ioc_helpers.run_state_machine_wrapped(state__31117__auto__);
}));

} else {
}

return chan;
}));

(shadow.dom.event_chan.cljs$lang$maxFixedArity = 4);


//# sourceMappingURL=shadow.dom.js.map
