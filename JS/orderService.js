let productOrderList = [];
let products = [];
let newProductAdd = [];

function start() {
    let html = '';
    document.querySelector('.order-list').innerHTML = html;
    displayCategory(1);
}
start();


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
            products = data;
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
    for(let i = 0; i <= productOrderList.length; i++) {
        if(productName == productOrderList[i]) {
            alert("Sản phẩm đã tồn tại trong order");
            return;
        }
    }
    productOrderList.push(productName);
    // end check product exist in order
    let orderList = document.querySelector(".order-list");

    const divElement = document.createElement('div');
    divElement.className = `order-item`;
    divElement.id = `order-${productId}`;

    // Kiểm tra xem là node thứ chẵn hay lẻ để thay đổi màu nền
    if (productOrderList.length % 2 === 0) {
        console.log(productOrderList.length);
        divElement.style.backgroundColor = "#D7F1F3"; // Màu nền cho node chẵn
    } else {
        console.log(productOrderList.length);
        divElement.style.backgroundColor = "#ffffff"; // Màu nền cho node lẻ
    }

    let html = `
            <div class="row order-item" text-align: center; padding:4px 0px">
                <div class="col-4">
                    ${productName}
                </div>
                <div class="col-2">
                    <div class="input-wrap">
                        <div class="minus" style="width:30%;cursor: pointer" onclick=minusProduct(${productId})>-</div>
                        <input type="text" style="width:40%;border:1px solid #ccc; text-align:center" value="1" id="input-${productId}">
                        <div class="plus" style="width:30%;cursor: pointer" onclick=plusProduct(${productId})>+</div>
                    </div>
                </div>
                <div class="col-3">
                    ${productPrice} VND
                </div>
                <div class="col-3">
                    <span id="total-price-product-${productId}">${productPrice}</span> VND
                    <span class='delete-btn'>
                        <i class="fa-solid fa-trash" style="color:red"></i>
                    </span>
                </div>
            </div>`;

    divElement.innerHTML = html; 
    console.log(divElement);
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
    let product = findProductById(productId);
    let totalMoney = parseInt(inputValue) * product.price;
    totalMoneyElement.innerHTML = totalMoney; 
}

function plusProduct(productId) {
    const clss = `#input-${productId}`;
    let inputValue = parseInt(document.querySelector(clss).value) + 1;
    document.querySelector(clss).value = inputValue;

    let totalMoneyElement = document.querySelector("#total-price-product-"+productId);
    let product = findProductById(productId);
    let totalMoney = parseInt(inputValue) * product.price;
    totalMoneyElement.innerHTML = totalMoney; 
}

function findProductById(id) {
    for(let product of products) {
        if (product.id == id) {
            return product;
        }
    }
}


