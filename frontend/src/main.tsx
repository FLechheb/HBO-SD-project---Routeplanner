import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import TestRouteForm from './testroute/routepage.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <TestRouteForm />
  </StrictMode>,
)
