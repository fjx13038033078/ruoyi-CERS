import request from '@/utils/request'

// 查询所有专业列表
export function listMajors(query) {
  return request({
    url: '/university/major/listAll',
    method: 'get',
    params: query
  })
}

// 查询专业详细信息
export function getMajor(majorId) {
  return request({
    url: '/university/major/detail',
    method: 'get',
    params: { majorId }
  })
}

// 根据高校id查询专业列表
export function listMajorsByUniversityId(universityId) {
  return request({
    url: '/university/major/listByUniversityId',
    method: 'get',
    params: { universityId }
  })
}

// 添加专业信息
export function addMajor(data) {
  return request({
    url: '/university/major/add',
    method: 'post',
    data: data
  })
}

// 更新专业信息
export function updateMajor(data) {
  return request({
    url: '/university/major/update',
    method: 'post',
    data: data
  })
}

// 删除专业信息
export function deleteMajor(majorId) {
  return request({
    url: '/university/major/delete',
    method: 'get',
    params: {majorId}
  })
}
