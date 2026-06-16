import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import toast from 'react-hot-toast'
import { register } from '../api/auth'
import { saveAuth } from '../store/auth'

// 注册页（见 docs/04 3.7）。仅普通用户；注册成功后端直接下发 token，跳用户中心。
export default function Register() {
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [nickname, setNickname] = useState('')
  const [password, setPassword] = useState('')
  const [confirm, setConfirm] = useState('')
  const [loading, setLoading] = useState(false)

  // 前端先做基本校验（与后端 @Valid 规则一致），减少无效请求
  function validate() {
    if (!/^[a-zA-Z0-9_]{4,20}$/.test(username)) {
      toast.error('用户名需为 4-20 位字母、数字或下划线')
      return false
    }
    if (nickname.trim().length < 2 || nickname.trim().length > 20) {
      toast.error('昵称长度需为 2-20 位')
      return false
    }
    if (password.length < 6 || password.length > 20) {
      toast.error('密码长度需为 6-20 位')
      return false
    }
    if (password !== confirm) {
      toast.error('两次输入的密码不一致')
      return false
    }
    return true
  }

  async function handleSubmit(e) {
    e.preventDefault()
    if (!validate()) {
      return
    }
    setLoading(true)
    try {
      const data = await register(username, password, nickname.trim())
      saveAuth(data.token, data.user)
      toast.success('注册成功，已自动登录')
      navigate('/user') // 注册仅产生普通用户
    } catch {
      // 错误（如用户名已存在）已由 http 拦截器 toast
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <form
        onSubmit={handleSubmit}
        className="w-80 bg-white rounded-xl shadow p-8 space-y-5"
      >
        <h1 className="text-xl font-bold text-center text-gray-800">注册新用户</h1>
        <div>
          <label className="block text-sm text-gray-600 mb-1">用户名</label>
          <input
            className="w-full border rounded px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="4-20 位字母/数字/下划线"
          />
        </div>
        <div>
          <label className="block text-sm text-gray-600 mb-1">昵称</label>
          <input
            className="w-full border rounded px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            placeholder="2-20 位，展示用名称"
          />
        </div>
        <div>
          <label className="block text-sm text-gray-600 mb-1">密码</label>
          <input
            type="password"
            className="w-full border rounded px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="6-20 位"
          />
        </div>
        <div>
          <label className="block text-sm text-gray-600 mb-1">确认密码</label>
          <input
            type="password"
            className="w-full border rounded px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200"
            value={confirm}
            onChange={(e) => setConfirm(e.target.value)}
            placeholder="再次输入密码"
          />
        </div>
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white rounded py-2 hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? '注册中…' : '注册'}
        </button>
        <p className="text-center text-sm text-gray-500">
          已有账号？
          <Link to="/login" className="text-blue-600 hover:underline ml-1">
            去登录
          </Link>
        </p>
      </form>
    </div>
  )
}
