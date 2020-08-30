goog.provide('rum.core');
/**
 * Given React component, returns Rum state associated with it.
 */
rum.core.state = (function rum$core$state(comp){
return goog.object.get(comp.state,":rum/state");
});
rum.core.extend_BANG_ = (function rum$core$extend_BANG_(obj,props){
var seq__38746 = cljs.core.seq(props);
var chunk__38748 = null;
var count__38749 = (0);
var i__38750 = (0);
while(true){
if((i__38750 < count__38749)){
var vec__38758 = chunk__38748.cljs$core$IIndexed$_nth$arity$2(null,i__38750);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38758,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38758,(1),null);
if((!((v == null)))){
goog.object.set(obj,cljs.core.name(k),cljs.core.clj__GT_js(v));


var G__38937 = seq__38746;
var G__38938 = chunk__38748;
var G__38939 = count__38749;
var G__38940 = (i__38750 + (1));
seq__38746 = G__38937;
chunk__38748 = G__38938;
count__38749 = G__38939;
i__38750 = G__38940;
continue;
} else {
var G__38941 = seq__38746;
var G__38942 = chunk__38748;
var G__38943 = count__38749;
var G__38944 = (i__38750 + (1));
seq__38746 = G__38941;
chunk__38748 = G__38942;
count__38749 = G__38943;
i__38750 = G__38944;
continue;
}
} else {
var temp__5735__auto__ = cljs.core.seq(seq__38746);
if(temp__5735__auto__){
var seq__38746__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__38746__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__38746__$1);
var G__38949 = cljs.core.chunk_rest(seq__38746__$1);
var G__38950 = c__4556__auto__;
var G__38951 = cljs.core.count(c__4556__auto__);
var G__38952 = (0);
seq__38746 = G__38949;
chunk__38748 = G__38950;
count__38749 = G__38951;
i__38750 = G__38952;
continue;
} else {
var vec__38761 = cljs.core.first(seq__38746__$1);
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38761,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38761,(1),null);
if((!((v == null)))){
goog.object.set(obj,cljs.core.name(k),cljs.core.clj__GT_js(v));


var G__38954 = cljs.core.next(seq__38746__$1);
var G__38955 = null;
var G__38956 = (0);
var G__38957 = (0);
seq__38746 = G__38954;
chunk__38748 = G__38955;
count__38749 = G__38956;
i__38750 = G__38957;
continue;
} else {
var G__38958 = cljs.core.next(seq__38746__$1);
var G__38959 = null;
var G__38960 = (0);
var G__38961 = (0);
seq__38746 = G__38958;
chunk__38748 = G__38959;
count__38749 = G__38960;
i__38750 = G__38961;
continue;
}
}
} else {
return null;
}
}
break;
}
});
rum.core.build_class = (function rum$core$build_class(render,mixins,display_name){
var init = rum.util.collect(new cljs.core.Keyword(null,"init","init",-1875481434),mixins);
var will_mount = rum.util.collect_STAR_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),new cljs.core.Keyword(null,"before-render","before-render",71256781)], null),mixins);
var render__$1 = render;
var wrap_render = rum.util.collect(new cljs.core.Keyword(null,"wrap-render","wrap-render",1782000986),mixins);
var wrapped_render = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__38765_SHARP_,p2__38764_SHARP_){
return (p2__38764_SHARP_.cljs$core$IFn$_invoke$arity$1 ? p2__38764_SHARP_.cljs$core$IFn$_invoke$arity$1(p1__38765_SHARP_) : p2__38764_SHARP_.call(null,p1__38765_SHARP_));
}),render__$1,wrap_render);
var did_mount = rum.util.collect_STAR_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),new cljs.core.Keyword(null,"after-render","after-render",1997533433)], null),mixins);
var did_remount = rum.util.collect(new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),mixins);
var should_update = rum.util.collect(new cljs.core.Keyword(null,"should-update","should-update",-1292781795),mixins);
var will_update = rum.util.collect_STAR_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"will-update","will-update",328062998),new cljs.core.Keyword(null,"before-render","before-render",71256781)], null),mixins);
var did_update = rum.util.collect_STAR_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"did-update","did-update",-2143702256),new cljs.core.Keyword(null,"after-render","after-render",1997533433)], null),mixins);
var did_catch = rum.util.collect(new cljs.core.Keyword(null,"did-catch","did-catch",2139522313),mixins);
var will_unmount = rum.util.collect(new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),mixins);
var child_context = rum.util.collect(new cljs.core.Keyword(null,"child-context","child-context",-1375270295),mixins);
var class_props = cljs.core.reduce.cljs$core$IFn$_invoke$arity$2(cljs.core.merge,rum.util.collect(new cljs.core.Keyword(null,"class-properties","class-properties",1351279702),mixins));
var static_props = cljs.core.reduce.cljs$core$IFn$_invoke$arity$2(cljs.core.merge,rum.util.collect(new cljs.core.Keyword(null,"static-properties","static-properties",-577838503),mixins));
var ctor = (function (props){
var this$ = this;
goog.object.set(this$,"state",({":rum/state": cljs.core.volatile_BANG_(rum.util.call_all.cljs$core$IFn$_invoke$arity$variadic(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(goog.object.get(props,":rum/initial-state"),new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248),this$),init,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([props], 0)))}));

return React.Component.call(this$,props);
});
var _ = goog.inherits(ctor,React.Component);
var prototype = goog.object.get(ctor,"prototype");
if(cljs.core.empty_QMARK_(will_mount)){
} else {
goog.object.set(prototype,"componentWillMount",(function (){
var this$ = this;
return cljs.core._vreset_BANG_(rum.core.state(this$),rum.util.call_all(cljs.core._deref(rum.core.state(this$)),will_mount));
}));
}

