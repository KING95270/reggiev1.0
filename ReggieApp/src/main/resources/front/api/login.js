function loginApi(data) {
    return $axios({
      'url': 'http://47.113.151.37:8080/user/login',
      'method': 'post',
      data
    })
  }

function loginoutApi() {
  return $axios({
    'url': 'http://47.113.151.37:8080/user/loginout',
    'method': 'post',
  })
}

  