const customerTableElement = document.getElementById("customerTable")
const ordersTableElement = document.getElementById("ordersTable")
const orderItemsTableElement = document.getElementById("orderItemsTable")
const searchInputElement = document.getElementById("searchInput")
const loadingElement = document.getElementById("loading")
const ordersLoadingElement = document.getElementById("ordersLoading")
const orderItemsLoadingElement = document.getElementById("orderItemsLoading")
const ordersModalElement = document.getElementById("ordersModal")
const orderItemsModalElement = document.getElementById("orderItemsModal")
const customerNameElement = document.getElementById("customerName")
const orderTitleElement = document.getElementById("orderTitle")
const orderTotalElement = document.getElementById("orderTotal")
const successSoundElement = document.getElementById("successSound")
const errorSoundElement = document.getElementById("errorSound")
const refreshButtonElement = document.getElementById("refreshButton")


// State
let customers = []
let selectedCustomer = null
let selectedOrder = null

// Track user interaction to enable sound
document.addEventListener("click", () => {
    document.documentElement.setAttribute("data-user-interacted", "true")
})

// Helper Functions
function formatDate(dateString) {
    const date = new Date(dateString)
    return date.toLocaleString("uz-UZ", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    })
}

function formatCurrency(amount) {
    return (
        new Intl.NumberFormat("uz-UZ", {
            style: "decimal",
            minimumFractionDigits: 0,
            maximumFractionDigits: 0,
        }).format(amount) + " so'm"
    )
}

function getStatusBadge(status) {
    switch (status) {
        case "PENDING":
            return '<span class="status-badge status-pending">Kutilmoqda</span>'
        case "COMPLETED":
            return '<span class="status-badge status-completed">Yakunlangan</span>'
        case "CANCELLED":
            return '<span class="status-badge status-cancelled">Bekor qilingan</span>'
        default:
            return '<span class="status-badge status-pending">Kutilmoqda</span>'
    }
}

function showLoading(element) {
    if (element) {
        element.style.display = "flex"
    }
}

function hideLoading(element) {
    if (element) {
        element.style.display = "none"
    }
}

function showToast(title, message, type = "success") {
    const toast = document.getElementById("toast")
    document.getElementById("toastTitle").textContent = title
    document.getElementById("toastMessage").textContent = message
    toast.className = `toast ${type}`

    // Play notification sound only if user has interacted with the page
    if (document.documentElement.hasAttribute("data-user-interacted")) {
        const sound = type === "error" ? errorSoundElement : successSoundElement
        sound.play().catch((err) => console.error("Error playing sound:", err))
    }

    // Show toast
    setTimeout(() => {
        toast.classList.add("show")
    }, 100)

    // Hide toast after 3 seconds
    setTimeout(() => {
        toast.classList.remove("show")
    }, 3000)
}

// API Functions
async function fetchCustomers() {
    showLoading(loadingElement)
    try {
        // Barcha foydalanuvchilarni olish
        const response = await request.get("/admin/all")
        customers = response.data

        // Har bir foydalanuvchi uchun buyurtmalar sonini olish
        for (const customer of customers) {
            try {
                const ordersResponse = await request.get(`/order/user/${customer.id}`)
                customer.orderCount = ordersResponse.data.length
            } catch (error) {
                console.error(`Error fetching orders for user ${customer.id}:`, error)
                customer.orderCount = 0
            }
        }

        renderCustomers()
        showToast("Muvaffaqiyat", "Mijozlar ro'yxati yuklandi")
    } catch (error) {
        console.error("Error fetching customers:", error)
        showToast("Xatolik", "Mijozlar ro'yxatini yuklashda xatolik yuz berdi", "error")
    } finally {
        hideLoading(loadingElement)
    }
}

async function fetchCustomerOrders(customerId) {
    showLoading(ordersLoadingElement)
    try {
        // Mijoz buyurtmalarini olish
        const response = await request.get(`/order/user/${customerId}`)
        const orders = response.data

        // Buyurtmalarni sana bo'yicha tartiblash (eng yangisi birinchi)
        orders.sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate))

        renderOrders(orders)
    } catch (error) {
        console.error("Error fetching customer orders:", error)
        showToast("Xatolik", "Buyurtmalarni yuklashda xatolik yuz berdi", "error")
    } finally {
        hideLoading(ordersLoadingElement)
    }
}

async function fetchOrderItems(orderId) {
    showLoading(orderItemsLoadingElement)
    try {
        // Buyurtma tafsilotlarini olish
        const response = await request.get(`/orderitems/${orderId}`)
        const orderItems = response.data

        renderOrderItems(orderItems)
    } catch (error) {
        console.error("Error fetching order items:", error)
        showToast("Xatolik", "Buyurtma tafsilotlarini yuklashda xatolik yuz berdi", "error")
    } finally {
        hideLoading(orderItemsLoadingElement)
    }
}

