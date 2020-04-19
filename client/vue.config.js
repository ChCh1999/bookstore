module.exports = {
  devServer: {
    proxy: {
      '/server': {
        target: "http://localhost:8080",
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/server': ''
        }
      }
    }
  }
}
