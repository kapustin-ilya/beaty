package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.entities.Category;
import web.entities.Master;
import web.entities.User;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.CategoryService;
import web.service.MasterService;
import web.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.stream.Collectors;

@MultipartConfig
public class ProfileUpdateController implements Command {
    static final Logger userLogger = LogManager.getLogger(ProfileUpdateController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        cleanSession(session);

        String password = req.getParameter("password");
        String surname = req.getParameter("surname");
        String name = req.getParameter("name");
        String phoneNumber = req.getParameter("phoneNumber");

        User user = (User) session.getAttribute("user");

        user.setPassword(password);
        user.setName(name);
        user.setSername(surname);
        user.setPhoneNumber(phoneNumber);
        try{
            if(user.getRoleId() == 3) {
                String levelQuality = req.getParameter("levelQuality");
                Integer categoryIdAdd = Integer.parseInt(req.getParameter("categoryIdAdd"));
                Integer categoryId = Integer.parseInt(req.getParameter("categoryId"));
                Master master = (Master) session.getAttribute("masterUser");
                master.setCategories(master.getCategories().stream().filter(c->c.getId() != categoryId).collect(Collectors.toSet()));

                try {
                    Part filePart = req.getPart("file");
                    String fileName =
                            Paths.get(filePart.getSubmittedFileName())
                                    .getFileName()
                                    .toString();

                    if (!(fileName.equals("") || fileName == null)) {
                        String outputFile = session.getServletContext()
                                .getRealPath("static/images/")
                                .concat(fileName);
                        InputStream fileContent = filePart.getInputStream();
                        Files.copy(fileContent,
                                Paths.get(outputFile),
                                StandardCopyOption.REPLACE_EXISTING);

                        master.setAdressPhoto(fileName);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    userLogger.error(String.format("Problem with load new file. (%d userId)", user.getId()), e);
                } catch (ServletException e) {
                    e.printStackTrace();
                }

                Category categoryAdd = CategoryService.getCategoryById(categoryIdAdd);
                Set<Category> categorySet = master.getCategories();
                if (categorySet.stream().filter(c -> c.getId() == categoryIdAdd).findFirst().isEmpty() && categoryAdd != null){
                    categorySet.add(categoryAdd);
                }
                master.setCategories(categorySet);
                master.setLevelQuality(levelQuality);

                master = MasterService.updateMaster(master);
                session.setAttribute("masterUser", master);
            }
            user = UserService.updateUser(user);
            userLogger.info(String.format("User (%d userId) update own profile.", user.getId()));
            session.setAttribute("user",user);
            return new ProfileCabinetController().execute(req,resp);
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        } catch (NumberFormatException e) {
            userLogger.error("User tried enter wrong parametrs",e.fillInStackTrace());
            return "index.jsp";
        }
    }
}
