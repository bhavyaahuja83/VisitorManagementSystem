package com.mycompany.visitormanagementsystem.servlet;

import com.mycompany.visitormanagementsystem.model.Visitor;
import com.mycompany.visitormanagementsystem.repository.VisitorRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javax.imageio.ImageIO;

@WebServlet("/visitor/card")
public class VisitorCardServlet extends HttpServlet {
    private static final int CARD_WIDTH = 1011;
    private static final int CARD_HEIGHT = 638;
    private final VisitorRepository visitorRepository = new VisitorRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        long visitorId = parseId(request.getParameter("id"));
        Object latestCardId = session == null ? null : session.getAttribute("latestVisitorCardId");
        if (!(latestCardId instanceof Long) || ((Long) latestCardId) != visitorId) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Optional<Visitor> matchingVisitor = visitorRepository.findById(visitorId);
            if (matchingVisitor.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            BufferedImage card = render(matchingVisitor.get());
            response.setContentType("image/png");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"visitor-card-" + visitorId + ".png\"");
            response.setHeader("Cache-Control", "no-store");
            ImageIO.write(card, "png", response.getOutputStream());
        } catch (RuntimeException e) {
            getServletContext().log("Could not create the visitor card.", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to create the visitor card.");
        }
    }

    private static long parseId(String value) {
        if (value == null) {
            return -1;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static BufferedImage render(Visitor visitor) throws IOException {
        BufferedImage card = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = card.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setColor(new Color(247, 249, 253));
            graphics.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

            graphics.setColor(new Color(46, 62, 148));
            graphics.fillRoundRect(24, 24, CARD_WIDTH - 48, 112, 24, 24);
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 38));
            graphics.drawString("VISITOR PASS", 58, 76);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
            graphics.drawString("VISITOR MANAGEMENT", 60, 110);

            graphics.setColor(new Color(225, 230, 241));
            graphics.fillRoundRect(58, 174, 244, 320, 20, 20);
            byte[] photoBytes = visitor.getVisitorPhoto();
            BufferedImage photo = photoBytes == null ? null
                    : ImageIO.read(new ByteArrayInputStream(photoBytes));
            if (photo != null) {
                drawCroppedPhoto(graphics, photo, 58, 174, 244, 320);
            } else {
                graphics.setColor(new Color(134, 145, 171));
                graphics.fillOval(139, 244, 82, 82);
                graphics.fillRoundRect(105, 340, 150, 116, 64, 64);
                graphics.setColor(new Color(100, 113, 143));
                graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
                graphics.drawString("NO PHOTO", 138, 474);
            }

            graphics.setColor(new Color(91, 103, 128));
            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
            graphics.drawString("VISITOR NAME", 350, 198);
            graphics.setColor(new Color(25, 34, 55));
            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
            graphics.drawString(fit(visitor.getVisitorName(), 32), 350, 238);

            drawField(graphics, "VISITOR ID", "VM-" + String.format("%06d", visitor.getId()), 350, 293);
            drawField(graphics, "MOBILE", visitor.getMobileNumber(), 350, 360);
            drawField(graphics, "VISITING", visitor.getPersonToVisit(), 350, 427);
            drawField(graphics, "CHECKED IN", visitor.getCheckInAt().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), 350, 494);

            graphics.setColor(new Color(46, 62, 148));
            graphics.fillRoundRect(58, 542, CARD_WIDTH - 116, 56, 14, 14);
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 19));
            graphics.drawString("PLEASE KEEP THIS PASS VISIBLE DURING YOUR VISIT", 78, 578);
        } finally {
            graphics.dispose();
        }
        return card;
    }

    private static void drawField(Graphics2D graphics, String label, String value, int x, int y) {
        graphics.setColor(new Color(91, 103, 128));
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        graphics.drawString(label, x, y);
        graphics.setColor(new Color(25, 34, 55));
        graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 21));
        graphics.drawString(fit(value, 42), x, y + 27);
    }

    private static void drawCroppedPhoto(Graphics2D graphics, BufferedImage source,
            int x, int y, int width, int height) {
        double scale = Math.max((double) width / source.getWidth(), (double) height / source.getHeight());
        int scaledWidth = (int) Math.ceil(source.getWidth() * scale);
        int scaledHeight = (int) Math.ceil(source.getHeight() * scale);
        int drawX = x + (width - scaledWidth) / 2;
        int drawY = y + (height - scaledHeight) / 2;
        java.awt.Shape oldClip = graphics.getClip();
        graphics.setClip(x, y, width, height);
        graphics.drawImage(source, drawX, drawY, scaledWidth, scaledHeight, null);
        graphics.setClip(oldClip);
    }

    private static String fit(String text, int maxCharacters) {
        if (text.length() <= maxCharacters) {
            return text;
        }
        return text.substring(0, maxCharacters - 1) + "…";
    }
}
