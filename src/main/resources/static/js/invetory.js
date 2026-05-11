// Sidebar toggle
function toggleSidebar() {
    document.body.classList.toggle('sidebar-open');

    const hamburger = document.querySelector('.hamburger');
    if (document.body.classList.contains('sidebar-open')) {
        hamburger.style.display = 'none';
    } else {
        hamburger.style.display = 'block';
    }
}

// Close sidebar on click outside
window.addEventListener('click', function(e) {
    const sidebar = document.getElementById('sidebar');
    const hamburger = document.querySelector('.hamburger');
    if (!sidebar.contains(e.target) && !hamburger.contains(e.target) && document.body.classList.contains('sidebar-open')) {
        document.body.classList.remove('sidebar-open');
        hamburger.style.display = 'block'; 
    }
});

// Image preview before upload (for admin or worker)
document.getElementById('imageFile')?.addEventListener('change', function(event) {
    const fileInput = event.target;
    const preview = document.getElementById('previewImage');
    const uploadBtn = document.getElementById('uploadBtn');

    if (fileInput.files && fileInput.files[0]) {
        const reader = new FileReader();
        reader.onload = function(e) {
            if(preview) preview.src = e.target.result;
        }
        reader.readAsDataURL(fileInput.files[0]);
        if(uploadBtn) uploadBtn.disabled = false;
    } else {
        if(preview) preview.src = document.querySelector('.sidebar img')?.src;
        if(uploadBtn) uploadBtn.disabled = true;
    }
});

function openImagePopup(imgId = 'profilePic', popupId = 'imagePopup', popupImgId = 'popupImage') {
    const popup = document.getElementById(popupId);
    const popupImg = document.getElementById(popupImgId);
    const profileImg = document.getElementById(imgId) || document.querySelector('.sidebar img');

    if(profileImg && popupImg && popup) {
        popupImg.src = profileImg.src;
        popup.style.display = "block";
    }
}

function closeImagePopup(popupId = 'imagePopup') {
    const popup = document.getElementById(popupId);
    if(popup) popup.style.display = 'none';
}

function uploadProfileImage(input) {
    const file = input.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("imageFile", file);

    fetch('/updateAdminImage', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            return response.text();
        } else {
            throw new Error("Upload failed");
        }
    })
    .then(result => {
        if (result === "success") {

            // update image instantly (no refresh)
            const profileImg = document.getElementById("adminProfileImg");

            if (profileImg) {
                profileImg.src = URL.createObjectURL(file);
            }

            alert("Profile image updated successfully!");
        } else {
            alert("Upload failed!");
        }
    })
    .catch(error => {
        console.error("Upload error:", error);
        alert("Error uploading image.");
    });
}

// Worker image live upload
function uploadWorkerProfileImage(input) {
    const file = input.files[0];
    if (!file) return;

    const email = document.getElementById("workerProfilePic").getAttribute("data-email");

    const formData = new FormData();
    formData.append("imageFile", file);
    formData.append("workerEmail", email);

    fetch('/worker/uploadImage', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            alert("Profile image updated successfully!");
            location.reload(); // reload to show updated image
        } else {
            alert("Failed to upload image.");
        }
    })
    .catch(error => {
        console.error(error);
        alert("Error uploading image.");
    });
}


// Shortcut functions for popup open/close for workers
function openWorkerImagePopup() {
    openImagePopup('workerProfilePic', 'workerImagePopup', 'popupWorkerImage');
}
function closeWorkerImagePopup() {
    closeImagePopup('workerImagePopup');
}

function confirmDelete(deptId) {
        if (confirm("If you delete this Department, the HOD will also be deleted along with all related data. Delete?")) {
            window.location.href = '/departments/delete/' + deptId;
        }
    }
