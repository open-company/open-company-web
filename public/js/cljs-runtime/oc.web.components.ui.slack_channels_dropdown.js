goog.provide('oc.web.components.ui.slack_channels_dropdown');
oc.web.components.ui.slack_channels_dropdown.filter_team_channels = (function oc$web$components$ui$slack_channels_dropdown$filter_team_channels(channels,s){
var look_for = cuerdas.core.lower(((cuerdas.core.starts_with_QMARK_(s,"#"))?cuerdas.core.strip_prefix(s,"#"):s));
return cljs.core.filterv((function (p1__46188_SHARP_){
return cuerdas.core.includes_QMARK_(cuerdas.core.lower(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(p1__46188_SHARP_)),look_for);
}),channels);
});
oc.web.components.ui.slack_channels_dropdown.slack_channels_dropdown = rum.core.build_defcs((function (s,p__46198){
var map__46199 = p__46198;
var map__46199__$1 = (((((!((map__46199 == null))))?(((((map__46199.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__46199.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__46199):map__46199);
var data = map__46199__$1;
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46199__$1,new cljs.core.Keyword(null,"disabled","disabled",-1529784218));
var initial_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46199__$1,new cljs.core.Keyword(null,"initial-value","initial-value",470619381));
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46199__$1,new cljs.core.Keyword(null,"on-change","on-change",-732046149));
var on_intermediate_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__46199__$1,new cljs.core.Keyword(null,"on-intermediate-change","on-intermediate-change",-1144231725));
var slack_teams = org.martinklepsch.derivatives.react(s,new cljs.core.Keyword(null,"team-channels","team-channels",321697292));
return React.createElement("div",({"onClick": (function (p1__46191_SHARP_){
if(cljs.core.truth_(disabled)){
return null;
} else {
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s),false);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s),cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s))));

return oc.web.lib.utils.event_stop(p1__46191_SHARP_);
}
}), "className": sablono.util.join_classes(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["slack-channels-dropdown",(cljs.core.truth_(disabled)?"disabled":"")], null))}),sablono.interpreter.create_element("input",({"value": (function (){var or__4126__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})(), "placeholder": (cljs.core.truth_(disabled)?"Not connected":"Select a channel..."), "onFocus": (function (){
return oc.web.lib.utils.after((100),(function (){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s),true);
}));
}), "onChange": (function (p1__46192_SHARP_){
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s),true);

if(cljs.core.fn_QMARK_(on_intermediate_change)){
var G__46205_46245 = p1__46192_SHARP_.target.value;
(on_intermediate_change.cljs$core$IFn$_invoke$arity$1 ? on_intermediate_change.cljs$core$IFn$_invoke$arity$1(G__46205_46245) : on_intermediate_change.call(null,G__46205_46245));
} else {
}

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s),p1__46192_SHARP_.target.value);
}), "disabled": disabled, "className": "board-edit-slack-channel oc-input"})),sablono.interpreter.interpret((cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.slack-channels-dropdown-list","div.slack-channels-dropdown-list",977499985),(function (){var iter__4529__auto__ = (function oc$web$components$ui$slack_channels_dropdown$iter__46206(s__46207){
return (new cljs.core.LazySeq(null,(function (){
var s__46207__$1 = s__46207;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__46207__$1);
if(temp__5735__auto__){
var s__46207__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__46207__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46207__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46209 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46208 = (0);
while(true){
if((i__46208 < size__4528__auto__)){
var t = cljs.core._nth(c__4527__auto__,i__46208);
var chs = (cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s));
} else {
return and__4115__auto__;
}
})())?oc.web.components.ui.slack_channels_dropdown.filter_team_channels(new cljs.core.Keyword(null,"channels","channels",1132759174).cljs$core$IFn$_invoke$arity$1(t),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s))):new cljs.core.Keyword(null,"channels","channels",1132759174).cljs$core$IFn$_invoke$arity$1(t));
var show_slack_team_name = (((cljs.core.count(slack_teams) > (1))) && ((cljs.core.count(chs) > (0))));
cljs.core.chunk_append(b__46209,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.slack-team","div.slack-team",-1738230813),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((show_slack_team_name)?"show-slack-name":null),new cljs.core.Keyword(null,"key","key",-1516042587),["slack-chs-dd-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(t))].join('')], null),((show_slack_team_name)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.slack-team-name","div.slack-team-name",-1566889961),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(t)], null):null),(function (){var iter__4529__auto__ = ((function (i__46208,chs,show_slack_team_name,t,c__4527__auto__,size__4528__auto__,b__46209,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change){
return (function oc$web$components$ui$slack_channels_dropdown$iter__46206_$_iter__46221(s__46222){
return (new cljs.core.LazySeq(null,((function (i__46208,chs,show_slack_team_name,t,c__4527__auto__,size__4528__auto__,b__46209,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change){
return (function (){
var s__46222__$1 = s__46222;
while(true){
var temp__5735__auto____$1 = cljs.core.seq(s__46222__$1);
if(temp__5735__auto____$1){
var s__46222__$2 = temp__5735__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__46222__$2)){
var c__4527__auto____$1 = cljs.core.chunk_first(s__46222__$2);
var size__4528__auto____$1 = cljs.core.count(c__4527__auto____$1);
var b__46224 = cljs.core.chunk_buffer(size__4528__auto____$1);
if((function (){var i__46223 = (0);
while(true){
if((i__46223 < size__4528__auto____$1)){
var c = cljs.core._nth(c__4527__auto____$1,i__46223);
cljs.core.chunk_append(b__46224,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.channel.group","div.channel.group",-308176166),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c),new cljs.core.Keyword(null,"key","key",-1516042587),["slack-chs-dd-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(t)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46223,i__46208,c,c__4527__auto____$1,size__4528__auto____$1,b__46224,s__46222__$2,temp__5735__auto____$1,chs,show_slack_team_name,t,c__4527__auto__,size__4528__auto__,b__46209,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change){
return (function (p1__46193_SHARP_){
(on_change.cljs$core$IFn$_invoke$arity$2 ? on_change.cljs$core$IFn$_invoke$arity$2(t,c) : on_change.call(null,t,c));

oc.web.lib.utils.event_stop(p1__46193_SHARP_);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s),["#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(c))].join(''));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s),false);
});})(i__46223,i__46208,c,c__4527__auto____$1,size__4528__auto____$1,b__46224,s__46222__$2,temp__5735__auto____$1,chs,show_slack_team_name,t,c__4527__auto__,size__4528__auto__,b__46209,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change))
], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.ch-prefix","span.ch-prefix",-1685023317),"#"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.ch","span.ch",-2071225293),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(c)], null)], null));

var G__46246 = (i__46223 + (1));
i__46223 = G__46246;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46224),oc$web$components$ui$slack_channels_dropdown$iter__46206_$_iter__46221(cljs.core.chunk_rest(s__46222__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46224),null);
}
} else {
var c = cljs.core.first(s__46222__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.channel.group","div.channel.group",-308176166),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c),new cljs.core.Keyword(null,"key","key",-1516042587),["slack-chs-dd-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(t)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46208,c,s__46222__$2,temp__5735__auto____$1,chs,show_slack_team_name,t,c__4527__auto__,size__4528__auto__,b__46209,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change){
return (function (p1__46193_SHARP_){
(on_change.cljs$core$IFn$_invoke$arity$2 ? on_change.cljs$core$IFn$_invoke$arity$2(t,c) : on_change.call(null,t,c));

oc.web.lib.utils.event_stop(p1__46193_SHARP_);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s),["#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(c))].join(''));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s),false);
});})(i__46208,c,s__46222__$2,temp__5735__auto____$1,chs,show_slack_team_name,t,c__4527__auto__,size__4528__auto__,b__46209,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change))
], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.ch-prefix","span.ch-prefix",-1685023317),"#"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.ch","span.ch",-2071225293),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(c)], null)], null),oc$web$components$ui$slack_channels_dropdown$iter__46206_$_iter__46221(cljs.core.rest(s__46222__$2)));
}
} else {
return null;
}
break;
}
});})(i__46208,chs,show_slack_team_name,t,c__4527__auto__,size__4528__auto__,b__46209,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change))
,null,null));
});})(i__46208,chs,show_slack_team_name,t,c__4527__auto__,size__4528__auto__,b__46209,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change))
;
return iter__4529__auto__(chs);
})()], null));

