<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<div id="master-cabinet" style = "background-color: #ffffff; border: 2px solid; position: absolute; top: 75px; left: 400px; z-index: 100;
        <c:if test="${empty masterCabinet}">
           display: none;
        </c:if>" >

        <table class="table-user" style = "text-align: center; padding-left: 50px; padding-right: 50px;">
            <tr style = "text-transform: uppercase; width: 200px">
               <th class="table-user-th-td ">Date visit</th>
               <th class="table-user-th-td ">Time visit</th>
               <th class="table-user-th-td ">Category</th>
               <th class="table-user-th-td ">Completed</th>
               <th class="table-user-th-td ">Comment</th>
               <th class="table-user-th-td "></th>
            </tr>
            <c:forEach var = "orderDTO" items = "${orderDTOList}">
                <tr style = "border: 1px solid black; padding-top: 10px; padding-bottom: 10px;">
                    <td class="table-user-th-td "><input type="date" name="dateOrder" value="${orderDTO.getDateOrder()}" disabled></td>
                    <td class="table-user-th-td ">
                         <select name = "hourOrder" disabled>
                             <option><c:out value = "${orderDTO.getHourOrder()}"/></option>
                         </select>
                    </td>
                    <td class="table-user-th-td ">
                       <ul style = "list-style-type: none;">
                          <c:forEach var = "category" items = "${orderDTO.getCategories()}">
                             <li>${category.getName()}</li>
                          </c:forEach>
                       </ul>
                    </td class="table-user-th-td ">
                    <form action="/beauty/b?command=masterCompleted&orderId=${orderDTO.getId()}" method="post">
                        <c:if test="${orderDTO.getCompleted() eq false}">
                            <td class="table-user-th-td "><input type="checkbox" name = "completed"></td>
                            <td class="table-user-th-td "> <textarea rows=3 col=20 disabled>${orderDTO.getComment()}</textarea></td>
                            <td class="table-user-th-td " ><input type="submit" value = "update"></td>
                        </c:if>
                        <c:if test="${orderDTO.getCompleted() eq true}">
                             <td class="table-user-th-td "><input type="checkbox" name = "completed" checked="on" disabled></td>
                             <td class="table-user-th-td "><textarea rows=3 col=20 disabled>${orderDTO.getComment()}</textarea></td>
                             <td class="table-user-th-td "></td>
                        </c:if>
                    </form>

                </tr>
            </c:forEach>
        </table>
        <div class="pagination" style="padding-left: 40%;">
                    <c:choose>
                        <c:when test="${page - 1 > 0}">
                            <a href="/beauty/b?command=masterCabinet&page=<c:out value = "${page-1}"/>"> Previous </a>
                        </c:when>
                    </c:choose>

                    <c:forEach var = "i" begin = "1" end = "${numberPages}" >
                        <a <c:if test="${page == i}"> class="active" </c:if>
                        href="/beauty/b?command=masterCabinet&page=<c:out value = "${i}"/>"> <c:out value = "${i}"/>   </a>
                    </c:forEach>

                    <c:choose>
                        <c:when test="${page + 1 <= numberPages}">
                            <a href="/beauty/b?command=masterCabinet&page=<c:out value = "${page+1}"/>"> Next </a>
                        </c:when>
                    </c:choose>
        </div>
</div>

