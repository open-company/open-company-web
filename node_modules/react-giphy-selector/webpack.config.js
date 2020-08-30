module.exports = {
  entry: "./src/index.ts",

  output: {
    filename: "index.js",
    path: __dirname + "/lib/",
    library: "ReactGiphySelector",
    libraryTarget: "umd"
  },

  // Enable sourcemaps for debugging webpack's output.
  devtool: "source-map",

  resolve: {
    // Add '.ts' and '.tsx' as resolvable extensions.
    extensions: [".ts", ".tsx", ".js", ".json"]
  },

  module: {
    rules: [
      // All files with a '.ts' or '.tsx' extension will be handled by 'awesome-typescript-loader'.
      { test: /\.tsx?$/, loader: "awesome-typescript-loader" },

      // All output '.js' files will have any sourcemaps re-processed by 'source-map-loader'.
      { enforce: "pre", test: /\.js$/, loader: "source-map-loader" },

      // All css files should be passed through 'style-loader' and 'css-loader'
      {
        test: /\.css$/,
        loader: ["style-loader", "css-loader?sourceMap&modules"]
      },

      // Load png files via 'url-loader'
      {
        test: /\.png$/,
        loader: "url-loader",
        query: { mimetype: "image/png" }
      }
    ]
  }
};
