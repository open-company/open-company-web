goog.provide('oc.web.actions.team');
oc.web.actions.team.roster_get = (function oc$web$actions$team$roster_get(roster_link){
return oc.web.api.get_team(roster_link,(function (p__38432){
var map__38433 = p__38432;
var map__38433__$1 = (((((!((map__38433 == null))))?(((((map__38433.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38433.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38433):map__38433);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38433__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38433__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38433__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var fixed_body = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core.truth_(success)){
var users = new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(fixed_body));
var fixed_roster_data = new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"team-id","team-id",-14505725),new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(fixed_body),new cljs.core.Keyword(null,"links","links",-654507394),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(fixed_body)),new cljs.core.Keyword(null,"users","users",-713552705),users], null);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"team-roster-loaded","team-roster-loaded",75281503),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),fixed_roster_data], null));

var activities_read = oc.web.dispatcher.activity_read_data();
var seq__38435 = cljs.core.seq(activities_read);
var chunk__38436 = null;
var count__38437 = (0);
var i__38438 = (0);
while(true){
if((i__38438 < count__38437)){
var vec__38445 = chunk__38436.cljs$core$IIndexed$_nth$arity$2(null,i__38438);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38445,(0),null);
var read_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38445,(1),null);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-reads","activity-reads",-203392960),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_uuid,new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(read_data),new cljs.core.Keyword(null,"reads","reads",-1215067361).cljs$core$IFn$_invoke$arity$1(read_data),fixed_roster_data], null));


var G__38581 = seq__38435;
var G__38582 = chunk__38436;
var G__38583 = count__38437;
var G__38584 = (i__38438 + (1));
seq__38435 = G__38581;
chunk__38436 = G__38582;
count__38437 = G__38583;
i__38438 = G__38584;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__38435);
if(temp__5735__auto__){
var seq__38435__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__38435__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__38435__$1);
var G__38585 = cljs.core.chunk_rest(seq__38435__$1);
var G__38586 = c__4556__auto__;
var G__38587 = cljs.core.count(c__4556__auto__);
var G__38588 = (0);
seq__38435 = G__38585;
chunk__38436 = G__38586;
count__38437 = G__38587;
i__38438 = G__38588;
continue;
} else {
var vec__38448 = cljs.core.first(seq__38435__$1);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38448,(0),null);
var read_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38448,(1),null);
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 6, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"activity-reads","activity-reads",-203392960),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),activity_uuid,new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(read_data),new cljs.core.Keyword(null,"reads","reads",-1215067361).cljs$core$IFn$_invoke$arity$1(read_data),fixed_roster_data], null));


var G__38589 = cljs.core.next(seq__38435__$1);
var G__38590 = null;
var G__38591 = (0);
var G__38592 = (0);
seq__38435 = G__38589;
chunk__38436 = G__38590;
count__38437 = G__38591;
i__38438 = G__38592;
continue;
}
} else {
return null;
}
}
break;
}
} else {
return null;
}
}));
});
oc.web.actions.team.enumerate_channels_cb = (function oc$web$actions$team$enumerate_channels_cb(team_id,p__38451){
var map__38452 = p__38451;
var map__38452__$1 = (((((!((map__38452 == null))))?(((((map__38452.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38452.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38452):map__38452);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38452__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38452__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38452__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var fixed_body = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
var channels = new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(fixed_body));
if(cljs.core.truth_(success)){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("channels-enumerate","success","channels-enumerate/success",-1838190865),team_id,channels], null));
} else {
return null;
}
});
oc.web.actions.team.enumerate_channels = (function oc$web$actions$team$enumerate_channels(team_data){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var team_id = new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(team_data);
if(cljs.core.truth_(team_id)){
var enumerate_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"channels","GET"], 0));
oc.web.api.enumerate_channels(enumerate_link,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.team.enumerate_channels_cb,team_id));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"channels-enumerate","channels-enumerate",375374686),team_id], null));
} else {
return null;
}
});
oc.web.actions.team.team_get = (function oc$web$actions$team$team_get(team_link){
return oc.web.api.get_team(team_link,(function (p__38454){
var map__38455 = p__38454;
var map__38455__$1 = (((((!((map__38455 == null))))?(((((map__38455.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38455.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38455):map__38455);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38455__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38455__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38455__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var team_data = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core.truth_(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"team-loaded","team-loaded",1150677177),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),team_data], null));

oc.web.lib.utils.after((100),oc.web.actions.org.maybe_show_integration_added_notification_QMARK_);

return oc.web.actions.team.enumerate_channels(team_data);
} else {
return null;
}
}));
});
oc.web.actions.team.force_team_refresh = (function oc$web$actions$team$force_team_refresh(team_id){
var temp__5735__auto__ = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1(team_id);
if(cljs.core.truth_(temp__5735__auto__)){
var team_data = temp__5735__auto__;
var temp__5735__auto___38593__$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["self","item"], null),"GET"], 0));
if(cljs.core.truth_(temp__5735__auto___38593__$1)){
var team_link_38594 = temp__5735__auto___38593__$1;
oc.web.actions.team.team_get(team_link_38594);
} else {
}

