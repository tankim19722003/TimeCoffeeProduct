let productOrderList = [];
// let products = [];

// adding order detail
let addingOrderDetails=[];
//updating order detail 
let updatingOrderDetails =[];

// store order from db
let OrderDetailFromDB = [];
let orderDetails = [];
let totalMoneyOrder = 0;
let tableTarget = null;

// money pay enter 
let payEnter = 0;

// Common in using
function getToken() {
    let userJSON = localStorage.getItem("userInfor");
    if (userJSON != null) {
        let user = JSON.parse(userJSON);
        return user.token;
    } else {
        return null;
    }
}

function start() {
    // check login 
    if (getUser() == null) window.location.href = "../index.html";
    let html = '';
    document.querySelector('.order-list').innerHTML = html;
    displayCategory(1);
    loadOrderItems();
}

start();

function getToken() {
    let userJSON = localStorage.getItem("userInfor");
    if (userJSON != null) {
        let user = JSON.parse(userJSON);
        return user.token;
    } else {
        return null;
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

function displayCategory(categoryId) {
    let options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
    }
    let api = 'http://localhost:8080/api/v1/timecoffee/category';
    let categoryList = document.querySelector(".category-list");
    fetch(api, options)
        .then(response => {
            return response.json();
        })
        .then(data => {
            let html = '';
            for (let category of data) {
                if (category.id == categoryId) {
                    html += `
                        <div class="category-item active-category id-${category.id}" onclick="displayProducts(${category.id})">${category.name}</div>
                    `;
                } else {
                    html += `
                        <div class="category-item id-${category.id}" onclick="displayProducts(${category.id})">${category.name}</div>
                    `;
                }
            }
            return html;
        })
        .then(html => {
            categoryList.innerHTML = html;
            displayProducts(categoryId);
        })
}

function displayProducts(categoryId) {
    let options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
    }
    let api =`http://localhost:8080/api/v1/timecoffee/product/${categoryId}`;
    let productWrap = document.querySelector(".product");
    let categoryItems = document.querySelectorAll(".category-item");
    fetch(api, options)
        .then(response => {
            return response.json();
        })
        .then(data => {
            // products = data;
            let html = '';
            for (let product of data) {
                html +=`
                <div class="col-6">
                    <div class="product-item" onclick="addToOrder('${product.name}', ${product.price}, ${product.id})">
                        <p class="product-name">${product.name}</p>
                        <p class="product-price">${product.price}/ly</p>
                    </div>
                </div>
                `;
            }
            return html;
        })
        .then(html => {
            productWrap.innerHTML = html;
            // update category current
            categoryItems.forEach(categoryItem => {
                categoryItem.classList.remove("active-category");
            })

            let findingWord = 'id-' + categoryId;
            categoryItems.forEach(categoryItem => {
                if (categoryItem.classList.contains(findingWord)){
                    categoryItem.classList.add("active-category");
                } 
            })
    
        })
}

function addToOrder(productName, productPrice, productId) {
    debugger
    for(let i = 0; i < productOrderList.length; i++) {
        if(productName == productOrderList[i].product.name) {
            plusProduct(productOrderList[i].id);
            return;
        }
    }
    // add product prepare to save 
    let product = {
        'id':productId + '2003',
        'quantity':1,
        'product': {
            'id':productId,
            'price': productPrice,
            'name':productName,
        }
    }
    
    handleTotalMoneyOrderShowing(product.product.price);
    renderOrderDetail(product, productOrderList.length);

    // products.push(product);
    addingOrderDetails.push(product);
    productOrderList.push(product);

    // handle total money
    totalMoneyOrder += product.product.price;
    handleTotalMoneyOrderShowing(totalMoneyOrder);
}

