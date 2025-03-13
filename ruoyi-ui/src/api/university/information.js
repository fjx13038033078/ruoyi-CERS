import request from '@/utils/request'

// 查询所有用户高考报名意愿信息
export function listAllInformation(query) {
  return request({
    url: '/university/information/listAll',
    method: 'get',
    params: query
  })
}

// 查询用户高考报名意愿详情
export function getInformation(infoId) {
  return request({
    url: '/university/information/detail',
    method: 'get',
    params: { infoId }
  })
}

// 根据用户ID查询用户高考报名意愿
export function listInformationByUserId(userId) {
  return request({
    url: '/university/information/listByUserId',
    method: 'get',
    params: { userId }
  })
}

// 添加用户高考报名意愿
export function addInformation(data) {
  return request({
    url: '/university/information/add',
    method: 'post',
    data: data
  })
}

// 更新用户高考报名意愿
export function updateInformation(data) {
  return request({
    url: '/university/information/update',
    method: 'post',
    data: data
  })
}

// 删除用户高考报名意愿
export function deleteInformation(infoId) {
  return request({
    url: '/university/information/delete',
    method: 'get',
    params: { infoId }
  })
}
