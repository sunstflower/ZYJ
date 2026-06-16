import http from './http'

// 登录（见 docs/04 3.1）
export function login(username, password) {
  return http.post('/auth/login', { username, password })
}
