package web.controller.commads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.controller.Command;
import web.entities.Category;
import web.entities.GeneralCategory;
import web.entities.Master;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.CategoryService;
import web.service.MasterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CategoryController implements Command {
    static final Logger userLogger = LogManager.getLogger(CategoryController.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        HttpSession session =  req.getSession();
        cleanSession(session);

        try {
            List<GeneralCategory> generalCategoryList = CategoryService.getAllGeneralCategories();
            List<Master> masterList = null;
            String sqlRequestMasterService = "";
            List<Object> parameters = new ArrayList<>();

            Integer idGeneralCategory;
            if (session.getAttribute("idGeneralCategory" ) != null) {
                idGeneralCategory = req.getAttribute("generalCategory") != null
                        ? Integer.parseInt(req.getAttribute("generalCategory").toString()) : (Integer) session.getAttribute("idGeneralCategory");
            } else {
                idGeneralCategory = -1;
            }

            if ((req.getAttribute("generalCategory") != null && req.getAttribute("generalCategory").equals("-1")) || idGeneralCategory==-1 ){
                if (req.getParameter("generalCategory") != null) {
                    session.removeAttribute("idCategory");
                    session.removeAttribute("search");
                    session.removeAttribute("rating");
                    session.removeAttribute("name");
                }
                session.setAttribute("lowLevel", false);
                sqlRequestMasterService = "select * from master m join user u on m.user_id=u.id ";
                session.setAttribute("idGeneralCategory", Integer.valueOf(-1));

            }
            if ((req.getAttribute("generalCategory") != null && !req.getAttribute("generalCategory").equals("-1")) || idGeneralCategory!=-1) {
                if (req.getParameter("generalCategory") != null) {
                    session.removeAttribute("idCategory");
                    session.removeAttribute("search");
                    session.removeAttribute("rating");
                    session.removeAttribute("name");
                }
                session.setAttribute("lowLevel", false);
                idGeneralCategory = req.getAttribute("generalCategory") != null?
                        Integer.parseInt(req.getAttribute("generalCategory").toString()) : idGeneralCategory;
                session.setAttribute("idGeneralCategory", idGeneralCategory);

                sqlRequestMasterService = "select * from master m join user u on m.user_id=u.id where m.id = any " +
                        "(select master_id from master_has_category where category_id = any (select id from category where general_category_id = ?)) ";
                parameters = new ArrayList<>();
                parameters.add(idGeneralCategory);
            }
            if (req.getAttribute("category") != null || session.getAttribute("idCategory") != null){
                if (req.getParameter("category") != null) {
                    session.removeAttribute("search");
                    session.removeAttribute("rating");
                    session.removeAttribute("name");
                }
                sqlRequestMasterService = "select * from master m join user u on m.user_id=u.id where m.id = any " +
                        "(select master_id from master_has_category where category_id = any (select id from category where id = ?)) ";
                parameters = new ArrayList<>();
                parameters.add(req.getAttribute("category")!=null?
                        Integer.parseInt(req.getAttribute("category").toString()):(Integer)session.getAttribute("idCategory"));

                Category category = CategoryService.getCategoryById(req.getAttribute("category")!=null?
                        Integer.parseInt(req.getAttribute("category").toString()):(Integer)session.getAttribute("idCategory"));

                session.setAttribute("idGeneralCategory", category.getGeneralCategoryId());
                session.setAttribute("idCategory", category.getId());
                session.setAttribute("lowLevel", true);
                session.setAttribute("priceLow", category.getPriceLow());
                session.setAttribute("priceHight", category.getPriceHight());
            }

            if (req.getParameter("search") !=null || session.getAttribute("search") != null){
                if (req.getParameter("search") != null) {
                    session.removeAttribute("idCategory");
                    session.removeAttribute("rating");
                    session.removeAttribute("name");
                }
                session.setAttribute("lowLevel", false);
                session.setAttribute("idGeneralCategory", Integer.valueOf(-1));
                sqlRequestMasterService = "select * from master m join user u on m.user_id=u.id where u.name like ? ";
                String search = req.getParameter("search") !=null ? req.getParameter("search") : session.getAttribute("search").toString();
                session.setAttribute("search",search);
                parameters = new ArrayList<>();
                parameters.add("%"+search+"%");
            }

            String sortSQLRequest = "";
            if (req.getAttribute("name") != null || session.getAttribute("name") != null) {
                sortSQLRequest = String.format("order by u.name %s ", req.getAttribute("name") != null
                        ? req.getAttribute("name") : session.getAttribute("name").toString() );
                session.setAttribute("name",req.getAttribute("name")!=null?req.getAttribute("name") : session.getAttribute("name").toString());
                if (req.getAttribute("name") != null) {
                    session.removeAttribute("rating");
                }
            }

            if (req.getAttribute("rating") != null || session.getAttribute("rating") != null) {
                sortSQLRequest = String.format("order by (rating_sum*100/rating_count) %s ", req.getAttribute("rating") != null
                        ? req.getAttribute("rating") : session.getAttribute("rating").toString());
                session.setAttribute("rating",req.getAttribute("rating")!=null?req.getAttribute("rating") : session.getAttribute("rating").toString());
                if (req.getAttribute("rating") != null) {
                    session.removeAttribute("name");
                }
            }
            sqlRequestMasterService +=sortSQLRequest;

            // Start block pagination
            Integer pageSize = 3;
            Integer masterCount = MasterService.countMaster("select count(*) " + sqlRequestMasterService.substring(9,sqlRequestMasterService.length()), parameters.toArray());
            if(req.getAttribute("pageSize") != null) {
                pageSize=Integer.parseInt(req.getAttribute("pageSize").toString());
            }

            session.setAttribute("numberPages", (masterCount/pageSize + (masterCount%pageSize != 0 ? 1 : 0)));

            Integer page = 1;
            if (req.getAttribute("page") != null ) {
                page = Integer.parseInt(req.getAttribute("page").toString());
            }

            session.setAttribute("page",page);

            sqlRequestMasterService += ("limit " + (page-1)*pageSize + "," + pageSize + ";");

            // Finish block pagination

            masterList = MasterService.getAllMasterBySQLRequest(sqlRequestMasterService,parameters.toArray());

            session.setAttribute("generalCategoryList", generalCategoryList);
            session.setAttribute("masterList", masterList);

            return "category.jsp";
        } catch (DBException | EntityException e) {
            userLogger.error(e.getMessage(),e.fillInStackTrace());
            return "error.jsp";
        } catch (NumberFormatException e) {
            userLogger.error("User tried enter wrong parametrs",e.fillInStackTrace());
            return "index.jsp";
        }
    }
}
