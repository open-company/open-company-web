goog.provide('oc.web.stores.contributions');
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("contributions-get","finish","contributions-get/finish",1354948902),(function (db,p__44150){
var vec__44151 = p__44150;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44151,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44151,(1),null);
var author_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44151,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44151,(3),null);
var contrib_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44151,(4),null);
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var prepare_container_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(new cljs.core.Keyword(null,"collection","collection",-683361892).cljs$core$IFn$_invoke$arity$1(contrib_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"contributions","contributions",-1280485964));
var fixed_contrib_data = oc.web.utils.activity.parse_contributions(prepare_container_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db),org_data,oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db),oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(org_slug,db),sort_type);
var contrib_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,author_uuid,sort_type);
var posts_key = oc.web.dispatcher.posts_data_key(org_slug);
var merged_items = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_key),new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_contrib_data)], 0));
return cljs.core.assoc_in(cljs.core.update_in.cljs$core$IFn$_invoke$arity$4(db,posts_key,cljs.core.merge,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_contrib_data)),contrib_data_key,cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(fixed_contrib_data,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159)));
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword(null,"contributions-more","contributions-more",1062383218),(function (db,p__44154){
var vec__44155 = p__44154;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44155,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44155,(1),null);
var author_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44155,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44155,(3),null);
var contrib_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,author_uuid,sort_type);
var contrib_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,contrib_data_key);
var next_contrib_data = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(contrib_data,new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667),true);
return cljs.core.assoc_in(db,contrib_data_key,next_contrib_data);
}));
oc.web.dispatcher.action.cljs$core$IMultiFn$_add_method$arity$3(null,new cljs.core.Keyword("contributions-more","finish","contributions-more/finish",1981208147),(function (db,p__44158){
var vec__44159 = p__44158;
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44159,(0),null);
var org_slug = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44159,(1),null);
var author_uuid = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44159,(2),null);
var sort_type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44159,(3),null);
var direction = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44159,(4),null);
var next_contrib_data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__44159,(5),null);
if(cljs.core.truth_(next_contrib_data)){
var contrib_data_key = oc.web.dispatcher.contributions_data_key.cljs$core$IFn$_invoke$arity$3(org_slug,author_uuid,sort_type);
var contrib_data = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,contrib_data_key);
var posts_data_key = oc.web.dispatcher.posts_data_key(org_slug);
var old_posts = cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(db,posts_data_key);
var prepare_contrib_data = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([next_contrib_data,new cljs.core.PersistentArrayMap(null, 3, [new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897),new cljs.core.Keyword(null,"posts-list","posts-list",-1847853897).cljs$core$IFn$_invoke$arity$1(contrib_data),new cljs.core.Keyword(null,"old-links","old-links",340078773),new cljs.core.Keyword(null,"links","links",-654507394).cljs$core$IFn$_invoke$arity$1(contrib_data),new cljs.core.Keyword(null,"container-slug","container-slug",365736492),new cljs.core.Keyword(null,"contributions","contributions",-1280485964)], null)], 0));
var org_data = oc.web.dispatcher.org_data.cljs$core$IFn$_invoke$arity$2(db,org_slug);
var fixed_contrib_data = oc.web.utils.activity.parse_contributions.cljs$core$IFn$_invoke$arity$variadic(prepare_contrib_data,oc.web.dispatcher.change_data.cljs$core$IFn$_invoke$arity$1(db),org_data,oc.web.dispatcher.active_users.cljs$core$IFn$_invoke$arity$2(org_slug,db),oc.web.dispatcher.follow_publishers_list.cljs$core$IFn$_invoke$arity$2(org_slug,db),sort_type,cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([direction], 0));
var new_items_map = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.prim_seq.cljs$core$IFn$_invoke$arity$2([old_posts,new cljs.core.Keyword(null,"fixed-items","fixed-items",-1848672159).cljs$core$IFn$_invoke$arity$1(fixed_contrib_data)], 0));
var new_contrib_data = cljs.core.dissoc.cljs$core$IFn$_invoke$arity$2(cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(fixed_contrib_data,new cljs.core.Keyword(null,"direction","direction",-633359395),direction),new cljs.core.Keyword(null,"loading-more","loading-more",-1145525667));
return cljs.core.assoc_in(cljs.core.assoc_in(db,contrib_data_key,new_contrib_data),posts_data_key,new_items_map);
} else {
return db;
}
}));

//# sourceMappingURL=oc.web.stores.contributions.js.map
