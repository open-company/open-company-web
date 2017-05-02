function getViewportWidth(){
  return Math.max(document.documentElement.clientWidth, window.innerWidth || 0);
}
function getViewportHeight(){
  return Math.max(document.documentElement.clientHeight, window.innerHeight || 0);
}
document.addEventListener("DOMContentLoaded", function(event) {
  document.body.classList.remove("loading");
  window.addEventListener("resize", function(event) {
    if(window.onNumberFormatApiLoad !== undefined){
      onNumberFormatApiLoad();
    }
  });
});