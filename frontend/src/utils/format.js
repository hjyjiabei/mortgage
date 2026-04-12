export function formatAmount(value) {
  if (!value) return '0.00'
  const num = Number(value)
  return num.toFixed(2)
}

export function formatAmountWithComma(value) {
  if (!value) return '0'
  const num = Number(value)
  return num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

export function formatRate(value) {
  if (!value) return '0.00%'
  const num = Number(value) * 100
  return num.toFixed(4) + '%'
}

export function formatDate(date) {
  if (!date) return ''
  if (typeof date === 'string') return date
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return ${year}--
}
