// 审核状态标签（PENDING/APPROVED/REJECTED）
const MAP = {
  PENDING: { text: '待审核', cls: 'bg-yellow-100 text-yellow-800' },
  APPROVED: { text: '已通过', cls: 'bg-green-100 text-green-800' },
  REJECTED: { text: '已驳回', cls: 'bg-red-100 text-red-800' },
}

export default function StatusBadge({ status }) {
  const s = MAP[status] || { text: status, cls: 'bg-gray-100 text-gray-700' }
  return (
    <span className={`inline-block px-2 py-0.5 rounded text-xs font-medium ${s.cls}`}>
      {s.text}
    </span>
  )
}
