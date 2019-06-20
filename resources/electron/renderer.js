var electron = require('electron');
var ipcRenderer = electron.ipcRenderer;

console.log("Carrot desktop engage!");

window.isDesktop = true;

window.setBadgeCount = function(count) {
  console.log("Sending badge count IPC: " + count);
  ipcRenderer.send('set-badge-count', count);
};