var temp__5735__auto____$1 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"roster"], 0));
if(cljs.core.truth_(temp__5735__auto____$1)){
var roster_link = temp__5735__auto____$1;
return oc.web.actions.team.roster_get(roster_link);
} else {
return null;
}
} else {
return null;
}
});
oc.web.actions.team.read_teams = (function oc$web$actions$team$read_teams(teams){
var current_panel = cljs.core.last(new cljs.core.Keyword(null,"panel-stack","panel-stack",1084180004).cljs$core$IFn$_invoke$arity$1(cljs.core.deref(oc.web.dispatcher.app_state)));
var load_delay = (cljs.core.truth_((function (){var fexpr__38457 = new cljs.core.PersistentHashSet(null, new cljs.core.PersistentArrayMap(null, 6, [new cljs.core.Keyword(null,"invite-slack","invite-slack",-1290785659),null,new cljs.core.Keyword(null,"invite-email","invite-email",1375794598),null,new cljs.core.Keyword(null,"integrations","integrations",1844532423),null,new cljs.core.Keyword(null,"org","org",1495985),null,new cljs.core.Keyword(null,"team","team",1355747699),null,new cljs.core.Keyword(null,"invite-picker","invite-picker",1426151962),null], null), null);
return (fexpr__38457.cljs$core$IFn$_invoke$arity$1 ? fexpr__38457.cljs$core$IFn$_invoke$arity$1(current_panel) : fexpr__38457.call(null,current_panel));
})())?(0):(2500));
var seq__38458 = cljs.core.seq(teams);
var chunk__38460 = null;
var count__38461 = (0);
var i__38462 = (0);
while(true){
if((i__38462 < count__38461)){
var team = chunk__38460.cljs$core$IIndexed$_nth$arity$2(null,i__38462);
var team_link_38595 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team),"item"], 0));
var channels_link_38596 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team),"channels"], 0));
var roster_link_38597 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team),"roster"], 0));
if(cljs.core.truth_(team_link_38595)){
oc.web.lib.utils.maybe_after(load_delay,((function (seq__38458,chunk__38460,count__38461,i__38462,team_link_38595,channels_link_38596,roster_link_38597,team,current_panel,load_delay){
return (function (){
return oc.web.actions.team.team_get(team_link_38595);
});})(seq__38458,chunk__38460,count__38461,i__38462,team_link_38595,channels_link_38596,roster_link_38597,team,current_panel,load_delay))
);
} else {
if(cljs.core.truth_(channels_link_38596)){
oc.web.lib.utils.maybe_after(load_delay,((function (seq__38458,chunk__38460,count__38461,i__38462,team_link_38595,channels_link_38596,roster_link_38597,team,current_panel,load_delay){
return (function (){
return oc.web.actions.team.enumerate_channels(team);
});})(seq__38458,chunk__38460,count__38461,i__38462,team_link_38595,channels_link_38596,roster_link_38597,team,current_panel,load_delay))
);
} else {
}
}

if(cljs.core.truth_(roster_link_38597)){
oc.web.actions.team.roster_get(roster_link_38597);
} else {
}


var G__38598 = seq__38458;
var G__38599 = chunk__38460;
var G__38600 = count__38461;
var G__38601 = (i__38462 + (1));
seq__38458 = G__38598;
chunk__38460 = G__38599;
count__38461 = G__38600;
i__38462 = G__38601;
continue;
} else {
var temp__5735__auto__ = cljs.core.seq(seq__38458);
if(temp__5735__auto__){
var seq__38458__$1 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(seq__38458__$1)){
var c__4556__auto__ = cljs.core.chunk_first(seq__38458__$1);
var G__38602 = cljs.core.chunk_rest(seq__38458__$1);
var G__38603 = c__4556__auto__;
var G__38604 = cljs.core.count(c__4556__auto__);
var G__38605 = (0);
seq__38458 = G__38602;
chunk__38460 = G__38603;
count__38461 = G__38604;
i__38462 = G__38605;
continue;
} else {
var team = cljs.core.first(seq__38458__$1);
var team_link_38606 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team),"item"], 0));
var channels_link_38607 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team),"channels"], 0));
var roster_link_38608 = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team),"roster"], 0));
if(cljs.core.truth_(team_link_38606)){
oc.web.lib.utils.maybe_after(load_delay,((function (seq__38458,chunk__38460,count__38461,i__38462,team_link_38606,channels_link_38607,roster_link_38608,team,seq__38458__$1,temp__5735__auto__,current_panel,load_delay){
return (function (){
return oc.web.actions.team.team_get(team_link_38606);
});})(seq__38458,chunk__38460,count__38461,i__38462,team_link_38606,channels_link_38607,roster_link_38608,team,seq__38458__$1,temp__5735__auto__,current_panel,load_delay))
);
} else {
if(cljs.core.truth_(channels_link_38607)){
oc.web.lib.utils.maybe_after(load_delay,((function (seq__38458,chunk__38460,count__38461,i__38462,team_link_38606,channels_link_38607,roster_link_38608,team,seq__38458__$1,temp__5735__auto__,current_panel,load_delay){
return (function (){
return oc.web.actions.team.enumerate_channels(team);
});})(seq__38458,chunk__38460,count__38461,i__38462,team_link_38606,channels_link_38607,roster_link_38608,team,seq__38458__$1,temp__5735__auto__,current_panel,load_delay))
);
} else {
}
}

if(cljs.core.truth_(roster_link_38608)){
oc.web.actions.team.roster_get(roster_link_38608);
} else {
}


