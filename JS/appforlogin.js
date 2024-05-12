// import jwt from 'jsonwebtoken';
window.onload = function() {
    debugger
    let user = getUser();
    if (user != null) {
        moveToHome(user.role);
    }
}
function login() {
    // check login before
    let accountValue = document.querySelector(".account-input").value;
    let passwordValue = document.querySelector(".password-input").value;

    let api = "http://localhost:8080/api/v1/timecoffee/users/login";
    
    let user = {
        "account":accountValue,
        "password":passwordValue
    }

    let options = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json' // Specify the content type as JSON
        },
        body: JSON.stringify(user) // Convert data to JSON string
      }
    fetch(api, options)
      .then(response =>{
        debugger
        console.log(response);
        if (!response.ok) {
            throw new Error('Invalid user');
          }

        return response.json();
      })
      .then (data => {

        let userInfoJSon = JSON.stringify(data);
        localStorage.setItem("userInfor",userInfoJSon);
        moveToHome(data.role);
      })

}

function moveToHome(role) {
  if(role == "user") {
      window.location.href = "../StaffHome.html";
  } else if(role == "admin"){
      window.location.href = "../AdminHome.html";
  }
}

function getUser() {
  let userJSON = localStorage.getItem("userInfor");
  if (userJSON != null) {
      let user = JSON.parse(userJSON);
      return user;
  } else {
      return null;
  }
}