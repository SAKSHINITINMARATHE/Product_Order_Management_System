const API = 'http://localhost:8085/api';

async function apiCall(endpoint, method = 'GET', body = null) {
    try {
        const options = { method, headers: { 'Content-Type': 'application/json' } };
        if (body) options.body = JSON.stringify(body);

        const res = await fetch(`${API}${endpoint}`, options);
        if (!res.ok) throw new Error(await res.text() || 'Action failed');

        
        const text = await res.text();
        return text ? JSON.parse(text) : null;
    } catch (err) {
        console.error(err);
        alert(err.message);
        return null;
    }
}

function getUser() { return JSON.parse(localStorage.getItem('user')); }

async function auth(action) {
    const isLogin = action === 'login';
    const data = {
        username: document.getElementById(isLogin ? 'username' : 'reg-username').value,
        password: document.getElementById(isLogin ? 'password' : 'reg-password').value,
        role: !isLogin ? document.getElementById('reg-role').value : undefined
    };

    const res = await apiCall(`/auth/${action}`, 'POST', data);

    if (res) {
        if (isLogin) {
            localStorage.setItem('user', JSON.stringify(res));
            if (res.role === 'ADMIN') {
                window.location.href = 'admin.html';
            } else {
                window.location.href = 'index.html';
            }
        } else {
            alert('Registration Successful! Please wait for Admin approval.');
            toggleAuth();
        }
    }
}

function logout() {
    localStorage.removeItem('user');
    window.location.href = 'login.html';
}

function checkAuth() {
    const user = getUser();
    const adminLink = document.getElementById('admin-link');
    const cartLink = document.querySelector('a[href="cart.html"]');
    const loginLink = document.getElementById('login-link');
    const logoutLink = document.getElementById('logout-link');

    if (user) {
        if (loginLink) loginLink.style.display = 'none';
        if (logoutLink) {
            logoutLink.style.display = 'inline';
            logoutLink.textContent = `Logout (${user.username})`;
        }

        
        if (user.role === 'ADMIN') {
            if (adminLink) adminLink.style.display = 'inline';
            if (cartLink) cartLink.style.display = 'none';
        } else {
            if (adminLink) adminLink.style.display = 'none';
            if (cartLink) cartLink.style.display = 'inline';
        }
    } else {
        // Guest
        if (loginLink) loginLink.style.display = 'inline';
        if (logoutLink) logoutLink.style.display = 'none';
        if (adminLink) adminLink.style.display = 'none';
        if (cartLink) cartLink.style.display = 'inline';
    }
}

async function loadProducts(isAdmin = false) {
    const endpoint = isAdmin ? '/products/admin' : '/products';
    const products = await apiCall(endpoint);

    const containerId = isAdmin ? 'admin-products-table' : 'products-grid';
    const container = document.getElementById(containerId);

    if (!products || !container) return;

    const displayProducts = isAdmin ? products : products.filter(p => p.active);

    container.innerHTML = displayProducts.map(p => isAdmin ? `
        <tr>
            <td>${p.id}</td>
            <td>${p.name}</td><td>₹${p.price}</td><td>${p.quantity}</td>
            <td style="color:${p.active ? 'green' : 'red'}">${p.active ? 'Active' : 'Inactive'}</td>
            <td>
                <button onclick="toggleStatus(${p.id}, ${!p.active})">${p.active ? 'Disable' : 'Enable'}</button>
                <button onclick="editProduct(${p.id}, '${p.name}', '${p.tagline}', '${p.description || ''}', ${p.price}, ${p.quantity}, ${p.active})">Edit</button>
                <button onclick="deleteProduct(${p.id})">Delete</button>
            </td>
        </tr>` : `
        <div class="card">
            <h3>${p.name}</h3>
            <p>${p.tagline}</p>
            <p><strong>₹${p.price}</strong> | Qty: ${p.quantity}</p>
            <button onclick="addToCart(${p.id})" class="btn" ${p.quantity < 1 ? 'disabled' : ''}>${p.quantity < 1 ? 'Out of Stock' : 'Add to Cart'}</button>
        </div>`
    ).join('');
}

