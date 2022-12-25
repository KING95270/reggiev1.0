function loginApi(data) {
  return $axios({
    'url': 'http://localhost:8080/employee/login',
    // headers: {
    //   'Content-Type': 'application/json',
    // },
    method:"POST",
    data
  })
}

function logoutApi(){
  return $axios({
    'url': 'http://localhost:8080/employee/logout',
    method: "post"
  })
}
