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

1. Run the SQL in `database/schema.sql` in your MySQL client. This creates the
   `visitor_management` database and `visitors` table. The web application can
   now start before the database is available, but visitor registration and the
   admin records page require this schema and a reachable MySQL instance.
2. Update `DB_USERNAME` and `DB_PASSWORD` in `.env` for your MySQL account.
   Change `ADMIN_PASSWORD` before using the application.
3. Build the WAR with `mvn package` and deploy
   `target/VisitorManagementSystem-1.0-SNAPSHOT.war` to your Jakarta-compatible
   servlet container.

Open the deployed application's root URL to register a visitor. Use **Admin
login** in the top-right corner to sign in and view visitor records.
