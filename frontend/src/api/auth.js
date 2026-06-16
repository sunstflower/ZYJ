import http from './http'

// 登录（见 docs/04 3.1）
export function login(username, password) {
  return http.post('/auth/login', { username, password })
}

// 注册普通用户（见 docs/04 3.7）。成功后端直接返回 token + user（注册即登录）
export function register(username, password, nickname) {
  return http.post('/auth/register', { username, password, nickname })
}
