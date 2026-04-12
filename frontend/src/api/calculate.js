import request from '@/utils/request'

export function calculate(data) {
  return request({
    url: '/calculate',
    method: 'post',
    data
  })
}

export function preview(data) {
  return request({
    url: '/calculate/preview',
    method: 'post',
    data
  })
}
