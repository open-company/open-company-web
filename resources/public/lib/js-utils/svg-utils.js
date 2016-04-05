function SVGgetWidth(el) {
  console.log(el, typeof el, el.getBBox);
  if (typeof el !== "undefined"){
    el.node().getBBox().width;
  }else{
    return null;
  }
}