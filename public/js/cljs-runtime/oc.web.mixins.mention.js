goog.provide('oc.web.mixins.mention');
oc.web.mixins.mention.remove_events = (function oc$web$mixins$mention$remove_events(s,events_list){
var seq__39068_39372 = cljs.core.seq(cljs.core.deref(events_list));
var chunk__39069_39373 = null;
var count__39070_39374 = (0);
var i__39071_39375 = (0);
while(true){
if((i__39071_39375 < count__39070_39374)){
var hover_ev_39378 = chunk__39069_39373.cljs$core$IIndexed$_nth$arity$2(null,i__39071_39375);
goog.events.unlistenByKey(hover_ev_39378);


var G__39379 = seq__39068_39372;
var G__39380 = chunk__39069_39373;
var G__39381 = count__39070_39374;
var G__39382 = (i__39071_39375 + (1));
seq__39068_39372 = G__39379;
chunk__39069_39373 = G__39380;
count__39070_39374 = G__39381;
i__39071_39375 = G__39382;
continue;
} else {
var temp__5735__auto___39383 = cljs.core.seq(seq__39068_39372);
if(temp__5735__auto___39383){
var seq__39068_39384__$1 = temp__5735__auto___39383;
if(cljs.core.chunked_seq_QMARK_(seq__39068_39384__$1)){
var c__4556__auto___39385 = cljs.core.chunk_first(seq__39068_39384__$1);
var G__39386 = cljs.core.chunk_rest(seq__39068_39384__$1);
var G__39387 = c__4556__auto___39385;
var G__39388 = cljs.core.count(c__4556__auto___39385);
var G__39389 = (0);
seq__39068_39372 = G__39386;
chunk__39069_39373 = G__39387;
count__39070_39374 = G__39388;
i__39071_39375 = G__39389;
continue;
} else {
var hover_ev_39390 = cljs.core.first(seq__39068_39384__$1);
goog.events.unlistenByKey(hover_ev_39390);


var G__39391 = cljs.core.next(seq__39068_39384__$1);
var G__39392 = null;
var G__39393 = (0);
var G__39394 = (0);
seq__39068_39372 = G__39391;
chunk__39069_39373 = G__39392;
count__39070_39374 = G__39393;
i__39071_39375 = G__39394;
continue;
}
} else {
}
}
break;
}

return cljs.core.reset_BANG_(events_list,cljs.core.PersistentVector.EMPTY);
});
oc.web.mixins.mention.add_events = (function oc$web$mixins$mention$add_events(s,events_list,click_QMARK_){
var dom_node = rum.core.dom_node(s);
var searching_node = $(dom_node).find(".oc-mentions.oc-mentions-hover");
var all_mentions = searching_node.find(":not(.no-mentions-popup).oc-mention[data-found]");
return all_mentions.each((function (_){
var this$ = this;
var enter_ev_39395 = goog.events.listen(this$,goog.events.EventType.MOUSEENTER,(function (e){
var temp__5735__auto__ = new cljs.core.Keyword("oc.web.mixins.mention","mount-el","oc.web.mixins.mention/mount-el",-338481914).cljs$core$IFn$_invoke$arity$1(s);
if(cljs.core.truth_(temp__5735__auto__)){
var mount_el = temp__5735__auto__;
var temp__33765__auto___39398 = (cljs.core.truth_(click_QMARK_)?cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"users-info-hover","users-info-hover",-941434570))):null);
if(cljs.core.truth_(temp__33765__auto___39398)){
var active_users_39399 = temp__33765__auto___39398;
var temp__33765__auto___39400__$1 = (cljs.core.truth_(click_QMARK_)?cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"current-user-data","current-user-data",-1647873915))):null);
if(cljs.core.truth_(temp__33765__auto___39400__$1)){
var current_user_data_39401 = temp__33765__auto___39400__$1;
var temp__33765__auto___39402__$2 = (function (){var target_obj_39072 = this$;
var _STAR_runtime_state_STAR__orig_val__39076 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39077 = oops.state.prepare_state(target_obj_39072,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39077);

try{var next_obj_39073 = ((oops.core.validate_object_access_dynamically(target_obj_39072,(0),"dataset",true,true,false))?(target_obj_39072["dataset"]):null);
var next_obj_39074 = ((oops.core.validate_object_access_dynamically(next_obj_39073,(1),"userId",true,true,false))?(next_obj_39073["userId"]):null);
if((!((next_obj_39074 == null)))){
return next_obj_39074;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39076);
}})();
if(cljs.core.truth_(temp__33765__auto___39402__$2)){
var user_id_39403 = temp__33765__auto___39402__$2;
var temp__33765__auto___39404__$3 = cljs.core.get.cljs$core$IFn$_invoke$arity$2(active_users_39399,user_id_39403);
if(cljs.core.truth_(temp__33765__auto___39404__$3)){
var user_data_39405 = temp__33765__auto___39404__$3;
rum.core.mount((function (){var G__39082 = new cljs.core.PersistentArrayMap(null, 5, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),user_data_39405,new cljs.core.Keyword(null,"portal-el","portal-el",1623429907),this$,new cljs.core.Keyword(null,"my-profile","my-profile",-1409530387),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data_39401),user_id_39403),new cljs.core.Keyword(null,"following","following",-2049193617),oc.web.lib.utils.in_QMARK_(cljs.core.mapv.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291),cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"follow-publishers-list","follow-publishers-list",-374150342)))),user_id_39403),new cljs.core.Keyword(null,"followers-count","followers-count",448882417),cljs.core.get.cljs$core$IFn$_invoke$arity$2(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"followers-publishers-count","followers-publishers-count",-692976579))),user_id_39403)], null);
return (oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1(G__39082) : oc.web.components.ui.info_hover_views.user_info_otf.call(null,G__39082));
})(),mount_el);
} else {
var user_data_39406 = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),(function (){var target_obj_39083 = this$;
var _STAR_runtime_state_STAR__orig_val__39086 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39087 = oops.state.prepare_state(target_obj_39083,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39087);

try{var next_obj_39084 = ((oops.core.validate_object_access_dynamically(target_obj_39083,(0),"dataset",true,true,false))?(target_obj_39083["dataset"]):null);
var next_obj_39085 = ((oops.core.validate_object_access_dynamically(next_obj_39084,(1),"firstName",true,true,false))?(next_obj_39084["firstName"]):null);
if((!((next_obj_39085 == null)))){
return next_obj_39085;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39086);
}})(),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),(function (){var target_obj_39088 = this$;
var _STAR_runtime_state_STAR__orig_val__39091 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39092 = oops.state.prepare_state(target_obj_39088,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39092);

try{var next_obj_39089 = ((oops.core.validate_object_access_dynamically(target_obj_39088,(0),"dataset",true,true,false))?(target_obj_39088["dataset"]):null);
var next_obj_39090 = ((oops.core.validate_object_access_dynamically(next_obj_39089,(1),"lastName",true,true,false))?(next_obj_39089["lastName"]):null);
if((!((next_obj_39090 == null)))){
return next_obj_39090;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39091);
}})(),new cljs.core.Keyword(null,"name","name",1843675177),(function (){var target_obj_39093 = this$;
var _STAR_runtime_state_STAR__orig_val__39096 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39097 = oops.state.prepare_state(target_obj_39093,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39097);

try{var next_obj_39094 = ((oops.core.validate_object_access_dynamically(target_obj_39093,(0),"dataset",true,true,false))?(target_obj_39093["dataset"]):null);
var next_obj_39095 = ((oops.core.validate_object_access_dynamically(next_obj_39094,(1),"name",true,true,false))?(next_obj_39094["name"]):null);
if((!((next_obj_39095 == null)))){
return next_obj_39095;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39096);
}})(),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),(function (){var target_obj_39098 = this$;
var _STAR_runtime_state_STAR__orig_val__39101 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39102 = oops.state.prepare_state(target_obj_39098,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39102);

try{var next_obj_39099 = ((oops.core.validate_object_access_dynamically(target_obj_39098,(0),"dataset",true,true,false))?(target_obj_39098["dataset"]):null);
var next_obj_39100 = ((oops.core.validate_object_access_dynamically(next_obj_39099,(1),"avatarUrl",true,true,false))?(next_obj_39099["avatarUrl"]):null);
if((!((next_obj_39100 == null)))){
return next_obj_39100;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39101);
}})(),new cljs.core.Keyword(null,"title","title",636505583),(function (){var target_obj_39103 = this$;
var _STAR_runtime_state_STAR__orig_val__39118 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39119 = oops.state.prepare_state(target_obj_39103,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39119);

try{var next_obj_39104 = ((oops.core.validate_object_access_dynamically(target_obj_39103,(0),"dataset",true,true,false))?(target_obj_39103["dataset"]):null);
var next_obj_39105 = ((oops.core.validate_object_access_dynamically(next_obj_39104,(1),"title",true,true,false))?(next_obj_39104["title"]):null);
if((!((next_obj_39105 == null)))){
return next_obj_39105;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39118);
}})(),new cljs.core.Keyword(null,"slack-username","slack-username",-757727350),(function (){var target_obj_39121 = this$;
var _STAR_runtime_state_STAR__orig_val__39127 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39128 = oops.state.prepare_state(target_obj_39121,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39128);

try{var next_obj_39123 = ((oops.core.validate_object_access_dynamically(target_obj_39121,(0),"dataset",true,true,false))?(target_obj_39121["dataset"]):null);
var next_obj_39124 = ((oops.core.validate_object_access_dynamically(next_obj_39123,(1),"slackUsername",true,true,false))?(next_obj_39123["slackUsername"]):null);
if((!((next_obj_39124 == null)))){
return next_obj_39124;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39127);
}})(),new cljs.core.Keyword(null,"email","email",1415816706),(function (){var target_obj_39130 = this$;
var _STAR_runtime_state_STAR__orig_val__39135 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39136 = oops.state.prepare_state(target_obj_39130,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39136);

try{var next_obj_39133 = ((oops.core.validate_object_access_dynamically(target_obj_39130,(0),"dataset",true,true,false))?(target_obj_39130["dataset"]):null);
var next_obj_39134 = ((oops.core.validate_object_access_dynamically(next_obj_39133,(1),"email",true,true,false))?(next_obj_39133["email"]):null);
if((!((next_obj_39134 == null)))){
return next_obj_39134;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39135);
}})(),new cljs.core.Keyword(null,"user-id","user-id",-206822291),(function (){var target_obj_39137 = this$;
var _STAR_runtime_state_STAR__orig_val__39140 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39141 = oops.state.prepare_state(target_obj_39137,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39141);

try{var next_obj_39138 = ((oops.core.validate_object_access_dynamically(target_obj_39137,(0),"dataset",true,true,false))?(target_obj_39137["dataset"]):null);
var next_obj_39139 = ((oops.core.validate_object_access_dynamically(next_obj_39138,(1),"userId",true,true,false))?(next_obj_39138["userId"]):null);
if((!((next_obj_39139 == null)))){
return next_obj_39139;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39140);
}})()], null);
rum.core.mount((function (){var G__39142 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),user_data_39406,new cljs.core.Keyword(null,"inline?","inline?",-1674483791),true,new cljs.core.Keyword(null,"portal-el","portal-el",1623429907),this$,new cljs.core.Keyword(null,"hide-buttons","hide-buttons",39532420),true], null);
return (oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1(G__39142) : oc.web.components.ui.info_hover_views.user_info_otf.call(null,G__39142));
})(),mount_el);
}
} else {
var user_data_39407 = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),(function (){var target_obj_39143 = this$;
var _STAR_runtime_state_STAR__orig_val__39146 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39147 = oops.state.prepare_state(target_obj_39143,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39147);

try{var next_obj_39144 = ((oops.core.validate_object_access_dynamically(target_obj_39143,(0),"dataset",true,true,false))?(target_obj_39143["dataset"]):null);
var next_obj_39145 = ((oops.core.validate_object_access_dynamically(next_obj_39144,(1),"firstName",true,true,false))?(next_obj_39144["firstName"]):null);
if((!((next_obj_39145 == null)))){
return next_obj_39145;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39146);
}})(),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),(function (){var target_obj_39148 = this$;
var _STAR_runtime_state_STAR__orig_val__39151 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39152 = oops.state.prepare_state(target_obj_39148,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39152);

try{var next_obj_39149 = ((oops.core.validate_object_access_dynamically(target_obj_39148,(0),"dataset",true,true,false))?(target_obj_39148["dataset"]):null);
var next_obj_39150 = ((oops.core.validate_object_access_dynamically(next_obj_39149,(1),"lastName",true,true,false))?(next_obj_39149["lastName"]):null);
if((!((next_obj_39150 == null)))){
return next_obj_39150;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39151);
}})(),new cljs.core.Keyword(null,"name","name",1843675177),(function (){var target_obj_39153 = this$;
var _STAR_runtime_state_STAR__orig_val__39156 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39157 = oops.state.prepare_state(target_obj_39153,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39157);

try{var next_obj_39154 = ((oops.core.validate_object_access_dynamically(target_obj_39153,(0),"dataset",true,true,false))?(target_obj_39153["dataset"]):null);
var next_obj_39155 = ((oops.core.validate_object_access_dynamically(next_obj_39154,(1),"name",true,true,false))?(next_obj_39154["name"]):null);
if((!((next_obj_39155 == null)))){
return next_obj_39155;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39156);
}})(),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),(function (){var target_obj_39176 = this$;
var _STAR_runtime_state_STAR__orig_val__39179 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39180 = oops.state.prepare_state(target_obj_39176,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39180);

try{var next_obj_39177 = ((oops.core.validate_object_access_dynamically(target_obj_39176,(0),"dataset",true,true,false))?(target_obj_39176["dataset"]):null);
var next_obj_39178 = ((oops.core.validate_object_access_dynamically(next_obj_39177,(1),"avatarUrl",true,true,false))?(next_obj_39177["avatarUrl"]):null);
if((!((next_obj_39178 == null)))){
return next_obj_39178;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39179);
}})(),new cljs.core.Keyword(null,"title","title",636505583),(function (){var target_obj_39183 = this$;
var _STAR_runtime_state_STAR__orig_val__39188 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39189 = oops.state.prepare_state(target_obj_39183,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39189);

try{var next_obj_39186 = ((oops.core.validate_object_access_dynamically(target_obj_39183,(0),"dataset",true,true,false))?(target_obj_39183["dataset"]):null);
var next_obj_39187 = ((oops.core.validate_object_access_dynamically(next_obj_39186,(1),"title",true,true,false))?(next_obj_39186["title"]):null);
if((!((next_obj_39187 == null)))){
return next_obj_39187;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39188);
}})(),new cljs.core.Keyword(null,"slack-username","slack-username",-757727350),(function (){var target_obj_39190 = this$;
var _STAR_runtime_state_STAR__orig_val__39193 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39194 = oops.state.prepare_state(target_obj_39190,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39194);

try{var next_obj_39191 = ((oops.core.validate_object_access_dynamically(target_obj_39190,(0),"dataset",true,true,false))?(target_obj_39190["dataset"]):null);
var next_obj_39192 = ((oops.core.validate_object_access_dynamically(next_obj_39191,(1),"slackUsername",true,true,false))?(next_obj_39191["slackUsername"]):null);
if((!((next_obj_39192 == null)))){
return next_obj_39192;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39193);
}})(),new cljs.core.Keyword(null,"email","email",1415816706),(function (){var target_obj_39199 = this$;
var _STAR_runtime_state_STAR__orig_val__39202 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39203 = oops.state.prepare_state(target_obj_39199,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39203);

try{var next_obj_39200 = ((oops.core.validate_object_access_dynamically(target_obj_39199,(0),"dataset",true,true,false))?(target_obj_39199["dataset"]):null);
var next_obj_39201 = ((oops.core.validate_object_access_dynamically(next_obj_39200,(1),"email",true,true,false))?(next_obj_39200["email"]):null);
if((!((next_obj_39201 == null)))){
return next_obj_39201;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39202);
}})(),new cljs.core.Keyword(null,"user-id","user-id",-206822291),(function (){var target_obj_39204 = this$;
var _STAR_runtime_state_STAR__orig_val__39207 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39208 = oops.state.prepare_state(target_obj_39204,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39208);

try{var next_obj_39205 = ((oops.core.validate_object_access_dynamically(target_obj_39204,(0),"dataset",true,true,false))?(target_obj_39204["dataset"]):null);
var next_obj_39206 = ((oops.core.validate_object_access_dynamically(next_obj_39205,(1),"userId",true,true,false))?(next_obj_39205["userId"]):null);
if((!((next_obj_39206 == null)))){
return next_obj_39206;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39207);
}})()], null);
rum.core.mount((function (){var G__39213 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),user_data_39407,new cljs.core.Keyword(null,"inline?","inline?",-1674483791),true,new cljs.core.Keyword(null,"portal-el","portal-el",1623429907),this$,new cljs.core.Keyword(null,"hide-buttons","hide-buttons",39532420),true], null);
return (oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1(G__39213) : oc.web.components.ui.info_hover_views.user_info_otf.call(null,G__39213));
})(),mount_el);
}
} else {
var user_data_39411 = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),(function (){var target_obj_39214 = this$;
var _STAR_runtime_state_STAR__orig_val__39219 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39220 = oops.state.prepare_state(target_obj_39214,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39220);

try{var next_obj_39215 = ((oops.core.validate_object_access_dynamically(target_obj_39214,(0),"dataset",true,true,false))?(target_obj_39214["dataset"]):null);
var next_obj_39216 = ((oops.core.validate_object_access_dynamically(next_obj_39215,(1),"firstName",true,true,false))?(next_obj_39215["firstName"]):null);
if((!((next_obj_39216 == null)))){
return next_obj_39216;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39219);
}})(),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),(function (){var target_obj_39224 = this$;
var _STAR_runtime_state_STAR__orig_val__39228 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39229 = oops.state.prepare_state(target_obj_39224,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39229);

try{var next_obj_39226 = ((oops.core.validate_object_access_dynamically(target_obj_39224,(0),"dataset",true,true,false))?(target_obj_39224["dataset"]):null);
var next_obj_39227 = ((oops.core.validate_object_access_dynamically(next_obj_39226,(1),"lastName",true,true,false))?(next_obj_39226["lastName"]):null);
if((!((next_obj_39227 == null)))){
return next_obj_39227;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39228);
}})(),new cljs.core.Keyword(null,"name","name",1843675177),(function (){var target_obj_39234 = this$;
var _STAR_runtime_state_STAR__orig_val__39240 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39241 = oops.state.prepare_state(target_obj_39234,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39241);

try{var next_obj_39237 = ((oops.core.validate_object_access_dynamically(target_obj_39234,(0),"dataset",true,true,false))?(target_obj_39234["dataset"]):null);
var next_obj_39238 = ((oops.core.validate_object_access_dynamically(next_obj_39237,(1),"name",true,true,false))?(next_obj_39237["name"]):null);
if((!((next_obj_39238 == null)))){
return next_obj_39238;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39240);
}})(),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),(function (){var target_obj_39242 = this$;
var _STAR_runtime_state_STAR__orig_val__39245 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39246 = oops.state.prepare_state(target_obj_39242,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39246);

try{var next_obj_39243 = ((oops.core.validate_object_access_dynamically(target_obj_39242,(0),"dataset",true,true,false))?(target_obj_39242["dataset"]):null);
var next_obj_39244 = ((oops.core.validate_object_access_dynamically(next_obj_39243,(1),"avatarUrl",true,true,false))?(next_obj_39243["avatarUrl"]):null);
if((!((next_obj_39244 == null)))){
return next_obj_39244;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39245);
}})(),new cljs.core.Keyword(null,"title","title",636505583),(function (){var target_obj_39247 = this$;
var _STAR_runtime_state_STAR__orig_val__39250 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39251 = oops.state.prepare_state(target_obj_39247,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39251);

try{var next_obj_39248 = ((oops.core.validate_object_access_dynamically(target_obj_39247,(0),"dataset",true,true,false))?(target_obj_39247["dataset"]):null);
var next_obj_39249 = ((oops.core.validate_object_access_dynamically(next_obj_39248,(1),"title",true,true,false))?(next_obj_39248["title"]):null);
if((!((next_obj_39249 == null)))){
return next_obj_39249;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39250);
}})(),new cljs.core.Keyword(null,"slack-username","slack-username",-757727350),(function (){var target_obj_39253 = this$;
var _STAR_runtime_state_STAR__orig_val__39256 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39257 = oops.state.prepare_state(target_obj_39253,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39257);

try{var next_obj_39254 = ((oops.core.validate_object_access_dynamically(target_obj_39253,(0),"dataset",true,true,false))?(target_obj_39253["dataset"]):null);
var next_obj_39255 = ((oops.core.validate_object_access_dynamically(next_obj_39254,(1),"slackUsername",true,true,false))?(next_obj_39254["slackUsername"]):null);
if((!((next_obj_39255 == null)))){
return next_obj_39255;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39256);
}})(),new cljs.core.Keyword(null,"email","email",1415816706),(function (){var target_obj_39258 = this$;
var _STAR_runtime_state_STAR__orig_val__39261 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39262 = oops.state.prepare_state(target_obj_39258,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39262);

try{var next_obj_39259 = ((oops.core.validate_object_access_dynamically(target_obj_39258,(0),"dataset",true,true,false))?(target_obj_39258["dataset"]):null);
var next_obj_39260 = ((oops.core.validate_object_access_dynamically(next_obj_39259,(1),"email",true,true,false))?(next_obj_39259["email"]):null);
if((!((next_obj_39260 == null)))){
return next_obj_39260;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39261);
}})(),new cljs.core.Keyword(null,"user-id","user-id",-206822291),(function (){var target_obj_39263 = this$;
var _STAR_runtime_state_STAR__orig_val__39266 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39267 = oops.state.prepare_state(target_obj_39263,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39267);

try{var next_obj_39264 = ((oops.core.validate_object_access_dynamically(target_obj_39263,(0),"dataset",true,true,false))?(target_obj_39263["dataset"]):null);
var next_obj_39265 = ((oops.core.validate_object_access_dynamically(next_obj_39264,(1),"userId",true,true,false))?(next_obj_39264["userId"]):null);
if((!((next_obj_39265 == null)))){
return next_obj_39265;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39266);
}})()], null);
rum.core.mount((function (){var G__39268 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),user_data_39411,new cljs.core.Keyword(null,"inline?","inline?",-1674483791),true,new cljs.core.Keyword(null,"portal-el","portal-el",1623429907),this$,new cljs.core.Keyword(null,"hide-buttons","hide-buttons",39532420),true], null);
return (oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1(G__39268) : oc.web.components.ui.info_hover_views.user_info_otf.call(null,G__39268));
})(),mount_el);
}
} else {
var user_data_39412 = new cljs.core.PersistentArrayMap(null, 8, [new cljs.core.Keyword(null,"first-name","first-name",-1559982131),(function (){var target_obj_39269 = this$;
var _STAR_runtime_state_STAR__orig_val__39272 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39273 = oops.state.prepare_state(target_obj_39269,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39273);

try{var next_obj_39270 = ((oops.core.validate_object_access_dynamically(target_obj_39269,(0),"dataset",true,true,false))?(target_obj_39269["dataset"]):null);
var next_obj_39271 = ((oops.core.validate_object_access_dynamically(next_obj_39270,(1),"firstName",true,true,false))?(next_obj_39270["firstName"]):null);
if((!((next_obj_39271 == null)))){
return next_obj_39271;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39272);
}})(),new cljs.core.Keyword(null,"last-name","last-name",-1695738974),(function (){var target_obj_39277 = this$;
var _STAR_runtime_state_STAR__orig_val__39280 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39281 = oops.state.prepare_state(target_obj_39277,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39281);

try{var next_obj_39278 = ((oops.core.validate_object_access_dynamically(target_obj_39277,(0),"dataset",true,true,false))?(target_obj_39277["dataset"]):null);
var next_obj_39279 = ((oops.core.validate_object_access_dynamically(next_obj_39278,(1),"lastName",true,true,false))?(next_obj_39278["lastName"]):null);
if((!((next_obj_39279 == null)))){
return next_obj_39279;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39280);
}})(),new cljs.core.Keyword(null,"name","name",1843675177),(function (){var target_obj_39282 = this$;
var _STAR_runtime_state_STAR__orig_val__39285 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39286 = oops.state.prepare_state(target_obj_39282,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39286);

try{var next_obj_39283 = ((oops.core.validate_object_access_dynamically(target_obj_39282,(0),"dataset",true,true,false))?(target_obj_39282["dataset"]):null);
var next_obj_39284 = ((oops.core.validate_object_access_dynamically(next_obj_39283,(1),"name",true,true,false))?(next_obj_39283["name"]):null);
if((!((next_obj_39284 == null)))){
return next_obj_39284;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39285);
}})(),new cljs.core.Keyword(null,"avatar-url","avatar-url",-1034626103),(function (){var target_obj_39291 = this$;
var _STAR_runtime_state_STAR__orig_val__39294 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39295 = oops.state.prepare_state(target_obj_39291,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39295);

try{var next_obj_39292 = ((oops.core.validate_object_access_dynamically(target_obj_39291,(0),"dataset",true,true,false))?(target_obj_39291["dataset"]):null);
var next_obj_39293 = ((oops.core.validate_object_access_dynamically(next_obj_39292,(1),"avatarUrl",true,true,false))?(next_obj_39292["avatarUrl"]):null);
if((!((next_obj_39293 == null)))){
return next_obj_39293;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39294);
}})(),new cljs.core.Keyword(null,"title","title",636505583),(function (){var target_obj_39296 = this$;
var _STAR_runtime_state_STAR__orig_val__39299 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39300 = oops.state.prepare_state(target_obj_39296,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39300);

try{var next_obj_39297 = ((oops.core.validate_object_access_dynamically(target_obj_39296,(0),"dataset",true,true,false))?(target_obj_39296["dataset"]):null);
var next_obj_39298 = ((oops.core.validate_object_access_dynamically(next_obj_39297,(1),"title",true,true,false))?(next_obj_39297["title"]):null);
if((!((next_obj_39298 == null)))){
return next_obj_39298;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39299);
}})(),new cljs.core.Keyword(null,"slack-username","slack-username",-757727350),(function (){var target_obj_39302 = this$;
var _STAR_runtime_state_STAR__orig_val__39305 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39306 = oops.state.prepare_state(target_obj_39302,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39306);

try{var next_obj_39303 = ((oops.core.validate_object_access_dynamically(target_obj_39302,(0),"dataset",true,true,false))?(target_obj_39302["dataset"]):null);
var next_obj_39304 = ((oops.core.validate_object_access_dynamically(next_obj_39303,(1),"slackUsername",true,true,false))?(next_obj_39303["slackUsername"]):null);
if((!((next_obj_39304 == null)))){
return next_obj_39304;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39305);
}})(),new cljs.core.Keyword(null,"email","email",1415816706),(function (){var target_obj_39307 = this$;
var _STAR_runtime_state_STAR__orig_val__39311 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39312 = oops.state.prepare_state(target_obj_39307,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39312);

try{var next_obj_39308 = ((oops.core.validate_object_access_dynamically(target_obj_39307,(0),"dataset",true,true,false))?(target_obj_39307["dataset"]):null);
var next_obj_39309 = ((oops.core.validate_object_access_dynamically(next_obj_39308,(1),"email",true,true,false))?(next_obj_39308["email"]):null);
if((!((next_obj_39309 == null)))){
return next_obj_39309;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39311);
}})(),new cljs.core.Keyword(null,"user-id","user-id",-206822291),(function (){var target_obj_39315 = this$;
var _STAR_runtime_state_STAR__orig_val__39319 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39320 = oops.state.prepare_state(target_obj_39315,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39320);

try{var next_obj_39316 = ((oops.core.validate_object_access_dynamically(target_obj_39315,(0),"dataset",true,true,false))?(target_obj_39315["dataset"]):null);
var next_obj_39317 = ((oops.core.validate_object_access_dynamically(next_obj_39316,(1),"userId",true,true,false))?(next_obj_39316["userId"]):null);
if((!((next_obj_39317 == null)))){
return next_obj_39317;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39319);
}})()], null);
rum.core.mount((function (){var G__39324 = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"user-data","user-data",2143823568),user_data_39412,new cljs.core.Keyword(null,"inline?","inline?",-1674483791),true,new cljs.core.Keyword(null,"portal-el","portal-el",1623429907),this$,new cljs.core.Keyword(null,"hide-buttons","hide-buttons",39532420),true], null);
return (oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1 ? oc.web.components.ui.info_hover_views.user_info_otf.cljs$core$IFn$_invoke$arity$1(G__39324) : oc.web.components.ui.info_hover_views.user_info_otf.call(null,G__39324));
})(),mount_el);
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.mixins.mention","portal-mounted","oc.web.mixins.mention/portal-mounted",2134799785).cljs$core$IFn$_invoke$arity$1(s),true);
} else {
return null;
}
}));
var leave_ev_39396 = goog.events.listen(this$,goog.events.EventType.MOUSELEAVE,(function (e){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.mixins.mention","portal-mounted","oc.web.mixins.mention/portal-mounted",2134799785).cljs$core$IFn$_invoke$arity$1(s)))){
rum.core.unmount(new cljs.core.Keyword("oc.web.mixins.mention","mount-el","oc.web.mixins.mention/mount-el",-338481914).cljs$core$IFn$_invoke$arity$1(s));
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.mixins.mention","portal-mounted","oc.web.mixins.mention/portal-mounted",2134799785).cljs$core$IFn$_invoke$arity$1(s),false);
}));
var click_ev_39397 = (cljs.core.truth_(click_QMARK_)?goog.events.listen(this$,goog.events.EventType.CLICK,(function (e){
var temp__5735__auto__ = (function (){var target_obj_39325 = this$;
var _STAR_runtime_state_STAR__orig_val__39328 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39329 = oops.state.prepare_state(target_obj_39325,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39329);

try{var next_obj_39326 = ((oops.core.validate_object_access_dynamically(target_obj_39325,(0),"dataset",true,true,false))?(target_obj_39325["dataset"]):null);
var next_obj_39327 = ((oops.core.validate_object_access_dynamically(next_obj_39326,(1),"userId",true,true,false))?(next_obj_39326["userId"]):null);
if((!((next_obj_39327 == null)))){
return next_obj_39327;
} else {
return null;
}
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39328);
}})();
if(cljs.core.truth_(temp__5735__auto__)){
var user_id = temp__5735__auto__;
return oc.web.actions.nav_sidebar.show_user_info(user_id);
} else {
return null;
}
})):null);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(events_list,cljs.core.concat,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [enter_ev_39395,leave_ev_39396,click_ev_39397], null));

