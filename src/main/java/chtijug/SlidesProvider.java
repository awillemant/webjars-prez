package chtijug;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/slides")
public class SlidesProvider extends HttpServlet {

    private static final String SLIDES = "[[SLIDES]]";

    private static final long serialVersionUID = 189098706874984983L;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();
        Path indexPath = FileUtils.getResourcesPath("index.html");
        String indexContent = new String(Files.readAllBytes(indexPath), "UTF-8");
        int beforeIndex = indexContent.indexOf(SLIDES);
        String before = indexContent.substring(0, beforeIndex);
        writer.print(before);
        Map<String, List<Path>> paths = FileUtils.listSourceFiles("slides", "slide_*.html");
        paths.forEach((k, v) -> {
            writer.print("<section>");
            v.forEach(FileUtils.printLines(writer));
            writer.print("</section>");
        });
        String after = indexContent.substring(beforeIndex + SLIDES.length());
        writer.print(after);
    }
}
