import request from '@/utils/request'

export function getPlan(planId) {
  return request({
    url: /plan/,
    method: 'get'
  })
}

export function getDetails(planId) {
  return request({
    url: /plan//details,
    method: 'get'
  })
}

export function getHistory(page, size) {
  return request({
    url: '/plan/history',
    method: 'get',
    params: { page, size }
  })
}