if(cljs.core.truth_(click_QMARK_)){
return this$.classList.add("expand-profile");
} else {
return null;
}
}));
});
oc.web.mixins.mention.mount_div = (function oc$web$mixins$mention$mount_div(){
var d = document.createElement("div");
var _ = (function (){var target_obj_39342 = d;
var _STAR_runtime_state_STAR__orig_val__39344 = oops.state._STAR_runtime_state_STAR_;
var _STAR_runtime_state_STAR__temp_val__39345 = oops.state.prepare_state(target_obj_39342,(new Error()),function(){arguments[0].apply(console,Array.prototype.slice.call(arguments,1))});
(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__temp_val__39345);

try{var parent_obj_39343_39414 = target_obj_39342;
if(oops.core.validate_object_access_dynamically(parent_obj_39343_39414,(0),"id",true,true,true)){
(parent_obj_39343_39414["id"] = (cljs.core.rand.cljs$core$IFn$_invoke$arity$1((10000)) | (0)));
} else {
}

return target_obj_39342;
}finally {(oops.state._STAR_runtime_state_STAR_ = _STAR_runtime_state_STAR__orig_val__39344);
}})();
var ___$1 = document.body.appendChild(d);
return d;
});
/**
 * Mixin to show a popup when hovering a span.oc-mention with the user's info.
 * If click? is true it shows the user's info popup with buttons to open profile and posts list.
 * To use click? it needs the following mixins:
 * - rum/reactive
 * - (drv/drv :current-user-data)
 * - (drv/drv :users-info-hover)
 * - (drv/drv :follow-publishers-list)
 * - (drv/drv :followers-publishers-count)
 */
