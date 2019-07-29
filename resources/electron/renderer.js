// The renderer process of the Carrot electron app
// https://electronjs.org/docs/tutorial/application-architecture#main-and-renderer-processes
var electron = require('electron');
var ipcRenderer = electron.ipcRenderer;

console.log("Carrot desktop engage!");

// By attaching fields and functions to the window object,
// we allow the web page to access a controlled set of native
// desktop funcitonality.

// NOTE: you need to add these fields/functions to externs.js
// in order for them to avoid munging in production builds!

window.isDesktop = true;

window.showDesktopWindow = function() {
  console.log("Sending show-desktop-window IPC");
  ipcRenderer.send('show-desktop-window');
}

window.setBadgeCount = function(count) {
  console.log("Sending set-badge-count IPC: " + count);
  ipcRenderer.send('set-badge-count', count);
};
