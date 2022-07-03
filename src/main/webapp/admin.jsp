<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <style>
    body {
        background-image: url("static/images/back-image.jpg");
    }
    <style>
    table {
        border-collapse: collapse;
        border-spacing: 0;
        width: 100%;
        border: 1px solid #ddd;
        font-size: 20px;
        font-weight: bold;
    }

    th, td {
        text-align: left;
        padding: 6px;
    }

    tr:nth-child(even) {
        background-color: #f2f2f2
    }
    </style>
    </style>
    <link href="static/css/header.css" type="text/css" rel="stylesheet" />
</head>
<body>
        <jsp:include page="header.jsp" />

        <jsp:useBean id="now" class="java.util.Date" scope="page"/>

        <br/><br/>
        <table class="table-user" style = " background-color: #ffffff; padding-left: 50px; padding-right: 50px;">
             <tr style = "text-transform: uppercase; width: 200px">
                 <td class="table-user-th-td">Date visit</td>
                 <td class="table-user-th-td">Time visit</td>
                 <td class="table-user-th-td">Master Name</td>
                 <td class="table-user-th-td">Client Name</td>
                 <td class="table-user-th-td">Category</td>
                 <td class="table-user-th-td">Sum visit</td>
                 <td class="table-user-th-td">Comment</td>
                 <td class="table-user-th-td">Completed</td>
                 <td class="table-user-th-td">Paid</td>
             </tr>
             <c:forEach var = "orderDTO" items = "${orderDTOList}">
                  <tr>
                     <form action="/beauty/b?command=adminCompleted&method=update&orderId=${orderDTO.getId()}" method="POST">
                        <td class="table-user-th-td"><input type="date" name="dateOrder" value="${orderDTO.getDateOrder()}" scope="request" min="${localDate}" ></td>
                        <td class="table-user-th-td">
                          <select name = "hourOrder" scope="request">
                                <option><c:out value = "${orderDTO.getHourOrder()}"/></option>
                                <option>--:--</option>
                                <c:forEach var = "i" begin = "10" end = "20">
                                     <option value="<c:out value = "${i}:00"/>"><c:out value = "${i}:00"/></option>
                                     <option value="<c:out value = "${i}:30"/>"><c:out value = "${i}:30"/></option>
                                </c:forEach>
                          </select>
                        </td>

                      <td class="table-user-th-td">${orderDTO.getMasterName()}</td>
                      <td class="table-user-th-td">${orderDTO.getClientName()}</td>

                      <td class="table-user-th-td">
                        <ul>
                          <c:forEach var = "category" items = "${orderDTO.getCategories()}">
                              <li>${category.getName()}</li>
                          </c:forEach>
                        </ul>
                      </td>
                      <td class="table-user-th-td">${orderDTO.getSumOrder()} </td>
                      <td class="table-user-th-td"><textarea rows=3 col=20 disabled>${orderDTO.getComment()}</textarea> </td>

                          <c:if test="${orderDTO.getCompleted() eq false}">
                              <td class="table-user-th-td"><input type="checkbox" name = "completed"></td>
                          </c:if>
                          <c:if test="${orderDTO.getCompleted() eq true}">
                               <td class="table-user-th-td"><input type="checkbox" name = "completed" checked="on" disabled></td>
                          </c:if>
                          <c:if test="${orderDTO.getPaid() eq false}">
                                <td class="table-user-th-td"><input type="checkbox" name = "paid"></td>
                          </c:if>
                          <c:if test="${orderDTO.getPaid() eq true}">
                                <td class="table-user-th-td"><input type="checkbox" name = "paid" checked="on" disabled></td>
                          </c:if>
                          <td class="table-user-th-td"><input type="submit" value = "update"></td>
                     </form>
                      <form action="/beauty/b?command=adminCompleted&method=delete&orderId=${orderDTO.getId()}" method="POST">
                           <td class="table-user-th-td"><input type="submit" value = "delete"></td>
                      </form>
                  </tr>
             </c:forEach>
        </table>
        <div class="pagination" style="padding-left: 40%;">
                    <c:choose>
                        <c:when test="${page - 1 > 0}">
                            <a href="/beauty/b?command=adminCabinet&page=<c:out value = "${page-1}"/>"> Previous </a>
                        </c:when>
                    </c:choose>

                    <c:forEach var = "i" begin = "1" end = "${numberPages}" >
                        <a <c:if test="${page == i}"> class="active" </c:if>
                        href="/beauty/b?command=adminCabinet&page=<c:out value = "${i}"/>"> <c:out value = "${i}"/>   </a>
                    </c:forEach>

                    <c:choose>
                        <c:when test="${page + 1 <= numberPages}">
                            <a href="/beauty/b?command=adminCabinet&page=<c:out value = "${page+1}"/>"> Next </a>
                        </c:when>
                    </c:choose>
            </div>

</body>
</html>