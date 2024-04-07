let productOrderList = [];
let products = [];
// let deletingElement = []
// let addingElement =[];
let orderDetails = [];
function start() {
    let html = '';
    document.querySelector('.order-list').innerHTML = html;
    displayCategory(1);
    loadOrderItems();
}

function displayCategory(categoryId) {
    let api = 'http://localhost:8080/api/v1/timecoffee/category';
    let categoryList = document.querySelector(".category-list");
    fetch(api)
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
            // console.log(html);
            displayProducts(categoryId);
        })
}

function displayProducts(categoryId) {
    let api =`http://localhost:8080/api/v1/timecoffee/product/${categoryId}`;
    let productWrap = document.querySelector(".product");
    let categoryItems = document.querySelectorAll(".category-item");
    fetch(api)
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
    // console.log(product);
    for(let i = 0; i < productOrderList.length; i++) {
        if(productName == productOrderList[i].name) {
            // alert("Sản phẩm đã tồn tại trong order");
            plusProduct(productId);
            return;
        }
    }
    // add product prepare to save 
    let product = {
        'id' : productId,
        'price': productPrice,
        'name':productName,
        'quantity':1
    }
    renderOrderDetail(product);
    products.push(product);
    addingElement.push(product);
    productOrderList.push(product);
}

function renderOrderDetail(product) {
     // end check product exist in order
    // let orderList = document.querySelector(".order-list");

    const divElement = document.createElement('div');
    divElement.classList.add("order-item");
    divElement.classList.add("row");
    divElement.id = `order-detail-${product.id}`;
    // Kiểm tra xem là node thứ chẵn hay lẻ để thay đổi màu nền
    if (productOrderList.length % 2 === 0) {
        divElement.style.backgroundColor = "#ffffff"; // Màu nền cho node chẵn
    } else {
        divElement.style.backgroundColor = "#D7F1F3"; // Màu nền cho node lẻ
    }

    let html = `
                <div class="col-4" style="text-align:left; padding-left:20px">
                    ${product.name}
                </div>
                <div class="col-2">
                    <div class="input-wrap">
                        <div class="minus" style="width:30%;cursor: pointer" onclick=minusProduct(${product.id})>-</div>
                        <input type="text" style="width:40%;border:1px solid #ccc; text-align:center" value="1" id="input-${product.id}">
                        <div class="plus" style="width:30%;cursor: pointer" onclick=plusProduct(${product.id})>+</div>
                    </div>
                </div>
                <div class="col-3">
                    ${product.price} VND
                </div>
                <div class="col-3">
                    <span id="total-price-product-${product.id}">${product.price}</span> VND
                    <span class='delete-btn' onclick=deleteOrderDetail(${product.id})>
                        <i class="fa-solid fa-trash" style="color:red"></i>
                    </span>
                </div>`;

    divElement.innerHTML = html; 
    // console.log(divElement);
    document.querySelector('.order-list').appendChild(divElement);
}

function minusProduct(productId) {
    const clss = `#input-${productId}`;
    let inputValue = parseInt(document.querySelector(clss).value) - 1;
    if (inputValue < 1) {
        alert("Số lượng order phải lớn hơn hoặc bằng 1");
        return;
    }
    document.querySelector(clss).value = inputValue;

    let totalMoneyElement = document.querySelector("#total-price-product-"+productId);
    let product = findProductById(productId, products);
    let totalMoney = parseInt(inputValue) * product.price;
    totalMoneyElement.innerHTML = totalMoney; 
}

function plusProduct(productId) {
    const clss = `#input-${productId}`;
    let inputValue = parseInt(document.querySelector(clss).value) + 1;
    document.querySelector(clss).value = inputValue;

    let totalMoneyElement = document.querySelector("#total-price-product-"+productId);
    let product = findProductById(productId, products);

    let totalMoney = parseInt(inputValue) * product.price;
    totalMoneyElement.innerHTML = totalMoney; 
}

function findProductById(id, list) {
    // console.log(list);
    for (let item of list) {
        if (item.id == id) return item;
    }
    return null;
}

function fetchOrderDetail(tableId) {
    let api = 'http://localhost:8080/api/v1/timecoffee/order_details/table/' + tableId;
    fetch(api)
        .then(response => {
            return response.json()
        })
        .then(orderDetails => {
            // console.log(orderDetails);
            // console.log(orderDetails.order_details);
            for (let order_detail of orderDetails.order_details) {

                let product = {
                    'name' : order_detail.product.name,
                    'price' : order_detail.product.price,
                    'id':order_detail.product.id,
                    'quantity':order_detail.quantity,
                    'order_detail'
                }
                renderOrderDetail(product);
                productOrderList.push(product);
                products.push(product);
            }
            console.log(productOrderList);
        })
        .catch(error => {
            // debugger
            // console.error(error);
        })
}
function loadOrderItems() {
    var tableStatus = PathVariable('tableStatus');
    var tableId = PathVariable('tableId');
    console.log(tableStatus);
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
            'product_id' : productOrderItem.id,
            'quantity': parseInt(productOrderItem.quantity)
        }
        productOrderItemsSave.products.push(product);
    })

    console.log(JSON.stringify(productOrderItemsSave));

    // check table status
    let options = {
        method: 'POST', // *GET, POST, PUT, DELETE, etc.
        headers: {
          'Content-Type': 'application/json'
          // 'Content-Type': 'application/x-www-form-urlencoded',
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
      })
      .catch(error => {
        debugger
        console.log(error.message);
      })
}


function removeOrderDetailNode(orderId) {
    let id = document.querySelector("#order-detail-"+orderId);
    console.log(id);
    id.remove();
}

function deleteElementByUsingAPI(id) {
    let api = `http://localhost:8080/api/v1/timecoffee/order_details/${id}`;
    let options = {
        method: 'DELETE', // Specify the HTTP method
        headers: {
          'Content-Type': 'application/json', // Specify the content type if needed
          // Other headers if required
        },
        // Optionally, include a request body if needed
        body: JSON.stringify({
          // Your request body data
        })
    }
    fetch(api,options)
        .then (response => {
            return response.json();
        })
        .then(data => {
            console.log(data);
        })
}
function deleteOrderDetail(productId) {
    let tableStatus = PathVariable("tableStatus");
    // console.log(product);
    // console
    if (tableStatus == 'true') {
        let product = findProductById(productId, addingElement);
        if (product != null) {
            removeOrderDetailNode(productId);
            return;
        }
        

    } else if (tableStatus == 'false'){
        // console.log("xóa");
        removeOrderDetailNode(productId);
    }
}