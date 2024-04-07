
// common in use 
function sort(list) {
    for (let i = 0; i < list.length-1; i++) {
        for (let j = i+1; j < list.length; j++) {
            let temp;
            if(list[i].name > list[j].name) {
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
    let tableWrap = document.querySelector(".container");
    let html ='';
    let api = "http://localhost:8080/api/v1/timecoffee/table/"+area;
    fetch(api) 
    .then(response => {
        return response.json();
    })
    .then(data => {
        // console.log(data.length);
        let tableSorted = sort(data);
        console.log(tableSorted);
        for (table of tableSorted) {
           if (table.status) {
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
    let AreaWrap = document.querySelector(".sidebar");
    let html ='';
    fetch("http://localhost:8080/api/v1/timecoffee/area") 
    .then(response => {
        return response.json();
    })
    .then(data => {
        // console.log(data);
        let areas = sort(data);
        // console.log(area);
        for (let area of data) {
            if (area.id == areaId) {
                html += `<li class="sidebar-item active" onclick='displayArea(${area.id})'>KHU ${area.name} </li>`;
            } else {
                html += `<li class="sidebar-item" onclick='displayArea(${area.id})'>KHU ${area.name} </li>`;
            }
        }
        return html;
    })
    .then(data => {
        displayTable(areaId);
        AreaWrap.innerHTML = html;
    })
}

// function loadOrder() {
    
// }
function start() {
    // displayTable(3);
    displayArea(3);
}
start();