import request from '@/utils/request'

export function compare(data) {
  return request({
    url: '/compare',
    method: 'post',
    data
  })
}
