// 查询列表接口
const getDishPage = (params) => {
  return $axios({
    url: 'http://localhost:8080/dish/page',
    method: 'get',
    params
  })
}

// 删除接口
const deleteDish = (ids) => {
  return $axios({
    url: 'http://localhost:8080/dish',
    method: 'delete',
    params: { ids }
  })
}

// 修改接口
const editDish = (params) => {
  return $axios({
    url: 'http://localhost:8080/dish',
    method: 'put',
    data: { ...params }
  })
}

// 新增接口
const addDish = (params) => {
  return $axios({
    url: 'http://localhost:8080/dish',
    method: 'post',
    data: { ...params }
  })
}

// 查询详情
const queryDishById = (id) => {
  return $axios({
    url: `http://localhost:8080/dish/${id}`,
    method: 'get'
  })
}

// 获取菜品分类列表
const getCategoryList = (params) => {
  return $axios({
    url: 'http://localhost:8080/category/list',
    method: 'get',
    params
  })
}

// 查菜品列表的接口
const queryDishList = (params) => {
  return $axios({
    url: 'http://localhost:8080/dish/list',
    method: 'get',
    params
  })
}

// 文件down预览
const commonDownload = (params) => {
  return $axios({
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
    },
    url: 'http://localhost:8080/common/download',
    method: 'get',
    params
  })
}

// 起售停售---批量起售停售接口
const dishStatusByStatus = (params) => {
  return $axios({
    url: `http://localhost:8080/dish/status/${params.status}`,
    method: 'post',
    params: { ids: params.id }
  })
}