// 登录态存储（演示级，localStorage；见 docs/02 第6节）

const TOKEN_KEY = 'jsa_token'
const USER_KEY = 'jsa_user'

export function saveAuth(token, user) {
  localStorage.setItem(TOKEN_KEY, token)
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function getUser() {
  const raw = localStorage.getItem(USER_KEY)
  return raw ? JSON.parse(raw) : null
}

export function isAdmin() {
  const u = getUser()
  return u && u.role === 'ADMIN'
}

export function clearAuth() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}
