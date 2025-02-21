import request from '@/utils/request'

export function recommendMajors() {
  return request({
    url: '/university/recommendation/listAll',
    method: 'get',
  })
}
