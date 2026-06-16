import { useState } from 'react'
import toast from 'react-hot-toast'
import { getAdvice } from '../api/ai'

// AI 健身建议对话面板（见 docs/05）。一期对接 Mock，端到端跑通。
// 维护一段简单的「我问 / AI 答」对话列表，一次性返回（非流式）。
export default function AiAdvicePanel() {
  const [question, setQuestion] = useState('')
  const [includeHistory, setIncludeHistory] = useState(false)
  const [loading, setLoading] = useState(false)
  const [messages, setMessages] = useState([]) // { role: 'user'|'ai', text, model?, createdAt? }

  async function handleAsk(e) {
    e.preventDefault()
    const q = question.trim()
    if (!q) {
      toast.error('请输入你的健身问题')
      return
    }
    setMessages((prev) => [...prev, { role: 'user', text: q }])
    setQuestion('')
    setLoading(true)
    try {
      const data = await getAdvice(q, includeHistory)
      setMessages((prev) => [
        ...prev,
        { role: 'ai', text: data.answer, model: data.model, createdAt: data.createdAt },
      ])
    } catch {
      // 拦截器已 toast；回填一条占位，便于用户知道这次提问失败
      setMessages((prev) => [
        ...prev,
        { role: 'ai', text: 'AI 服务暂时不可用，请稍后再试。', model: 'error' },
      ])
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="bg-white rounded-xl shadow p-6">
      <div className="flex items-center justify-between mb-4">
        <h2 className="font-semibold text-gray-800">AI 健身建议</h2>
        <label className="flex items-center gap-1.5 text-xs text-gray-500 select-none">
          <input
            type="checkbox"
            checked={includeHistory}
            onChange={(e) => setIncludeHistory(e.target.checked)}
          />
          结合我的打卡记录
        </label>
      </div>

      {/* 对话区 */}
      <div className="space-y-3 mb-4 max-h-72 overflow-y-auto">
        {messages.length === 0 ? (
          <p className="text-gray-400 text-sm">
            向 AI 提问，例如「我想增肌，每周练 3 次，怎么安排？」
          </p>
        ) : (
          messages.map((m, i) => (
            <div key={i} className={m.role === 'user' ? 'text-right' : 'text-left'}>
              <div
                className={`inline-block max-w-[85%] rounded-lg px-3 py-2 text-sm whitespace-pre-wrap ${
                  m.role === 'user'
                    ? 'bg-blue-600 text-white'
                    : 'bg-gray-100 text-gray-800'
                }`}
              >
                {m.text}
              </div>
              {m.role === 'ai' && m.model && (
                <div className="text-[11px] text-gray-400 mt-0.5">
                  模型：{m.model}
                  {m.createdAt ? ` · ${m.createdAt}` : ''}
                </div>
              )}
            </div>
          ))
        )}
        {loading && <p className="text-gray-400 text-sm">AI 思考中…</p>}
      </div>

      {/* 输入区 */}
      <form onSubmit={handleAsk} className="flex gap-2">
        <input
          className="flex-1 border rounded px-3 py-2 text-sm focus:outline-none focus:ring focus:ring-blue-200"
          placeholder="输入你的健身问题…"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          disabled={loading}
        />
        <button
          type="submit"
          disabled={loading}
          className="bg-blue-600 text-white rounded px-4 py-2 text-sm hover:bg-blue-700 disabled:opacity-50"
        >
          {loading ? '请稍候' : '提问'}
        </button>
      </form>
    </section>
  )
}