function renderOrderDetail(orderDetail, index) {
     // end check product exist in order
    // let orderList = document.querySelector(".order-list");

    const divElement = document.createElement('div');
    divElement.classList.add("order-item");
    divElement.classList.add("row");
    divElement.id = `order-detail-${orderDetail.id}`;
    // Kiểm tra xem là node thứ chẵn hay lẻ để thay đổi màu nền
    if (index % 2 === 0) {
        divElement.style.backgroundColor = "#ffffff"; // Màu nền cho node chẵn
    } else {
        divElement.style.backgroundColor = "#D7F1F3"; // Màu nền cho node lẻ
    }
    let totalMoney = orderDetail.quantity * orderDetail.product.price;
    // console.log(totalMoney);
    let html = `
                <div class="col-4" style="text-align:left; padding-left:20px">
                    ${orderDetail.product.name}
                </div>
                <div class="col-2">
                    <div class="input-wrap">
                        <div class="minus" style="width:30%;cursor: pointer" onclick=minusProduct(${orderDetail.id})>-</div>
                        <input type="text" style="width:40%;border:1px solid #ccc; text-align:center" value="${orderDetail.quantity}" id="input-${orderDetail.id}">
                        <div class="plus" style="width:30%;cursor: pointer" onclick=plusProduct(${orderDetail.id})>+</div>
                    </div>
                </div>
                <div class="col-3">
                    ${orderDetail.product.price} VND
                </div>
                <div class="col-3">
                    <span id="total-price-product-${orderDetail.product.id}">${totalMoney}</span> VND
                    <span class='delete-btn' onclick=deleteOrderDetail(${orderDetail.id})>
                        <i class="fa-solid fa-trash" style="color:red"></i>
                    </span>
                </div>`;

    divElement.innerHTML = html; 
    document.querySelector('.order-list').appendChild(divElement);
}

function minusProduct(orderDetailId) {
    debugger
    // add element to update
    // handleUpdatingOrderDetail(orderDetailId);
    handleAddOrderDetailToUpdatingOrder(orderDetailId);

    const clss = `#input-${orderDetailId}`;
    let inputValue = parseInt(document.querySelector(clss).value) - 1;
    if (inputValue < 1) {
        alert("Số lượng order phải lớn hơn hoặc bằng 1");
        return;
    }
    document.querySelector(clss).value = inputValue;

    let orderDetail = findProductById(orderDetailId, productOrderList);
    let totalMoneyElement = document.querySelector("#total-price-product-"+orderDetail.product.id);
    let totalMoney = parseInt(inputValue) * orderDetail.product.price;
    totalMoneyElement.innerHTML = totalMoney; 
    totalMoneyOrder -= orderDetail.product.price;
    handleTotalMoneyOrderShowing(totalMoneyOrder);
}

function handleTotalMoneyOrderShowing(totalMoney) {
    document.querySelector("#total-money-order").innerHTML = formatNumber(totalMoney);
}

function handleAddOrderDetailToUpdatingOrder(orderDetailId) {
    debugger
    // console.log(tableStatus);
    if (checkOrderDetailIsLoadFromDB(orderDetailId)) {
        // check does order detail exist in list id order detail
        let IsUpdatingIdNotExisting = true;
        // check order detail exist in updating order
        for (let id of updatingOrderDetails) {
            if (id == orderDetailId) {
                IsUpdatingIdNotExisting = false;
                break;
            };
        }
        if (IsUpdatingIdNotExisting) updatingOrderDetails.push(orderDetailId); 
    }
}

function plusProduct(orderDetailId) {
    debugger
    handleAddOrderDetailToUpdatingOrder(orderDetailId);
    // handleUpdatingOrderDetail(orderDetailId);

    const clss = `#input-${orderDetailId}`;
    let inputValue = parseInt(document.querySelector(clss).value) + 1;
    document.querySelector(clss).value = inputValue;
    // updating value 

    let orderDetail = findProductById(orderDetailId, productOrderList);
    let totalMoneyElement = document.querySelector("#total-price-product-"+orderDetail.product.id);
    let totalMoney = parseInt(inputValue) * orderDetail.product.price;
    totalMoneyElement.innerHTML = totalMoney; 

    totalMoneyOrder += orderDetail.product.price;
    handleTotalMoneyOrderShowing(totalMoneyOrder);
}

