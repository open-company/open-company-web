const path = require('path');

module.exports = {
  packagerConfig: {
    icon: path.resolve(__dirname, "carrot.iconset/icon")
  },
  makers: [
    {
      name: "@electron-forge/maker-dmg",
      config: {}
    }
  ]
}
