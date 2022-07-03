package web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@MultipartConfig
@WebServlet(name = "update", urlPatterns = "/test3")
public class TestServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("Test3#doPost");

        Part filePart = req.getPart("file");

        System.out.println("filePart ==> " + filePart);

        String fileName =
                Paths.get(filePart.getSubmittedFileName())
                        .getFileName()
                        .toString();

        System.out.println("fileName ==> " + fileName);

        String outputFile = getServletContext()
                .getRealPath("static/images/")
                .concat(fileName);
        System.out.println("address ==> " + outputFile);


        InputStream fileContent = filePart.getInputStream();
        Files.copy(fileContent,
                Paths.get(outputFile),
                StandardCopyOption.REPLACE_EXISTING);

        req.getSession().setAttribute("uploadedFile", fileName);

        resp.sendRedirect("test3.jsp");
    }
}