var G__46250 = (i__46208 + (1));
i__46208 = G__46250;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46209),oc$web$components$ui$slack_channels_dropdown$iter__46206(cljs.core.chunk_rest(s__46207__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46209),null);
}
} else {
var t = cljs.core.first(s__46207__$2);
var chs = (cljs.core.truth_((function (){var and__4115__auto__ = cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s));
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s));
} else {
return and__4115__auto__;
}
})())?oc.web.components.ui.slack_channels_dropdown.filter_team_channels(new cljs.core.Keyword(null,"channels","channels",1132759174).cljs$core$IFn$_invoke$arity$1(t),cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s))):new cljs.core.Keyword(null,"channels","channels",1132759174).cljs$core$IFn$_invoke$arity$1(t));
var show_slack_team_name = (((cljs.core.count(slack_teams) > (1))) && ((cljs.core.count(chs) > (0))));
return cljs.core.cons(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.slack-team","div.slack-team",-1738230813),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"class","class",-2030961996),((show_slack_team_name)?"show-slack-name":null),new cljs.core.Keyword(null,"key","key",-1516042587),["slack-chs-dd-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(t))].join('')], null),((show_slack_team_name)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.slack-team-name","div.slack-team-name",-1566889961),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(t)], null):null),(function (){var iter__4529__auto__ = ((function (chs,show_slack_team_name,t,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change){
return (function oc$web$components$ui$slack_channels_dropdown$iter__46206_$_iter__46233(s__46234){
return (new cljs.core.LazySeq(null,(function (){
var s__46234__$1 = s__46234;
while(true){
var temp__5735__auto____$1 = cljs.core.seq(s__46234__$1);
if(temp__5735__auto____$1){
var s__46234__$2 = temp__5735__auto____$1;
if(cljs.core.chunked_seq_QMARK_(s__46234__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__46234__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__46236 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__46235 = (0);
while(true){
if((i__46235 < size__4528__auto__)){
var c = cljs.core._nth(c__4527__auto__,i__46235);
cljs.core.chunk_append(b__46236,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.channel.group","div.channel.group",-308176166),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c),new cljs.core.Keyword(null,"key","key",-1516042587),["slack-chs-dd-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(t)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (i__46235,c,c__4527__auto__,size__4528__auto__,b__46236,s__46234__$2,temp__5735__auto____$1,chs,show_slack_team_name,t,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change){
return (function (p1__46193_SHARP_){
(on_change.cljs$core$IFn$_invoke$arity$2 ? on_change.cljs$core$IFn$_invoke$arity$2(t,c) : on_change.call(null,t,c));

oc.web.lib.utils.event_stop(p1__46193_SHARP_);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s),["#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(c))].join(''));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s),false);
});})(i__46235,c,c__4527__auto__,size__4528__auto__,b__46236,s__46234__$2,temp__5735__auto____$1,chs,show_slack_team_name,t,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change))
], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.ch-prefix","span.ch-prefix",-1685023317),"#"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.ch","span.ch",-2071225293),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(c)], null)], null));

var G__46253 = (i__46235 + (1));
i__46235 = G__46253;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__46236),oc$web$components$ui$slack_channels_dropdown$iter__46206_$_iter__46233(cljs.core.chunk_rest(s__46234__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__46236),null);
}
} else {
var c = cljs.core.first(s__46234__$2);
return cljs.core.cons(new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.channel.group","div.channel.group",-308176166),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"value","value",305978217),new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c),new cljs.core.Keyword(null,"key","key",-1516042587),["slack-chs-dd-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561).cljs$core$IFn$_invoke$arity$1(t)),"-",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"id","id",-1388402092).cljs$core$IFn$_invoke$arity$1(c))].join(''),new cljs.core.Keyword(null,"on-click","on-click",1632826543),((function (c,s__46234__$2,temp__5735__auto____$1,chs,show_slack_team_name,t,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change){
return (function (p1__46193_SHARP_){
(on_change.cljs$core$IFn$_invoke$arity$2 ? on_change.cljs$core$IFn$_invoke$arity$2(t,c) : on_change.call(null,t,c));

oc.web.lib.utils.event_stop(p1__46193_SHARP_);

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s),["#",cljs.core.str.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(c))].join(''));

cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s),false);

return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491).cljs$core$IFn$_invoke$arity$1(s),false);
});})(c,s__46234__$2,temp__5735__auto____$1,chs,show_slack_team_name,t,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change))
], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.ch-prefix","span.ch-prefix",-1685023317),"#"], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"span.ch","span.ch",-2071225293),new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(c)], null)], null),oc$web$components$ui$slack_channels_dropdown$iter__46206_$_iter__46233(cljs.core.rest(s__46234__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});})(chs,show_slack_team_name,t,s__46207__$2,temp__5735__auto__,slack_teams,map__46199,map__46199__$1,data,disabled,initial_value,on_change,on_intermediate_change))
;
return iter__4529__auto__(chs);
})()], null),oc$web$components$ui$slack_channels_dropdown$iter__46206(cljs.core.rest(s__46207__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(slack_teams);
})()], null):null)));
}),new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","field-value","oc.web.components.ui.slack-channels-dropdown/field-value",220952240)),rum.core.local.cljs$core$IFn$_invoke$arity$2(null,new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","team-channels-requested","oc.web.components.ui.slack-channels-dropdown/team-channels-requested",-289951248)),rum.core.local.cljs$core$IFn$_invoke$arity$2("",new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868)),rum.core.local.cljs$core$IFn$_invoke$arity$2(false,new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","typing","oc.web.components.ui.slack-channels-dropdown/typing",134181491)),rum.core.reactive,oc.web.mixins.ui.on_click_out.cljs$core$IFn$_invoke$arity$1((function (s,e){
if(cljs.core.truth_(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s)))){
return cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","show-channels-dropdown","oc.web.components.ui.slack-channels-dropdown/show-channels-dropdown",64546513).cljs$core$IFn$_invoke$arity$1(s),false);
} else {
return null;
}
})),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-data","team-data",-732020079)], 0)),org.martinklepsch.derivatives.drv.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"team-channels","team-channels",321697292)], 0)),new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"will-mount","will-mount",-434633071),(function (s){
var initial_value_46260 = new cljs.core.Keyword(null,"initial-value","initial-value",470619381).cljs$core$IFn$_invoke$arity$1(cljs.core.nth.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword("rum","args","rum/args",1315791754).cljs$core$IFn$_invoke$arity$1(s),(0)));
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","slack-channel","oc.web.components.ui.slack-channels-dropdown/slack-channel",-904045868).cljs$core$IFn$_invoke$arity$1(s),initial_value_46260);

