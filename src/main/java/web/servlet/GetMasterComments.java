package web.servlet;

import web.dto.OrderDTO;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/GetMasterComments")
public class GetMasterComments extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        Integer idMaster = Integer.parseInt(request.getParameter("idMaster"));

        try {
            List<OrderDTO> orderDTOList = OrderService.getOrderDTOsMaster(idMaster);

        StringBuilder result = new StringBuilder("<tr style = \"width: 200px\"><th class=\"table-user-th-td\"> Author </th><th class=\"table-user-th-td\"> Comment </th></tr>");
        String temp = "<tr style = \"width: 200px\"><th class=\"table-user-th-td\"> %s </th><th class=\"table-user-th-td\"> %s </th></tr>";

        for (OrderDTO orderDTO : orderDTOList) {
            if (orderDTO.getComment() != null && !orderDTO.getComment().equals("")) {
                result.append(String.format(temp, orderDTO.getClientName(), orderDTO.getComment()));
            }
        }

        response.getWriter().println(result.toString());
        response.setStatus(200);

        } catch (DBException | EntityException e) {
            e.printStackTrace();
        }
    }
}
