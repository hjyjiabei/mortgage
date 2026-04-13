import request from '@/utils/request'

export function simulate(data) {
  return request({
    url: '/prepay',
    method: 'post',
    data
  })
}
