goog.provide('oc.web.components.ui.dropdown_list');
/**
 * Component to create a dropdown list. Accept a map with these keys:
 * :items - the list of items map to render, see below for the format to use
 * :value - current value, add the class selected to the corresponing value li
 * :on-change - fn callback for changes
 * :on-blur - fn called on click out of the dropdown cb
 * :selected-icon - full url of an icon to show besides the selected item, ignored if empty
 * :unselected-icon - full url of an icon to show besides the unselected item, ignored if empty
 *   Elements should be passed in a vector with this format:
 *   {:value "the value"
 * :label "The label to show, optional"
 * :color "optional"
 * :user-map "optional: to user the user avatars"
 * :disabled "optional: not usable"
 * :tooltip  "optional: tooltip to show on the row"}.
 *   Elements with this format will be transfomed into a divider line:
 *   {:value nil :label :divider-line}.
 */
oc.web.components.ui.dropdown_list.dropdown_list = rum.core.build_defc((function (p__45716){
var map__45717 = p__45716;
var map__45717__$1 = (((((!((map__45717 == null))))?(((((map__45717.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__45717.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__45717):map__45717);
var items = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45717__$1,new cljs.core.Keyword(null,"items","items",1031954938));
var value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45717__$1,new cljs.core.Keyword(null,"value","value",305978217));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45717__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var on_blur = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45717__$1,new cljs.core.Keyword(null,"on-blur","on-blur",814300747));
var selected_icon = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45717__$1,new cljs.core.Keyword(null,"selected-icon","selected-icon",-1343164076));
var unselected_icon = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45717__$1,new cljs.core.Keyword(null,"unselected-icon","unselected-icon",828951628));
var placeholder = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__45717__$1,new cljs.core.Keyword(null,"placeholder","placeholder",-104873083));
var fixed_items = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__45714_SHARP_){
if((!(cljs.core.contains_QMARK_(p1__45714_SHARP_,new cljs.core.Keyword(null,"label","label",1718410804))))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(p1__45714_SHARP_,new cljs.core.Keyword(null,"label","label",1718410804),new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(p1__45714_SHARP_));
} else {
return p1__45714_SHARP_;
}
}),items);
return React.createElement("div",({"ref": "dropdown-list", "className": "dropdown-list-container"}),React.createElement("div",({"className": "triangle"})),React.createElement("div",({"className": "dropdown-list-content"}),React.createElement("ul",({"className": "dropdown-list"}),cljs.core.into_array.cljs$core$IFn$_invoke$arity$1((function (){var iter__4529__auto__ = (function oc$web$components$ui$dropdown_list$iter__45721(s__45722){
return (new cljs.core.LazySeq(null,(function (){
var s__45722__$1 = s__45722;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__45722__$1);
if(temp__5735__auto__){
var s__45722__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__45722__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__45722__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__45724 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__45723 = (0);
while(true){
if((i__45723 < size__4528__auto__)){
var item = cljs.core._nth(c__4527__auto__,i__45723);
cljs.core.chunk_append(b__45724,React.createElement("li",({"key": ["dropdown-list-item-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"divider-line","divider-line",24778982)))?"divider":new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item)))].join(''), "onClick": ((function (i__45723,item,c__4527__auto__,size__4528__auto__,b__45724,s__45722__$2,temp__5735__auto__,fixed_items,map__45717,map__45717__$1,items,value,on_change,on_blur,selected_icon,unselected_icon,placeholder){
return (function (p1__45715_SHARP_){
if(cljs.core.truth_(new cljs.core.Keyword(null,"disabled","disabled",-1529784218).cljs$core$IFn$_invoke$arity$1(item))){
return oc.web.lib.utils.event_stop(p1__45715_SHARP_);
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.fn_QMARK_(on_change);
} else {
return and__4115__auto__;
}
})())){
return (on_change.cljs$core$IFn$_invoke$arity$2 ? on_change.cljs$core$IFn$_invoke$arity$2(item,p1__45715_SHARP_) : on_change.call(null,item,p1__45715_SHARP_));
} else {
return null;
}
}
});})(i__45723,item,c__4527__auto__,size__4528__auto__,b__45724,s__45722__$2,temp__5735__auto__,fixed_items,map__45717,map__45717__$1,items,value,on_change,on_blur,selected_icon,unselected_icon,placeholder))
, "style": sablono.interpreter.attributes(((cljs.core.seq(new cljs.core.Keyword(null,"color","color",1011675173).cljs$core$IFn$_invoke$arity$1(item)))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),new cljs.core.Keyword(null,"color","color",1011675173).cljs$core$IFn$_invoke$arity$1(item)], null):null)), "title": new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058).cljs$core$IFn$_invoke$arity$1(item), "data-toggle": (cljs.core.truth_(new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058).cljs$core$IFn$_invoke$arity$1(item))?"tooltip":null), "data-placement": "top", "data-container": "body", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["dropdown-list-item",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"select","select",1147833503),(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(value,new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"divider-line","divider-line",24778982),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"divider-line","divider-line",24778982)),new cljs.core.Keyword(null,"user-avatar-icons","user-avatar-icons",2009688520),new cljs.core.Keyword(null,"user-map","user-map",-1037585771).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),new cljs.core.Keyword(null,"disabled","disabled",-1529784218).cljs$core$IFn$_invoke$arity$1(item),cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(item)),cljs.core.seq(new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(item))]))], null))}),sablono.interpreter.interpret((cljs.core.truth_(((typeof new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item) === 'string')?(function (){var or__4126__auto__ = selected_icon;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return unselected_icon;
}
})():false))?(cljs.core.truth_((function (){var and__4115__auto__ = selected_icon;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item),value);
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img.dropdown-list-item-icon","img.dropdown-list-item-icon",694239122),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),selected_icon], null)], null):(cljs.core.truth_(unselected_icon)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img.dropdown-list-item-icon","img.dropdown-list-item-icon",694239122),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),unselected_icon], null)], null):null)):null)),sablono.interpreter.interpret((cljs.core.truth_(((typeof new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item) === 'string')?(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"user-map","user-map",-1037585771).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-map","user-map",-1037585771).cljs$core$IFn$_invoke$arity$1(item));
} else {
return and__4115__auto__;
}
})():false))?(function (){var G__45756 = new cljs.core.Keyword(null,"user-map","user-map",-1037585771).cljs$core$IFn$_invoke$arity$1(item);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(G__45756) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__45756));
})():null)),sablono.interpreter.interpret(((typeof new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item) === 'string')?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.dropdown-list-item-label","span.dropdown-list-item-label",-556861262),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item)], null):null))));

var G__45760 = (i__45723 + (1));
i__45723 = G__45760;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__45724),oc$web$components$ui$dropdown_list$iter__45721(cljs.core.chunk_rest(s__45722__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__45724),null);
}
} else {
var item = cljs.core.first(s__45722__$2);
return cljs.core.cons(React.createElement("li",({"key": ["dropdown-list-item-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"divider-line","divider-line",24778982)))?"divider":new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item)))].join(''), "onClick": ((function (item,s__45722__$2,temp__5735__auto__,fixed_items,map__45717,map__45717__$1,items,value,on_change,on_blur,selected_icon,unselected_icon,placeholder){
return (function (p1__45715_SHARP_){
if(cljs.core.truth_(new cljs.core.Keyword(null,"disabled","disabled",-1529784218).cljs$core$IFn$_invoke$arity$1(item))){
return oc.web.lib.utils.event_stop(p1__45715_SHARP_);
} else {
if(cljs.core.truth_((function (){var and__4115__auto__ = new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.fn_QMARK_(on_change);
} else {
return and__4115__auto__;
}
})())){
return (on_change.cljs$core$IFn$_invoke$arity$2 ? on_change.cljs$core$IFn$_invoke$arity$2(item,p1__45715_SHARP_) : on_change.call(null,item,p1__45715_SHARP_));
} else {
return null;
}
}
});})(item,s__45722__$2,temp__5735__auto__,fixed_items,map__45717,map__45717__$1,items,value,on_change,on_blur,selected_icon,unselected_icon,placeholder))
, "style": sablono.interpreter.attributes(((cljs.core.seq(new cljs.core.Keyword(null,"color","color",1011675173).cljs$core$IFn$_invoke$arity$1(item)))?new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"color","color",1011675173),new cljs.core.Keyword(null,"color","color",1011675173).cljs$core$IFn$_invoke$arity$1(item)], null):null)), "title": new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058).cljs$core$IFn$_invoke$arity$1(item), "data-toggle": (cljs.core.truth_(new cljs.core.Keyword(null,"tooltip","tooltip",-1809677058).cljs$core$IFn$_invoke$arity$1(item))?"tooltip":null), "data-placement": "top", "data-container": "body", "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["dropdown-list-item",oc.web.lib.utils.class_set(cljs.core.PersistentArrayMap.createAsIfByAssoc([new cljs.core.Keyword(null,"select","select",1147833503),(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(value,new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item));
} else {
return and__4115__auto__;
}
})(),new cljs.core.Keyword(null,"divider-line","divider-line",24778982),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"divider-line","divider-line",24778982)),new cljs.core.Keyword(null,"user-avatar-icons","user-avatar-icons",2009688520),new cljs.core.Keyword(null,"user-map","user-map",-1037585771).cljs$core$IFn$_invoke$arity$1(item),new cljs.core.Keyword(null,"disabled","disabled",-1529784218),new cljs.core.Keyword(null,"disabled","disabled",-1529784218).cljs$core$IFn$_invoke$arity$1(item),cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(item)),cljs.core.seq(new cljs.core.Keyword(null,"class","class",-2030961996).cljs$core$IFn$_invoke$arity$1(item))]))], null))}),sablono.interpreter.interpret((cljs.core.truth_(((typeof new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item) === 'string')?(function (){var or__4126__auto__ = selected_icon;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return unselected_icon;
}
})():false))?(cljs.core.truth_((function (){var and__4115__auto__ = selected_icon;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"value","value",305978217).cljs$core$IFn$_invoke$arity$1(item),value);
} else {
return and__4115__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img.dropdown-list-item-icon","img.dropdown-list-item-icon",694239122),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),selected_icon], null)], null):(cljs.core.truth_(unselected_icon)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"img.dropdown-list-item-icon","img.dropdown-list-item-icon",694239122),new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"src","src",-1651076051),unselected_icon], null)], null):null)):null)),sablono.interpreter.interpret((cljs.core.truth_(((typeof new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item) === 'string')?(function (){var and__4115__auto__ = new cljs.core.Keyword(null,"user-map","user-map",-1037585771).cljs$core$IFn$_invoke$arity$1(item);
if(cljs.core.truth_(and__4115__auto__)){
return new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"user-map","user-map",-1037585771).cljs$core$IFn$_invoke$arity$1(item));
} else {
return and__4115__auto__;
}
})():false))?(function (){var G__45759 = new cljs.core.Keyword(null,"user-map","user-map",-1037585771).cljs$core$IFn$_invoke$arity$1(item);
return (oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.user_avatar.user_avatar_image.cljs$core$IFn$_invoke$arity$1(G__45759) : oc.web.components.ui.user_avatar.user_avatar_image.call(null,G__45759));
})():null)),sablono.interpreter.interpret(((typeof new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item) === 'string')?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.dropdown-list-item-label","span.dropdown-list-item-label",-556861262),new cljs.core.Keyword(null,"label","label",1718410804).cljs$core$IFn$_invoke$arity$1(item)], null):null))),oc$web$components$ui$dropdown_list$iter__45721(cljs.core.rest(s__45722__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(fixed_items);
})())),sablono.interpreter.interpret((cljs.core.truth_(placeholder)?placeholder:null))));
}),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"dropdown-list","dropdown-list",-99599474),(function (s,e){
var on_blur = new cljs.core.Keyword(null,"on-blur","on-blur",814300747).cljs$core$IFn$_invoke$arity$1(cljs.core.first(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s)));
if(cljs.core.fn_QMARK_(on_blur)){
return (on_blur.cljs$core$IFn$_invoke$arity$0 ? on_blur.cljs$core$IFn$_invoke$arity$0() : on_blur.call(null));
} else {
return null;
}
})),oc.web.mixins.ui.refresh_tooltips_mixin], null),"dropdown-list");

//# sourceMappingURL=oc.web.components.ui.dropdown_list.js.map