if(cljs.core.empty_QMARK_(did_mount)){
} else {
goog.object.set(prototype,"componentDidMount",(function (){
var this$ = this;
return cljs.core._vreset_BANG_(rum.core.state(this$),rum.util.call_all(cljs.core._deref(rum.core.state(this$)),did_mount));
}));
}

goog.object.set(prototype,"componentWillReceiveProps",(function (next_props){
var this$ = this;
var old_state = cljs.core.deref(rum.core.state(this$));
var state = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_state,goog.object.get(next_props,":rum/initial-state")], 0));
var next_state = cljs.core.reduce.cljs$core$IFn$_invoke$arity$3((function (p1__38767_SHARP_,p2__38766_SHARP_){
return (p2__38766_SHARP_.cljs$core$IFn$_invoke$arity$2 ? p2__38766_SHARP_.cljs$core$IFn$_invoke$arity$2(old_state,p1__38767_SHARP_) : p2__38766_SHARP_.call(null,old_state,p1__38767_SHARP_));
}),state,did_remount);
return this$.setState(({":rum/state": cljs.core.volatile_BANG_(next_state)}));
}));

if(cljs.core.empty_QMARK_(should_update)){
} else {
goog.object.set(prototype,"shouldComponentUpdate",(function (next_props,next_state){
var this$ = this;
var old_state = cljs.core.deref(rum.core.state(this$));
var new_state = cljs.core.deref(goog.object.get(next_state,":rum/state"));
var or__4126__auto__ = cljs.core.some((function (p1__38768_SHARP_){
return (p1__38768_SHARP_.cljs$core$IFn$_invoke$arity$2 ? p1__38768_SHARP_.cljs$core$IFn$_invoke$arity$2(old_state,new_state) : p1__38768_SHARP_.call(null,old_state,new_state));
}),should_update);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return false;
}
}));
}

if(cljs.core.empty_QMARK_(will_update)){
} else {
goog.object.set(prototype,"componentWillUpdate",(function (___$1,next_state){
var this$ = this;
var new_state = goog.object.get(next_state,":rum/state");
return cljs.core._vreset_BANG_(new_state,rum.util.call_all(cljs.core._deref(new_state),will_update));
}));
}

goog.object.set(prototype,"render",(function (){
var this$ = this;
var state = rum.core.state(this$);
var vec__38774 = (function (){var G__38777 = cljs.core.deref(state);
return (wrapped_render.cljs$core$IFn$_invoke$arity$1 ? wrapped_render.cljs$core$IFn$_invoke$arity$1(G__38777) : wrapped_render.call(null,G__38777));
})();
var dom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38774,(0),null);
var next_state = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38774,(1),null);
cljs.core.vreset_BANG_(state,next_state);

return dom;
}));

if(cljs.core.empty_QMARK_(did_update)){
} else {
goog.object.set(prototype,"componentDidUpdate",(function (___$1,___$2){
var this$ = this;
return cljs.core._vreset_BANG_(rum.core.state(this$),rum.util.call_all(cljs.core._deref(rum.core.state(this$)),did_update));
}));
}

if(cljs.core.empty_QMARK_(did_catch)){
} else {
goog.object.set(prototype,"componentDidCatch",(function (error,info){
var this$ = this;
cljs.core._vreset_BANG_(rum.core.state(this$),rum.util.call_all.cljs$core$IFn$_invoke$arity$variadic(cljs.core._deref(rum.core.state(this$)),did_catch,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([error,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("rum","component-stack","rum/component-stack",2037541138),goog.object.get(info,"componentStack")], null)], 0)));

return this$.forceUpdate();
}));
}

goog.object.set(prototype,"componentWillUnmount",(function (){
var this$ = this;
if(cljs.core.empty_QMARK_(will_unmount)){
} else {
cljs.core._vreset_BANG_(rum.core.state(this$),rum.util.call_all(cljs.core._deref(rum.core.state(this$)),will_unmount));
}

return goog.object.set(this$,":rum/unmounted?",true);
}));

if(cljs.core.empty_QMARK_(child_context)){
} else {
goog.object.set(prototype,"getChildContext",(function (){
var this$ = this;
var state = cljs.core.deref(rum.core.state(this$));
return cljs.core.clj__GT_js(cljs.core.transduce.cljs$core$IFn$_invoke$arity$4(cljs.core.map.cljs$core$IFn$_invoke$arity$1((function (p1__38769_SHARP_){
return (p1__38769_SHARP_.cljs$core$IFn$_invoke$arity$1 ? p1__38769_SHARP_.cljs$core$IFn$_invoke$arity$1(state) : p1__38769_SHARP_.call(null,state));
})),cljs.core.merge,cljs.core.PersistentArrayMap.EMPTY,child_context));
}));
}