var G__38609 = cljs.core.next(seq__38458__$1);
var G__38610 = null;
var G__38611 = (0);
var G__38612 = (0);
seq__38458 = G__38609;
chunk__38460 = G__38610;
count__38461 = G__38611;
i__38462 = G__38612;
continue;
}
} else {
return null;
}
}
break;
}
});
oc.web.actions.team.teams_get_cb = (function oc$web$actions$team$teams_get_cb(p__38464){
var map__38465 = p__38464;
var map__38465__$1 = (((((!((map__38465 == null))))?(((((map__38465.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38465.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38465):map__38465);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38465__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38465__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38465__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var fixed_body = (cljs.core.truth_(success)?oc.web.lib.json.json__GT_cljs(body):null);
if(cljs.core.truth_(success)){
var teams = new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(fixed_body));
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"teams-loaded","teams-loaded",-1387044499),teams], null));

return oc.web.actions.team.read_teams(teams);
} else {
if(((((500) <= status)) && ((status <= (599))))){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"input","input",556931961),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"team-data-requested","team-data-requested",-1711650213)], null),false], null));
} else {
return null;
}
}
});
oc.web.actions.team.teams_get = (function oc$web$actions$team$teams_get(){
var auth_settings = oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0();
var temp__5735__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(auth_settings),"collection","GET"], 0));
if(cljs.core.truth_(temp__5735__auto__)){
var enumerate_link = temp__5735__auto__;
oc.web.api.get_teams(enumerate_link,oc.web.actions.team.teams_get_cb);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"teams-get","teams-get",1862968866)], null));
} else {
return null;
}
});
oc.web.actions.team.teams_get_if_needed = (function oc$web$actions$team$teams_get_if_needed(){
var auth_settings = oc.web.dispatcher.auth_settings.cljs$core$IFn$_invoke$arity$0();
var teams_data_requested = oc.web.dispatcher.teams_data_requested.cljs$core$IFn$_invoke$arity$0();
var teams_data = oc.web.dispatcher.teams_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(((cljs.core.empty_QMARK_(teams_data))?(function (){var and__4115__auto__ = auth_settings;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not(teams_data_requested);
} else {
return and__4115__auto__;
}
})():false))){
return oc.web.actions.team.teams_get();
} else {
return null;
}
});
oc.web.actions.team.refresh_team_data = (function oc$web$actions$team$refresh_team_data(org_data){
return oc.web.actions.org.get_org.cljs$core$IFn$_invoke$arity$3(org_data,true,(function (){
return oc.web.actions.team.teams_get();
}));
});
oc.web.actions.team.author_change_cb = (function oc$web$actions$team$author_change_cb(p__38467){
var map__38468 = p__38467;
var map__38468__$1 = (((((!((map__38468 == null))))?(((((map__38468.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38468.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38468):map__38468);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38468__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
return oc.web.actions.team.refresh_team_data(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0());
} else {
return null;
}
});
oc.web.actions.team.remove_author = (function oc$web$actions$team$remove_author(author){
var remove_author_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(author),"remove"], 0));
return oc.web.api.remove_author(remove_author_link,author,oc.web.actions.team.author_change_cb);
});
oc.web.actions.team.add_author = (function oc$web$actions$team$add_author(author){
var add_author_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0()),"add"], 0));
return oc.web.api.add_author(add_author_link,new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(author),oc.web.actions.team.author_change_cb);
});
oc.web.actions.team.admin_change_cb = (function oc$web$actions$team$admin_change_cb(user,p__38470){
var map__38471 = p__38470;
var map__38471__$1 = (((((!((map__38471 == null))))?(((((map__38471.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38471.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38471):map__38471);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38471__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
oc.web.actions.team.teams_get();

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("invite-user","success","invite-user/success",365990759),user], null));
} else {
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("invite-user","failed","invite-user/failed",1226102163),user], null));
}
});
oc.web.actions.team.add_admin = (function oc$web$actions$team$add_admin(user){
var add_admin_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(user),"add"], 0));
return oc.web.api.add_admin(add_admin_link,user,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.team.admin_change_cb,user));
});
oc.web.actions.team.remove_admin = (function oc$web$actions$team$remove_admin(user){
var remove_admin_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(user),"remove","DELETE",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"ref","ref",1289896967),"application/vnd.open-company.team.admin.v1"], null)], 0));
return oc.web.api.remove_admin(remove_admin_link,user,cljs.core.partial.cljs$core$IFn$_invoke$arity$2(oc.web.actions.team.admin_change_cb,user));
});
oc.web.actions.team.invite_user_failed = (function oc$web$actions$team$invite_user_failed(user_data){
return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("invite-user","failed","invite-user/failed",1226102163),user_data], null));
});
oc.web.actions.team.invite_user_success = (function oc$web$actions$team$invite_user_success(user_data){
oc.web.actions.team.teams_get();

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("invite-user","success","invite-user/success",365990759),user_data], null));
});
oc.web.actions.team.switch_user_type_cb = (function oc$web$actions$team$switch_user_type_cb(user_data,p__38474){
var map__38475 = p__38474;
var map__38475__$1 = (((((!((map__38475 == null))))?(((((map__38475.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38475.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38475):map__38475);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38475__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
return oc.web.actions.team.invite_user_success(user_data);
} else {
return oc.web.actions.team.invite_user_failed(user_data);
}
});
/**
 * Given an existing user switch user type
 */
oc.web.actions.team.switch_user_type = (function oc$web$actions$team$switch_user_type(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38613 = arguments.length;
var i__4737__auto___38614 = (0);
while(true){
if((i__4737__auto___38614 < len__4736__auto___38613)){
args__4742__auto__.push((arguments[i__4737__auto___38614]));

var G__38615 = (i__4737__auto___38614 + (1));
i__4737__auto___38614 = G__38615;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((4) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((4)),(0),null)):null);
return oc.web.actions.team.switch_user_type.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),argseq__4743__auto__);
});

(oc.web.actions.team.switch_user_type.cljs$core$IFn$_invoke$arity$variadic = (function (complete_user_data,old_user_type,new_user_type,user,p__38494){
var vec__38495 = p__38494;
var author_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38495,(0),null);
if(cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(old_user_type,new_user_type)){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var fixed_author_data = (function (){var or__4126__auto__ = author_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.utils.user.get_author(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(org_data));
}
})();
var add_admin_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new_user_type,new cljs.core.Keyword(null,"admin","admin",-1239101627));
var remove_admin_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(old_user_type,new cljs.core.Keyword(null,"admin","admin",-1239101627));
var add_author_QMARK_ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new_user_type,new cljs.core.Keyword(null,"author","author",2111686192))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new_user_type,new cljs.core.Keyword(null,"admin","admin",-1239101627))));
var remove_author_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new_user_type,new cljs.core.Keyword(null,"viewer","viewer",-783949853));
if(add_admin_QMARK_){
oc.web.actions.team.add_admin(user);
} else {
}

