import axios from 'axios'
import toast from 'react-hot-toast'
import { getToken, clearAuth } from '../store/auth'

// 统一 axios 实例（见 docs/02 前端目录、docs/04 通用约定）
const http = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

// 请求拦截器：自动注入 token
http.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器：拆解统一响应体 { code, message, data }，错误统一 toast
http.interceptors.response.use(
  (response) => {
    const body = response.data
    // 约定 code===200 为成功，直接返回 data
    if (body && typeof body.code !== 'undefined') {
      if (body.code === 200) {
        return body.data
      }
      toast.error(body.message || '请求失败')
      return Promise.reject(body)
    }
    return body
  },
  (error) => {
    const status = error.response?.status
    const msg = error.response?.data?.message || error.message || '网络错误'
    if (status === 401) {
      clearAuth()
      // 401 跳回登录由页面/路由守卫处理
    }
    toast.error(msg)
    return Promise.reject(error)
  },
)

export default http
