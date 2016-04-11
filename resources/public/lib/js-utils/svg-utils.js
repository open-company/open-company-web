function SVGgetWidth(el) {
  if (typeof el !== "undefined"){
    return el.node().getBBox().width;
  }else{
    return null;
  }
}