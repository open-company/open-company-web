goog.provide('oc.web.components.ui.lazy_stream');
oc.web.components.ui.lazy_stream.lazy_stream = rum.core.build_defcs((function (s,stream_comp){
var container_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"container-data","container-data",-53681130));
var activity_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var foc_layout = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965));
var ready_QMARK_ = (function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.lazy-stream","delayed","oc.web.components.ui.lazy-stream/delayed",-234228760).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.map_QMARK_(container_data);
} else {
return and__4115__auto__;
}
})();
var attrs40144 = (cljs.core.truth_(ready_QMARK_)?(stream_comp.cljs$core$IFn$_invoke$arity$0 ? stream_comp.cljs$core$IFn$_invoke$arity$0() : stream_comp.call(null)):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.lazy-stream-interstitial","div.lazy-stream-interstitial",265761491),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(foc_layout,oc.web.dispatcher.other_foc_layout))?"collapsed":null),new cljs.core.Keyword(null,"style","style",-496642736),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"height","height",1025178622),[cljs.core.str.cljs$core$IFn$_invoke$arity$1((oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"scroll-y","scroll-y",-1381960567)) + (function (){var or__4126__auto__ = document.documentElement.clientHeight;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return window.innerHeight;
}
})())),"px"].join('')], null)], null)], null));
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs40144))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["lazy-stream"], null)], null),attrs40144], 0))):({"className": "lazy-stream"})),((cljs.core.map_QMARK_(attrs40144))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs40144)], null)));
}),new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.lazy-stream","delayed","oc.web.components.ui.lazy-stream/delayed",-234228760)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"container-data","container-data",-53681130)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"activity-data","activity-data",1293689136)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"foc-layout","foc-layout",-1925028965)], 0)),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.lib.utils.scroll_to_y.cljs$core$IFn$_invoke$arity$variadic(oc.web.dispatcher.route_param.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"scroll-y","scroll-y",-1381960567)),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([(0)], 0));

oc.web.lib.utils.after((100),(function (){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.lazy-stream","delayed","oc.web.components.ui.lazy-stream/delayed",-234228760).cljs$core$IFn$_invoke$arity$1(s),true);
}));

return s;
})], null)], null),"lazy-stream");

//# sourceMappingURL=oc.web.components.ui.lazy_stream.js.map
