import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import toast from 'react-hot-toast'
import { listAllCheckins, reviewCheckin } from '../api/checkin'
import { getUser, clearAuth } from '../store/auth'
import StatusBadge from '../components/StatusBadge'

// 管理后台：查看全部打卡 + 审核（见 docs/04 3.5~3.6）
export default function AdminHome() {
  const navigate = useNavigate()
  const user = getUser()
  const [records, setRecords] = useState([])
  const [filter, setFilter] = useState('') // ''=全部

  async function load() {
    try {
      setRecords(await listAllCheckins(filter || undefined))
    } catch {
      /* 拦截器已提示 */
    }
  }

  useEffect(() => {
    load()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filter])

  async function handleReview(id, action) {
    try {
      await reviewCheckin(id, action)
      toast.success('审核完成')
      load()
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
        <h1 className="font-bold text-gray-800">健身打卡 · 管理后台</h1>
        <div className="text-sm text-gray-600">
          {user?.nickname}
          <button onClick={logout} className="ml-4 text-blue-600 hover:underline">
            退出
          </button>
        </div>
      </header>

      <main className="max-w-5xl mx-auto p-6">
        <section className="bg-white rounded-xl shadow p-6">
          <div className="flex justify-between items-center mb-4">
            <h2 className="font-semibold text-gray-800">全部打卡记录</h2>
            <select
              className="border rounded px-3 py-1.5 text-sm"
              value={filter}
              onChange={(e) => setFilter(e.target.value)}
            >
              <option value="">全部状态</option>
              <option value="PENDING">待审核</option>
              <option value="APPROVED">已通过</option>
              <option value="REJECTED">已驳回</option>
            </select>
          </div>

          {records.length === 0 ? (
            <p className="text-gray-400 text-sm">暂无记录</p>
          ) : (
            <table className="w-full text-sm">
              <thead>
                <tr className="text-left text-gray-500 border-b">
                  <th className="py-2">用户</th>
                  <th>运动项目</th>
                  <th>内容</th>
                  <th>打卡时间</th>
                  <th>状态</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
                {records.map((r) => (
                  <tr key={r.id} className="border-b last:border-0">
                    <td className="py-2">{r.nickname || r.username}</td>
                    <td>{r.sportName}</td>
                    <td>{r.content}</td>
                    <td>{r.checkinTime}</td>
                    <td>
                      <StatusBadge status={r.status} />
                    </td>
                    <td>
                      {r.status === 'PENDING' ? (
                        <div className="flex gap-2">
                          <button
                            onClick={() => handleReview(r.id, 'APPROVE')}
                            className="text-green-600 hover:underline"
                          >
                            通过
                          </button>
                          <button
                            onClick={() => handleReview(r.id, 'REJECT')}
                            className="text-red-600 hover:underline"
                          >
                            驳回
                          </button>
                        </div>
                      ) : (
                        <span className="text-gray-400">—</span>
                      )}
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
