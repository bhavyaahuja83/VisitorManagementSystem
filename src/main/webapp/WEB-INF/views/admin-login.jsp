<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/visitor">Visitor Management</a>
    <a class="button button-outline" href="${pageContext.request.contextPath}/visitor">Visitor check-in</a>
</header>
<main class="page narrow">
    <section class="card">
        <p class="eyebrow">ADMINISTRATION</p>
        <h1>Admin login</h1>
        <p class="muted">Sign in to view registered visitors.</p>
        <c:if test="${not empty error}">
            <div class="notice error" role="alert"><c:out value="${error}"/></div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/admin/login" class="form">
            <label for="username">Username</label>
            <input id="username" name="username" autocomplete="username" required>
            <label for="password">Password</label>
            <input id="password" name="password" type="password" autocomplete="current-password" required>
            <button class="button button-primary" type="submit">Sign in</button>
        </form>
    </section>
</main>
</body>
</html>
