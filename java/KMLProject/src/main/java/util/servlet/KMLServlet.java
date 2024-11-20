package util.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Map this servlet to the URL pattern "/generateKML"
@WebServlet("/generateKML")
public class KMLServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/vnd.google-earth.kml+xml");
        response.setHeader("Content-Disposition", "attachment; filename=\"generatedKML.kml\"");

        // Generate KML content
        String kmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"
                + "  <Document>\n"
                + "    <name>Generated KML</name>\n"
                + "    <Placemark>\n"
                + "      <name>Example Placemark</name>\n"
                + "      <Point>\n"
                + "        <coordinates>-122.0822035425683,37.42228990140251,0</coordinates>\n"
                + "      </Point>\n"
                + "    </Placemark>\n"
                + "  </Document>\n"
                + "</kml>";

        // Write KML content to the response output stream
        response.getWriter().write(kmlContent);
    }
}