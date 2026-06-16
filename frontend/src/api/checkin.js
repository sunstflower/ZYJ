import http from './http'

// 运动项目列表（见 docs/04 3.2）
export function listSports() {
  return http.get('/sports')
}

// 提交打卡（见 docs/04 3.3）
export function submitCheckin(sportId, content) {
  return http.post('/checkins', { sportId, content })
}

// 查看本人打卡记录（见 docs/04 3.4）
export function listMyCheckins(status) {
  return http.get('/checkins/mine', { params: status ? { status } : {} })
}

// 查看全部打卡记录（管理员，见 docs/04 3.5）
export function listAllCheckins(status) {
  return http.get('/checkins', { params: status ? { status } : {} })
}

// 审核打卡（管理员，见 docs/04 3.6）action: 'APPROVE' | 'REJECT'
export function reviewCheckin(id, action) {
  return http.patch(`/checkins/${id}/review`, { action })
}
