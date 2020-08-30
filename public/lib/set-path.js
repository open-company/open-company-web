if (OCStaticGetParameterByName("path")) {
  history.replaceState(window.history.state, "Page could not be found", OCStaticGetParameterByName("path"));
}