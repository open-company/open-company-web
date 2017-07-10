if (getParameterByName("path")) {
  history.pushState(null, "Page could not be found", getParameterByName("path"));
}