rum.core.extend_BANG_(prototype,class_props);

goog.object.set(ctor,"displayName",display_name);

rum.core.extend_BANG_(ctor,static_props);

return ctor;
});
rum.core.build_ctor = (function rum$core$build_ctor(render,mixins,display_name){
var class$ = rum.core.build_class(render,mixins,display_name);
var key_fn = cljs.core.first(rum.util.collect(new cljs.core.Keyword(null,"key-fn","key-fn",-636154479),mixins));
var ctor = (((!((key_fn == null))))?(function() { 
var G__38975__delegate = function (args){
var props = ({":rum/initial-state": new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("rum","args","rum/args",1315791754),args], null), "key": cljs.core.apply.cljs$core$IFn$_invoke$arity$2(key_fn,args)});
return React.createElement(class$,props);
};
var G__38975 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__38976__i = 0, G__38976__a = new Array(arguments.length -  0);
while (G__38976__i < G__38976__a.length) {G__38976__a[G__38976__i] = arguments[G__38976__i + 0]; ++G__38976__i;}
  args = new cljs.core.IndexedSeq(G__38976__a,0,null);
} 
return G__38975__delegate.call(this,args);};
G__38975.cljs$lang$maxFixedArity = 0;
G__38975.cljs$lang$applyTo = (function (arglist__38977){
var args = cljs.core.seq(arglist__38977);
return G__38975__delegate(args);
});
G__38975.cljs$core$IFn$_invoke$arity$variadic = G__38975__delegate;
return G__38975;
})()
:(function() { 
var G__38978__delegate = function (args){
var props = ({":rum/initial-state": new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("rum","args","rum/args",1315791754),args], null)});
return React.createElement(class$,props);
};
var G__38978 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__38979__i = 0, G__38979__a = new Array(arguments.length -  0);
while (G__38979__i < G__38979__a.length) {G__38979__a[G__38979__i] = arguments[G__38979__i + 0]; ++G__38979__i;}
  args = new cljs.core.IndexedSeq(G__38979__a,0,null);
} 
return G__38978__delegate.call(this,args);};
G__38978.cljs$lang$maxFixedArity = 0;
G__38978.cljs$lang$applyTo = (function (arglist__38980){
var args = cljs.core.seq(arglist__38980);
return G__38978__delegate(args);
});
G__38978.cljs$core$IFn$_invoke$arity$variadic = G__38978__delegate;
return G__38978;
})()
);
return cljs.core.with_meta(ctor,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("rum","class","rum/class",-2030775258),class$], null));
});
rum.core.build_defc = (function rum$core$build_defc(render_body,mixins,display_name){
if(cljs.core.empty_QMARK_(mixins)){
var class$ = (function (props){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$2(render_body,(props[":rum/args"]));
});
var _ = (class$["displayName"] = display_name);
var ctor = (function() { 
var G__38981__delegate = function (args){
return React.createElement(class$,({":rum/args": args}));
};
var G__38981 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__38982__i = 0, G__38982__a = new Array(arguments.length -  0);
while (G__38982__i < G__38982__a.length) {G__38982__a[G__38982__i] = arguments[G__38982__i + 0]; ++G__38982__i;}
  args = new cljs.core.IndexedSeq(G__38982__a,0,null);
} 
return G__38981__delegate.call(this,args);};
G__38981.cljs$lang$maxFixedArity = 0;
G__38981.cljs$lang$applyTo = (function (arglist__38983){
var args = cljs.core.seq(arglist__38983);
return G__38981__delegate(args);
});
G__38981.cljs$core$IFn$_invoke$arity$variadic = G__38981__delegate;
return G__38981;
})()
;
return cljs.core.with_meta(ctor,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword("rum","class","rum/class",-2030775258),class$], null));
} else {
var render = (function (state){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.apply.cljs$core$IFn$_invoke$arity$2(render_body,new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(state)),state], null);
});
return rum.core.build_ctor(render,mixins,display_name);
}
});
rum.core.build_defcs = (function rum$core$build_defcs(render_body,mixins,display_name){
var render = (function (state){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.apply.cljs$core$IFn$_invoke$arity$3(render_body,state,new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(state)),state], null);
});
return rum.core.build_ctor(render,mixins,display_name);
});
rum.core.build_defcc = (function rum$core$build_defcc(render_body,mixins,display_name){
var render = (function (state){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.apply.cljs$core$IFn$_invoke$arity$3(render_body,new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(state),new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(state)),state], null);
});
return rum.core.build_ctor(render,mixins,display_name);
});
rum.core.schedule = (function (){var or__4126__auto__ = (((typeof window !== 'undefined'))?(function (){var or__4126__auto__ = window.requestAnimationFrame;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = window.webkitRequestAnimationFrame;
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
var or__4126__auto____$2 = window.mozRequestAnimationFrame;
if(cljs.core.truth_(or__4126__auto____$2)){
return or__4126__auto____$2;
} else {
return window.msRequestAnimationFrame;
}
}
}
})():false);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return (function (p1__38823_SHARP_){
return setTimeout(p1__38823_SHARP_,(16));
});
}
})();
rum.core.batch = (function (){var or__4126__auto__ = (((typeof ReactNative !== 'undefined'))?ReactNative.unstable_batchedUpdates:null);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
var or__4126__auto____$1 = (((typeof ReactDOM !== 'undefined'))?ReactDOM.unstable_batchedUpdates:null);
if(cljs.core.truth_(or__4126__auto____$1)){
return or__4126__auto____$1;
} else {
return (function (f,a){
return (f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(a) : f.call(null,a));
});
}
}
})();
rum.core.empty_queue = cljs.core.PersistentVector.EMPTY;
rum.core.render_queue = cljs.core.volatile_BANG_(rum.core.empty_queue);
rum.core.render_all = (function rum$core$render_all(queue){
var seq__38827 = cljs.core.seq(queue);
var chunk__38829 = null;
var count__38830 = (0);
var i__38831 = (0);
while(true){
if((i__38831 < count__38830)){
var comp = chunk__38829.cljs$core$IIndexed$_nth$arity$2(null,i__38831);
if((((!((comp == null)))) && (cljs.core.not(goog.object.get(comp,":rum/unmounted?"))))){
comp.forceUpdate();


var G__38984 = seq__38827;
var G__38985 = chunk__38829;
var G__38986 = count__38830;
var G__38987 = (i__38831 + (1));
seq__38827 = G__38984;
chunk__38829 = G__38985;
count__38830 = G__38986;
i__38831 = G__38987;
continue;
} else {
var G__38988 = seq__38827;
var G__38989 = chunk__38829;
var G__38990 = count__38830;
var G__38991 = (i__38831 + (1));
seq__38827 = G__38988;
chunk__38829 = G__38989;
count__38830 = G__38990;
i__38831 = G__38991;
continue;
}
} else {
var temp__5735__auto__ = cljs.core.seq(seq__38827);
if(temp__5735__auto__){
var seq__38827__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__38827__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__38827__$1);
var G__38992 = cljs.core.chunk_rest(seq__38827__$1);
var G__38993 = c__4556__auto__;
var G__38994 = cljs.core.count(c__4556__auto__);
var G__38995 = (0);
seq__38827 = G__38992;
chunk__38829 = G__38993;
count__38830 = G__38994;
i__38831 = G__38995;
continue;
} else {
var comp = cljs.core.first(seq__38827__$1);
if((((!((comp == null)))) && (cljs.core.not(goog.object.get(comp,":rum/unmounted?"))))){
comp.forceUpdate();


var G__38996 = cljs.core.next(seq__38827__$1);
var G__38997 = null;
var G__38998 = (0);
var G__38999 = (0);
seq__38827 = G__38996;
chunk__38829 = G__38997;
count__38830 = G__38998;
i__38831 = G__38999;
continue;
} else {
var G__39000 = cljs.core.next(seq__38827__$1);
var G__39001 = null;
var G__39002 = (0);
var G__39003 = (0);
seq__38827 = G__39000;
chunk__38829 = G__39001;
count__38830 = G__39002;
i__38831 = G__39003;
continue;
}
}
} else {
return null;
}
}
break;
}
});
rum.core.render = (function rum$core$render(){
var queue = cljs.core.deref(rum.core.render_queue);
cljs.core.vreset_BANG_(rum.core.render_queue,rum.core.empty_queue);

return (rum.core.batch.cljs$core$IFn$_invoke$arity$2 ? rum.core.batch.cljs$core$IFn$_invoke$arity$2(rum.core.render_all,queue) : rum.core.batch.call(null,rum.core.render_all,queue));
});
/**
 * Schedules react component to be rendered on next animation frame.
 */
