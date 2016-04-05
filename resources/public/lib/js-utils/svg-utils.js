function SVGgetWidth(el) {
  console.log(el, typeof el);
  if (typeof el !== "undefined"){
    el.getBBox().width;
  }else{
    return null;
  }
}