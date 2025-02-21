import request from '@/utils/request'

// 添加收藏
export function addStoreup(data) {
  return request({
    url: '/university/storeup/add',
    method: 'post',
    data: data
  })
}

// 删除收藏
export function deleteStoreup(storeupId) {
  return request({
    url: '/university/storeup/delete/',
    method: 'get',
    params: { storeupId }
  })
}