oc.web.mixins.mention.oc_mentions_hover = (function oc$web$mixins$mention$oc_mentions_hover(var_args){
var args__4742__auto__ = [];
var len__4736__auto___39415 = arguments.length;
var i__4737__auto___39416 = (0);
while(true){
if((i__4737__auto___39416 < len__4736__auto___39415)){
args__4742__auto__.push((arguments[i__4737__auto___39416]));

var G__39417 = (i__4737__auto___39416 + (1));
i__4737__auto___39416 = G__39417;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.mixins.mention.oc_mentions_hover.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.mixins.mention.oc_mentions_hover.cljs$core$IFn$_invoke$arity$variadic = (function (p__39352){
var vec__39353 = p__39352;
var map__39356 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__39353,(0),null);
var map__39356__$1 = (((((!((map__39356 == null))))?(((((map__39356.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__39356.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__39356):map__39356);
var click_QMARK_ = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__39356__$1,new cljs.core.Keyword(null,"click?","click?",-1210364665));
var events_list = cljs.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentVector.EMPTY);
return new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"did-mount","did-mount",918232960),(function (s){
oc.web.mixins.mention.add_events(s,events_list,click_QMARK_);

return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(s,new cljs.core.Keyword("oc.web.mixins.mention","mount-el","oc.web.mixins.mention/mount-el",-338481914),oc.web.mixins.mention.mount_div()),new cljs.core.Keyword("oc.web.mixins.mention","portal-mounted","oc.web.mixins.mention/portal-mounted",2134799785),cljs.core.atom.cljs$core$IFn$_invoke$arity$1(false));
}),new cljs.core.Keyword(null,"after-render","after-render",1997533433),(function (s){
oc.web.mixins.mention.remove_events(s,events_list);

oc.web.mixins.mention.add_events(s,events_list,click_QMARK_);

return s;
}),new cljs.core.Keyword(null,"will-unmount","will-unmount",-808051550),(function (s){
oc.web.mixins.mention.remove_events(s,events_list);

var temp__5733__auto__ = new cljs.core.Keyword("oc.web.mixins.mention","mount-el","oc.web.mixins.mention/mount-el",-338481914).cljs$core$IFn$_invoke$arity$1(s);
if(cljs.core.truth_(temp__5733__auto__)){
var mount_el = temp__5733__auto__;
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.mixins.mention","portal-mounted","oc.web.mixins.mention/portal-mounted",2134799785).cljs$core$IFn$_invoke$arity$1(s)))){
rum.core.unmount(mount_el);
} else {
}

if(cljs.core.truth_((function (){var and__4115__auto__ = mount_el;
if(cljs.core.truth_(and__4115__auto__)){
return mount_el.parentElement;
} else {
return and__4115__auto__;
}
})())){
document.body.removeChild(mount_el);
} else {
}

return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(s,new cljs.core.Keyword("oc.web.mixins.mention","mount-el","oc.web.mixins.mention/mount-el",-338481914));
} else {
return s;
}
})], null);
}));

(oc.web.mixins.mention.oc_mentions_hover.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.mixins.mention.oc_mentions_hover.cljs$lang$applyTo = (function (seq39351){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq39351));
}));


//# sourceMappingURL=oc.web.mixins.mention.js.map
