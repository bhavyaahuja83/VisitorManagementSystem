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
        <c:if test="${param.submitted == '1'}">
            <div class="notice success" role="status">Your visit has been registered. Thank you.</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="notice error" role="alert"><c:out value="${error}"/></div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/visitor" class="form">
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

            <button class="button button-primary" type="submit">Register visit</button>
        </form>
    </section>
</main>
</body>
</html>
