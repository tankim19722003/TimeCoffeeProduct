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
    displayCategory(1);
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
                        <div class="category-item active-category id-${category.id}" onclick="displayProducts(${category.id})">
                            ${category.name}
                        </div>
                    `;
                } else {
                    html += `
                        <div class="category-item id-${category.id}" onclick="displayProducts(${category.id})">
                        ${category.name}
                        </div>
                    `;
                }
            }
            html += `<div class="category-item category-item_add" onclick="showAddCategory(${categoryId})" style="text-align:center; cursor:pointer; background-color:#fff; 
            border:1px dotted #ccc ">
                <img src="../images/plus.png" id="category-plus-icon" style = "width:15px;height:15px;"/>
            </div>`;
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
                <div class="col-4">
                    <div class="product-item product-item-${product.id}"  style="position:relative">
                        <span onclick=removeProduct(${product.id}) id="remove-product-icon">x</span>
                        <p class="product-name">${product.name}</p>
                        <p class="product-price">${product.price}/ly</p>
                    </div>
                </div>
                `;
            }

            html +=`
            <div class="col-4 product-item-add">
                <div class="product-item" onclick="showAddProduct(${categoryId})" 
                style = "display:flex; align-items:center; justify-content:center;height:48px; background-color:white; border:2px dotted #ccc">
                    <img src="../images/plus.png" id="table-plus-icon" style = "width:20px"/>
                </div>
            </div>
            `;
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


function getToken() {
    let userJSON = localStorage.getItem("userInfor");
    if (userJSON != null) {
        let user = JSON.parse(userJSON);
        return user.token;
    } else {
        return null;
    }
}

// add category 
function saveCategory() {
    let url = "http://localhost:8080/api/v1/timecoffee/category";
    let categoryName = document.querySelector(".add-category_body-input").value;
    
    // check null category name
    if (categoryName == "") {
        alert("Category's name can't be null");
        return;
    }
    let options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
        body: JSON.stringify({
            "name": categoryName
        })
    }

    fetch(url, options)
        .then(response => {
            return response.json();
        })
        .then(category => {
            alert("thêm thành công");
                // remove category add
            document.querySelector(".category-item_add").remove();

            // add category
            html = `<div class="category-item id-${category.id}" onclick="displayProducts(${category.id})">
                 ${category.name}
            </div>`;

            // add node plus again
            html += `<div class="category-item category-item_add" onclick="addCategory()" style="text-align:center; cursor:pointer; background-color:#fff; 
            border:1px dotted #ccc ">
                <img src="../images/plus.png" id="category-plus-icon" style = "width:15px;height:15px;"/>
            </div>`;

            let categoryList = document.querySelector(".category-list");
            categoryList.innerHTML += html;
            closeAdd();

        })
}

function showAddCategory() {
    // show body adding form
    document.querySelector(".add").classList.remove("d-none");

    //show add form
    document.querySelector(".add-category").classList.remove("d-none");
}
// add product 

// close add 
function closeAdd() {
    document.querySelector(".add").classList.add("d-none");
    let categoryAdding = document.querySelector(".add-category");
    categoryAdding.classList.add("d-none");
    document.querySelector(".add-product").classList.add("d-none");

    // reset value of input
    document.querySelector(".add-category_body-input").value = "";
    document.querySelector(".add-product_body-input-name").value = "";
    document.querySelector(".add-product_body-input-price").value = "";
}

function showAddProduct(cateogryId) {
        // show body adding form
        document.querySelector(".add").classList.remove("d-none");

        //show add product form
        document.querySelector(".add-product").classList.remove("d-none");

        // add categoryId 
        document.querySelector("#category-id").value = cateogryId;
}

function saveProduct() {
    debugger
    let name = document.querySelector(".add-product_body-input-name").value;
    let price = document.querySelector(".add-product_body-input-price").value;
    let categoryId =document.querySelector("#category-id").value;
    if (name == '' || price =='') {
        alert("Vui lòng điền đủ thông tin");
        return;
    }
    
    let urlSavingProduct = 'http://localhost:8080/api/v1/timecoffee/product';
    let data = {
        "name":name,
        "price":price,
        "category_id":categoryId
    }

    let options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
        body: JSON.stringify(data)
    }

    fetch(urlSavingProduct, options)
        .then(response => {
            return response.json();
        })
        .then(product => {
            debugger
            alert("Add product successfully");
            let productWrap = document.querySelector(".product");
            let plusAddProduct =`<div class="col-4 product-item-add">
                <div class="product-item" onclick="showAddProduct(${categoryId})" 
                style = "display:flex; align-items:center; justify-content:center;height:48px; background-color:white; border:2px dotted #ccc">
                    <img src="../images/plus.png" id="table-plus-icon" style = "width:20px"/>
                </div>
            </div>
            `;
            // remove temp plus product
            document.querySelector(".product-item-add").remove();

            let html = productWrap.innerHTML;
            // add product to node
            html +=`
            <div class="col-4">
                <div class="product-item product-item-${product.id}"  style="position:relative">
                    <span onclick=removeTable(${product.id}) id="remove-product-icon">x</span>
                    <p class="product-name">${product.name}</p>
                    <p class="product-price">${product.price}/ly</p>
                </div>
            </div>
            `;
            
            // re add plus icon add product 
            html += plusAddProduct;
            productWrap.innerHTML = html;
            closeAdd();
        })
}

function removeProduct(productId) {
    let urlSavingProduct = 'http://localhost:8080/api/v1/timecoffee/product/'+productId;
    let options = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${getToken()}` // Include the token in the Authorization header
        },
    }

    fetch(urlSavingProduct, options)
        .then(() => {
            // notify delete status
        alert("Xóa thành công");
            // remove temp plus product
            document.querySelector(".product-item-"+productId).remove();
        })
}