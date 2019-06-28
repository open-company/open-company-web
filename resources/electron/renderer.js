// The renderer process of the Carrot electron app
// https://electronjs.org/docs/tutorial/application-architecture#main-and-renderer-processes
var electron = require('electron');
var ipcRenderer = electron.ipcRenderer;

console.log("Carrot desktop engage!");

// By attaching fields and functions to the window object,
// we allow the web page to access a controlled set of native
// desktop funcitonality.

window.isDesktop = true;

window.setBadgeCount = function(count) {
  console.log("Sending badge count IPC: " + count);
  ipcRenderer.send('set-badge-count', count);
};
