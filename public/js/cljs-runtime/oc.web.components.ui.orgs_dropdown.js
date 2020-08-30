goog.provide('oc.web.components.ui.orgs_dropdown');
oc.web.components.ui.orgs_dropdown.org_dropdown_item = rum.core.build_defc((function (current_slug,org__$1,is_mobile_QMARK_){
return React.createElement("li",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org__$1),current_slug))?"active":null)], null))}),React.createElement("a",({"href": oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org__$1)), "onClick": (function (e){
oc.web.lib.utils.event_stop(e);

return oc.web.actions.routing.switch_org_dashboard(org__$1);
})}),sablono.interpreter.interpret((oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3(org__$1,false,new cljs.core.Keyword(null,"always","always",-1772028770)) : oc.web.components.ui.org_avatar.org_avatar.call(null,org__$1,false,new cljs.core.Keyword(null,"always","always",-1772028770))))));
}),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$], null),"org-dropdown-item");
oc.web.components.ui.orgs_dropdown.orgs_dropdown = rum.core.build_defcs((function (s){
var orgs = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"orgs","orgs",155776628));
var org_data = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"org-data","org-data",96720321));
var current_org_slug = new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data);
var should_show_dropdown_QMARK_ = (cljs.core.count(orgs) > (1));
var orgs_dropdown_visible = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944));
var is_mobile_QMARK_ = oc.web.lib.responsive.is_tablet_or_mobile_QMARK_();
return React.createElement("div",({"className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["orgs-dropdown",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dropdown","dropdown",1343185805),should_show_dropdown_QMARK_,new cljs.core.Keyword(null,"org-has-logo","org-has-logo",-1139778177),cljs.core.seq(new cljs.core.Keyword(null,"logo-url","logo-url",-1629105032).cljs$core$IFn$_invoke$arity$1(org_data))], null))], null))}),React.createElement("button",({"id": "orgs-dropdown", "onClick": (function (e){
oc.web.lib.utils.event_stop(e);

if(should_show_dropdown_QMARK_){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944)], null),cljs.core.not(orgs_dropdown_visible)], null));
} else {
return null;
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["orgs-dropdown-btn",oc.web.lib.utils.class_set(new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"dropdown-toggle","dropdown-toggle",-831679844),should_show_dropdown_QMARK_,new cljs.core.Keyword(null,"show-dropdown-caret","show-dropdown-caret",239926234),orgs_dropdown_visible], null))], null))}),sablono.interpreter.interpret((function (){var G__45858 = org_data;
var G__45859 = (!(should_show_dropdown_QMARK_));
var G__45860 = new cljs.core.Keyword(null,"always","always",-1772028770);
return (oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.org_avatar.org_avatar.cljs$core$IFn$_invoke$arity$3(G__45858,G__45859,G__45860) : oc.web.components.ui.org_avatar.org_avatar.call(null,G__45858,G__45859,G__45860));
})())),sablono.interpreter.interpret((cljs.core.truth_(orgs_dropdown_visible)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.orgs-dropdown-container","div.orgs-dropdown-container",-614442682),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.triangle","div.triangle",1701038841)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"ul.orgs-dropdown-menu","ul.orgs-dropdown-menu",-1270302780),(function (){var iter__4529__auto__ = (function oc$web$components$ui$orgs_dropdown$iter__45861(s__45862){
return (new cljs.core.LazySeq(null,(function (){
var s__45862__$1 = s__45862;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__45862__$1);
if(temp__5735__auto__){
var s__45862__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__45862__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__45862__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__45864 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__45863 = (0);
while(true){
if((i__45863 < size__4528__auto__)){
var org__$1 = cljs.core._nth(c__4527__auto__,i__45863);
cljs.core.chunk_append(b__45864,rum.core.with_key((oc.web.components.ui.orgs_dropdown.org_dropdown_item.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.orgs_dropdown.org_dropdown_item.cljs$core$IFn$_invoke$arity$3(current_org_slug,org__$1,is_mobile_QMARK_) : oc.web.components.ui.orgs_dropdown.org_dropdown_item.call(null,current_org_slug,org__$1,is_mobile_QMARK_)),["org-dropdown-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org__$1))].join('')));

var G__45865 = (i__45863 + (1));
i__45863 = G__45865;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__45864),oc$web$components$ui$orgs_dropdown$iter__45861(cljs.core.chunk_rest(s__45862__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__45864),null);
}
} else {
var org__$1 = cljs.core.first(s__45862__$2);
return cljs.core.cons(rum.core.with_key((oc.web.components.ui.orgs_dropdown.org_dropdown_item.cljs$core$IFn$_invoke$arity$3 ? oc.web.components.ui.orgs_dropdown.org_dropdown_item.cljs$core$IFn$_invoke$arity$3(current_org_slug,org__$1,is_mobile_QMARK_) : oc.web.components.ui.orgs_dropdown.org_dropdown_item.call(null,current_org_slug,org__$1,is_mobile_QMARK_)),["org-dropdown-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org__$1))].join('')),oc$web$components$ui$orgs_dropdown$iter__45861(cljs.core.rest(s__45862__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(orgs);
})()], null)], null):null)));
}),new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,rum.core.reactive,org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"orgs","orgs",155776628)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"org-data","org-data",96720321)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944)], 0)),oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$1((function (s,e){
if(cljs.core.truth_(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944))))){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"orgs-dropdown-visible","orgs-dropdown-visible",801323944)], null),false], null));
} else {
return null;
}
}))], null),"orgs-dropdown");

//# sourceMappingURL=oc.web.components.ui.orgs_dropdown.js.map