if(remove_admin_QMARK_){
oc.web.actions.team.remove_admin(user);
} else {
}

if(add_author_QMARK_){
oc.web.actions.team.add_author(user);
} else {
}

if(remove_author_QMARK_){
return oc.web.actions.team.remove_author(fixed_author_data);
} else {
return null;
}
} else {
return null;
}
}));

(oc.web.actions.team.switch_user_type.cljs$lang$maxFixedArity = (4));

/** @this {Function} */
(oc.web.actions.team.switch_user_type.cljs$lang$applyTo = (function (seq38485){
var G__38486 = cljs.core.first(seq38485);
var seq38485__$1 = cljs.core.next(seq38485);
var G__38487 = cljs.core.first(seq38485__$1);
var seq38485__$2 = cljs.core.next(seq38485__$1);
var G__38488 = cljs.core.first(seq38485__$2);
var seq38485__$3 = cljs.core.next(seq38485__$2);
var G__38489 = cljs.core.first(seq38485__$3);
var seq38485__$4 = cljs.core.next(seq38485__$3);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38486,G__38487,G__38488,G__38489,seq38485__$4);
}));

oc.web.actions.team.send_invitation_cb = (function oc$web$actions$team$send_invitation_cb(invite_data,user_type,p__38502){
var map__38503 = p__38502;
var map__38503__$1 = (((((!((map__38503 == null))))?(((((map__38503.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38503.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38503):map__38503);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38503__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38503__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
if(cljs.core.truth_(success)){
var new_user = oc.web.lib.json.json__GT_cljs(body);
if(((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"author","author",2111686192))) || (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(user_type,new cljs.core.Keyword(null,"admin","admin",-1239101627))))){
oc.web.actions.team.add_author(new_user);
} else {
}

return oc.web.actions.team.invite_user_success(invite_data);
} else {
return oc.web.actions.team.invite_user_failed(invite_data);
}
});
oc.web.actions.team.invite_user = (function oc$web$actions$team$invite_user(org_data,team_data,invite_data,note){
var invite_from = new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(invite_data);
var email = new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(invite_data);
var slack_user = new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(invite_data);
var user_type = new cljs.core.Keyword(null,"role","role",-736691072).cljs$core$IFn$_invoke$arity$1(invite_data);
var parsed_email = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("email",invite_from))?oc.web.lib.utils.parse_input_email(email):null);
var email_name = new cljs.core.Keyword(null,"name","name",1843675177).cljs$core$IFn$_invoke$arity$1(parsed_email);
var email_address = new cljs.core.Keyword(null,"address","address",559499426).cljs$core$IFn$_invoke$arity$1(parsed_email);
var user = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invite_from,"email"))?cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38505_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(p1__38505_SHARP_),email_address);
}),new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_data))):null);
var old_user_type = (cljs.core.truth_(user)?oc.web.utils.user.get_user_type.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,org_data], 0)):null);
if(cljs.core.truth_((function (){var or__4126__auto__ = cljs.core.not(user);
if(or__4126__auto__){
return or__4126__auto__;
} else {
var and__4115__auto__ = user;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(clojure.string.lower_case(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(user)),"pending");
} else {
return and__4115__auto__;
}
}
})())){
var splitted_name = clojure.string.split.cljs$core$IFn$_invoke$arity$2(email_name,/\s/);
var name_size = cljs.core.count(splitted_name);
var splittable_name_QMARK_ = cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(name_size,(2));
var first_name = ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invite_from,"email")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(name_size,(1)))))?email_name:((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invite_from,"email")) && (splittable_name_QMARK_)))?cljs.core.first(splitted_name):((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invite_from,"slack")) && (cljs.core.seq(new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(slack_user)))))?new cljs.core.Keyword(null,"first-name","first-name",-1559982131).cljs$core$IFn$_invoke$arity$1(slack_user):""
)));
var last_name = ((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invite_from,"email")) && (splittable_name_QMARK_)))?cljs.core.second(splitted_name):((((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invite_from,"slack")) && (cljs.core.seq(new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(slack_user)))))?new cljs.core.Keyword(null,"last-name","last-name",-1695738974).cljs$core$IFn$_invoke$arity$1(slack_user):""
));
var user_value = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(invite_from,"email"))?email_address:slack_user);
if(cljs.core.truth_((function (){var and__4115__auto__ = user;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(old_user_type,user_type);
} else {
return and__4115__auto__;
}
})())){
oc.web.actions.team.switch_user_type(invite_data,old_user_type,user_type,user);
} else {
}