function findProductById(id, list) {
    for (let item of list) {
        if (item.id == id) return item;
    }
    return null;
}

function checkOrderDetailIsLoadFromDB(orderDetailId) {
    for (let order_detail of OrderDetailFromDB) {
            if (order_detail.id == orderDetailId) return true;
    }
    return false;
}

function fetchOrderDetail(tableId) {
    let options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
    };
    let api = 'http://localhost:8080/api/v1/timecoffee/order_details/table/' + tableId;
    fetch(api, options)
        .then(response => {
            return response.json()
        })
        .then(orderDetails => {
            for (let order_detail of orderDetails.order_details) {
                renderOrderDetail(order_detail, productOrderList.length);
                productOrderList.push(order_detail);
                OrderDetailFromDB.push(order_detail);
                // console.log(order_detail);
                totalMoneyOrder += order_detail.product.price * order_detail.quantity;
                // console.log(order_detail);
            }
            handleTotalMoneyOrderShowing(totalMoneyOrder);

            // addContentToPrintArea(orderDetails);
        })
        .catch(error => {
        })
}
function loadOrderItems() {
    var tableStatus = PathVariable('tableStatus');
    var tableId = PathVariable('tableId');
    if (tableStatus == 'true') {
        fetchOrderDetail(tableId);
    } else {
        console.log("table is empty");
    }
}

function PathVariable(param) {
    let url = window.location.href;
    var urlParams = new URLSearchParams(new URL(url).search);
    return urlParams.get(param); 
}

function saveOrder() {
    // data to save 
    // let orderItems = document.querySelectorAll(".order-item");
    let tableStatus = PathVariable("tableStatus");
    if (tableStatus == 'false') {
        createNewOrder();
    } else {
        handleUpdatingOrderDetail();
    }
    
}

function handleUpdatingOrderDetail() {
    // console.log(updatingOrderDetails);
    // console.log(addingOrderDetails); 

    debugger
    let orderDetailUpdating = [];
    // order detail updating item 
    for (let id of updatingOrderDetails) {
            for(let item of productOrderList) {
                if (id == item.id) orderDetailUpdating.push(item);
            }
    }

    // updating quantity from updating order details
    orderDetailUpdating =updateQuantityOfOrderDetails(orderDetailUpdating);

    // order detail adding item 
    addingOrderDetails = updateQuantityOfOrderDetails(addingOrderDetails);
    

    // map to data fetch
    let OrderDetailUpdatingFetching = orderDetailUpdating.map(orderDetail => {
        return {
            "id": orderDetail.id,
            "quantity":orderDetail.quantity
        }
    })

    let addingOrderDetailsFetching = addingOrderDetails.map(orderDetail => {
        return {
            "product_id": orderDetail.product.id,
            "quantity":orderDetail.quantity
        }
    })

    // console.log(OrderDetailUpdatingFetching);
    // console.log(addingOrderDetailsFetch);

    // call api save updating
    let tableId = PathVariable("tableId");
    
    let data = {
        "table_id" : tableId,
        "addingOrderDetails": addingOrderDetailsFetching,
        "updatingOrderDetails": OrderDetailUpdatingFetching
    }

    let apiUpdate = "http://localhost:8080/api/v1/timecoffee/order_details";
    let options = {
        method: 'PUT', // *GET, POST, PUT, DELETE, etc.
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
        body: JSON.stringify(data)
    };

    fetch(apiUpdate, options)
        .then(() =>{
            alert("Updating successfully");
            window.location.reload();
        })
}

function getQuantityInput(orderDetailId) {
    let quantityValue = document.querySelector("#input-" + orderDetailId).value;
    return quantityValue;
}

function updateQuantityOfOrderDetails(orderDetails) {
    orderDetails = orderDetails.map(orderDetailItem => {
        orderDetailItem.quantity = getQuantityInput(orderDetailItem.id);
        return orderDetailItem;
    })
    return orderDetails;
}