async function saveProduct() {
    const id = document.getElementById('product-id').value;
    const product = {
        name: document.getElementById('product-name').value,
        tagline: document.getElementById('product-tagline').value,
        description: document.getElementById('product-desc').value,
        price: document.getElementById('product-price').value,
        quantity: document.getElementById('product-qty').value,
        active: document.getElementById('product-status').value === 'true'
    };

    const url = id ? `/products/${id}` : '/products';
    const method = id ? 'PUT' : 'POST';

    if (await apiCall(url, method, product)) {
        alert('Saved!');
        resetForm();
        loadProducts(true);
    }
}

function editProduct(id, name, tagline, desc, price, qty, active) {
    document.getElementById('product-id').value = id;
    document.getElementById('product-name').value = name;
    document.getElementById('product-tagline').value = tagline;
    document.getElementById('product-desc').value = desc;
    document.getElementById('product-price').value = price;
    document.getElementById('product-qty').value = qty;
    document.getElementById('product-status').value = active;
    window.scrollTo(0, 0);
}

function resetForm() {
    document.getElementById('product-id').value = '';
    document.getElementById('product-name').value = '';
    document.getElementById('product-tagline').value = '';
    document.getElementById('product-desc').value = '';
    document.getElementById('product-price').value = '';
    document.getElementById('product-qty').value = '';
    document.getElementById('product-status').value = 'true';
}

async function toggleStatus(id, status) { await apiCall(`/products/${id}/status?isActive=${status}`, 'PATCH'); loadProducts(true); }
async function deleteProduct(id) { if (confirm('Sure?')) { await apiCall(`/products/${id}`, 'DELETE'); loadProducts(true); } }

async function updateCart() {
    const user = getUser();
    if (!user) {
        if (document.getElementById('cart-count')) document.getElementById('cart-count').textContent = '0';
        return;
    }

    const cart = await apiCall(`/cart?userId=${user.id}`);

    if (!cart) {
        if (document.getElementById('cart-count')) document.getElementById('cart-count').textContent = '0';
        return;
    }

    const items = cart.items || [];

    if (document.getElementById('cart-count')) {
        let count = 0;
        for (let item of items) {
            count += item.quantity;
        }
        document.getElementById('cart-count').textContent = count;
    }

    const container = document.getElementById('cart-items');
    if (container) {
        if (items.length > 0) {
            container.innerHTML = items.map(i => `
                <div style="display:flex; justify-content:space-between; border-bottom:1px solid #ddd; padding:10px;">
                    <div><b>${i.productName}</b> <br> ₹${i.price} x ${i.quantity}</div>
                    <button onclick="removeFromCart(${i.productId})" class="btn-danger btn">Remove</button>
                </div>`).join('');
            document.getElementById('cart-total').textContent = cart.totalAmount;
        } else {
            container.innerHTML = '<p>Empty Cart</p>';
            document.getElementById('cart-total').textContent = '0';
        }
    }
}

async function addToCart(pid) {
    const user = getUser();
    if (!user) {
        alert('Please login to add items to cart');
        window.location.href = 'login.html';
        return;
    }
    await apiCall(`/cart/add?userId=${user.id}&productId=${pid}&quantity=1`, 'POST');
    updateCart();
    alert('Added!');
}

async function clearCart() {
    if (confirm('Are you sure you want to clear your cart?')) {
        const user = getUser();
        if (!user) return;
        await apiCall(`/cart/clear?userId=${user.id}`, 'DELETE');
        updateCart();
        alert('Cart Cleared!');
    }
}

async function removeFromCart(pid) { await apiCall(`/cart/remove?userId=${getUser().id}&productId=${pid}`, 'DELETE'); updateCart(); }

async function placeOrder() {
    const user = getUser();
    if (!user) {
        alert('Please login to place order');
        window.location.href = 'login.html';
        return;
    }

    if (await apiCall(`/orders/place?userId=${user.id}`, 'POST')) {
        alert('Order Placed Successfully!');
        updateCart();
        loadOrders();
    }
}

async function loadOrders() {
    const user = getUser();
    if (!user) return;

    const orders = await apiCall(`/orders?userId=${user.id}`);
    const tbody = document.getElementById('orders-table-body');
    if (orders && tbody) {
        tbody.innerHTML = orders.map(o => `
            <tr><td>${o.id}</td><td>${o.status}</td><td>₹${o.totalAmount}</td></tr>
        `).join('');
    }
}
