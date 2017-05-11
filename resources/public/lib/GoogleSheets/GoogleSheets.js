function getViewportWidth(){
  return Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
}
function getViewportHeight(){
  return Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
}
document.addEventListener("DOMContentLoaded", function(event) {
  window.addEventListener("resize", function(event) {
    document.body.classList.add("loading");
    if(window.onNumberFormatApiLoad !== undefined){
      onNumberFormatApiLoad();
    }
  });
});