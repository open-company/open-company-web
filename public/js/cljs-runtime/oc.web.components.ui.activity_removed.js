goog.provide('oc.web.components.ui.activity_removed');
oc.web.components.ui.activity_removed.close_clicked = (function oc$web$components$ui$activity_removed$close_clicked(e){
return oc.web.actions.nav_sidebar.nav_to_url_BANG_.cljs$core$IFn$_invoke$arity$3(e,oc.web.urls.default_board_slug,oc.web.urls.default_landing.cljs$core$IFn$_invoke$arity$0());
});
oc.web.components.ui.activity_removed.activity_removed = rum.core.build_defc((function (){
return React.createElement("div",({"className": "activity-removed-container"}),React.createElement("div",({"className": "activity-removed"}),React.createElement("button",({"onClick": oc.web.components.ui.activity_removed.close_clicked, "className": "settings-modal-close mlb-reset"})),React.createElement("div",({"className": "activity-removed-mobile-header"}),React.createElement("button",({"onClick": oc.web.components.ui.activity_removed.close_clicked, "className": "mlb-reset activity-removed-mobile-close"})),React.createElement("div",({"className": "activity-removed-mobile-header-logo"}))),React.createElement("div",({"className": "activity-removed-icon"})),React.createElement("div",({"className": "activity-removed-title"}),"Post not available"),React.createElement("div",({"className": "activity-removed-description"}),"Looks like you don\u2019t have access to the post or it doesn\u2019t exist."),React.createElement("button",({"onClick": oc.web.components.ui.activity_removed.close_clicked, "className": "mlb-reset activity-removed-button"}),"Return to recent")));
}),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [rum.core.static$,oc.web.mixins.ui.no_scroll_mixin], null),"activity-removed");

//# sourceMappingURL=oc.web.components.ui.activity_removed.js.map