if(((cljs.core.not(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"team-channels","team-channels",321697292))))) && (cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","team-channels-requested","oc.web.components.ui.slack-channels-dropdown/team-channels-requested",-289951248).cljs$core$IFn$_invoke$arity$1(s)))))){
var temp__5735__auto___46262 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"team-data","team-data",-732020079)));
if(cljs.core.truth_(temp__5735__auto___46262)){
var team_data_46264 = temp__5735__auto___46262;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","team-channels-requested","oc.web.components.ui.slack-channels-dropdown/team-channels-requested",-289951248).cljs$core$IFn$_invoke$arity$1(s),true);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"channels-enumerate","channels-enumerate",375374686),new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data_46264)], null));
} else {
}
} else {
}

return s;
}),new cljs.core.Keyword(null,"did-remount","did-remount",1362550500),(function (o,s){
if(((cljs.core.not(cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"team-channels","team-channels",321697292))))) && (cljs.core.not(cljs.core.deref(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","team-channels-requested","oc.web.components.ui.slack-channels-dropdown/team-channels-requested",-289951248).cljs$core$IFn$_invoke$arity$1(s)))))){
var temp__5735__auto___46266 = cljs.core.deref(org.martinklepsch.derivatives.get_ref(s,new cljs.core.Keyword(null,"team-data","team-data",-732020079)));
if(cljs.core.truth_(temp__5735__auto___46266)){
var team_data_46267 = temp__5735__auto___46266;
cljs.core.reset_BANG_(new cljs.core.Keyword("oc.web.components.ui.slack-channels-dropdown","team-channels-requested","oc.web.components.ui.slack-channels-dropdown/team-channels-requested",-289951248).cljs$core$IFn$_invoke$arity$1(s),true);

oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"channels-enumerate","channels-enumerate",375374686),new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data_46267)], null));
} else {
}
} else {
}

return s;
})], null)], null),"slack-channels-dropdown");

//# sourceMappingURL=oc.web.components.ui.slack_channels_dropdown.js.map
