var electron = require('electron');
var ipcRenderer = electron.ipcRenderer;

console.log("Carrot desktop engage!");

window.isDesktop = true;

window.setBadgeCount = function(count) {
  ipcRenderer.send('set-badge-count', count);
};
