if (OCStaticGetParameterByName("path")) {
  history.pushState(null, "Page could not be found", OCStaticGetParameterByName("path"));
}