var team_data__$1 = (function (){var or__4126__auto__ = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$0();
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38506_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data),new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(p1__38506_SHARP_));
}),oc.web.dispatcher.teams_data.cljs$core$IFn$_invoke$arity$0()));
}
})();
var invitation_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data__$1),"add","POST",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"content-type","content-type",-508222634),"application/vnd.open-company.team.invite.v1"], null)], 0));
return oc.web.api.send_invitation(invitation_link,user_value,invite_from,user_type,first_name,last_name,note,cljs.core.partial.cljs$core$IFn$_invoke$arity$3(oc.web.actions.team.send_invitation_cb,invite_data,user_type));
} else {
return null;
}
});
oc.web.actions.team.valid_inviting_user_QMARK_ = (function oc$web$actions$team$valid_inviting_user_QMARK_(user){
var or__4126__auto__ = ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("email",new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user)))?oc.web.lib.utils.valid_email_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user)):false);
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("slack",new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user))) && (cljs.core.map_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user))) && (cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"slack-org-id","slack-org-id",1547636561))) && (cljs.core.contains_QMARK_(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user),new cljs.core.Keyword(null,"slack-id","slack-id",862141985))));
}
});
oc.web.actions.team.duplicated_email_addresses = (function oc$web$actions$team$duplicated_email_addresses(user,users_list){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user),"email")){
return (cljs.core.count(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38507_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__38507_SHARP_),new cljs.core.Keyword(null,"address","address",559499426).cljs$core$IFn$_invoke$arity$1(oc.web.lib.utils.parse_input_email(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user))));
}),users_list)) > (1));
} else {
return null;
}
});
oc.web.actions.team.duplicated_team_user = (function oc$web$actions$team$duplicated_team_user(user,users_list){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"type","type",1174270348).cljs$core$IFn$_invoke$arity$1(user),"email")){
var parsed_email = oc.web.lib.utils.parse_input_email(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(user));
var dup_user = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__38508_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"email","email",1415816706).cljs$core$IFn$_invoke$arity$1(p1__38508_SHARP_),new cljs.core.Keyword(null,"address","address",559499426).cljs$core$IFn$_invoke$arity$1(parsed_email));
}),users_list));
var and__4115__auto__ = dup_user;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(clojure.string.lower_case(new cljs.core.Keyword(null,"status","status",-1997798413).cljs$core$IFn$_invoke$arity$1(dup_user)),"pending");
} else {
return and__4115__auto__;
}
} else {
return null;
}
});
oc.web.actions.team.invite_users = (function oc$web$actions$team$invite_users(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38616 = arguments.length;
var i__4737__auto___38617 = (0);
while(true){
if((i__4737__auto___38617 < len__4736__auto___38616)){
args__4742__auto__.push((arguments[i__4737__auto___38617]));

var G__38618 = (i__4737__auto___38617 + (1));
i__4737__auto___38617 = G__38618;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.team.invite_users.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.team.invite_users.cljs$core$IFn$_invoke$arity$variadic = (function (inviting_users,p__38513){
var vec__38514 = p__38513;
var note = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38514,(0),null);
var note__$1 = (function (){var or__4126__auto__ = note;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return "";
}
})();
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var team_data = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data));
var filter_empty = cljs.core.filterv((function (p1__38509_SHARP_){
return cljs.core.seq(new cljs.core.Keyword(null,"user","user",1532431356).cljs$core$IFn$_invoke$arity$1(p1__38509_SHARP_));
}),inviting_users);
var checked_users = (function (){var iter__4529__auto__ = (function oc$web$actions$team$iter__38517(s__38518){
return (new cljs.core.LazySeq(null,(function (){
var s__38518__$1 = s__38518;
while(true){
var temp__5735__auto__ = cljs.core.seq(s__38518__$1);
if(temp__5735__auto__){
var s__38518__$2 = temp__5735__auto__;
if(cljs.core.chunked_seq_QMARK_(s__38518__$2)){
var c__4527__auto__ = cljs.core.chunk_first(s__38518__$2);
var size__4528__auto__ = cljs.core.count(c__4527__auto__);
var b__38520 = cljs.core.chunk_buffer(size__4528__auto__);
if((function (){var i__38519 = (0);
while(true){
if((i__38519 < size__4528__auto__)){
var user = cljs.core._nth(c__4527__auto__,i__38519);
cljs.core.chunk_append(b__38520,(function (){var valid_QMARK_ = oc.web.actions.team.valid_inviting_user_QMARK_(user);
var intive_duplicated_QMARK_ = oc.web.actions.team.duplicated_email_addresses(user,inviting_users);
var team_duplicated_QMARK_ = oc.web.actions.team.duplicated_team_user(user,new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_data));
if(cljs.core.not(valid_QMARK_)){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),true,new cljs.core.Keyword(null,"success","success",1890645906),false], null)], 0));
} else {
if(cljs.core.truth_(team_duplicated_QMARK_)){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),"User already active",new cljs.core.Keyword(null,"success","success",1890645906),false], null)], 0));
} else {
if(cljs.core.truth_(intive_duplicated_QMARK_)){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),"Duplicated email address",new cljs.core.Keyword(null,"success","success",1890645906),false], null)], 0));
} else {
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(user,new cljs.core.Keyword(null,"error","error",-978969032));

}
}
}
})());

