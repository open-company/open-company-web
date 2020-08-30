goog.provide('oc.web.lib.react_utils');
oc.web.lib.react_utils.build = (function oc$web$lib$react_utils$build(var_args){
var args__4742__auto__ = [];
var len__4736__auto___51577 = arguments.length;
var i__4737__auto___51578 = (0);
while(true){
if((i__4737__auto___51578 < len__4736__auto___51577)){
args__4742__auto__.push((arguments[i__4737__auto___51578]));

var G__51579 = (i__4737__auto___51578 + (1));
i__4737__auto___51578 = G__51579;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((2) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((2)),(0),null)):null);
return oc.web.lib.react_utils.build.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),argseq__4743__auto__);
});

(oc.web.lib.react_utils.build.cljs$core$IFn$_invoke$arity$variadic = (function (component,props,children){
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(window.React.createElement,component,cljs.core.clj__GT_js(props),children);
}));

(oc.web.lib.react_utils.build.cljs$lang$maxFixedArity = (2));

/** @this {Function} */
(oc.web.lib.react_utils.build.cljs$lang$applyTo = (function (seq51574){
var G__51575 = cljs.core.first(seq51574);
var seq51574__$1 = cljs.core.next(seq51574);
var G__51576 = cljs.core.first(seq51574__$1);
var seq51574__$2 = cljs.core.next(seq51574__$1);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__51575,G__51576,seq51574__$2);
}));


//# sourceMappingURL=oc.web.lib.react_utils.js.map
