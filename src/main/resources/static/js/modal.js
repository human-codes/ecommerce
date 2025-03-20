// Vanilla JavaScript version for direct HTML integration

// Function to open the modal with product data
function openProductModal(productData) {
    const modal = document.getElementById("productModal")
    const modalProductName = document.getElementById("modalProductName")
    const modalProductPrice = document.getElementById("modalProductPrice")
    const modalProductCategory = document.getElementById("modalProductCategory")
    const modalProductDescription = document.getElementById("modalProductDescription")
    const modalProductImage = document.getElementById("modalProductImage")

    // Set product data
    if (productData) {
        modalProductName.textContent = productData.name
        modalProductPrice.textContent = productData.price
        modalProductCategory.textContent = productData.category
        modalProductDescription.textContent = productData.description
        modalProductImage.src = productData.image
    }

    // Show modal with animation
    modal.style.display = "flex"
    document.body.style.overflow = "hidden"

    // Trigger animations for content
    const animatedElements = document.querySelectorAll(".animate-item")
    animatedElements.forEach((el, index) => {
        el.style.transitionDelay = `${(index + 1) * 100}ms`
        setTimeout(() => {
            el.classList.add("show")
        }, 50)
    })
}

// Function to close the modal
function closeProductModal() {
    const modal = document.getElementById("productModal")

    // Hide modal with animation
    modal.classList.add("closing")
    document.body.style.overflow = "auto"

    // Reset animations
    const animatedElements = document.querySelectorAll(".animate-item")
    animatedElements.forEach((el) => {
        el.classList.remove("show")
    })

    // Complete closing after animation
    setTimeout(() => {
        modal.style.display = "none"
        modal.classList.remove("closing")
    }, 300)
}

// Close modal when clicking outside
window.addEventListener("click", (event) => {
    const modal = document.getElementById("productModal")
    if (event.target === modal) {
        closeProductModal()
    }
})

// Example function to demonstrate opening the modal
function showDemoProduct(product) {
    openProductModal({
        name: product.name,
        price: product.price,
        category: product.category.name,
        description:
            "",
        image: "http://localhost:8080/api/file/"+product.attachment.id
    })
}
//
// // Initialize animations when DOM is loaded
// document.addEventListener("DOMContentLoaded", () => {
//     // Add demo button if needed
//     const demoButton = document.createElement("button")
//     demoButton.textContent = "Open Product Modal"
//     demoButton.className = "demo-button"
//     demoButton.onclick = showDemoProduct
//     document.body.appendChild(demoButton)
// })
//
