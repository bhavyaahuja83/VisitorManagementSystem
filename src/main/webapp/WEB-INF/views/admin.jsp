<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Visitor records</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body>
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/admin">Visitor Management</a>
    <div class="topbar-actions">
        <a class="button button-outline" href="${pageContext.request.contextPath}/visitor">Visitor page</a>
        <form method="post" action="${pageContext.request.contextPath}/admin/logout">
            <button class="button button-outline" type="submit">Log out</button>
        </form>
    </div>
</header>
<main class="page">
    <div class="page-heading">
        <div>
            <p class="eyebrow">ADMINISTRATION</p>
            <h1>Visitor records</h1>
            <p class="muted">Manage current check-ins and review past visits.</p>
        </div>
        <span class="count"><c:out value="${visitors.size()}"/> shown</span>
    </div>
    <nav class="status-tabs" aria-label="Filter visitors">
        <a class="status-tab ${status == 'active' ? 'selected' : ''}" href="${pageContext.request.contextPath}/admin?status=active">Active <span>${activeCount}</span></a>
        <a class="status-tab ${status == 'past' ? 'selected' : ''}" href="${pageContext.request.contextPath}/admin?status=past">Past <span>${pastCount}</span></a>
        <a class="status-tab ${status == 'all' ? 'selected' : ''}" href="${pageContext.request.contextPath}/admin?status=all">All <span>${allCount}</span></a>
    </nav>
    <c:if test="${param.checkedOut == '1'}">
        <div class="notice success" role="status">Visitor checked out.</div>
    </c:if>
    <section class="card table-card">
        <c:choose>
            <c:when test="${empty visitors}">
                <p class="empty-state">No visitors have checked in yet.</p>
            </c:when>
            <c:otherwise>
                <div class="table-scroll">
                    <table>
                        <thead>
                        <tr>
                            <th>Visitor</th>
                            <th>Photo</th>
                            <th>Mobile number</th>
                            <th>Person to visit</th>
                            <th>Purpose</th>
                            <th>Check-in time</th>
                            <th>Check-out time</th>
                            <th>Status / action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="visitor" items="${visitors}">
                            <tr>
                                <td><c:out value="${visitor.visitorName}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${not empty visitor.visitorPhoto}">
                                            <img class="visitor-thumbnail" src="${pageContext.request.contextPath}/admin/photo?id=${visitor.id}" alt="Visitor photo">
                                        </c:when>
                                        <c:otherwise><span class="muted">—</span></c:otherwise>
                                    </c:choose>
                                </td>
                                <td><c:out value="${visitor.mobileNumber}"/></td>
                                <td><c:out value="${visitor.personToVisit}"/></td>
                                <td><c:out value="${visitor.purpose}"/></td>
                                <td><c:out value="${visitor.checkInAtDisplay}"/></td>
                                <td><c:out value="${visitor.checkOutAtDisplay}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty visitor.checkOutAt}">
                                            <form method="post" action="${pageContext.request.contextPath}/admin/checkout" class="checkout-form">
                                                <input type="hidden" name="visitorId" value="${visitor.id}">
                                                <input type="hidden" name="status" value="${status}">
                                                <button class="button button-small" type="submit">Check out</button>
                                            </form>
                                        </c:when>
                                        <c:otherwise><span class="status-past">Past</span></c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
