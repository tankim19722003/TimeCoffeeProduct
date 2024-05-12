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
            html += `
                    <a class="table-item table-item-${table.id}" style="position:relative">
                        ${table.name}
                        <span onclick=removeTable(${table.id}) id="remove-table-icon">x</span>
                    </a>`;
        }
        html += `<a id="add-table-btn" class="table-item" onclick="showAddTable()" 
        style="display:flex; justify-content:center; align-items:center; background-color:white; border:1px dotted #ccc">
            <img src="../images/plus.png" id="table-plus-icon"/>
        </a>`
        document.querySelector("#area-id").value = area;
        tableWrap.innerHTML = html;
        // handleString();
    })
}

function StringHTMLReplace() {
    let html  = document.querySelector(".container").innerHTML;
    let subString = '<a id="add-table-btn"';
    let pos = html.indexOf(subString);
    html = html.slice(pos, html.length);
    return html;
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
                html += `<li class="sidebar-item active" onclick='displayArea(${area.id})'>
                KHU ${area.name}
                </li>`;
            } else {
                html += `<li class="sidebar-item" onclick='displayArea(${area.id})'>KHU ${area.name} </li>`;
            }
        }
        html += `<li id="add-area-btn" class="sidebar-item" onclick="addArea()"
        style="background-color:white; border:1px dotted #ccc">
            <img src="../images/plus.png" style="width:30px"/>
        </li>`
        displayTable(areaId);
        AreaWrap.innerHTML = html;
    })
    .then(() => {
        // return new Promise.resolve()
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

// show menu add area
function addArea() {
    let addSection = document.querySelector(".add");
    let area = document.querySelector(".add-area");
    addSection.classList.remove("d-none");
    area.classList.remove("d-none");
}

function closeAddArea() {
    let addSection = document.querySelector(".add");
    addSection.classList.add("d-none");
    let area = document.querySelector(".add-area");
    area.classList.add("d-none");
    document.querySelector(".add-area-input").value = "";
}

function closeAddTable() {
    let addSection = document.querySelector(".add");
    addSection.classList.add("d-none");
    let table = document.querySelector(".add-table");
    table.classList.add("d-none");

    // set input null
    document.querySelector(".add-table_body-input").value = "";
}


function showAddTable(areaId) {
    let addSection = document.querySelector(".add");
    let addTable = document.querySelector(".add-table");

    addTable.classList.remove("d-none");
    addSection.classList.remove("d-none");
}

function saveTable() {
    debugger
    let areaId = document.querySelector("#area-id").value;
    let tableName = document.querySelector(".add-table_body-input").value;
    if (tableName == '') {
        alert("Table's name cann't be null");
        return;
    }

    let url = "http://localhost:8080/api/v1/timecoffee/table";
    let table = {
        "name": tableName,
        "area_id": areaId
    }

    let options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
        body: JSON.stringify(table)
    }

    fetch(url, options)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data != null) {
                debugger
                alert("Add table successfully");
                renderTableJustAdding(data);
            }
        })
}

function renderTableJustAdding(data) {
    debugger
    let tableWrap =  document.querySelector(".container");
    let html = tableWrap.innerHTML;
    let addTableBtn = `<a id="add-table-btn" class="table-item" onclick="showAddTable()" 
    style="display:flex; justify-content:center; align-items:center; background-color:white; border:1px dotted #ccc">
        <img src="../images/plus.png" id="table-plus-icon"/>
    </a>`;
    html= html.replace(StringHTMLReplace(), '');

    html += `<a class="table-item table-item-${data.id}" style="position:relative">
                    ${data.name}
                    <span onclick=removeTable(${data.id}) id="remove-table-icon">x</span>
                </a>`;

    html += addTableBtn;

    tableWrap.innerHTML = html;
    closeAddTable();
}
// renderTableJustAdding();
// save area 
function saveArea() {
    debugger
    let area = document.querySelector(".add-area_body-input").value;
    if (area == '') {
        alert("Area's name can't empty");
        return;
    }
    let api = "http://localhost:8080/api/v1/timecoffee/area";
    let data = {
        name : area
    }
    let options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
        body: JSON.stringify(data)
    }

    // save(api, options, "Add Area successfully");
    fetch(api, options)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data != null) {
                alert("Add Area successfully");
                window.location.reload();
            }
        })
}

function removeTable(tableId) {
    let api = "http://localhost:8080/api/v1/timecoffee/table/"+tableId;

    let options = {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
    }
    fetch(api, options)
        .then(() =>{
            let nodeDeleting = document.querySelector(".table-item-"+tableId);
            alert("Xóa thành công");
            nodeDeleting.remove();
        })
}
function save(url, options,messages) {
    fetch(url, options)
        .then(response => {
            return response.json();
        })
        .then(data => {
            if (data != null) {
                alert(messages);
                window.location.reload();
            }
        })
}
function getToken() {
    let userJSON = localStorage.getItem("userInfor");
    if (userJSON != null) {
        let user = JSON.parse(userJSON);
        return user.token;
    } else {
        return null;
    }
}

