<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Visitor check-in</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body class="visitor-body">
<header class="topbar visitor-topbar">
    <a class="brand" href="${pageContext.request.contextPath}/visitor">
        <span class="brand-mark">V</span>
        <span>Visitor<span class="brand-light">desk</span><small>WELCOME CENTER</small></span>
    </a>
    <a class="button button-outline" href="${pageContext.request.contextPath}/admin/login">Admin sign in <span aria-hidden="true">↗</span></a>
</header>
<main class="visitor-page">
    <section class="visitor-hero">
        <div class="hero-copy">
            <span class="hero-badge"><span></span> VISITOR RECEPTION</span>
            <h1>Good to have<br>you <em>here.</em></h1>
            <p>Complete your check-in and receive a digital visitor pass. It only takes a moment.</p>
            <div class="hero-points">
                <span><b>01</b> Add your details</span>
                <span><b>02</b> Get your visitor pass</span>
            </div>
        </div>
        <div class="hero-art" aria-hidden="true">
            <div class="art-orbit orbit-one"></div>
            <div class="art-orbit orbit-two"></div>
            <div class="art-building">
                <div class="building-roof"></div>
                <div class="building-top"><i></i><i></i><i></i></div>
                <div class="building-body"><i></i><i></i><i></i><i></i><i></i><i></i><i></i><i></i></div>
                <div class="building-door"></div>
            </div>
            <div class="art-card"><span>VISITOR PASS</span><b>Welcome in.</b><i>CHECK-IN COMPLETE</i></div>
            <div class="art-spark spark-one">✦</div>
            <div class="art-spark spark-two">✳</div>
        </div>
    </section>

    <section class="checkin-layout">
        <aside class="checkin-aside">
            <p class="eyebrow">START YOUR VISIT</p>
            <h2>Let's get you checked in.</h2>
            <p class="muted">Your information helps us welcome you and let your host know you've arrived.</p>
            <div class="aside-note">
                <span class="note-icon">✦</span>
                <span><strong>Your privacy matters</strong><small>Your details are used only to manage this visit.</small></span>
            </div>
            <div class="aside-hours"><span class="live-dot"></span> Reception is ready for you</div>
        </aside>
        <section class="checkin-card">
            <div class="form-heading">
                <div>
                    <span class="step-label">VISITOR DETAILS</span>
                    <h2>Check in</h2>
                </div>
                <span class="required-note"><i>*</i> Required fields</span>
            </div>
            <c:if test="${param.submitted == '1' and not empty sessionScope.latestVisitorCardId}">
                <div class="notice success card-ready" role="status">
                    <div><strong>You're checked in!</strong><span>Your visitor pass is ready to download.</span></div>
                    <a class="button button-primary" href="${pageContext.request.contextPath}/visitor/card?id=${sessionScope.latestVisitorCardId}">↓ Download pass</a>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="notice error" role="alert"><c:out value="${error}"/></div>
            </c:if>
            <form method="post" action="${pageContext.request.contextPath}/visitor" class="form visitor-form" enctype="multipart/form-data">
                <div class="form-row">
                    <div class="field">
                        <label for="visitorName">Your full name <i>*</i></label>
                        <input id="visitorName" name="visitorName" maxlength="120" autocomplete="name"
                               placeholder="e.g. Alex Morgan" value="<c:out value='${visitorName}'/>" required>
                    </div>
                    <div class="field">
                        <label for="mobileNumber">Mobile number <i>*</i></label>
                        <input id="mobileNumber" name="mobileNumber" type="tel" maxlength="25"
                               autocomplete="tel" inputmode="tel" placeholder="+1 555 010 1234"
                               value="<c:out value='${mobileNumber}'/>" required>
                    </div>
                </div>
                <div class="field">
                    <label for="personToVisit">Who are you here to see? <i>*</i></label>
                    <input id="personToVisit" name="personToVisit" maxlength="120"
                           placeholder="Enter your host's name" value="<c:out value='${personToVisit}'/>" required>
                </div>
                <div class="field">
                    <label for="purpose">What's the purpose of your visit? <i>*</i></label>
                    <textarea id="purpose" name="purpose" rows="3" maxlength="500"
                              placeholder="A short description is perfect" required><c:out value="${purpose}"/></textarea>
                </div>

                <div class="photo-field">
                    <div class="photo-heading">
                        <div><label for="visitorPhoto">Add a photo <span>(optional)</span></label><small>Personalize the photo on your visitor pass.</small></div>
                        <span class="photo-label">PHOTO ID</span>
                    </div>
                    <div class="photo-tools">
                        <div class="photo-placeholder" id="photoPlaceholder" aria-hidden="true"><span>+</span></div>
                        <div class="photo-actions">
                            <div class="camera-controls">
                                <button class="button button-outline" type="button" id="startCamera">Open camera</button>
                                <button class="button button-outline" type="button" id="takePhoto" hidden>Take photo</button>
                            </div>
                            <input id="visitorPhoto" name="visitorPhoto" type="file" accept="image/*" capture="user">
                            <small class="muted">JPG, PNG or WebP. Up to 5 MB.</small>
                        </div>
                    </div>
                    <video id="cameraPreview" class="camera-preview" autoplay playsinline hidden></video>
                    <img id="photoPreview" class="photo-preview" alt="Selected visitor photo preview" hidden>
                    <p id="cameraMessage" class="muted photo-help" role="status" aria-live="polite"></p>
                </div>
                <button class="button button-primary submit-visit" type="submit">Complete check-in <span aria-hidden="true">→</span></button>
                <p class="form-footnote">By checking in, you agree that your visit details can be recorded for reception purposes.</p>
            </form>
        </section>
    </section>
    <footer class="visitor-footer"><span>VisitorDesk</span><span>A smooth welcome starts here.</span></footer>
</main>
<script src="${pageContext.request.contextPath}/assets/visitor-camera.js"></script>
</body>
</html>
