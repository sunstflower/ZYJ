import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import toast from 'react-hot-toast'
import { login } from '../api/auth'
import { saveAuth } from '../store/auth'

// 登录页（见 docs/04 3.1）。登录成功后按 role 跳转用户中心/管理后台。
export default function Login() {
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading] = useState(false)

  async function handleSubmit(e) {
    e.preventDefault()
    if (!username || !password) {
      toast.error('请输入用户名和密码')
      return
    }
    setLoading(true)
    try {
      const data = await login(username, password)
      saveAuth(data.token, data.user)
      toast.success('登录成功')
      navigate(data.user.role === 'ADMIN' ? '/admin' : '/user')
    } catch {
      // 错误已由 http 拦截器 toast
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
        <h1 className="text-xl font-bold text-center text-gray-800">健身打卡系统</h1>
        <div>
          <label className="block text-sm text-gray-600 mb-1">用户名</label>
          <input
            className="w-full border rounded px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="admin / alice / bob"
          />
        </div>
        <div>
          <label className="block text-sm text-gray-600 mb-1">密码</label>
          <input
            type="password"
            className="w-full border rounded px-3 py-2 focus:outline-none focus:ring focus:ring-blue-200"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="123456"
          />
        </div>
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white rounded py-2 hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? '登录中…' : '登录'}
        </button>
      </form>
    </div>
  )
}
