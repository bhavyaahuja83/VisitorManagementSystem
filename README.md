# Visitor Management System

## Requirements

- Java 17
- Maven
- MySQL
- A Jakarta Servlet 6.0/JSP 3.1 compatible servlet container, such as Tomcat 10.1

The visitor check-in page is public. The admin page requires the username and
password configured in the project-root `.env` file. The application reads
`.env` from the process working directory or by searching upward from the
deployed application classes; operating-system environment variables with the
same names take precedence. You can set the JVM property
`-Dvisitor.management.env=C:\path\to\.env` to point to a specific file.

## Setup

1. If this is a new database, run `database/schema.sql` in your MySQL client.
   If you created the database using an earlier version of the app, run
   `database/migrations/002_visitor_cards_and_checkout.sql` once to add photo
   storage and check-out timestamps.
2. Update `DB_USERNAME` and `DB_PASSWORD` in `.env` for your MySQL account.
   Change `ADMIN_PASSWORD` before using the application.
3. Build the WAR with `mvn package` and deploy
   `target/VisitorManagementSystem-1.0-SNAPSHOT.war` to your Jakarta-compatible
   servlet container.

Open the deployed application's root URL to register a visitor and download a
PNG visitor pass after check-in. Photo capture is optional; the browser asks
for camera access when **Open camera** is selected. Camera capture requires
HTTPS, except on `localhost`; the image picker remains available as a fallback.
Photos are stored with visitor records in MySQL.

Use **Admin login** in the top-right corner to see the **Active**, **Past**, and
**All** visitor lists. Use **Check out** on an active visitor to record the
check-out time and move that visit to the past list.
