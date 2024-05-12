function start() {
    let token = getToken();
    if (token == null) window.location.href = "../index.html";

    // show account
    displayArea();
    let account = document.querySelector(".account-name");
    account.innerHTML = getUser().account;
}
start();
// common in use 
function sort(list) {
    console.log(list);
    for (let i = 0; i < list.length-1; i++) {
        for (let j = i+1; j < list.length; j++) {
            let first = parseInt(list[i].name.slice(1));
            let second = parseInt(list[j].name.slice(1));
            if(first > second) {
                temp = list[i];
                list[i] = list[j];
                list[j] = temp;
            }
        }
    }
    return list;
}

// end 

function displayTable(area) {
    let token = getToken();
    let tableWrap = document.querySelector(".container");
    let html ='';
    
    let api = "http://localhost:8080/api/v1/timecoffee/table/"+area;
    let options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` // Include the token in the Authorization header
        },
      }
    fetch(api,options) 
    .then(response => {
        return response.json();
    })
    .then(data => {
        // console.log(data.length);
        let tableSorted = sort(data);
        console.log(tableSorted);
        for (table of tableSorted) {
           if (table.status) {
            console.log(table);
                html += `
                    <a href="./order.html?tableId=${table.id}&&tableStatus=${table.status}" class="table-item table-ocuppied">
                        ${table.name}
                    </a>`;
                // console.log(html);
           } else {
            html += `
                    <a href="./order.html?tableId=${table.id}&&tableStatus=${table.status}" class="table-item">
                        ${table.name}
                    </a>`;
           }
        }

        tableWrap.innerHTML = html;
    })
    // console.log(html);
}

function displayArea(areaId) {
    let token = getToken();

    let AreaWrap = document.querySelector(".sidebar");
    let html ='';
    
    let options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` // Include the token in the Authorization header
        },
      }
    let areaApi = "http://localhost:8080/api/v1/timecoffee/area";
    fetch(areaApi, options) 
    .then(response => {
        return response.json();
    })
    .then(data => {
        // console.log(data);
        if (areaId == null) areaId = data[0].id;
        let areas = sort(data);
        // console.log(area);
        for (let area of data) {
            if (area.id == areaId) {
                html += `<li class="sidebar-item active" onclick='displayArea(${area.id})'>KHU ${area.name} </li>`;
            } else {
                html += `<li class="sidebar-item" onclick='displayArea(${area.id})'>KHU ${area.name} </li>`;
            }
        }
        displayTable(areaId);
        AreaWrap.innerHTML = html;
    })
}

// get token
function getToken() {
    let userJSON = localStorage.getItem("userInfor");
    if (userJSON != null) {
        let user = JSON.parse(userJSON);
        return user.token;
    } else {
        return null;
    }
}
// function loadOrder() {
    
// }

function getUser() {
    let userJSON = localStorage.getItem("userInfor");
    if (userJSON != null) {
        let user = JSON.parse(userJSON);
        return user;
    } else {
        return null;
    }
}

function signOut() {
    localStorage.removeItem("userInfor");
    window.location.href = "../index.html";
}