<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Visitor Check-in</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/visitor">Visitor Management</a>
    <a class="button button-outline" href="${pageContext.request.contextPath}/admin/login">Admin login</a>
</header>
<main class="page narrow">
    <section class="card">
        <p class="eyebrow">WELCOME</p>
        <h1>Visitor check-in</h1>
        <p class="muted">Please enter your details to register your visit.</p>
        <c:if test="${param.submitted == '1' and not empty sessionScope.latestVisitorCardId}">
            <div class="notice success" role="status">
                <span>Your visit has been registered. Your visitor pass is ready.</span>
                <a class="button button-primary" href="${pageContext.request.contextPath}/visitor/card?id=${sessionScope.latestVisitorCardId}">Download visitor card</a>
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="notice error" role="alert"><c:out value="${error}"/></div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/visitor" class="form" enctype="multipart/form-data">
            <label for="visitorName">Your name</label>
            <input id="visitorName" name="visitorName" maxlength="120" autocomplete="name"
                   value="<c:out value='${visitorName}'/>" required>

            <label for="mobileNumber">Mobile number</label>
            <input id="mobileNumber" name="mobileNumber" type="tel" maxlength="25"
                   autocomplete="tel" inputmode="tel" placeholder="+1 555 010 1234"
                   value="<c:out value='${mobileNumber}'/>" required>

            <label for="personToVisit">Person you are visiting</label>
            <input id="personToVisit" name="personToVisit" maxlength="120"
                   value="<c:out value='${personToVisit}'/>" required>

            <label for="purpose">Purpose of visit</label>
            <textarea id="purpose" name="purpose" rows="4" maxlength="500" required><c:out value="${purpose}"/></textarea>

            <label for="visitorPhoto">Visitor photo (optional)</label>
            <p class="muted photo-help">Take a quick photo or choose an image. It will appear on your visitor card.</p>
            <div class="camera-controls">
                <button class="button button-outline" type="button" id="startCamera">Open camera</button>
                <button class="button button-outline" type="button" id="takePhoto" hidden>Take photo</button>
            </div>
            <video id="cameraPreview" class="camera-preview" autoplay playsinline hidden></video>
            <img id="photoPreview" class="photo-preview" alt="Selected visitor photo preview" hidden>
            <input id="visitorPhoto" name="visitorPhoto" type="file" accept="image/*" capture="user">
            <p id="cameraMessage" class="muted photo-help" role="status" aria-live="polite"></p>

            <button class="button button-primary" type="submit">Register visit</button>
        </form>
    </section>
</main>
<script src="${pageContext.request.contextPath}/assets/visitor-camera.js"></script>
</body>
</html>
