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
            <p class="muted">All visitor check-ins, newest first.</p>
        </div>
        <span class="count"><c:out value="${visitors.size()}"/> visits</span>
    </div>
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
                            <th>Mobile number</th>
                            <th>Person to visit</th>
                            <th>Purpose</th>
                            <th>Check-in time</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="visitor" items="${visitors}">
                            <tr>
                                <td><c:out value="${visitor.visitorName}"/></td>
                                <td><c:out value="${visitor.mobileNumber}"/></td>
                                <td><c:out value="${visitor.personToVisit}"/></td>
                                <td><c:out value="${visitor.purpose}"/></td>
                                <td><c:out value="${visitor.checkInAtDisplay}"/></td>
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
