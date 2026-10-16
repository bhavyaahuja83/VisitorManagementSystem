<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:url var="activeUrl" value="/admin"><c:param name="status" value="active"/><c:param name="from" value="${from}"/><c:param name="to" value="${to}"/><c:param name="search" value="${search}"/></c:url>
<c:url var="pastUrl" value="/admin"><c:param name="status" value="past"/><c:param name="from" value="${from}"/><c:param name="to" value="${to}"/><c:param name="search" value="${search}"/></c:url>
<c:url var="allUrl" value="/admin"><c:param name="status" value="all"/><c:param name="from" value="${from}"/><c:param name="to" value="${to}"/><c:param name="search" value="${search}"/></c:url>
<c:url var="exportUrl" value="/admin/export"><c:param name="status" value="${status}"/><c:param name="from" value="${from}"/><c:param name="to" value="${to}"/><c:param name="search" value="${search}"/></c:url>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Visitor dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/app.css">
</head>
<body class="admin-body">
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/admin">
        <span class="brand-mark">V</span>
        <span>Visitor<span class="brand-light">desk</span><small>ADMIN CONSOLE</small></span>
    </a>
    <div class="topbar-actions">
        <a class="button button-outline" href="${pageContext.request.contextPath}/visitor">Visitor check-in</a>
        <form method="post" action="${pageContext.request.contextPath}/admin/logout">
            <button class="button button-outline" type="submit">Log out</button>
        </form>
    </div>