rum.core.request_render = (function rum$core$request_render(component){
if(cljs.core.empty_QMARK_(cljs.core.deref(rum.core.render_queue))){
(rum.core.schedule.cljs$core$IFn$_invoke$arity$1 ? rum.core.schedule.cljs$core$IFn$_invoke$arity$1(rum.core.render) : rum.core.schedule.call(null,rum.core.render));
} else {
}

return rum.core.render_queue.cljs$core$IVolatile$_vreset_BANG_$arity$2(null,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(rum.core.render_queue.cljs$core$IDeref$_deref$arity$1(null),component));
});
/**
 * Add element to the DOM tree. Idempotent. Subsequent mounts will just update element.
 */
rum.core.mount = (function rum$core$mount(element,node){
ReactDOM.render(element,node);

return null;
});
/**
 * Removes component from the DOM tree.
 */
rum.core.unmount = (function rum$core$unmount(node){
return ReactDOM.unmountComponentAtNode(node);
});
/**
 * Same as [[mount]] but must be called on DOM tree already rendered by a server via [[render-html]].
 */
rum.core.hydrate = (function rum$core$hydrate(element,node){
return ReactDOM.hydrate(element,node);
});
/**
 * Render `element` in a DOM `node` that is ouside of current DOM hierarchy.
 */
