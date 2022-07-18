package web.servlet;


import web.entities.Category;
import web.entities.Order;
import web.exception.DBException;
import web.exception.EntityException;
import web.service.CategoryService;
import web.service.OrderService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UpdateTimeAdmin")
public class UpdateTimeAdmin extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        try {

            String idOrder = request.getParameter("idOrder");
            String dateOrder = request.getParameter("dateOrder");

            String[] d = dateOrder.split("-");
            LocalDate localDate = LocalDate.of(Integer.parseInt(d[0]),Integer.parseInt(d[1]),Integer.parseInt(d[2]));

            List<LocalTime> listTimeOrder = new ArrayList<>();
            for (int i = 10; i<=20; i++) {
                listTimeOrder.add(LocalTime.of(i,0));
                listTimeOrder.add(LocalTime.of(i,30));
            }
            Order orderUpdate = OrderService.getOrderById(Integer.parseInt(idOrder));

            List<Order> listMaster = OrderService.getOrdersByDateOrderAndMasterAndIdIsNot(localDate, orderUpdate.getMasterId(),orderUpdate.getId());


            for (Order order : listMaster){
                LocalTime durationOrder = LocalTime.of(0,0);
                LocalTime timeStartOrder = order.getTimeOrder();
                LocalTime timeFinishOrder = order.getTimeOrder();
                for (Category category : order.getCategories()){
                    LocalTime workTimeCategory = category.getWorkTime();
                    timeFinishOrder = timeFinishOrder.plusHours(workTimeCategory.getHour());
                    timeFinishOrder = timeFinishOrder.plusMinutes(workTimeCategory.getMinute());
                    durationOrder = durationOrder.plusHours(workTimeCategory.getHour());
                    durationOrder = durationOrder.plusMinutes(workTimeCategory.getMinute());
                }
                int temp = durationOrder.getHour()*2 + durationOrder.getMinute()/30;
                LocalTime tempTimeOrder = timeStartOrder;
                for (int i = 0; i < temp; i++){
                    if (listTimeOrder.contains(tempTimeOrder)){
                        listTimeOrder.remove(tempTimeOrder);
                    }
                    tempTimeOrder = tempTimeOrder.plusMinutes(30);
                }
            }

            List<Integer> freeMinuteToNextOrder = new ArrayList<>();
            if (listTimeOrder.size() !=0) {
                freeMinuteToNextOrder.add(30);
                for (int i = listTimeOrder.size()-2; i>=0; i--){
                    if (((listTimeOrder.get(i+1).getHour()-listTimeOrder.get(i).getHour()) == 0
                            && (listTimeOrder.get(i+1).getMinute()-listTimeOrder.get(i).getMinute()) == 30) ||
                            ((listTimeOrder.get(i+1).getHour()-listTimeOrder.get(i).getHour()) == 1
                                    && (listTimeOrder.get(i+1).getMinute()-listTimeOrder.get(i).getMinute()) == -30)){
                        freeMinuteToNextOrder.add(30+freeMinuteToNextOrder.get(listTimeOrder.size()-i-2));
                    } else {
                        freeMinuteToNextOrder.add(30);
                    }
                }
            }

            LocalTime durationRecording = LocalTime.of(0,0);
            for (Category category : orderUpdate.getCategories()){
                if (category != null){
                    durationRecording=durationRecording.plusHours(category.getWorkTime().getHour());
                    durationRecording=durationRecording.plusMinutes(category.getWorkTime().getMinute());
                }
            }

            Integer durationRecordingInMinute = durationRecording.getHour()*60+durationRecording.getMinute();

            for (int i = 0; i < listTimeOrder.size(); ){
                if (freeMinuteToNextOrder.get(freeMinuteToNextOrder.size()-i-1) >= durationRecordingInMinute){
                    i++;
                } else {
                    listTimeOrder.remove(i);
                    freeMinuteToNextOrder.remove(freeMinuteToNextOrder.size()-i-1);
                }
            }


            String temp = "<option value='%s'>%s</option>\n";
            StringBuilder result = new StringBuilder();
            result.append(String.format(temp,orderUpdate.getTimeOrder().toString(),orderUpdate.getTimeOrder().toString()));
            result.append(String.format(temp,"--:--","--:--"));
            for (LocalTime localTime : listTimeOrder){
                result.append(String.format(temp,localTime.toString(),localTime.toString()));
            }

            response.getWriter().println(result.toString());
            response.setStatus(200);

        } catch (DBException | EntityException e) {
            e.printStackTrace();
        }

    }
}