function createNewOrder() {
    for (let i = 0; i < productOrderList.length; i++) {
        let productQuantity = document.querySelector("#input-" + productOrderList[i].id).value;
        productOrderList[i].quantity = productQuantity;
    }

    let productOrderItemsSave = {
        'table_id' : parseInt(PathVariable("tableId")),
        'products':[]
    }
    productOrderList.forEach(productOrderItem => {
        let product = {
            'product_id' : productOrderItem.product.id,
            'quantity': parseInt(productOrderItem.quantity)
        }
        productOrderItemsSave.products.push(product);
    })


    // check table status
    let options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
        body: JSON.stringify(productOrderItemsSave)
    };

    let api = 'http://localhost:8080/api/v1/timecoffee/order';
    
    fetch(api,options)// body data type must match "Content-Type" header
      .then(response => {
        if (!response.ok) {
                alert("Table is not empty");
          }
          return response.json();
      })
      .then(order => {
            alert("Đặt hàng thành công");
            let tableId = PathVariable("tableId");
            window.history.pushState({}, '', `/order.html?tableId=${tableId}&&tableStatus=true`);
      })
      .catch(error => {
      })
}

function removeOrderDetailNode(orderId) {
    let id = document.querySelector("#order-detail-"+orderId);
    id.remove();
    debugger
    productOrderList = productOrderList.filter(productOrderItem => {
        return productOrderItem.id != orderId;
    })

    document.querySelector('.order-list').innerHTML = "";
    let index = 0;
    for (item of productOrderList) {
        renderOrderDetail(item, index++);
    }    // console.log(productOrderList);
}

function deleteOrderDetailByUsingAPI(id) {
    let api = `http://localhost:8080/api/v1/timecoffee/order_details/${id}`;
    let options = {
        method: 'DELETE', // Specify the HTTP method
        headers: {
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
    }
    fetch(api,options)
        .then(() =>{
                alert("Xóa thành công");
            }
        )
}

function removeOrderDetailOutOfOrderDetailList(orderDetailId) {
    productOrderList = productOrderList.filter(productOrderItem => {
        return productOrderItem.id != orderDetailId;
    })
}
function deleteOrderDetail(orderDetailId) {

    debugger
    let tableStatus = PathVariable("tableStatus");
    // console
    if (tableStatus == 'true') {
        let product = findProductById(orderDetailId, OrderDetailFromDB);
        let orderDetailItem = findProductById(orderDetailId, productOrderList);
        totalMoneyOrder -= orderDetailItem.product.price * orderDetailItem.quantity;

        // item just add to order
        if (product == null) {
            handleTotalMoneyOrderShowing(totalMoneyOrder);
            removeOrderDetailNode(orderDetailId);
            return;
        }

        deleteOrderDetailByUsingAPI(orderDetailId);
        removeOrderDetailNode(orderDetailId);

        // update total money
        handleTotalMoneyOrderShowing(totalMoneyOrder);

        removeOrderDetailOutOfOrderDetailList(orderDetailId);
        
    } 
}

// move table
function moveToNewTable(id) {
    let api = `http://localhost:8080/api/v1/timecoffee/order/changeTableInOrder/`;
    let options = {
        method: 'DELETE', // Specify the HTTP method
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
        body: JSON.stringify({
        })
    }
    fetch(api,options)
        .then (response => {
            return response.json();
        })
        .then(data => {
        })
}

// common in use
function formatNumber(num) {
    return num.toLocaleString('vi-VN');
}



function showTableList() {

    // hide menu and show table changing
    let category = document.querySelector("#category");
    let product = document.querySelector("#product");
    let changingTable = document.querySelector(".changing-table");

    category.classList.add("d-none");
    product.classList.add("d-none");
    changingTable.classList.remove("d-none");
    // end hide menu
    let token = getToken();
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
        if (data != null) {
            // show area 
            appendAreaChild(data);

            // show table of area
            let areaFirstId = data[0].id;
            displayTable(areaFirstId);

        }
    })
}

