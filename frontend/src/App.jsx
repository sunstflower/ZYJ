import { Navigate, Route, Routes } from 'react-router-dom'
import { getToken, isAdmin } from './store/auth'
import Login from './pages/Login'
import Register from './pages/Register'
import UserHome from './pages/UserHome'
import AdminHome from './pages/AdminHome'

// 路由守卫：未登录跳登录；需要管理员的页面校验角色
function RequireAuth({ children, adminOnly = false }) {
  if (!getToken()) {
    return <Navigate to="/login" replace />
  }
  if (adminOnly && !isAdmin()) {
    return <Navigate to="/user" replace />
  }
  return children
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route
        path="/user"
        element={
          <RequireAuth>
            <UserHome />
          </RequireAuth>
        }
      />
      <Route
        path="/admin"
        element={
          <RequireAuth adminOnly>
            <AdminHome />
          </RequireAuth>
        }
      />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  )
}
