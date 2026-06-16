import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import toast from 'react-hot-toast'
import { listSports, submitCheckin, listMyCheckins } from '../api/checkin'
import { getUser, clearAuth } from '../store/auth'
import StatusBadge from '../components/StatusBadge'

// 用户中心：提交打卡 + 查看本人记录（见 docs/04 3.2~3.4）
export default function UserHome() {
  const navigate = useNavigate()
  const user = getUser()
  const [sports, setSports] = useState([])
  const [sportId, setSportId] = useState('')
  const [content, setContent] = useState('')
  const [records, setRecords] = useState([])

  async function loadSports() {
    try {
      setSports(await listSports())
    } catch {
      /* 拦截器已提示 */
    }
  }

  async function loadRecords() {
    try {
      setRecords(await listMyCheckins())
    } catch {
      /* 拦截器已提示 */
    }
  }

  useEffect(() => {
    loadSports()
    loadRecords()
  }, [])

  async function handleSubmit(e) {
    e.preventDefault()
    if (!sportId || !content) {
      toast.error('请选择运动项目并填写打卡内容')
      return
    }
    try {
      await submitCheckin(Number(sportId), content)
      toast.success('打卡成功，等待审核')
      setContent('')
      setSportId('')
      loadRecords()
    } catch {
      /* 拦截器已提示 */
    }
  }

  function logout() {
    clearAuth()
    navigate('/login')
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow px-6 py-3 flex justify-between items-center">
        <h1 className="font-bold text-gray-800">健身打卡 · 用户中心</h1>
        <div className="text-sm text-gray-600">
          {user?.nickname}
          <button onClick={logout} className="ml-4 text-blue-600 hover:underline">
            退出
          </button>
        </div>
      </header>

      <main className="max-w-3xl mx-auto p-6 space-y-6">
        {/* 提交打卡 */}
        <section className="bg-white rounded-xl shadow p-6">
          <h2 className="font-semibold text-gray-800 mb-4">提交打卡</h2>
          <form onSubmit={handleSubmit} className="space-y-4">
            <select
              className="w-full border rounded px-3 py-2"
              value={sportId}
              onChange={(e) => setSportId(e.target.value)}
            >
              <option value="">请选择运动项目</option>
              {sports.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.name}
                </option>
              ))}
            </select>
            <textarea
              className="w-full border rounded px-3 py-2"
              rows={3}
              placeholder="今天的训练内容…"
              value={content}
              onChange={(e) => setContent(e.target.value)}
            />
            <button className="bg-blue-600 text-white rounded px-4 py-2 hover:bg-blue-700">
              提交打卡
            </button>
          </form>
        </section>

        {/* 我的打卡记录 */}
        <section className="bg-white rounded-xl shadow p-6">
          <h2 className="font-semibold text-gray-800 mb-4">我的打卡记录</h2>
          {records.length === 0 ? (
            <p className="text-gray-400 text-sm">暂无记录</p>
          ) : (
            <table className="w-full text-sm">
              <thead>
                <tr className="text-left text-gray-500 border-b">
                  <th className="py-2">运动项目</th>
                  <th>内容</th>
                  <th>打卡时间</th>
                  <th>状态</th>
                </tr>
              </thead>
              <tbody>
                {records.map((r) => (
                  <tr key={r.id} className="border-b last:border-0">
                    <td className="py-2">{r.sportName}</td>
                    <td>{r.content}</td>
                    <td>{r.checkinTime}</td>
                    <td>
                      <StatusBadge status={r.status} />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </section>
      </main>
    </div>
  )
}
