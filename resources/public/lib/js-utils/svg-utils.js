function SVGgetWidth(el) {
  if (typeof el !== "undefined"){
    el.getBBox().width;
  }else{
    return null;
  }
}