</header>
<main class="admin-page">
    <section class="dashboard-heading">
        <div>
            <p class="eyebrow">OVERVIEW</p>
            <h1>Visitor dashboard</h1>
            <p class="muted">A clear view of today's check-ins and your visitor history.</p>
        </div>
        <a class="button button-primary" href="${pageContext.request.contextPath}/visitor">＋ New visitor check-in</a>
    </section>

    <section class="stat-grid" aria-label="Visitor statistics">
        <article class="stat-card stat-accent">
            <span class="stat-icon">↗</span>
            <p>Currently on site</p>
            <strong><c:out value="${activeCount}"/></strong>
            <span class="stat-foot">Active check-ins</span>
        </article>
        <article class="stat-card">
            <span class="stat-icon stat-icon-blue">◷</span>
            <p>Arrivals today</p>
            <strong><c:out value="${todayCount}"/></strong>
            <span class="stat-foot">Check-ins since midnight</span>
        </article>
        <article class="stat-card">
            <span class="stat-icon stat-icon-violet">▤</span>
            <p>In selected dates</p>
            <strong><c:out value="${periodCount}"/></strong>
            <span class="stat-foot">Arrivals in date range</span>
        </article>
        <article class="stat-card">
            <span class="stat-icon stat-icon-green">✓</span>
            <p>Past visits</p>
            <strong><c:out value="${pastCount}"/></strong>
            <span class="stat-foot">Checked-out visitors</span>
        </article>
    </section>

    <section class="dashboard-grid">
        <article class="panel analytics-panel">
            <div class="panel-heading">
                <div>
                    <p class="eyebrow">TRAFFIC</p>
                    <h2>Daily check-ins</h2>
                </div>
                <span class="panel-note">Latest 14 days with arrivals</span>
            </div>
            <c:choose>
                <c:when test="${empty dailyCounts}">
                    <p class="empty-chart">No check-ins found for these dates.</p>
                </c:when>
                <c:otherwise>
                    <div class="chart" role="img" aria-label="Daily visitor check-in counts">
                        <c:forEach var="day" items="${dailyCounts}">
                            <div class="chart-column" title="${day.label}: ${day.count} visitors">
                                <span class="chart-value"><c:out value="${day.count}"/></span>
                                <div class="chart-track"><span class="chart-bar" style="height:${day.barHeight}%"></span></div>
                                <span class="chart-label"><c:out value="${day.label}"/></span>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </article>
        <article class="panel filter-panel">
            <div class="panel-heading">
                <div>
                    <p class="eyebrow">FIND A VISIT</p>
                    <h2>Search &amp; filter</h2>
                </div>
            </div>
            <form method="get" action="${pageContext.request.contextPath}/admin" class="filter-form">
                <input type="hidden" name="status" value="${status}">
                <label for="search">Search visitors</label>
                <input id="search" name="search" type="search" maxlength="100"
                       placeholder="Name, mobile, person or purpose" value="<c:out value='${search}'/>">
                <div class="date-fields">
                    <div>
                        <label for="from">From</label>
                        <input id="from" name="from" type="date" value="<c:out value='${from}'/>">
                    </div>
                    <div>
                        <label for="to">To</label>
                        <input id="to" name="to" type="date" value="<c:out value='${to}'/>">
                    </div>
                </div>
                <div class="filter-actions">
                    <button class="button button-primary" type="submit">Apply filters</button>
                    <a class="button button-quiet" href="${pageContext.request.contextPath}/admin?status=${status}">Clear</a>
                </div>
            </form>
        </article>
    </section>

    <section class="visitors-section">
        <div class="visitors-heading">
            <div>
                <p class="eyebrow">VISIT LOG</p>
                <h2>Visitors</h2>
            </div>
            <a class="button button-outline" href="${exportUrl}">↓ Export CSV</a>
        </div>
        <nav class="status-tabs" aria-label="Filter visitors">
            <a class="status-tab ${status == 'active' ? 'selected' : ''}" href="${activeUrl}">Active <span><c:out value="${activeCount}"/></span></a>
            <a class="status-tab ${status == 'past' ? 'selected' : ''}" href="${pastUrl}">Past <span><c:out value="${pastCount}"/></span></a>
            <a class="status-tab ${status == 'all' ? 'selected' : ''}" href="${allUrl}">All <span><c:out value="${allCount}"/></span></a>
        </nav>
        <c:if test="${param.checkedOut == '1'}">
            <div class="notice success" role="status">Visitor checked out successfully.</div>
        </c:if>
        <section class="panel table-panel">
            <c:choose>
                <c:when test="${empty visitors}">
                    <div class="empty-state">
                        <span class="empty-icon">⌕</span>
                        <strong>No visitors found</strong>
                        <span>Try changing the date range or search terms.</span>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-scroll">
                        <table>
                            <thead>
                            <tr>
                                <th>Visitor</th>
                                <th>Mobile</th>
                                <th>Visiting</th>
                                <th>Purpose</th>
                                <th>Check-in</th>
                                <th>Check-out</th>
                                <th>Status</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="visitor" items="${visitors}">
                                <tr>
                                    <td data-label="Visitor">
                                        <div class="visitor-identity">
                                            <c:choose>
                                                <c:when test="${not empty visitor.visitorPhoto}">
                                                    <img class="visitor-thumbnail" src="${pageContext.request.contextPath}/admin/photo?id=${visitor.id}" alt="">
                                                </c:when>
                                                <c:otherwise><span class="visitor-avatar"><c:out value="${visitor.visitorName.substring(0, 1).toUpperCase()}"/></span></c:otherwise>
                                            </c:choose>
                                            <span><strong><c:out value="${visitor.visitorName}"/></strong><small>VM-<c:out value="${visitor.id}"/></small></span>
                                        </div>
                                    </td>
                                    <td data-label="Mobile"><c:out value="${visitor.mobileNumber}"/></td>
                                    <td data-label="Visiting"><c:out value="${visitor.personToVisit}"/></td>
                                    <td data-label="Purpose" class="purpose-cell"><c:out value="${visitor.purpose}"/></td>
                                    <td data-label="Check-in"><c:out value="${visitor.checkInAtDisplay}"/></td>
                                    <td data-label="Check-out">
                                        <c:choose>
                                            <c:when test="${empty visitor.checkOutAt}"><span class="muted">—</span></c:when>
                                            <c:otherwise><c:out value="${visitor.checkOutAtDisplay}"/></c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td data-label="Status">
                                        <c:choose>
                                            <c:when test="${empty visitor.checkOutAt}">
                                                <form method="post" action="${pageContext.request.contextPath}/admin/checkout" class="checkout-form">
                                                    <input type="hidden" name="visitorId" value="${visitor.id}">
                                                    <input type="hidden" name="status" value="${status}">
                                                    <button class="status-pill status-active" type="submit"><span></span>Check out</button>
                                                </form>
                                            </c:when>
                                            <c:otherwise><span class="status-pill status-past"><span></span>Checked out</span></c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
            <c:if test="${not empty visitors}">
                <footer class="table-footer">Showing <c:out value="${visitors.size()}"/> visitor records</footer>
            </c:if>
        </section>
    </section>
</main>
</body>
</html>