rum.core.portal = (function rum$core$portal(element,node){
return ReactDOM.createPortal(element,node);
});
/**
 * Adds React key to element.
 * 
 * ```
 * (rum/defc label [text] [:div text])
 * 
 * (-> (label)
 *     (rum/with-key "abc")
 *     (rum/mount js/document.body))
 * ```
 */
rum.core.with_key = (function rum$core$with_key(element,key){
return React.cloneElement(element,({"key": key}),null);
});
/**
 * Adds React ref (string or callback) to element.
 * 
 * ```
 * (rum/defc label [text] [:div text])
 * 
 * (-> (label)
 *     (rum/with-ref "abc")
 *     (rum/mount js/document.body))
 * ```
 */
rum.core.with_ref = (function rum$core$with_ref(element,ref){
return React.cloneElement(element,({"ref": ref}),null);
});
/**
 * Given state, returns top-level DOM node of component. Call it during lifecycle callbacks. Can’t be called during render.
 */
rum.core.dom_node = (function rum$core$dom_node(state){
return ReactDOM.findDOMNode(new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(state));
});
/**
 * Given state and ref handle, returns React component.
 */
rum.core.ref = (function rum$core$ref(state,key){
return ((new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(state)["refs"])[cljs.core.name(key)]);
});
/**
 * Given state and ref handle, returns DOM node associated with ref.
 */
rum.core.ref_node = (function rum$core$ref_node(state,key){
return ReactDOM.findDOMNode(rum.core.ref(state,cljs.core.name(key)));
});
/**
 * Mixin. Will avoid re-render if none of component’s arguments have changed. Does equality check (`=`) on all arguments.
 *   
 * ```
 * (rum/defc label < rum/static
 *   [text]
 *   [:div text])
 *   
 * (rum/mount (label "abc") js/document.body)
 * 
 * ;; def != abc, will re-render
 * (rum/mount (label "def") js/document.body)
 * 
 * ;; def == def, won’t re-render
 * (rum/mount (label "def") js/document.body)
 * ```
 */
rum.core.static$ = new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"should-update","should-update",-1292781795),(function (old_state,new_state){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(old_state),new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(new_state));
})], null);
/**
 * Mixin constructor. Adds an atom to component’s state that can be used to keep stuff during component’s lifecycle. Component will be re-rendered if atom’s value changes. Atom is stored under user-provided key or under `:rum/local` by default.
 *   
 * ```
 * (rum/defcs counter < (rum/local 0 :cnt)
 *   [state label]
 *   (let [*cnt (:cnt state)]
 *     [:div {:on-click (fn [_] (swap! *cnt inc))}
 *       label @*cnt]))
 * 
 * (rum/mount (counter "Click count: "))
 * ```
 */
rum.core.local = (function rum$core$local(var_args){
var G__38892 = arguments.length;
switch (G__38892) {
case 1:
return rum.core.local.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return rum.core.local.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error(["Invalid arity: ",cljs.core.str.cljs$core$IFn$_invoke$arity$1(arguments.length)].join('')));

}
});

(rum.core.local.cljs$core$IFn$_invoke$arity$1 = (function (initial){
return rum.core.local.cljs$core$IFn$_invoke$arity$2(initial,new cljs.core.Keyword("rum","local","rum/local",-1497916586));
}));

(rum.core.local.cljs$core$IFn$_invoke$arity$2 = (function (initial,key){
return new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (state){
var local_state = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(initial);
var component = new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(state);
cljs.core.add_watch(local_state,key,(function (_,___$1,___$2,___$3){
return rum.core.request_render(component);
}));

return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(state,key,local_state);
})], null);
}));

(rum.core.local.cljs$lang$maxFixedArity = 2);

/**
 * Mixin. Works in conjunction with [[react]].
 *   
 * ```
 * (rum/defc comp < rum/reactive
 *   [*counter]
 *   [:div (rum/react counter)])
 * 
 * (def *counter (atom 0))
 * (rum/mount (comp *counter) js/document.body)
 * (swap! *counter inc) ;; will force comp to re-render
 * ```
 */