function appendAreaChild(data) {
    var selectElement = document.querySelector('.changing-table_select-area_select');

    for (item of data) {
            // Create a new <option> element
        var newOption = document.createElement('option');

        // Set the value and text content of the <option> element
        newOption.value = item.id;
        newOption.textContent = "Area " + item.name;

        // Append the <option> element to the <select> element
        selectElement.appendChild(newOption);
    }

}

function showTableByArea() {
    var selectElement = document.querySelector('.changing-table_select-area_select');
    let selectedElement = selectElement.value;
    displayTable(selectedElement);
}


function displayTable(areaId) {
    let token = getToken();
    let tableWrap = document.querySelector(".table-container");
    let html ='';
    
    let api = "http://localhost:8080/api/v1/timecoffee/table/"+areaId;
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
        // console.log(tableSorted);
        for (table of tableSorted) {
           if (table.status) {
                html += `
                    <a class="table-item table-ocuppied"  data-id = ${table.id} id = "table-${table.id}">
                        ${table.name}
                    </a>`;
                // console.log(html);
           } else {
            html += `
                    <a class="table-item" data-id = ${table.id}  id = "table-${table.id}">
                        ${table.name}
                    </a>`;
           }
        }

        tableWrap.innerHTML = html;
    })
    // console.log(html);
}

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


//handle focus table changing
document.querySelector(".table-container").onclick = function(e) {
    let targetElement = e.target;
    if (!targetElement.classList.contains("table-item")) return;
    // xóa đi các đối tượng đc kích hoạt 
    if (tableTarget != null) {
        tableTarget.classList.remove("table-focus-empty");
    }

    tableTarget = targetElement;
    if (tableTarget.classList.contains("table-ocuppied")) {
        alert("table is not empty");
        return;
    }
    tableTarget.classList.add("table-focus-empty");
}

function handleSaveChangingTable() {
    // let api = "http://localhost:8080/api/v1/timecoffee/order/changeTableInOrder";
    // let body = {
    //     "table_id":,
    //     "order_id":
    // }
    let tableId =parseInt(tableTarget.dataset.id);
    let existingTableId = PathVariable("tableId"); 
    let token = getToken();
    let apiGetOrderById = "http://localhost:8080/api/v1/timecoffee/order/table/" + existingTableId;
    let options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` // Include the token in the Authorization header
        },
    }

    fetch(apiGetOrderById, options) 
        .then(response => {
            return response.json();
        })
        .then(data => {
            // console.log(data);
            if (data != null) {
                updateTableInOrder(data.id, tableId);
            }
        })
}

function updateTableInOrder(orderId, tableId) {
    let token = getToken();
    let existingTableId = PathVariable("tableId"); 
    let apiUpdateOrder = "http://localhost:8080/api/v1/timecoffee/order/changeTableInOrder";
    let data = {
        "order_id":orderId,
        "table_id": tableId
    }
    let options = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}` // Include the token in the Authorization header
        },
        body: JSON.stringify(data)
    };

    fetch(apiUpdateOrder, options)
        .then(response => {
            return response.json();
        })
        .then(data => {
            renderTableChanging(existingTableId,tableId);
        })
}

function renderTableChanging(tablePresentId, tableMovingId) {
    let tablePresent = document.querySelector("#table-"+tablePresentId);
    let tableMoving = document.querySelector("#table-"+tableMovingId);
    tablePresent.classList.remove("table-ocuppied");
    tableMoving.classList.add("table-ocuppied");
    debugger
    window.history.pushState({}, '', `/order.html?tableId=${tableMovingId}&&tableStatus=true`);
    alert("Chuyển bàn thành công");
}


function closeChangingTable() {
       // hide menu and show table changing
       let category = document.querySelector("#category");
       let product = document.querySelector("#product");
       let changingTable = document.querySelector(".changing-table");
   
       category.classList.remove("d-none");
       product.classList.remove("d-none");
       changingTable.classList.add("d-none");
       // end hide menu
}