var G__38619 = (i__38519 + (1));
i__38519 = G__38619;
continue;
} else {
return true;
}
break;
}
})()){
return cljs.core.chunk_cons(cljs.core.chunk(b__38520),oc$web$actions$team$iter__38517(cljs.core.chunk_rest(s__38518__$2)));
} else {
return cljs.core.chunk_cons(cljs.core.chunk(b__38520),null);
}
} else {
var user = cljs.core.first(s__38518__$2);
return cljs.core.cons((function (){var valid_QMARK_ = oc.web.actions.team.valid_inviting_user_QMARK_(user);
var intive_duplicated_QMARK_ = oc.web.actions.team.duplicated_email_addresses(user,inviting_users);
var team_duplicated_QMARK_ = oc.web.actions.team.duplicated_team_user(user,new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_data));
if(cljs.core.not(valid_QMARK_)){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),true,new cljs.core.Keyword(null,"success","success",1890645906),false], null)], 0));
} else {
if(cljs.core.truth_(team_duplicated_QMARK_)){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),"User already active",new cljs.core.Keyword(null,"success","success",1890645906),false], null)], 0));
} else {
if(cljs.core.truth_(intive_duplicated_QMARK_)){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([user,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"error","error",-978969032),"Duplicated email address",new cljs.core.Keyword(null,"success","success",1890645906),false], null)], 0));
} else {
return cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(user,new cljs.core.Keyword(null,"error","error",-978969032));

}
}
}
})(),oc$web$actions$team$iter__38517(cljs.core.rest(s__38518__$2)));
}
} else {
return null;
}
break;
}
}),null,null));
});
return iter__4529__auto__(filter_empty);
})();
var cleaned_inviting_users = cljs.core.filterv((function (p1__38510_SHARP_){
return cljs.core.not(new cljs.core.Keyword(null,"error","error",-978969032).cljs$core$IFn$_invoke$arity$1(p1__38510_SHARP_));
}),checked_users);
if((cljs.core.count(cleaned_inviting_users) <= cljs.core.count(filter_empty))){
var seq__38521_38620 = cljs.core.seq(cleaned_inviting_users);
var chunk__38522_38621 = null;
var count__38523_38622 = (0);
var i__38524_38623 = (0);
while(true){
if((i__38524_38623 < count__38523_38622)){
var user_38624 = chunk__38522_38621.cljs$core$IIndexed$_nth$arity$2(null,i__38524_38623);
oc.web.actions.team.invite_user(org_data,team_data,user_38624,note__$1);


var G__38625 = seq__38521_38620;
var G__38626 = chunk__38522_38621;
var G__38627 = count__38523_38622;
var G__38628 = (i__38524_38623 + (1));
seq__38521_38620 = G__38625;
chunk__38522_38621 = G__38626;
count__38523_38622 = G__38627;
i__38524_38623 = G__38628;
continue;
} else {
var temp__5735__auto___38629 = cljs.core.seq(seq__38521_38620);
if(temp__5735__auto___38629){
var seq__38521_38630__$1 = temp__5735__auto___38629;
if(cljs.core.chunked_seq_QMARK_(seq__38521_38630__$1)){
var c__4556__auto___38631 = cljs.core.chunk_first(seq__38521_38630__$1);
var G__38632 = cljs.core.chunk_rest(seq__38521_38630__$1);
var G__38633 = c__4556__auto___38631;
var G__38634 = cljs.core.count(c__4556__auto___38631);
var G__38635 = (0);
seq__38521_38620 = G__38632;
chunk__38522_38621 = G__38633;
count__38523_38622 = G__38634;
i__38524_38623 = G__38635;
continue;
} else {
var user_38636 = cljs.core.first(seq__38521_38630__$1);
oc.web.actions.team.invite_user(org_data,team_data,user_38636,note__$1);


var G__38637 = cljs.core.next(seq__38521_38630__$1);
var G__38638 = null;
var G__38639 = (0);
var G__38640 = (0);
seq__38521_38620 = G__38637;
chunk__38522_38621 = G__38638;
count__38523_38622 = G__38639;
i__38524_38623 = G__38640;
continue;
}
} else {
}
}
break;
}
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"invite-users","invite-users",107417337),cljs.core.vec(checked_users)], null));
}));

(oc.web.actions.team.invite_users.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.team.invite_users.cljs$lang$applyTo = (function (seq38511){
var G__38512 = cljs.core.first(seq38511);
var seq38511__$1 = cljs.core.next(seq38511);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38512,seq38511__$1);
}));

oc.web.actions.team.user_action_cb = (function oc$web$actions$team$user_action_cb(_){
return oc.web.actions.team.teams_get();
});
oc.web.actions.team.user_action = (function oc$web$actions$team$user_action(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38641 = arguments.length;
var i__4737__auto___38642 = (0);
while(true){
if((i__4737__auto___38642 < len__4736__auto___38641)){
args__4742__auto__.push((arguments[i__4737__auto___38642]));

var G__38643 = (i__4737__auto___38642 + (1));
i__4737__auto___38642 = G__38643;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((6) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((6)),(0),null)):null);
return oc.web.actions.team.user_action.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),(arguments[(1)]),(arguments[(2)]),(arguments[(3)]),(arguments[(4)]),(arguments[(5)]),argseq__4743__auto__);
});

(oc.web.actions.team.user_action.cljs$core$IFn$_invoke$arity$variadic = (function (team_id,invitation,action,method,other_link_params,payload,p__38533){
var vec__38534 = p__38533;
var finished_cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38534,(0),null);
var team_data = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1(team_id);
var idx = new cljs.core.Keyword(null,"users","users",-713552705).cljs$core$IFn$_invoke$arity$1(team_data).indexOf(invitation);
if((idx > (-1))){
oc.web.api.user_action(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(invitation),action,method,other_link_params], 0)),payload,(function (p1__38525_SHARP_){
if(cljs.core.fn_QMARK_(finished_cb)){
(finished_cb.cljs$core$IFn$_invoke$arity$7 ? finished_cb.cljs$core$IFn$_invoke$arity$7(team_id,invitation,action,method,other_link_params,payload,p1__38525_SHARP_) : finished_cb.call(null,team_id,invitation,action,method,other_link_params,payload,p1__38525_SHARP_));
} else {
}

return oc.web.actions.team.user_action_cb(p1__38525_SHARP_);
}));

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"user-action","user-action",1326176390),team_id,idx], null));
} else {
return null;
}
}));

(oc.web.actions.team.user_action.cljs$lang$maxFixedArity = (6));

/** @this {Function} */
(oc.web.actions.team.user_action.cljs$lang$applyTo = (function (seq38526){
var G__38527 = cljs.core.first(seq38526);
var seq38526__$1 = cljs.core.next(seq38526);
var G__38528 = cljs.core.first(seq38526__$1);
var seq38526__$2 = cljs.core.next(seq38526__$1);
var G__38529 = cljs.core.first(seq38526__$2);
var seq38526__$3 = cljs.core.next(seq38526__$2);
var G__38530 = cljs.core.first(seq38526__$3);
var seq38526__$4 = cljs.core.next(seq38526__$3);
var G__38531 = cljs.core.first(seq38526__$4);
var seq38526__$5 = cljs.core.next(seq38526__$4);
var G__38532 = cljs.core.first(seq38526__$5);
var seq38526__$6 = cljs.core.next(seq38526__$5);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38527,G__38528,G__38529,G__38530,G__38531,G__38532,seq38526__$6);
}));

