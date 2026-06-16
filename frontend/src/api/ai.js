import http from './http'

// 获取 AI 健身建议（见 docs/05 3.1）。一期后端返回 Mock 数据。
// includeHistory: 是否结合本人近期打卡记录（默认 false）
export function getAdvice(question, includeHistory = false) {
  return http.post('/ai/advice', { question, includeHistory })
}
