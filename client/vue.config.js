module.exports = {
  lintOnSave: false,
  devServer: {
    proxy: {
      '/server': {
        target: "http://118.31.58.31:8080",
        // target: "http://localhost:8080",
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/server': ''
        }
      }
    }
  }
}
