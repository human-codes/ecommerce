// DOM Elements
const inquiryListElement = document.getElementById('inquiryList');
const loadingElement = document.getElementById('loading');
const tableContainerElement = document.getElementById('tableContainer');
const refreshButton = document.getElementById('refreshButton');
const confirmModal = document.getElementById('confirmModal');
const confirmButton = document.getElementById('confirmButton');
const cancelButton = document.getElementById('cancelButton');
const toast = document.getElementById('toast');
const toastTitle = document.getElementById('toastTitle');
const toastMessage = document.getElementById('toastMessage');
const successSound = document.getElementById('successSound');
const errorSound = document.getElementById('errorSound');

// State
let inquiries = [];
let currentUser = 'admin'; // Default user
let selectedInquiryId = null;
//
// // Mock API functions (replace with real API calls)
// async function fetchInquiriesFromAPI() {
//     // Simulate API call
//     return new Promise((resolve) => {
//         setTimeout(() => {
//             resolve([
//                 {
//                     id: 1,
//                     fullName: "Alisher Navoi",
//                     message: "Saytingiz juda yaxshi ishlayapti, lekin mobil versiyasida ba'zi muammolar bor.",
//                     email: "alisher@example.com",
//                     phone: "+998 90 123 45 67",
//                     reviewed: false
//                 },
//                 {
//                     id: 2,
//                     fullName: "Zulfiya Isroilova",
//                     message: "Mahsulotlar haqida ko'proq ma'lumot bo'lsa yaxshi bo'lardi.",
//                     email: "zulfiya@example.com",
//                     phone: "+998 91 234 56 78",
//                     reviewed: true
//                 },
//                 {
//                     id: 3,
//                     fullName: "Abdulla Qodiriy",
//                     message: "Buyurtma berish jarayonini soddalashtirish kerak.",
//                     email: "abdulla@example.com",
//                     phone: "+998 93 345 67 89",
//                     reviewed: false
//                 },
//                 {
//                     id: 4,
//                     fullName: "Muhammadjon Yusupov",
//                     message: "Saytingiz dizayni juda chiroyli, lekin tezligi sekinroq.",
//                     email: "muhammadjon@example.com",
//                     phone: "+998 94 456 78 90",
//                     reviewed: false
//                 },
//                 {
//                     id: 5,
//                     fullName: "Nodira Begim",
//                     message: "Qo'shimcha to'lov usullarini qo'shishingizni so'rayman.",
//                     email: "nodira@example.com",
//                     phone: "+998 95 567 89 01",
//                     reviewed: true
//                 }
//             ]);
//         }, 800);
//     });
// }

async function markInquiryAsReviewed(id) {
    // Simulate API call
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            const inquiry = inquiries.find(item => item.id === id);
            if (!inquiry) {
                reject(new Error("Taklif topilmadi"));
                return;
            }

            if (inquiry.reviewed) {
                reject(new Error("Taklif allaqachon ko'rib chiqilgan"));
                return;
            }

            inquiry.reviewed = true;
            inquiry.reviewedBy = currentUser;
            resolve(inquiry);
        }, 500);
    });
}

// UI Functions
function renderInquiries() {
    if (inquiries.length === 0) {
        inquiryListElement.innerHTML = `
      <tr>
        <td colspan="6" class="empty-message">Hozircha takliflar mavjud emas</td>
      </tr>
    `;
        return;
    }

    inquiryListElement.innerHTML = inquiries.map(inquiry => `
    <tr>
      <td>${inquiry.fullName}</td>
      <td class="message-cell">${inquiry.message}</td>
      <td>${inquiry.email}</td>
      <td>${inquiry.phone}</td>
      <td>
        ${inquiry.reviewed
        ? `<span class="badge badge-success">
              <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                <polyline points="22 4 12 14.01 9 11.01"></polyline>
              </svg>
              Ko'rib chiqilgan
            </span>`
        : `<span class="badge badge-warning">
              <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"></circle>
                <polyline points="12 6 12 12 16 14"></polyline>
              </svg>
              Yangi
            </span>`
    }
      </td>
      <td>
        ${!inquiry.reviewed
        ? `<button class="button button-primary button-sm" onclick="openConfirmModal(${inquiry.id})">Ko'rib chiqildi</button>`
        : ''
    }
      </td>
    </tr>
  `).join('');
}

function showLoading() {
    loadingElement.style.display = 'flex';
    tableContainerElement.style.display = 'none';
}

function hideLoading() {
    loadingElement.style.display = 'none';
    tableContainerElement.style.display = 'block';
}

function openConfirmModal(id) {
    selectedInquiryId = id;
    confirmModal.style.display = 'flex';
}

function closeConfirmModal() {
    confirmModal.style.display = 'none';
    selectedInquiryId = null;
}

function showToast(title, message, type = 'success') {
    toastTitle.textContent = title;
    toastMessage.textContent = message;
    toast.className = `toast ${type}`;

    // Play sound
    if (type === 'success') {
        successSound.play().catch(err => console.error("Error playing sound:", err));
    } else {
        errorSound.play().catch(err => console.error("Error playing sound:", err));
    }

    // Show toast
    setTimeout(() => {
        toast.classList.add('show');
    }, 100);

    // Hide toast after 3 seconds
    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Main Functions
async function fetchInquiries() {
    showLoading();
    try {
        inquiries = await fetchInquiriesFromAPI();
        renderInquiries();
    } catch (error) {
        console.error("Error fetching inquiries:", error);
        showToast("Xatolik", "Takliflarni yuklashda xatolik yuz berdi!", "error");
    } finally {
        hideLoading();
    }
}

async function handleMarkAsReviewed() {
    if (!selectedInquiryId) return;

    closeConfirmModal();

    try {
        await markInquiryAsReviewed(selectedInquiryId);

        // Update UI
        inquiries = inquiries.map(inquiry =>
            inquiry.id === selectedInquiryId
                ? { ...inquiry, reviewed: true, reviewedBy: currentUser }
                : inquiry
        );

        renderInquiries();
        showToast("Muvaffaqiyat", "Taklif ko'rib chiqildi deb belgilandi!");
    } catch (error) {
        console.error("Error marking inquiry as reviewed:", error);
        showToast("Xatolik", error.message || "Taklifni yangilashda xatolik yuz berdi", "error");
    }
}

// Event Listeners
refreshButton.addEventListener('click', fetchInquiries);
confirmButton.addEventListener('click', handleMarkAsReviewed);
cancelButton.addEventListener('click', closeConfirmModal);

// Make openConfirmModal globally available
window.openConfirmModal = openConfirmModal;

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    fetchInquiries();
});