function printDocument() {
    window.print();
}

function addContentToPrintArea() {
    debugger
    let totalMoneyBill = 0;

    let tableBill = document.querySelector(".order-items-bill");
    tableBill.innerHTML = "";
    let count = 1;
    for(orderItem of productOrderList) {
        let tr = document.createElement("tr");
        let STT = document.createElement("td");
        let productName = document.createElement("td");
        let quantity = document.createElement("td");
        let productPrice = document.createElement("td");
        let totalMoney = document.createElement("td");

        STT.textContent = count++;
        productName.textContent = orderItem.product.name;
        quantity.textContent = orderItem.quantity;
        productPrice.textContent = orderItem.product.price;
        totalMoney.textContent = orderItem.quantity * orderItem.product.price;
        totalMoneyBill += orderItem.quantity * orderItem.product.price;
        tr.appendChild(STT);
        tr.appendChild(productName);
        tr.appendChild(quantity);
        tr.appendChild(productPrice);
        tr.appendChild(totalMoney);

        tableBill.appendChild(tr);
    }
    document.querySelector(".total-money-bill").innerHTML = formatNumber(totalMoneyBill);
}

function printBill() {
    debugger
    let tableStatus = PathVariable("tableStatus");
    if (tableStatus == "true") {
        addContentToPrintArea(productOrderList);
        window.print();
    } 
    else alert("Table is empty")
}

function payTheBill() {
    let payment = document.querySelector(".payment");
    // console.log(payment);
    payment.classList.add("d-flex");
}

function confirmBill() {
    let paymentEnterMoney = document.querySelector(".payment-content_enter-money");
    paymentEnterMoney.classList.add("d-none");

    let confirmBill = document.querySelector(".payment-content_confirm");
    confirmBill.classList.add("d-confirm");
    let moneyRecieveInputValue = document.querySelector(".money-receive_input").value;

    let MoneyReceiving = parseInt(moneyRecieveInputValue);
    payEnter = MoneyReceiving;
    document.querySelector(".payment-content_confirm-money-customer-span").innerHTML =  formatNumber(MoneyReceiving);
    document.querySelector(".payment-content_confirm-total-money-span").innerHTML = formatNumber(totalMoneyOrder);

    let moneyRemaining = document.querySelector(".payment-content_confirm-pay-back");
    let html = '';
    if (MoneyReceiving < totalMoneyOrder) {
        html = `Số tiền còn thiếu: <span class="payment-content_confirm-pay-back-span">${formatNumber(totalMoneyOrder - MoneyReceiving)}</span> VND`;
    } else {
        html = `Số tiền thừa: <span class="payment-content_confirm-pay-back-span">${formatNumber(MoneyReceiving- totalMoneyOrder)}</span> VND`;
    }

    moneyRemaining.innerHTML = html;

}

function saveBill() {
    if (payEnter < totalMoneyOrder) {
        alert("Vui lòng thanh toán số tiền còn lại " + (totalMoneyOrder - payEnter));
        return;
    } 
    let tableId = PathVariable("tableId");
    // goi phuong thuc luu bill
    let payOrderApi = "http://localhost:8080/api/v1/timecoffee/order/pay_order?table_id="+tableId;
    let options = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        }
    }

    fetch(payOrderApi, options)
        .then(() => {
            alert("Thanh toán thành công");
            window.location.href = "./StaffHome.html";
        })
    // cho ve trang chu 

}

function closePayTheBill() {
    let payment = document.querySelector(".payment");
    payment.classList.remove("d-flex");
    
    // close confirm 
    let confirmBill = document.querySelector(".payment-content_confirm");
    confirmBill.classList.remove("d-confirm");

    // show enter money
    let paymentEnterMoney = document.querySelector(".payment-content_enter-money");
    paymentEnterMoney.classList.remove("d-none");
}