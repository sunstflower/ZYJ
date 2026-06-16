import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// 开发态把 /api 代理到后端 8080，规避跨域（见 docs/02 第7节）。
// 演示打包时可改为构建到后端 static 目录同源托管。
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