rum.core.reactive = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"init","init",-1875481434),(function (state,props){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(state,new cljs.core.Keyword("rum.reactive","key","rum.reactive/key",-803425142),cljs.core.random_uuid());
}),new cljs.core.Keyword(null,"wrap-render","wrap-render",1782000986),(function (render_fn){
return (function (state){
var _STAR_reactions_STAR__orig_val__38900 = rum.core._STAR_reactions_STAR_;
var _STAR_reactions_STAR__temp_val__38901 = cljs.core.volatile_BANG_(cljs.core.PersistentHashSet.EMPTY);
(rum.core._STAR_reactions_STAR_ = _STAR_reactions_STAR__temp_val__38901);

try{var comp = new cljs.core.Keyword("rum","react-component","rum/react-component",-1879897248).cljs$core$IFn$_invoke$arity$1(state);
var old_reactions = new cljs.core.Keyword("rum.reactive","refs","rum.reactive/refs",-814076325).cljs$core$IFn$_invoke$arity$2(state,cljs.core.PersistentHashSet.EMPTY);
var vec__38902 = (render_fn.cljs$core$IFn$_invoke$arity$1 ? render_fn.cljs$core$IFn$_invoke$arity$1(state) : render_fn.call(null,state));
var dom = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38902,(0),null);
var next_state = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38902,(1),null);
var new_reactions = cljs.core.deref(rum.core._STAR_reactions_STAR_);
var key = new cljs.core.Keyword("rum.reactive","key","rum.reactive/key",-803425142).cljs$core$IFn$_invoke$arity$1(state);
var seq__38905_39010 = cljs.core.seq(old_reactions);
var chunk__38906_39011 = null;
var count__38907_39012 = (0);
var i__38908_39013 = (0);
while(true){
if((i__38908_39013 < count__38907_39012)){
var ref_39014 = chunk__38906_39011.cljs$core$IIndexed$_nth$arity$2(null,i__38908_39013);
if(cljs.core.contains_QMARK_(new_reactions,ref_39014)){
} else {
cljs.core.remove_watch(ref_39014,key);
}


var G__39015 = seq__38905_39010;
var G__39016 = chunk__38906_39011;
var G__39017 = count__38907_39012;
var G__39018 = (i__38908_39013 + (1));
seq__38905_39010 = G__39015;
chunk__38906_39011 = G__39016;
count__38907_39012 = G__39017;
i__38908_39013 = G__39018;
continue;
} else {
var temp__5735__auto___39019 = cljs.core.seq(seq__38905_39010);
if(temp__5735__auto___39019){
var seq__38905_39020__$1 = temp__5735__auto___39019;
if(cljs.core.chunked_seq_QMARK_(seq__38905_39020__$1)){
var c__4556__auto___39021 = cljs.core.chunk_first(seq__38905_39020__$1);
var G__39022 = cljs.core.chunk_rest(seq__38905_39020__$1);
var G__39023 = c__4556__auto___39021;
var G__39024 = cljs.core.count(c__4556__auto___39021);
var G__39025 = (0);
seq__38905_39010 = G__39022;
chunk__38906_39011 = G__39023;
count__38907_39012 = G__39024;
i__38908_39013 = G__39025;
continue;
} else {
var ref_39026 = cljs.core.first(seq__38905_39020__$1);
if(cljs.core.contains_QMARK_(new_reactions,ref_39026)){
} else {
cljs.core.remove_watch(ref_39026,key);
}


var G__39027 = cljs.core.next(seq__38905_39020__$1);
var G__39028 = null;
var G__39029 = (0);
var G__39030 = (0);
seq__38905_39010 = G__39027;
chunk__38906_39011 = G__39028;
count__38907_39012 = G__39029;
i__38908_39013 = G__39030;
continue;
}
} else {
}
}
break;
}

var seq__38909_39031 = cljs.core.seq(new_reactions);
var chunk__38910_39032 = null;
var count__38911_39033 = (0);
var i__38912_39034 = (0);
while(true){
if((i__38912_39034 < count__38911_39033)){
var ref_39035 = chunk__38910_39032.cljs$core$IIndexed$_nth$arity$2(null,i__38912_39034);
if(cljs.core.contains_QMARK_(old_reactions,ref_39035)){
} else {
cljs.core.add_watch(ref_39035,key,((function (seq__38909_39031,chunk__38910_39032,count__38911_39033,i__38912_39034,ref_39035,comp,old_reactions,vec__38902,dom,next_state,new_reactions,key,_STAR_reactions_STAR__orig_val__38900,_STAR_reactions_STAR__temp_val__38901){
return (function (_,___$1,___$2,___$3){
return rum.core.request_render(comp);
});})(seq__38909_39031,chunk__38910_39032,count__38911_39033,i__38912_39034,ref_39035,comp,old_reactions,vec__38902,dom,next_state,new_reactions,key,_STAR_reactions_STAR__orig_val__38900,_STAR_reactions_STAR__temp_val__38901))
);
}


var G__39036 = seq__38909_39031;
var G__39037 = chunk__38910_39032;
var G__39038 = count__38911_39033;
var G__39039 = (i__38912_39034 + (1));
seq__38909_39031 = G__39036;
chunk__38910_39032 = G__39037;
count__38911_39033 = G__39038;
i__38912_39034 = G__39039;
continue;
} else {
var temp__5735__auto___39040 = cljs.core.seq(seq__38909_39031);
if(temp__5735__auto___39040){
var seq__38909_39041__$1 = temp__5735__auto___39040;
if(cljs.core.chunked_seq_QMARK_(seq__38909_39041__$1)){
var c__4556__auto___39042 = cljs.core.chunk_first(seq__38909_39041__$1);
var G__39043 = cljs.core.chunk_rest(seq__38909_39041__$1);
var G__39044 = c__4556__auto___39042;
var G__39045 = cljs.core.count(c__4556__auto___39042);
var G__39046 = (0);
seq__38909_39031 = G__39043;
chunk__38910_39032 = G__39044;
count__38911_39033 = G__39045;
i__38912_39034 = G__39046;
continue;
} else {
var ref_39047 = cljs.core.first(seq__38909_39041__$1);
if(cljs.core.contains_QMARK_(old_reactions,ref_39047)){
} else {
cljs.core.add_watch(ref_39047,key,((function (seq__38909_39031,chunk__38910_39032,count__38911_39033,i__38912_39034,ref_39047,seq__38909_39041__$1,temp__5735__auto___39040,comp,old_reactions,vec__38902,dom,next_state,new_reactions,key,_STAR_reactions_STAR__orig_val__38900,_STAR_reactions_STAR__temp_val__38901){
return (function (_,___$1,___$2,___$3){
return rum.core.request_render(comp);
});})(seq__38909_39031,chunk__38910_39032,count__38911_39033,i__38912_39034,ref_39047,seq__38909_39041__$1,temp__5735__auto___39040,comp,old_reactions,vec__38902,dom,next_state,new_reactions,key,_STAR_reactions_STAR__orig_val__38900,_STAR_reactions_STAR__temp_val__38901))
);
}


var G__39048 = cljs.core.next(seq__38909_39041__$1);
var G__39049 = null;
var G__39050 = (0);
var G__39051 = (0);
seq__38909_39031 = G__39048;
chunk__38910_39032 = G__39049;
count__38911_39033 = G__39050;
i__38912_39034 = G__39051;
continue;
}
} else {
}
}
break;
}

return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [dom,cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(next_state,new cljs.core.Keyword("rum.reactive","refs","rum.reactive/refs",-814076325),new_reactions)], null);
}finally {(rum.core._STAR_reactions_STAR_ = _STAR_reactions_STAR__orig_val__38900);
}});
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (state){
var key_39053 = new cljs.core.Keyword("rum.reactive","key","rum.reactive/key",-803425142).cljs$core$IFn$_invoke$arity$1(state);
var seq__38922_39054 = cljs.core.seq(new cljs.core.Keyword("rum.reactive","refs","rum.reactive/refs",-814076325).cljs$core$IFn$_invoke$arity$1(state));
var chunk__38923_39055 = null;
var count__38924_39056 = (0);
var i__38925_39057 = (0);
while(true){
if((i__38925_39057 < count__38924_39056)){
var ref_39058 = chunk__38923_39055.cljs$core$IIndexed$_nth$arity$2(null,i__38925_39057);
cljs.core.remove_watch(ref_39058,key_39053);


var G__39059 = seq__38922_39054;
var G__39060 = chunk__38923_39055;
var G__39061 = count__38924_39056;
var G__39062 = (i__38925_39057 + (1));
seq__38922_39054 = G__39059;
chunk__38923_39055 = G__39060;
count__38924_39056 = G__39061;
i__38925_39057 = G__39062;
continue;
} else {
var temp__5735__auto___39063 = cljs.core.seq(seq__38922_39054);
if(temp__5735__auto___39063){
var seq__38922_39065__$1 = temp__5735__auto___39063;
if(cljs.core.chunked_seq_QMARK_(seq__38922_39065__$1)){
var c__4556__auto___39066 = cljs.core.chunk_first(seq__38922_39065__$1);
var G__39070 = cljs.core.chunk_rest(seq__38922_39065__$1);
var G__39071 = c__4556__auto___39066;
var G__39072 = cljs.core.count(c__4556__auto___39066);
var G__39073 = (0);
seq__38922_39054 = G__39070;
chunk__38923_39055 = G__39071;
count__38924_39056 = G__39072;
i__38925_39057 = G__39073;
continue;
} else {
var ref_39074 = cljs.core.first(seq__38922_39065__$1);
cljs.core.remove_watch(ref_39074,key_39053);


var G__39075 = cljs.core.next(seq__38922_39065__$1);
var G__39076 = null;
var G__39077 = (0);
var G__39078 = (0);
seq__38922_39054 = G__39075;
chunk__38923_39055 = G__39076;
count__38924_39056 = G__39077;
i__38925_39057 = G__39078;
continue;
}
} else {
}
}
break;
}

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$variadic(state,new cljs.core.Keyword("rum.reactive","refs","rum.reactive/refs",-814076325),cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword("rum.reactive","key","rum.reactive/key",-803425142)], 0));
})], null);
/**
 * Works in conjunction with [[reactive]] mixin. Use this function instead of `deref` inside render, and your component will subscribe to changes happening to the derefed atom.
 */
