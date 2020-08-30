goog.provide('oc.web.components.ui.alert_modal');
oc.web.components.ui.alert_modal.show_alert = (function oc$web$components$ui$alert_modal$show_alert(modal_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214)], null),modal_data], null));
});
oc.web.components.ui.alert_modal.hide_alert = (function oc$web$components$ui$alert_modal$hide_alert(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214),new cljs.core.Keyword(null,"dismiss","dismiss",412569545)], null),true], null));
});
oc.web.components.ui.alert_modal.dismiss_modal = (function oc$web$components$ui$alert_modal$dismiss_modal(){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214)], null),null], null));
});
oc.web.components.ui.alert_modal.close_clicked = (function oc$web$components$ui$alert_modal$close_clicked(s){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.alert-modal","dismiss","oc.web.components.ui.alert-modal/dismiss",808135695).cljs$core$IFn$_invoke$arity$1(s),true);

return oc.web.lib.utils.after((180),oc.web.components.ui.alert_modal.dismiss_modal);
});
oc.web.components.ui.alert_modal.link_button_clicked = (function oc$web$components$ui$alert_modal$link_button_clicked(alert_modal,e){
oc.web.lib.utils.event_stop(e);

if(cljs.core.fn_QMARK_(new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301).cljs$core$IFn$_invoke$arity$1(alert_modal))){
var fexpr__45695 = new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301).cljs$core$IFn$_invoke$arity$1(alert_modal);
return (fexpr__45695.cljs$core$IFn$_invoke$arity$0 ? fexpr__45695.cljs$core$IFn$_invoke$arity$0() : fexpr__45695.call(null));
} else {
return null;
}
});
oc.web.components.ui.alert_modal.solid_button_clicked = (function oc$web$components$ui$alert_modal$solid_button_clicked(alert_modal,e){
oc.web.lib.utils.event_stop(e);

if(cljs.core.fn_QMARK_(new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787).cljs$core$IFn$_invoke$arity$1(alert_modal))){
var fexpr__45696 = new cljs.core.Keyword(null,"solid-button-cb","solid-button-cb",-763410787).cljs$core$IFn$_invoke$arity$1(alert_modal);
return (fexpr__45696.cljs$core$IFn$_invoke$arity$0 ? fexpr__45696.cljs$core$IFn$_invoke$arity$0() : fexpr__45696.call(null));
} else {
return null;
}
});
oc.web.components.ui.alert_modal.bottom_button_clicked = (function oc$web$components$ui$alert_modal$bottom_button_clicked(alert_modal,e){
oc.web.lib.utils.event_stop(e);

if(cljs.core.fn_QMARK_(new cljs.core.Keyword(null,"bottom-button-cb","bottom-button-cb",1482855598).cljs$core$IFn$_invoke$arity$1(alert_modal))){
var fexpr__45697 = new cljs.core.Keyword(null,"bottom-button-cb","bottom-button-cb",1482855598).cljs$core$IFn$_invoke$arity$1(alert_modal);
return (fexpr__45697.cljs$core$IFn$_invoke$arity$0 ? fexpr__45697.cljs$core$IFn$_invoke$arity$0() : fexpr__45697.call(null));
} else {
return null;
}
});
/**
 * Customizable alert modal. It gets the following property from the :alert-modal derivative:
 * :icon The src to use for an image, it's encapsulated in utils/cdn.
 * :emoji-icon The emoji char to use instead of an image at the top.
 * :title The title of the view.
 * :message A description message to show in the view.
 * :link-button-style The color of the font for the link button
 * :link-button-title The title for the first button, it's black link styled.
 * :link-button-cb The function to execute when the first button is clicked.
 * :solid-button-style The style of the button: default green, :red.
 * :solid-button-title The title for the second button, it's green solid styled.
 * :solid-button-cb The function to execute when the second button is clicked.
 * :bottom-button-style The style for the button at the bottom of the view.
 * :bottom-button-title The title for the bottom button, it's green solid styled.
 * :bottom-button-cb The function to execute when the bottom button is clicked.
 */
