(() => {
    const photoInput = document.getElementById("visitorPhoto");
    const preview = document.getElementById("photoPreview");
    const video = document.getElementById("cameraPreview");
    const startButton = document.getElementById("startCamera");
    const takeButton = document.getElementById("takePhoto");
    const message = document.getElementById("cameraMessage");
    let cameraStream;
    let previewUrl;

    const stopCamera = () => {
        if (cameraStream) {
            cameraStream.getTracks().forEach((track) => track.stop());
            cameraStream = undefined;
        }
        video.hidden = true;
        takeButton.hidden = true;
    };

    const showPreview = (file) => {
        if (previewUrl) {
            URL.revokeObjectURL(previewUrl);
        }
        previewUrl = URL.createObjectURL(file);
        preview.src = previewUrl;
        preview.hidden = false;
    };

    photoInput.addEventListener("change", () => {
        const [file] = photoInput.files;
        if (file) {
            stopCamera();
            showPreview(file);
            message.textContent = "";
        }
    });

    startButton.addEventListener("click", async () => {
        if (!navigator.mediaDevices?.getUserMedia) {
            message.textContent = "Camera access is unavailable here. Use the image picker below instead.";
            return;
        }
        try {
            stopCamera();
            cameraStream = await navigator.mediaDevices.getUserMedia({
                video: { facingMode: "user" },
                audio: false
            });
            video.srcObject = cameraStream;
            video.hidden = false;
            takeButton.hidden = false;
            message.textContent = "Camera ready. Take a photo or choose an image below.";
        } catch {
            message.textContent = "Camera permission was unavailable. Use the image picker below instead.";
        }
    });

    takeButton.addEventListener("click", () => {
        const canvas = document.createElement("canvas");
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;
        canvas.getContext("2d").drawImage(video, 0, 0);
        canvas.toBlob((blob) => {
            if (!blob) {
                message.textContent = "Could not capture the photo. Please try again or choose an image.";
                return;
            }
            const photo = new File([blob], "visitor-photo.jpg", { type: "image/jpeg" });
            const transfer = new DataTransfer();
            transfer.items.add(photo);
            photoInput.files = transfer.files;
            showPreview(photo);
            message.textContent = "Photo captured.";
            stopCamera();
        }, "image/jpeg", 0.9);
    });

    window.addEventListener("pagehide", stopCamera);
})();
