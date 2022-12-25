function loginApi(data) {
    return $axios({
      'url': 'http://localhost:8080/user/login',
      'method': 'post',
      data
    })
  }

function loginoutApi() {
  return $axios({
    'url': 'http://localhost:8080/user/loginout',
    'method': 'post',
  })
}

  