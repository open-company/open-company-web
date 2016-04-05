function SVGgetWidth(el) {
  console.log(el, typeof el, el.node().getBBox, el.node().bounds);
  if (typeof el !== "undefined"){
    el.node().getBBox().width;
  }else{
    return null;
  }
}