oc.web.actions.team.email_domain_team_add_cb = (function oc$web$actions$team$email_domain_team_add_cb(p__38537){
var map__38538 = p__38537;
var map__38538__$1 = (((((!((map__38538 == null))))?(((((map__38538.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38538.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38538):map__38538);
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38538__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38538__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38538__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.truth_(success)){
oc.web.actions.team.teams_get();
} else {
}

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword("email-domain-team-add","finish","email-domain-team-add/finish",-917388775),cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(status,(204))], null));
});
oc.web.actions.team.add_email_domain_link = (function oc$web$actions$team$add_email_domain_link(team_data_links){
return oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([team_data_links,"add","POST",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"content-type","content-type",-508222634),"application/vnd.open-company.team.email-domain.v1+json"], null)], 0));
});
oc.web.actions.team.email_domain_team_add = (function oc$web$actions$team$email_domain_team_add(domain,cb){
if(cljs.core.truth_(oc.web.lib.utils.valid_domain_QMARK_(domain))){
var team_data_38644 = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$0();
var add_email_domain_link_38645 = oc.web.actions.team.add_email_domain_link(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data_38644));
var fixed_domain_38646 = (cljs.core.truth_(domain.startsWith("@"))?cljs.core.subs.cljs$core$IFn$_invoke$arity$2(domain,(1)):domain);
oc.web.api.add_email_domain(add_email_domain_link_38645,fixed_domain_38646,(function (p__38540){
var map__38541 = p__38540;
var map__38541__$1 = (((((!((map__38541 == null))))?(((((map__38541.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38541.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38541):map__38541);
var resp = map__38541__$1;
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38541__$1,new cljs.core.Keyword(null,"success","success",1890645906));
oc.web.actions.team.email_domain_team_add_cb(resp);

if(cljs.core.fn_QMARK_(cb)){
return (cb.cljs$core$IFn$_invoke$arity$1 ? cb.cljs$core$IFn$_invoke$arity$1(success) : cb.call(null,success));
} else {
return null;
}
}),team_data_38644);

return oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"email-domain-team-add","email-domain-team-add",-665391449)], null));
} else {
return null;
}
});
oc.web.actions.team.can_add_email_domain_QMARK_ = (function oc$web$actions$team$can_add_email_domain_QMARK_(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38647 = arguments.length;
var i__4737__auto___38648 = (0);
while(true){
if((i__4737__auto___38648 < len__4736__auto___38647)){
args__4742__auto__.push((arguments[i__4737__auto___38648]));

var G__38649 = (i__4737__auto___38648 + (1));
i__4737__auto___38648 = G__38649;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((0) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((0)),(0),null)):null);
return oc.web.actions.team.can_add_email_domain_QMARK_.cljs$core$IFn$_invoke$arity$variadic(argseq__4743__auto__);
});

(oc.web.actions.team.can_add_email_domain_QMARK_.cljs$core$IFn$_invoke$arity$variadic = (function (p__38544){
var vec__38545 = p__38544;
var team_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38545,(0),null);
var fixed_team_data = (function (){var or__4126__auto__ = team_data;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$0();
}
})();
return cljs.core.seq(oc.web.actions.team.add_email_domain_link(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(fixed_team_data)));
}));

(oc.web.actions.team.can_add_email_domain_QMARK_.cljs$lang$maxFixedArity = (0));

/** @this {Function} */
(oc.web.actions.team.can_add_email_domain_QMARK_.cljs$lang$applyTo = (function (seq38543){
var self__4724__auto__ = this;
return self__4724__auto__.cljs$core$IFn$_invoke$arity$variadic(cljs.core.seq(seq38543));
}));

oc.web.actions.team.slack_team_add = (function oc$web$actions$team$slack_team_add(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38650 = arguments.length;
var i__4737__auto___38651 = (0);
while(true){
if((i__4737__auto___38651 < len__4736__auto___38650)){
args__4742__auto__.push((arguments[i__4737__auto___38651]));

var G__38652 = (i__4737__auto___38651 + (1));
i__4737__auto___38651 = G__38652;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.team.slack_team_add.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.team.slack_team_add.cljs$core$IFn$_invoke$arity$variadic = (function (current_user_data,p__38550){
var vec__38551 = p__38550;
var redirect_to = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38551,(0),null);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$0();
var team_id = new cljs.core.Keyword(null,"team-id","team-id",-14505725).cljs$core$IFn$_invoke$arity$1(org_data);
var team_data = oc.web.dispatcher.team_data.cljs$core$IFn$_invoke$arity$1(team_id);
var add_slack_team_link = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(team_data),"authenticate","GET",new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"auth-source","auth-source",1912135250),"slack"], null)], 0));
var redirect = (function (){var or__4126__auto__ = redirect_to;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return oc.web.router.get_token();
}
})();
var with_add_team = encodeURIComponent((((redirect.indexOf("?") > (-1)))?[cljs.core.str.cljs$core$IFn$_invoke$arity$1(redirect),"&add=team"].join(''):[cljs.core.str.cljs$core$IFn$_invoke$arity$1(redirect),"?add=team"].join('')));
var fixed_add_slack_team_link = oc.web.utils.user.auth_link_with_state(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(add_slack_team_link),new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"user-id","user-id",-206822291),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(current_user_data),new cljs.core.Keyword(null,"team-id","team-id",-14505725),team_id,new cljs.core.Keyword(null,"redirect","redirect",-1975673286),with_add_team], null));
if(cljs.core.truth_(fixed_add_slack_team_link)){
return oc.web.router.redirect_BANG_(fixed_add_slack_team_link);
} else {
return null;
}
}));

(oc.web.actions.team.slack_team_add.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.team.slack_team_add.cljs$lang$applyTo = (function (seq38548){
var G__38549 = cljs.core.first(seq38548);
var seq38548__$1 = cljs.core.next(seq38548);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38549,seq38548__$1);
}));