// Render Functions
function renderCustomers() {
    if (customers.length === 0) {
        customerTableElement.innerHTML = `
            <tr>
                <td colspan="6" class="empty-message">NOT CUSTOMERS</td>
            </tr>
        `
        return
    }

    customerTableElement.innerHTML = customers
        .map(
            (customer) => `
        <tr>
            <td>${customer.id}</td>
            <td>${customer.username}</td>
            <td>${customer.email}</td>
            <td>${customer.phone}</td>
            <td>
                <span class="badge badge-primary">${customer.orderCount || 0}</span>
            </td>
            <td>
                <button class="button button-primary button-sm" onclick="viewCustomerOrders(${customer.id}, '${customer.username}')">
                    VIEW ORDERS
                </button>
            </td>
        </tr>
    `,
        )
        .join("")
}

function renderOrders(orders) {
    if (orders.length === 0) {
        ordersTableElement.innerHTML = `
            <tr>
                <td colspan="7" class="empty-message">Orders not found</td>
            </tr>
        `
        return
    }

    ordersTableElement.innerHTML = orders
        .map(
            (order) => `
        <tr>
            <td>${order.id}</td>
            <td>${formatDate(order.orderDate)}</td>
            <td>${getStatusBadge(order.status)}</td>
            <td>${order.phoneNumber}</td>
            <td>${order.deliveredAddress ? order.deliveredAddress.fullAddress : "-"}</td>
            <td>${order.notes || "-"}</td>
            <td>
                <button class="button button-primary button-sm" onclick="viewOrderItems(${order.id})">
                    VIEW DETAILS
                </button>
            </td>
        </tr>
    `,
        )
        .join("")
}

function renderOrderItems(items) {
    if (items.length === 0) {
        orderItemsTableElement.innerHTML = `
            <tr>
                <td colspan="5" class="empty-message">Buyurtma tafsilotlari mavjud emas</td>
            </tr>
        `
        orderTotalElement.textContent = formatCurrency(0)
        return
    }

    let total = 0

    orderItemsTableElement.innerHTML = items
        .map((item) => {
            const itemTotal = item.price * item.quantity
            total += itemTotal

            return `
            <tr>
                <td>${item.id}</td>
                <td>${item.product ? item.product.name : "Mahsulot"}</td>
                <td>${formatCurrency(item.price)}</td>
                <td>${item.quantity}</td>
                <td>${formatCurrency(itemTotal)}</td>
            </tr>
        `
        })
        .join("")

    orderTotalElement.textContent = formatCurrency(total)
}

// Event Handlers
function viewCustomerOrders(customerId, customerName) {
    selectedCustomer = customers.find((c) => c.id === customerId)
    customerNameElement.textContent = `${customerName} - Orders`
    ordersModalElement.style.display = "flex"
    fetchCustomerOrders(customerId)
}

function viewOrderItems(orderId) {
    selectedOrder = orderId
    orderTitleElement.textContent = `Order #${orderId} details`
    orderItemsModalElement.style.display = "flex"
    fetchOrderItems(orderId)
}

function closeOrdersModal() {
    ordersModalElement.style.display = "none"
    selectedCustomer = null
}

function closeOrderItemsModal() {
    orderItemsModalElement.style.display = "none"
    selectedOrder = null
}

function searchCustomers() {
    const searchTerm = searchInputElement.value.toLowerCase()

    if (!searchTerm) {
        renderCustomers()
        return
    }

    const filteredCustomers = customers.filter(
        (customer) =>
            customer.username.toLowerCase().includes(searchTerm) ||
            customer.email.toLowerCase().includes(searchTerm) ||
            customer.phone.toLowerCase().includes(searchTerm),
    )

    if (filteredCustomers.length === 0) {
        customerTableElement.innerHTML = `
            <tr>
                <td colspan="6" class="empty-message">Qidiruv bo'yicha mijozlar topilmadi</td>
            </tr>
        `
    } else {
        customerTableElement.innerHTML = filteredCustomers
            .map(
                (customer) => `
            <tr>
                <td>${customer.id}</td>
                <td>${customer.username}</td>
                <td>${customer.email}</td>
                <td>${customer.phone}</td>
                <td>
                    <span class="badge badge-primary">${customer.orderCount || 0}</span>
                </td>
                <td>
                    <button class="button button-primary button-sm" onclick="viewCustomerOrders(${customer.id}, '${customer.username}')">
                        VIEW
                    </button>
                </td>
            </tr>
        `,
            )
            .join("")
    }
}

// Event Listeners
searchInputElement.addEventListener("input", searchCustomers)
refreshButtonElement.addEventListener("click", fetchCustomers)

// Initialize
document.addEventListener("DOMContentLoaded", () => {
    fetchCustomers()
})

// Global functions for modal buttons
window.viewCustomerOrders = viewCustomerOrders
window.viewOrderItems = viewOrderItems
window.closeOrdersModal = closeOrdersModal
window.closeOrderItemsModal = closeOrderItemsModal

