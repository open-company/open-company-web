goog.provide('oc.web.utils.ui');
oc.web.utils.ui.resize_textarea = (function oc$web$utils$ui$resize_textarea(textarea){
if(cljs.core.truth_(textarea)){
(textarea.style.height = "");

return (textarea.style.height = [cljs.core.str.cljs$core$IFn$_invoke$arity$1(textarea.scrollHeight),"px"].join(''));
} else {
return null;
}
});
oc.web.utils.ui.ui_compose = (function oc$web$utils$ui$ui_compose(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38259 = arguments.length;
var i__4737__auto___38260 = (0);
while(true){
if((i__4737__auto___38260 < len__4736__auto___38259)){
args__4742__auto__.push((arguments[i__4737__auto___38260]));

var G__38261 = (i__4737__auto___38260 + (1));
i__4737__auto___38260 = G__38261;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.utils.ui.ui_compose.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.utils.ui.ui_compose.cljs$core$IFn$_invoke$arity$variadic = (function (p__38255){
var vec__38256 = p__38255;
var show_add_post_tooltip = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38256,(0),null);
oc.web.lib.utils.remove_tooltips();

oc.web.actions.activity.activity_edit.cljs$core$IFn$_invoke$arity$0();

if(cljs.core.truth_(show_add_post_tooltip)){
return oc.web.lib.utils.after((1000),oc.web.actions.nux.dismiss_add_post_tooltip);
} else {
return null;
}
}));

(oc.web.utils.ui.ui_compose.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.utils.ui.ui_compose.cljs$lang$applyTo = (function (seq38254){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq38254));
}));


//# sourceMappingURL=oc.web.utils.ui.js.map