rum.core.react = (function rum$core$react(ref){
if(cljs.core.truth_(rum.core._STAR_reactions_STAR_)){
} else {
throw (new Error(["Assert failed: ","rum.core/react is only supported in conjunction with rum.core/reactive","\n","*reactions*"].join('')));
}

cljs.core._vreset_BANG_(rum.core._STAR_reactions_STAR_,cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core._deref(rum.core._STAR_reactions_STAR_),ref));

return cljs.core.deref(ref);
});
/**
 * Use this to create “chains” and acyclic graphs of dependent atoms.
 * 
 *           [[derived-atom]] will:
 *        
 *           - Take N “source” refs.
 *           - Set up a watch on each of them.
 *           - Create “sink” atom.
 *           - When any of source refs changes:
 *              - re-run function `f`, passing N dereferenced values of source refs.
 *              - `reset!` result of `f` to the sink atom.
 *           - Return sink atom.
 * 
 *           Example:
 * 
 *           ```
 *           (def *a (atom 0))
 *           (def *b (atom 1))
 *           (def *x (derived-atom [*a *b] ::key
 *                     (fn [a b]
 *                       (str a ":" b))))
 *           
 *           (type *x)  ;; => clojure.lang.Atom
 *           (deref *x) ;; => "0:1"
 *           
 *           (swap! *a inc)
 *           (deref *x) ;; => "1:1"
 *           
 *           (reset! *b 7)
 *           (deref *x) ;; => "1:7"
 *           ```
 * 
 *           Arguments:
 *        
 *           - `refs` - sequence of source refs,
 *           - `key`  - unique key to register watcher, same as in `clojure.core/add-watch`,
 *           - `f`    - function that must accept N arguments (same as number of source refs) and return a value to be written to the sink ref. Note: `f` will be called with already dereferenced values,
 *           - `opts` - optional. Map of:
 *             - `:ref` - use this as sink ref. By default creates new atom,
 *             - `:check-equals?` - Defaults to `true`. If equality check should be run on each source update: `(= @sink (f new-vals))`. When result of recalculating `f` equals to the old value, `reset!` won’t be called. Set to `false` if checking for equality can be expensive.
 */
