import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// 开发态把 /api 代理到后端（默认 8080），规避跨域（见 docs/02 第7节）。
// 后端端口被占用（如本机 8080 被 Docker 占用）时，用 VITE_API_TARGET 覆盖，
// 例：VITE_API_TARGET=http://localhost:8081 npm run dev
// 演示打包时可改为构建到后端 static 目录同源托管。
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: process.env.VITE_API_TARGET || 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
