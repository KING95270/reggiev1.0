// 查询列表接口
const getCategoryPage = (params) => {
  return $axios({
    url: 'http://47.113.151.37:8080/category/page',
    method: 'get',
    params
  })
}

// 编辑页面反查详情接口
const queryCategoryById = (id) => {
  return $axios({
    url: `/category/${id}`,
    method: 'get'
  })
}

// 删除当前列的接口
const deleCategory = (ids) => {
  return $axios({
    url: 'http://47.113.151.37:8080/category',
    method: 'delete',
    params: { ids }
  })
}

// 修改接口
const editCategory = (params) => {
  return $axios({
    url: 'http://47.113.151.37:8080/category',
    method: 'put',
    data: { ...params }
  })
}

// 新增接口
const addCategory = (params) => {
  return $axios({
    url: 'http://47.113.151.37:8080/category',
    method: 'post',
    data: { ...params }
  })
}