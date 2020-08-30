goog.provide('oc.web.utils.reaction');
/**
 * Given an emoji and the list of the current reactions
 * check if the user can react.
 * A user can react if:
 * - the reaction is NOT already in the reactions list
 * - the reaction is already in the reactions list and its not reacted
 */
oc.web.utils.reaction.can_pick_reaction_QMARK_ = (function oc$web$utils$reaction$can_pick_reaction_QMARK_(emoji,reactions_data){
var reaction_map = cljs.core.first(cljs.core.filter.cljs$core$IFn$_invoke$arity$2((function (p1__52478_SHARP_){
return cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(new cljs.core.Keyword(null,"reaction","reaction",490869788).cljs$core$IFn$_invoke$arity$1(p1__52478_SHARP_),emoji);
}),reactions_data));
return ((cljs.core.not(reaction_map)) || (((cljs.core.map_QMARK_(reaction_map)) && (cljs.core.not(new cljs.core.Keyword(null,"reacted","reacted",523485502).cljs$core$IFn$_invoke$arity$1(reaction_map))))));
});

//# sourceMappingURL=oc.web.utils.reaction.js.map