oc.web.components.ui.alert_modal.alert_modal = rum.core.build_defcs((function (s){
var alert_modal = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214));
var action = ((cljs.core.empty_QMARK_(new cljs.core.Keyword(null,"action","action",-811238024).cljs$core$IFn$_invoke$arity$1(alert_modal)))?"no-action":new cljs.core.Keyword(null,"action","action",-811238024).cljs$core$IFn$_invoke$arity$1(alert_modal));
var has_buttons = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976).cljs$core$IFn$_invoke$arity$1(alert_modal);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567).cljs$core$IFn$_invoke$arity$1(alert_modal);
}
})();
return React.createElement("div",({"onClick": (function (){
if(cljs.core.truth_(has_buttons)){
return null;
} else {
return oc.web.components.ui.alert_modal.hide_alert();
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["alert-modal-container",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"will-appear","will-appear",579342096),(function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.alert-modal","dismiss","oc.web.components.ui.alert-modal/dismiss",808135695).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.not(cljs.core.deref(new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667).cljs$core$IFn$_invoke$arity$1(s)));
}
})(),new cljs.core.Keyword(null,"appear","appear",440543877),((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.alert-modal","dismiss","oc.web.components.ui.alert-modal/dismiss",808135695).cljs$core$IFn$_invoke$arity$1(s))))?cljs.core.deref(new cljs.core.Keyword(null,"first-render-done","first-render-done",1105112667).cljs$core$IFn$_invoke$arity$1(s)):false),action,true]))], null))}),React.createElement("div",({"className": "modal-wrapper"}),React.createElement("button",({"onClick": (function (p1__45700_SHARP_){
if(cljs.core.fn_QMARK_(new cljs.core.Keyword(null,"link-button-cb","link-button-cb",559710301).cljs$core$IFn$_invoke$arity$1(alert_modal))){
return oc.web.components.ui.alert_modal.link_button_clicked(alert_modal,p1__45700_SHARP_);
} else {
return oc.web.components.ui.alert_modal.close_clicked(s);
}
}), "className": "mlb-reset modal-close-bt"})),React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["alert-modal",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"has-buttons","has-buttons",1035324376),has_buttons,new cljs.core.Keyword(null,"has-bottom-button","has-bottom-button",957695377),cljs.core.seq(new cljs.core.Keyword(null,"bottom-button-title","bottom-button-title",1550148886).cljs$core$IFn$_invoke$arity$1(alert_modal))], null))], null))}),(cljs.core.truth_(new cljs.core.Keyword(null,"emoji-icon","emoji-icon",99205896).cljs$core$IFn$_invoke$arity$1(alert_modal))?(function (){var attrs45708 = new cljs.core.Keyword(null,"emoji-icon","emoji-icon",99205896).cljs$core$IFn$_invoke$arity$1(alert_modal);
return cljs.core.apply.cljs$core$IFn$_invoke$arity$4(React.createElement,"div",((cljs.core.map_QMARK_(attrs45708))?sablono.interpreter.attributes(sablono.normalize.merge_with_class.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, ["alert-modal-emoji-icon"], null)], null),attrs45708], 0))):({"className": "alert-modal-emoji-icon"})),((cljs.core.map_QMARK_(attrs45708))?null:new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [sablono.interpreter.interpret(attrs45708)], null)));
})():sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"icon","icon",1679606541).cljs$core$IFn$_invoke$arity$1(alert_modal))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img.alert-modal-icon","img.alert-modal-icon",1319272063),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),oc.web.lib.utils.cdn(new cljs.core.Keyword(null,"icon","icon",1679606541).cljs$core$IFn$_invoke$arity$1(alert_modal))], null)], null):null))),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(alert_modal))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.alert-modal-title","div.alert-modal-title",-1471860500),new cljs.core.Keyword(null,"title","title",636505583).cljs$core$IFn$_invoke$arity$1(alert_modal)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(alert_modal))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.alert-modal-message","div.alert-modal-message",1038625906),new cljs.core.Keyword(null,"message","message",-406056002).cljs$core$IFn$_invoke$arity$1(alert_modal)], null):null)),sablono.interpreter.interpret((cljs.core.truth_(has_buttons)?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.alert-modal-buttons.group","div.alert-modal-buttons.group",-2022508826),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"class","class",-2030961996),oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"single-button","single-button",-1697146188),((cljs.core.not(new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976).cljs$core$IFn$_invoke$arity$1(alert_modal))) || (cljs.core.not(new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567).cljs$core$IFn$_invoke$arity$1(alert_modal))))], null))], null),(cljs.core.truth_(new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976).cljs$core$IFn$_invoke$arity$1(alert_modal))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.mlb-link-black","button.mlb-reset.mlb-link-black",-1966092583),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45701_SHARP_){
return oc.web.components.ui.alert_modal.link_button_clicked(alert_modal,p1__45701_SHARP_);
}),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"link-button-style","link-button-style",1552381990).cljs$core$IFn$_invoke$arity$1(alert_modal))?cljs.core.name(new cljs.core.Keyword(null,"link-button-style","link-button-style",1552381990).cljs$core$IFn$_invoke$arity$1(alert_modal)):null)], null),new cljs.core.Keyword(null,"link-button-title","link-button-title",-812305976).cljs$core$IFn$_invoke$arity$1(alert_modal)], null):null),(cljs.core.truth_(new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567).cljs$core$IFn$_invoke$arity$1(alert_modal))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.mlb-default","button.mlb-reset.mlb-default",-943829328),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45702_SHARP_){
return oc.web.components.ui.alert_modal.solid_button_clicked(alert_modal,p1__45702_SHARP_);
}),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244).cljs$core$IFn$_invoke$arity$1(alert_modal))?cljs.core.name(new cljs.core.Keyword(null,"solid-button-style","solid-button-style",-723792244).cljs$core$IFn$_invoke$arity$1(alert_modal)):null)], null),new cljs.core.Keyword(null,"solid-button-title","solid-button-title",-970842567).cljs$core$IFn$_invoke$arity$1(alert_modal)], null):null)], null):null)),sablono.interpreter.interpret(((cljs.core.seq(new cljs.core.Keyword(null,"bottom-button-title","bottom-button-title",1550148886).cljs$core$IFn$_invoke$arity$1(alert_modal)))?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"button.mlb-reset.bottom-button","button.mlb-reset.bottom-button",-2079303543),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"on-click","on-click",1632826543),(function (p1__45703_SHARP_){
return oc.web.components.ui.alert_modal.bottom_button_clicked(alert_modal,p1__45703_SHARP_);
}),new cljs.core.Keyword(null,"class","class",-2030961996),(cljs.core.truth_(new cljs.core.Keyword(null,"bottom-button-style","bottom-button-style",1586529892).cljs$core$IFn$_invoke$arity$1(alert_modal))?cljs.core.name(new cljs.core.Keyword(null,"bottom-button-style","bottom-button-style",1586529892).cljs$core$IFn$_invoke$arity$1(alert_modal)):null)], null),new cljs.core.Keyword(null,"bottom-button-title","bottom-button-title",1550148886).cljs$core$IFn$_invoke$arity$1(alert_modal)], null):null)))));
}),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214)], 0)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.alert-modal","dismiss","oc.web.components.ui.alert-modal/dismiss",808135695)),oc.web.mixins.ui.no_scroll_mixin,oc.web.mixins.ui.first_render_mixin,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (s){
var alert_modal_45757 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"alert-modal","alert-modal",-1208187214)));
if(cljs.core.truth_(((cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.alert-modal","dismiss","oc.web.components.ui.alert-modal/dismiss",808135695).cljs$core$IFn$_invoke$arity$1(s))))?new cljs.core.Keyword(null,"dismiss","dismiss",412569545).cljs$core$IFn$_invoke$arity$1(alert_modal_45757):false))){
oc.web.components.ui.alert_modal.close_clicked(s);
} else {
}

return s;
})], null)], null),"alert-modal");

//# sourceMappingURL=oc.web.components.ui.alert_modal.js.map
