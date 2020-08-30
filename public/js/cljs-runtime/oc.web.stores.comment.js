goog.provide('oc.web.stores.comment');
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("add-comment","reply","add-comment/reply",-2059207478),(function (db,p__42791){
var vec__42792 = p__42791;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42792,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42792,(1),null);
var focus_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42792,(2),null);
var reply_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42792,(3),null);
var t = db;
var t__$1 = cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(t,oc.web.dispatcher.comment_reply_to_key(org_slug),cljs.core.merge,cljs.core.PersistentArrayMap.createAsIfByAssoc([focus_value,reply_data]));
if(cljs.core.truth_(new cljs.core.Keyword(null,"focus","focus",234677911).cljs$core$IFn$_invoke$arity$1(reply_data))){
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(t__$1,new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461),focus_value);
} else {
return t__$1;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("add-comment","reset-reply","add-comment/reset-reply",558424878),(function (db,p__42795){
var vec__42796 = p__42795;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42796,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42796,(1),null);
var focus_value = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42796,(2),null);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,oc.web.dispatcher.comment_reply_to_key(org_slug),cljs.core.dissoc,focus_value);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"add-comment-change","add-comment-change",1301937897),(function (db,p__42801){
var vec__42802 = p__42801;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42802,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42802,(1),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42802,(2),null);
var parent_comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42802,(3),null);
var comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42802,(4),null);
var comment_body = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42802,(5),null);
var force_update_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42802,(6),null);
var comment_key = oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3(activity_uuid,parent_comment_uuid,comment_uuid);
var add_comment_activity_key = oc.web.dispatcher.add_comment_activity_key(org_slug,comment_key);
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(db,add_comment_activity_key,comment_body),oc.web.dispatcher.add_comment_force_update_key(comment_key),(function (p1__42799_SHARP_){
if(cljs.core.truth_(force_update_QMARK_)){
return oc.web.lib.utils.activity_uuid();
} else {
return p1__42799_SHARP_;
}
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"add-comment-reset","add-comment-reset",130264416),(function (db,p__42807){
var vec__42808 = p__42807;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42808,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42808,(1),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42808,(2),null);
var parent_comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42808,(3),null);
var comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42808,(4),null);
var add_comment_key = oc.web.dispatcher.add_comment_key(org_slug);
var comment_key = oc.web.dispatcher.add_comment_string_key.cljs$core$IFn$_invoke$arity$3(activity_uuid,parent_comment_uuid,comment_uuid);
var add_comment_activity_key = oc.web.dispatcher.add_comment_activity_key(org_slug,comment_key);
return cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,add_comment_key,cljs.core.dissoc,comment_key),oc.web.dispatcher.add_comment_force_update_key(comment_key),oc.web.lib.utils.activity_uuid());
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461),(function (db,p__42811){
var vec__42812 = p__42811;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42812,(0),null);
var focus_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42812,(1),null);
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461),focus_uuid);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"add-comment-blur","add-comment-blur",1415256628),(function (db,p__42816){
var vec__42817 = p__42816;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42817,(0),null);
var focus_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42817,(1),null);
return cljs.core.update.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"add-comment-focus","add-comment-focus",-452934461),(function (p1__42815_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(focus_uuid,p1__42815_SHARP_)){
return null;
} else {
return p1__42815_SHARP_;
}
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"comment-add","comment-add",1843969593),(function (db,p__42820){
var vec__42821 = p__42820;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42821,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42821,(1),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42821,(2),null);
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42821,(3),null);
var parent_comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42821,(4),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42821,(5),null);
var activity_key = oc.web.dispatcher.activity_key(org_slug,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(activity_data));
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var comments_data = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var new_comment_data = oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug),activity_data,comment_data], 0));
var all_comments = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(comments_data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new_comment_data], null));
var sorted_comments = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([all_comments], 0));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(cljs.core.assoc_in(cljs.core.assoc_in(db,sorted_comments_key,sorted_comments),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(activity_key,new cljs.core.Keyword(null,"new-comments-count","new-comments-count",46784695)),(0)),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(activity_key,new cljs.core.Keyword(null,"unseen-comments","unseen-comments",-793262869)),false),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(activity_key,new cljs.core.Keyword(null,"links","links",-654507394)),(function (links){
return cljs.core.mapv.cljs$core$IFn$_invoke$arity$2((function (link){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(link),"follow")){
return cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([link,new cljs.core.PersistentArrayMap(null, 2, [new cljs.core.Keyword(null,"href","href",-793805698),cuerdas.core.replace(new cljs.core.Keyword(null,"href","href",-793805698).cljs$core$IFn$_invoke$arity$1(link),/\/follow\/?$/,"/unfollow/"),new cljs.core.Keyword(null,"rel","rel",1378823488),"unfollow"], null)], 0));
} else {
return link;
}
}),links);
}));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("comment-add","replace","comment-add/replace",762118999),(function (db,p__42825){
var vec__42826 = p__42825;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42826,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42826,(1),null);
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42826,(2),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42826,(3),null);
var new_comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42826,(4),null);
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var comments_data = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var old_comments_data = cljs.core.filterv((function (p1__42824_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42824_SHARP_),new_comment_uuid);
}),comments_data);
var new_comment_data = oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$1(db),activity_data,comment_data], 0));
var all_comments = cljs.core.concat.cljs$core$IFn$_invoke$arity$2(old_comments_data,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new_comment_data], null));
var sorted_all_comments = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([all_comments], 0));
return cljs.core.assoc_in(db,sorted_comments_key,sorted_all_comments);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("comment-add","finish","comment-add/finish",1539262095),(function (db,p__42869){
var vec__42870 = p__42869;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42870,(0),null);
var map__42873 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42870,(1),null);
var map__42873__$1 = (((((!((map__42873 == null))))?(((((map__42873.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42873.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42873):map__42873);
var activity_data = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42873__$1,new cljs.core.Keyword(null,"activity-data","activity-data",1293689136));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42873__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
return cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(db,new cljs.core.Keyword(null,"comment-add-finish","comment-add-finish",-1383135832),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("comment-add","failed","comment-add/failed",828640623),(function (db,p__42884){
var vec__42885 = p__42884;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42885,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42885,(1),null);
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42885,(2),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42885,(3),null);
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var all_comments = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var filtered_comments = cljs.core.filterv((function (p1__42883_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42883_SHARP_));
}),all_comments);
var sorted_filtered_comments = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([filtered_comments], 0));
return cljs.core.assoc_in(db,sorted_comments_key,sorted_filtered_comments);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("comment-save","failed","comment-save/failed",-1040739977),(function (db,p__42893){
var vec__42894 = p__42893;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42894,(0),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42894,(1),null);
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42894,(2),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42894,(3),null);
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var all_comments = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var filtered_comments = cljs.core.filterv((function (p1__42892_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42892_SHARP_));
}),all_comments);
var sorted_filtered_comments = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.conj.cljs$core$IFn$_invoke$arity$2(filtered_comments,comment_data)], 0));
return cljs.core.assoc_in(db,sorted_comments_key,sorted_filtered_comments);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"comments-get","comments-get",477068556),(function (db,p__42898){
var vec__42899 = p__42898;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42899,(0),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42899,(1),null);
var activity_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42899,(2),null);
return cljs.core.assoc_in(db,cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,new cljs.core.Keyword(null,"loading","loading",-737050189))),true);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("comments-get","finish","comments-get/finish",-1883926059),(function (db,p__42911){
var vec__42912 = p__42911;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42912,(0),null);
var map__42915 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42912,(1),null);
var map__42915__$1 = (((((!((map__42915 == null))))?(((((map__42915.cljs$lang$protocol_mask$partition0$ & (64))) || ((cljs.core.PROTOCOL_SENTINEL === map__42915.cljs$core$ISeq$))))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__42915):map__42915);
var success = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42915__$1,new cljs.core.Keyword(null,"success","success",1890645906));
var error = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42915__$1,new cljs.core.Keyword(null,"error","error",-978969032));
var comments_key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42915__$1,new cljs.core.Keyword(null,"comments-key","comments-key",-1941842753));
var body = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42915__$1,new cljs.core.Keyword(null,"body","body",-2049205669));
var secure_activity_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42915__$1,new cljs.core.Keyword(null,"secure-activity-uuid","secure-activity-uuid",597625027));
var activity_uuid = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__42915__$1,new cljs.core.Keyword(null,"activity-uuid","activity-uuid",-663317778));
if(cljs.core.truth_(success)){
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$1(db);
var activity_data = (cljs.core.truth_(secure_activity_uuid)?oc.web.dispatcher.secure_activity_data.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),secure_activity_uuid,db):oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),activity_uuid,db));
var cleaned_comments = cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__42902_SHARP_){
return oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,activity_data,p1__42902_SHARP_], 0));
}),new cljs.core.Keyword(null,"items","items",1031954938).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(body)));
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var sorted_comments = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cleaned_comments], 0));
return cljs.core.assoc_in(cljs.core.assoc_in(db,sorted_comments_key,sorted_comments),cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,new cljs.core.Keyword(null,"loading","loading",-737050189))),false);
} else {
return cljs.core.assoc_in(db,cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,new cljs.core.Keyword(null,"loading","loading",-737050189))),false);
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"comment-delete","comment-delete",-1631742525),(function (db,p__42933){
var vec__42934 = p__42933;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42934,(0),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42934,(1),null);
var comment_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42934,(2),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42934,(3),null);
var item_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(comment_data);
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var comments_data = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var new_comments_data = cljs.core.filterv((function (p1__42932_SHARP_){
return ((cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(item_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42932_SHARP_))) && (cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(item_uuid,new cljs.core.Keyword(null,"parent-uuid","parent-uuid",-2003485227).cljs$core$IFn$_invoke$arity$1(p1__42932_SHARP_))));
}),comments_data);
var sorted_comments = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new_comments_data], 0));
return cljs.core.assoc_in(db,sorted_comments_key,sorted_comments);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"comment-reaction-toggle","comment-reaction-toggle",-1399257510),(function (db,p__42940){
var vec__42941 = p__42940;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42941,(0),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42941,(1),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42941,(2),null);
var comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42941,(3),null);
var reaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42941,(4),null);
var reacting_QMARK_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42941,(5),null);
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var comments_data = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var comment_data = cljs.core.some((function (p1__42937_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(comment_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42937_SHARP_))){
return p1__42937_SHARP_;
} else {
return null;
}
}),comments_data);
if(cljs.core.truth_(comment_data)){
var reactions_data = new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(comment_data);
var reaction = new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(reaction_data);
var reaction_idx = oc.web.lib.utils.index_of(reactions_data,(function (p1__42938_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(p1__42938_SHARP_),reaction);
}));
var reacted_QMARK_ = cljs.core.not(new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(reaction_data));
var old_link = cljs.core.first(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reaction_data));
var new_link = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_link,new cljs.core.Keyword(null,"method","method",55703592),((reacted_QMARK_)?"DELETE":"PUT"));
var with_new_link = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reaction_data,new cljs.core.Keyword(null,"links","links",-654507394),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new_link], null));
var with_new_reacted = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_new_link,new cljs.core.Keyword(null,"reacted","reacted",523485502),reacted_QMARK_);
var new_count = ((reacted_QMARK_)?(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data) + (1)):(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data) - (1)));
var new_reaction_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_new_reacted,new cljs.core.Keyword(null,"count","count",2139924085),new_count);
var new_reactions_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reactions_data,reaction_idx,new_reaction_data);
var new_comment_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(comment_data,new cljs.core.Keyword(null,"reactions","reactions",2029850654),new_reactions_data);
var new_comments_data = cljs.core.cons(new_comment_data,cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__42939_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42939_SHARP_),comment_uuid);
}),comments_data));
var new_sorted_comments_data = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new_comments_data], 0));
return cljs.core.assoc_in(db,sorted_comments_key,new_sorted_comments_data);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"comment-react-from-picker","comment-react-from-picker",2131407811),(function (db,p__42947){
var vec__42949 = p__42947;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42949,(0),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42949,(1),null);
var activity_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42949,(2),null);
var comment_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42949,(3),null);
var reaction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42949,(4),null);
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var comments_data = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var comment_data = cljs.core.some((function (p1__42944_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42944_SHARP_),comment_uuid)){
return p1__42944_SHARP_;
} else {
return null;
}
}),comments_data);
if(cljs.core.truth_(comment_data)){
var reactions_data = new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(comment_data);
var reaction_idx = oc.web.lib.utils.index_of(reactions_data,(function (p1__42945_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(p1__42945_SHARP_),reaction);
}));
var reaction_data = (cljs.core.truth_(reaction_idx)?cljs.core.get.cljs$core$IFn$_invoke$arity$2(reactions_data,reaction_idx):null);
var reacted_QMARK_ = (cljs.core.truth_(reaction_data)?cljs.core.not(new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(reaction_data)):true);
var old_link = (cljs.core.truth_(reaction_data)?cljs.core.first(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(reaction_data)):null);
var new_link = (cljs.core.truth_(old_link)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(old_link,new cljs.core.Keyword(null,"method","method",55703592),((reacted_QMARK_)?"DELETE":"PUT")):null);
var new_count = ((reacted_QMARK_)?(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data) + (1)):(new cljs.core.Keyword(null,"count","count",2139924085).cljs$core$IFn$_invoke$arity$1(reaction_data) - (1)));
var new_reaction_data = new cljs.core.PersistentArrayMap(null, 4, [new cljs.core.Keyword(null,"links","links",-654507394),new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new_link], null),new cljs.core.Keyword(null,"reacted","reacted",523485502),reacted_QMARK_,new cljs.core.Keyword(null,"reaction","reaction",490869788),reaction,new cljs.core.Keyword(null,"count","count",2139924085),new_count], null);
var new_reactions_data = (cljs.core.truth_(reaction_idx)?cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(reactions_data,reaction_idx,new_reaction_data):cljs.core.conj.cljs$core$IFn$_invoke$arity$2(reactions_data,new_reaction_data));
var new_comment_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(comment_data,new cljs.core.Keyword(null,"reactions","reactions",2029850654),new_reactions_data);
var new_comments_data = cljs.core.cons(new_comment_data,cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__42946_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42946_SHARP_),comment_uuid);
}),comments_data));
var new_sorted_comments_data = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new_comments_data], 0));
return cljs.core.assoc_in(db,sorted_comments_key,new_sorted_comments_data);
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"comment-save","comment-save",-691862293),(function (db,p__42981){
var vec__42982 = p__42981;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42982,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42982,(1),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42982,(2),null);
var updated_comment_map_STAR_ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42982,(3),null);
var updated_comment_map = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(updated_comment_map_STAR_,new cljs.core.Keyword(null,"thread-children","thread-children",78675219));
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var all_comments = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var filtered_comments = cljs.core.filterv((function (p1__42980_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42980_SHARP_),new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(updated_comment_map));
}),all_comments);
var sorted_new_comments = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.conj.cljs$core$IFn$_invoke$arity$2(filtered_comments,updated_comment_map)], 0));
return cljs.core.assoc_in(db,sorted_comments_key,sorted_new_comments);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("ws-interaction","comment-update","ws-interaction/comment-update",-1083818759),(function (db,p__42987){
var vec__42988 = p__42987;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42988,(0),null);
var comments_key = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42988,(1),null);
var interaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__42988,(2),null);
var activity_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$1(db);
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),activity_uuid,db);
var ws_comment_data = new cljs.core.Keyword(null,"interaction","interaction",-2143888916).cljs$core$IFn$_invoke$arity$1(interaction_data);
var item_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(ws_comment_data);
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var comments_data = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var comment_data = cljs.core.some((function (p1__42985_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(item_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42985_SHARP_))){
return p1__42985_SHARP_;
} else {
return null;
}
}),comments_data);
if(cljs.core.truth_(comment_data)){
if((oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(comment_data)], 0)) <= oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(comment_data)], 0)))){
var body_comment_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(comment_data,new cljs.core.Keyword(null,"body","body",-2049205669),new cljs.core.Keyword(null,"body","body",-2049205669).cljs$core$IFn$_invoke$arity$1(ws_comment_data));
var update_comment_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(body_comment_data,new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336),new cljs.core.Keyword(null,"updated-at","updated-at",-1592622336).cljs$core$IFn$_invoke$arity$1(ws_comment_data));
var new_comment_data = ((cljs.core.contains_QMARK_(update_comment_data,new cljs.core.Keyword(null,"reactions","reactions",2029850654)))?update_comment_data:cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(update_comment_data,new cljs.core.Keyword(null,"reactions","reactions",2029850654),new cljs.core.Keyword(null,"reactions","reactions",2029850654).cljs$core$IFn$_invoke$arity$1(ws_comment_data)));
var new_comments_data = cljs.core.cons(new_comment_data,cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__42986_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__42986_SHARP_),item_uuid);
}),comments_data));
var new_sorted_comments_data = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new_comments_data], 0));
return cljs.core.assoc_in(db,sorted_comments_key,new_sorted_comments_data);
} else {
return db;
}
} else {
return db;
}
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("ws-interaction","comment-delete","ws-interaction/comment-delete",-908789150),(function (db,p__43015){
var vec__43016 = p__43015;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43016,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43016,(1),null);
var interaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43016,(2),null);
var activity_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var item_uuid = new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(new cljs.core.Keyword(null,"interaction","interaction",-2143888916).cljs$core$IFn$_invoke$arity$1(interaction_data));
var last_read_at = new cljs.core.Keyword(null,"last-read-at","last-read-at",-216601930).cljs$core$IFn$_invoke$arity$1(oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(org_slug,activity_uuid,db));
var comments_key = oc.web.dispatcher.activity_comments_key(org_slug,activity_uuid);
var sorted_comments_key = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(comments_key,oc.web.dispatcher.sorted_comments_key));
var comments_data = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var deleting_comment_data = cljs.core.some((function (p1__43012_SHARP_){
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43012_SHARP_),item_uuid)){
return p1__43012_SHARP_;
} else {
return null;
}
}),comments_data);
var current_user_id = oc.web.lib.jwt.user_id();
var deleting_new_comment_QMARK_ = (cljs.core.truth_(deleting_comment_data)?oc.web.utils.comment.comment_unread_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([deleting_comment_data,last_read_at], 0)):null);
var new_comments_data = cljs.core.vec(cljs.core.remove.cljs$core$IFn$_invoke$arity$2((function (p1__43013_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(item_uuid,new cljs.core.Keyword(null,"uuid","uuid",-2145095719).cljs$core$IFn$_invoke$arity$1(p1__43013_SHARP_));
}),comments_data));
var new_sorted_comments_data = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new_comments_data], 0));
var last_not_own_comment = cljs.core.last(cljs.core.sort_by.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"created-at","created-at",-89248644),cljs.core.filterv((function (p1__43014_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__43014_SHARP_),current_user_id);
}),new_comments_data)));
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(cljs.core.assoc_in(db,sorted_comments_key,new_sorted_comments_data),oc.web.dispatcher.activity_key(org_slug,activity_uuid),cljs.core.merge,new cljs.core.PersistentArrayMap(null, 1, [new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(last_not_own_comment)], null)),cljs.core.conj.cljs$core$IFn$_invoke$arity$2(oc.web.dispatcher.activity_key(org_slug,activity_uuid),new cljs.core.Keyword(null,"new-comments-count","new-comments-count",46784695)),(cljs.core.truth_(deleting_new_comment_QMARK_)?cljs.core.dec:cljs.core.identity));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("ws-interaction","comment-add","ws-interaction/comment-add",-1026147104),(function (db,p__43029){
var vec__43030 = p__43029;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43030,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43030,(1),null);
var entry_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43030,(2),null);
var interaction_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__43030,(3),null);
if(cljs.core.truth_(entry_data)){
var activity_uuid = new cljs.core.Keyword(null,"resource-uuid","resource-uuid",1798351789).cljs$core$IFn$_invoke$arity$1(interaction_data);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var activity_data = oc.web.dispatcher.activity_data.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"slug","slug",2029314850).cljs$core$IFn$_invoke$arity$1(org_data),activity_uuid,db);
var comment_data = oc.web.utils.activity.parse_comment.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([org_data,activity_data,new cljs.core.Keyword(null,"interaction","interaction",-2143888916).cljs$core$IFn$_invoke$arity$1(interaction_data)], 0));
var created_at = new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data);
var comments_link_idx = oc.web.lib.utils.index_of(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),(function (p1__43024_SHARP_){
return ((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"rel","rel",1378823488).cljs$core$IFn$_invoke$arity$1(p1__43024_SHARP_),"comments")) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"method","method",55703592).cljs$core$IFn$_invoke$arity$1(p1__43024_SHARP_),"GET")));
}));
var with_increased_count = cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(activity_data,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"links","links",-654507394),comments_link_idx,new cljs.core.Keyword(null,"count","count",2139924085)], null),cljs.core.inc);
var old_authors = (function (){var or__4126__auto__ = new cljs.core.Keyword(null,"authors","authors",2063018172).cljs$core$IFn$_invoke$arity$1(cljs.core.get.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(activity_data),comments_link_idx));
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return cljs.core.PersistentVector.EMPTY;
}
})();
var new_author = new cljs.core.Keyword(null,"author","author",2111686192).cljs$core$IFn$_invoke$arity$1(comment_data);
var new_authors = (cljs.core.truth_((function (){var and__4115__auto__ = old_authors;
if(cljs.core.truth_(and__4115__auto__)){
return cljs.core.some((function (p1__43025_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(p1__43025_SHARP_),new cljs.core.Keyword(null,"user-id","user-id",-206822291).cljs$core$IFn$_invoke$arity$1(new_author));
}),old_authors);
} else {
return and__4115__auto__;
}
})())?old_authors:cljs.core.concat.cljs$core$IFn$_invoke$arity$2(new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [new_author], null),old_authors));
var with_authors = cljs.core.assoc_in(with_increased_count,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"links","links",-654507394),comments_link_idx,new cljs.core.Keyword(null,"authors","authors",2063018172)], null),new_authors);
var old_comments_count = new cljs.core.Keyword(null,"new-comments-count","new-comments-count",46784695).cljs$core$IFn$_invoke$arity$1(activity_data);
var new_comments_count = ((((cljs.core.not(new cljs.core.Keyword(null,"author?","author?",-1083349935).cljs$core$IFn$_invoke$arity$1(comment_data))) && ((oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998).cljs$core$IFn$_invoke$arity$1(activity_data)], 0)).getTime() > oc.web.lib.utils.js_date.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(comment_data)], 0)).getTime()))))?(old_comments_count + (1)):old_comments_count);
var with_last_activity_at = (cljs.core.truth_(new cljs.core.Keyword(null,"author?","author?",-1083349935).cljs$core$IFn$_invoke$arity$1(comment_data))?with_authors:cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(with_authors,new cljs.core.Keyword(null,"last-activity-at","last-activity-at",670511998),created_at),new cljs.core.Keyword(null,"new-comments-count","new-comments-count",46784695),new_comments_count));
var sorted_comments_key = oc.web.dispatcher.activity_sorted_comments_key(org_slug,activity_uuid);
var all_old_comments_data = oc.web.utils.comment.ungroup_comments(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,sorted_comments_key));
var replies_badge_key = oc.web.dispatcher.replies_badge_key(org_slug);
var follow_boards_list = oc.web.dispatcher.follow_boards_list.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var follow_boards_set = cljs.core.set(cljs.core.map.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"uuid","uuid",-2145095719),follow_boards_list));
var replies_data = oc.web.dispatcher.replies_data.cljs$core$IFn$_invoke$arity$2(org_slug,db);
var should_badge_replies_QMARK_ = ((cljs.core.not(new cljs.core.Keyword(null,"author?","author?",-1083349935).cljs$core$IFn$_invoke$arity$1(comment_data)))?(function (){var and__4115__auto__ = oc.web.lib.utils.link_for.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(with_last_activity_at),"unfollow"], 0));
if(cljs.core.truth_(and__4115__auto__)){
return oc.web.utils.comment.comment_unseen_QMARK_.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([comment_data,new cljs.core.Keyword(null,"last-seen-at","last-seen-at",1929467667).cljs$core$IFn$_invoke$arity$1(replies_data)], 0));
} else {
return and__4115__auto__;
}
})():false);
if(cljs.core.truth_(all_old_comments_data)){
var old_comments_data = cljs.core.filterv(new cljs.core.Keyword(null,"links","links",-654507394),all_old_comments_data);
var new_comments_data = cljs.core.vec(cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__43026_SHARP_){
return cljs.core.not_EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"created-at","created-at",-89248644).cljs$core$IFn$_invoke$arity$1(p1__43026_SHARP_),created_at);
}),old_comments_data),comment_data));
var new_sorted_comments_data = oc.web.utils.comment.sort_comments.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([new_comments_data], 0));
return cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(db,sorted_comments_key,new_sorted_comments_data),replies_badge_key,(function (p1__43027_SHARP_){
var or__4126__auto__ = should_badge_replies_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return p1__43027_SHARP_;
}
})),oc.web.dispatcher.activity_key(org_slug,activity_uuid),with_last_activity_at);
} else {
return cljs.core.update_in.cljs$core$IFn$_invoke$arity$3(cljs.core.assoc_in(db,oc.web.dispatcher.activity_key(org_slug,activity_uuid),with_last_activity_at),replies_badge_key,(function (p1__43028_SHARP_){
var or__4126__auto__ = should_badge_replies_QMARK_;
if(cljs.core.truth_(or__4126__auto__)){
return or__4126__auto__;
} else {
return p1__43028_SHARP_;
}
}));
}
} else {
return db;
}
}));

//# sourceMappingURL=oc.web.stores.comment.js.map