rum.core.derived_atom = rum.derived_atom.derived_atom;
/**
 * Given atom with deep nested value and path inside it, creates an atom-like structure
 * that can be used separately from main atom, but will sync changes both ways:
 *   
 * ```
 * (def db (atom { :users { "Ivan" { :age 30 }}}))
 * 
 * (def ivan (rum/cursor db [:users "Ivan"]))
 * (deref ivan) ;; => { :age 30 }
 * 
 * (swap! ivan update :age inc) ;; => { :age 31 }
 * (deref db) ;; => { :users { "Ivan" { :age 31 }}}
 * 
 * (swap! db update-in [:users "Ivan" :age] inc)
 * ;; => { :users { "Ivan" { :age 32 }}}
 * 
 * (deref ivan) ;; => { :age 32 }
 * ```
 *   
 * Returned value supports `deref`, `swap!`, `reset!`, watches and metadata.
 *   
 * The only supported option is `:meta`
 */
rum.core.cursor_in = (function rum$core$cursor_in(var_args){
var args__4742__auto__ = [];
var len__4736__auto___39079 = arguments.length;
var i__4737__auto___39080 = (0);
while(true){
if((i__4737__auto___39080 < len__4736__auto___39079)){
args__4742__auto__.push((arguments[i__4737__auto___39080]));

var G__39081 = (i__4737__auto___39080 + (1));
i__4737__auto___39080 = G__39081;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return rum.core.cursor_in.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(rum.core.cursor_in.cljs$core$IFn$_invoke$arity$variadic = (function (ref,path,p__38929){
var map__38930 = p__38929;
var map__38930__$1 = (((((!((map__38930 == null))))?(((((map__38930.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38930.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38930):map__38930);
var options = map__38930__$1;
if((ref instanceof rum.cursor.Cursor)){
return (new rum.cursor.Cursor(ref.ref,cljs.core.into.cljs$core$IFn$_invoke$arity$2(ref.path,path),new cljs.core.Keyword(null,"meta","meta",1499536964).cljs$core$IFn$_invoke$arity$1(options)));
} else {
return (new rum.cursor.Cursor(ref,path,new cljs.core.Keyword(null,"meta","meta",1499536964).cljs$core$IFn$_invoke$arity$1(options)));
}
}));

(rum.core.cursor_in.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(rum.core.cursor_in.cljs$lang$applyTo = (function (seq38926){
var G__38927 = cljs.core.first(seq38926);
var seq38926__$1 = cljs.core.next(seq38926);
var G__38928 = cljs.core.first(seq38926__$1);
var seq38926__$2 = cljs.core.next(seq38926__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38927,G__38928,seq38926__$2);
}));

/**
 * Same as [[cursor-in]] but accepts single key instead of path vector.
 */
rum.core.cursor = (function rum$core$cursor(var_args){
var args__4742__auto__ = [];
var len__4736__auto___39082 = arguments.length;
var i__4737__auto___39083 = (0);
while(true){
if((i__4737__auto___39083 < len__4736__auto___39082)){
args__4742__auto__.push((arguments[i__4737__auto___39083]));

var G__39084 = (i__4737__auto___39083 + (1));
i__4737__auto___39083 = G__39084;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return rum.core.cursor.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(rum.core.cursor.cljs$core$IFn$_invoke$arity$variadic = (function (ref,key,options){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(rum.core.cursor_in,ref,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [key], null),options);
}));

(rum.core.cursor.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(rum.core.cursor.cljs$lang$applyTo = (function (seq38932){
var G__38933 = cljs.core.first(seq38932);
var seq38932__$1 = cljs.core.next(seq38932);
var G__38934 = cljs.core.first(seq38932__$1);
var seq38932__$2 = cljs.core.next(seq38932__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38933,G__38934,seq38932__$2);
}));


//# sourceMappingURL=rum.core.js.map
