import request from '@/utils/request'

export function simulatePrepay(data) {
  return request({
    url: '/prepay/simulate',
    method: 'post',
    data
  })
}