oc.web.actions.team.remove_team = (function oc$web$actions$team$remove_team(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38654 = arguments.length;
var i__4737__auto___38655 = (0);
while(true){
if((i__4737__auto___38655 < len__4736__auto___38654)){
args__4742__auto__.push((arguments[i__4737__auto___38655]));

var G__38656 = (i__4737__auto___38655 + (1));
i__4737__auto___38655 = G__38656;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.team.remove_team.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.team.remove_team.cljs$core$IFn$_invoke$arity$variadic = (function (team_links,p__38556){
var vec__38557 = p__38556;
var cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38557,(0),null);
return oc.web.api.user_action(oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([team_links,"remove","DELETE"], 0)),null,(function (p__38560){
var map__38561 = p__38560;
var map__38561__$1 = (((((!((map__38561 == null))))?(((((map__38561.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38561.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38561):map__38561);
var resp = map__38561__$1;
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38561__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38561__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38561__$1,new cljs.core.Keyword(null,"success","success",1890645906));
if(cljs.core.fn_QMARK_(cb)){
(cb.cljs$core$IFn$_invoke$arity$1 ? cb.cljs$core$IFn$_invoke$arity$1(success) : cb.call(null,success));
} else {
}

return oc.web.actions.team.user_action_cb(resp);
}));
}));

(oc.web.actions.team.remove_team.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.team.remove_team.cljs$lang$applyTo = (function (seq38554){
var G__38555 = cljs.core.first(seq38554);
var seq38554__$1 = cljs.core.next(seq38554);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38555,seq38554__$1);
}));

oc.web.actions.team.create_invite_token_link = (function oc$web$actions$team$create_invite_token_link(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38661 = arguments.length;
var i__4737__auto___38662 = (0);
while(true){
if((i__4737__auto___38662 < len__4736__auto___38661)){
args__4742__auto__.push((arguments[i__4737__auto___38662]));

var G__38663 = (i__4737__auto___38662 + (1));
i__4737__auto___38662 = G__38663;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.team.create_invite_token_link.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.team.create_invite_token_link.cljs$core$IFn$_invoke$arity$variadic = (function (create_token_link,p__38565){
var vec__38566 = p__38565;
var cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38566,(0),null);
if(cljs.core.truth_(create_token_link)){
return oc.web.api.handle_invite_link(create_token_link,(function (p__38569){
var map__38570 = p__38569;
var map__38570__$1 = (((((!((map__38570 == null))))?(((((map__38570.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38570.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38570):map__38570);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38570__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38570__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38570__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"team-loaded","team-loaded",1150677177),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.json.json__GT_cljs(body)], null));

if(cljs.core.fn_QMARK_(cb)){
return (cb.cljs$core$IFn$_invoke$arity$1 ? cb.cljs$core$IFn$_invoke$arity$1(success) : cb.call(null,success));
} else {
return null;
}
} else {
return null;
}
}));
} else {
return null;
}
}));

(oc.web.actions.team.create_invite_token_link.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.team.create_invite_token_link.cljs$lang$applyTo = (function (seq38563){
var G__38564 = cljs.core.first(seq38563);
var seq38563__$1 = cljs.core.next(seq38563);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38564,seq38563__$1);
}));

oc.web.actions.team.delete_invite_token_link = (function oc$web$actions$team$delete_invite_token_link(var_args){
var args__4742__auto__ = [];
var len__4736__auto___38666 = arguments.length;
var i__4737__auto___38667 = (0);
while(true){
if((i__4737__auto___38667 < len__4736__auto___38666)){
args__4742__auto__.push((arguments[i__4737__auto___38667]));

var G__38668 = (i__4737__auto___38667 + (1));
i__4737__auto___38667 = G__38668;
continue;
} else {
}
break;
}

var argseq__4743__auto__ = ((((1) < args__4742__auto__.length))?(new cljs.core.IndexedSeq(args__4742__auto__.slice((1)),(0),null)):null);
return oc.web.actions.team.delete_invite_token_link.cljs$core$IFn$_invoke$arity$variadic((arguments[(0)]),argseq__4743__auto__);
});

(oc.web.actions.team.delete_invite_token_link.cljs$core$IFn$_invoke$arity$variadic = (function (delete_invite_link,p__38574){
var vec__38575 = p__38574;
var cb = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__38575,(0),null);
if(cljs.core.truth_(delete_invite_link)){
return oc.web.api.handle_invite_link(delete_invite_link,(function (p__38578){
var map__38579 = p__38578;
var map__38579__$1 = (((((!((map__38579 == null))))?(((((map__38579.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__38579.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__38579):map__38579);
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38579__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38579__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var status = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__38579__$1,new cljs.core.Keyword(null,"status","status",-1997798413));
if(cljs.core.truth_(success)){
oc.web.dispatcher.dispatch_BANG_(new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"team-loaded","team-loaded",1150677177),oc.web.dispatcher.current_org_slug.cljs$core$IFn$_invoke$arity$0(),oc.web.lib.json.json__GT_cljs(body)], null));

if(cljs.core.fn_QMARK_(cb)){
return (cb.cljs$core$IFn$_invoke$arity$1 ? cb.cljs$core$IFn$_invoke$arity$1(success) : cb.call(null,success));
} else {
return null;
}
} else {
return null;
}
}));
} else {
return null;
}
}));

(oc.web.actions.team.delete_invite_token_link.cljs$lang$maxFixedArity = (1));

/** @this {Function} */
(oc.web.actions.team.delete_invite_token_link.cljs$lang$applyTo = (function (seq38572){
var G__38573 = cljs.core.first(seq38572);
var seq38572__$1 = cljs.core.next(seq38572);
var self__4723__auto__ = this;
return self__4723__auto__.cljs$core$IFn$_invoke$arity$variadic(G__38573,seq38572__$1);
}));


//# sourceMappingURL=oc.web.actions.team.js.map
