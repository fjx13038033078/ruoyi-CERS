import request from '@/utils/request'

// 添加收藏
export function addStoreup(data) {
  return request({
    url: '/university/storeup/add',
    method: 'post',
    data: data
  })
}

// 取消收藏
export function deleteStoreup(storeupId) {
  return request({
    url: '/university/storeup/delete/',
    method: 'get',
    params: { storeupId }
  })
}

// 查询收藏列表
export function listStoreups(query) {
  return request({
    url: '/university/storeup/listAll',
    method: 'get',
    params: query
  })
}
