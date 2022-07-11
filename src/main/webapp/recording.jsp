<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page import = "entities.*" %>
<%@ page import = "java.util.*" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>


<div id="recording-cabinet" style = "background-color: #ffffff; width: 300px; border: 2px solid; position: absolute; top: 75px; left: 500px; z-index: 100;
        <c:if test="${empty recordingCabinet}">
           display: none; visibility: hidden;
        </c:if> ">
    <div class="container">
        <br/>
        <h2><fmt:message key="recording.recording"/></h2>
        <form action="/beauty/b?command=newRecording" method = "post">
            <label for="master"><b><fmt:message key="recording.master"/></b></label>
            <select id= "master-id" class ="profile-select master_select" name = "master">
                <option class = "master_option" value = "-1"><fmt:message key="recording.master"/></option>
                <c:forEach var = "master" items = "${masterList}" >
                    <option class = "master_option <c:forEach var = "category" items = "${master.getCategories()}"> <c:out value = "${category.getId()}IdCategory"/> </c:forEach> " value = "${master.getId()}"> <c:out value = "${master.getUser().getName()}"/> </option>
                </c:forEach>
            </select>
            <br/><br/>
            <label for="generalCategory"><b><fmt:message key="recording.generalCategory"/></b></label>
            <select class = "profile-select gCategory_select" name = "generalCategory">
                <option class = "gCategory_option -1IdGCategory_gen_sel" value = "-1"><fmt:message key="recording.generalCategory"/></option>
                <c:forEach var = "generalCategory" items = "${generalCategoryList}" >
                     <option class = "gCategory_option ${category.getGeneralCategoryId()}IdGCategory_gen_sel" value = "${generalCategory.getId()}"> <c:out value = "${generalCategory.getName()}"/> </option>
                </c:forEach>
            </select>

            <br/><br/>
            <label><b><fmt:message key="cabinet.category"/></b></label>
            <select id="first-category" class = "profile-select category_select firstOrderSelect" name = "categoryFirst">
                 <option class = "category_option -1IdGCategory_cat_sel" value = "-1"><fmt:message key="recording.allCategorys"/></option>
                 <c:forEach var = "category" items = "${categoryList}" >
                        <option class = "category_option ${category.getGeneralCategoryId()}IdGCategory_cat_sel" value = "${category.getId()}"> <c:out value = "${category.getName()}"/> </option>
                 </c:forEach>
            </select>
            <button class = "enter-buttom-form-login addSecondOrderButton" form = ""><fmt:message key="recording.addCategory"/></button>

            <select id="second-category" class = "profile-select category_select secondOrderSelect" name = "categorySecond" style="display:none">
                  <option class = "category_option -1IdGCategory_cat_sel" value = "-1"><fmt:message key="recording.allCategorys"/></option>
                  <c:forEach var = "category" items = "${categoryList}" >
                        <option class = "category_option ${category.getGeneralCategoryId()}IdGCategory_cat_sel" value = "${category.getId()}"> <c:out value = "${category.getName()}"/> </option>
                  </c:forEach>
            </select>
            <button class = "enter-buttom-form-login addThirdOrderButton" form = "" style="display:none"><fmt:message key="recording.addCategory"/></button>
            <button class = "enter-buttom-form-login removeSecondOrderButton" form = "" style="display:none"><fmt:message key="recording.removeCategory"/></button>

            <select id="third-category" class = "profile-select category_select thirdOrderSelect" name = "categoryThird" style="display:none">
                    <option class = "category_option -1IdGCategory_cat_sel" value = "-1"><fmt:message key="recording.allCategorys"/></option>
                    <c:forEach var = "category" items = "${categoryList}" >
                           <option class = "category_option ${category.getGeneralCategoryId()}IdGCategory_cat_sel" value = "${category.getId()}"> <c:out value = "${category.getName()}"/> </option>
                    </c:forEach>
            </select>
            <button class = "enter-buttom-form-login removeThirdOrderButton" form = "" style="display:none"><fmt:message key="recording.removeCategory"/></button>
            <br/><br/>
            <label for="dateOrder"><b><fmt:message key="recording.date"/></b></label>
            <input id="date-order" type="date" name = "dateOrder" min="${localDate}"/>
            <select id="time-order" name = "timeOrder">
                  <option value="<c:out value = "--:--"/>"><c:out value = "--:--"/></option>
            </select>
            <button id="getTimeOrder" type="button">UP</button>
            <br/><br/>
            <button class="enter-buttom-form-login" type="submit"><fmt:message key="recording.order"/></button>
        </form>
    </div>
</div>

<script>
        $(document).ready(function() {
            $('#getTimeOrder').on('click',function() {
                var master_id = document.getElementById("master-id").value;
                var date_order = document.getElementById("date-order").value;

                var first_category = document.getElementById("first-category").value;
                var second_category = document.getElementById("second-category").value;
                var third_category = document.getElementById("third-category").value;

                if (master_id == -1 || date_order == ""){
                    document.getElementById("master-id").style.borderColor = "red";
                    document.getElementById("date-order").style.borderColor = "red";
                } else {
                    document.getElementById("master-id").style.borderColor = "#ccc";
                    document.getElementById("date-order").style.borderColor = "#ccc";
                   $.get('UpdateTimeMaster', {"master_id": master_id,"date_order": date_order,
                                               "first_category": first_category, "second_category": second_category, "third_category": third_category  },
                            function(responseText) {
                                $("#time-order").html(responseText);
                            });
                }
            });
         });

            document.getElementsByClassName("addSecondOrderButton")[0].addEventListener('click',function() {
                document.getElementsByClassName("addSecondOrderButton")[0].style.display="none";
                document.getElementsByClassName("secondOrderSelect")[0].style.display="";
                document.getElementsByClassName("addThirdOrderButton")[0].style.display="";
                document.getElementsByClassName("removeSecondOrderButton")[0].style.display="";
            },true);

            document.getElementsByClassName("addThirdOrderButton")[0].addEventListener('click',function() {
                document.getElementsByClassName("removeSecondOrderButton")[0].style.display="none";
                document.getElementsByClassName("addThirdOrderButton")[0].style.display="none";
                document.getElementsByClassName("thirdOrderSelect")[0].style.display="";
                document.getElementsByClassName("removeThirdOrderButton")[0].style.display="";

            },true);

            document.getElementsByClassName("removeSecondOrderButton")[0].addEventListener('click',function() {
                  document.getElementsByClassName("secondOrderSelect")[0].selectedIndex=0;
                  document.getElementsByClassName("addSecondOrderButton")[0].style.display="";
                  document.getElementsByClassName("secondOrderSelect")[0].style.display="none";
                  document.getElementsByClassName("addThirdOrderButton")[0].style.display="none";
                  document.getElementsByClassName("removeSecondOrderButton")[0].style.display="none";
            },true);

            document.getElementsByClassName("removeThirdOrderButton")[0].addEventListener('click',function() {
                  document.getElementsByClassName("thirdOrderSelect")[0].selectedIndex=0;
                  document.getElementsByClassName("addThirdOrderButton")[0].style.display="";
                  document.getElementsByClassName("thirdOrderSelect")[0].style.display="none";
                  document.getElementsByClassName("removeThirdOrderButton")[0].style.display="none";
                  document.getElementsByClassName("removeSecondOrderButton")[0].style.display="";
            },true);

            var gCategorySelector = document.getElementsByClassName("gCategory_select");

            for (var i = 0; i < gCategorySelector.length; i++) {
                gCategorySelector[i].addEventListener('click', openCategory, true);
            }

            function openCategory(e) {
                document.getElementsByClassName("master_select")[0].selectedIndex=0;
                document.getElementsByClassName("firstOrderSelect")[0].selectedIndex=0;
                document.getElementsByClassName("secondOrderSelect")[0].selectedIndex=0;
                document.getElementsByClassName("thirdOrderSelect")[0].selectedIndex=0;

                var categoryOption = document.getElementsByClassName("category_option");
                var masterOption = document.getElementsByClassName("master_option");
                var idGeneralCategory = document.getElementsByClassName("gCategory_option")[e.target.selectedIndex].value;
                console.log(idGeneralCategory);
                if (idGeneralCategory == -1){
                    for (var i = 0; i < masterOption.length; i++) {
                        masterOption[i].style.display = "";
                    }
                    for (var i = 0; i < categoryOption.length; i++) {
                        categoryOption[i].style.display = "";
                    }
                } else {
                    for (var i = 0; i < masterOption.length; i++) {
                        masterOption[i].style.display = "none";
                    }
                    for (var i = 0; i < categoryOption.length; i++) {
                        console.log(categoryOption[i].className.split(" ")[1]);
                        if (categoryOption[i].className.split(" ")[1] == (idGeneralCategory+"IdGCategory_cat_sel")){
                            categoryOption[i].style.display = "";

                            for (var j = 0; j < masterOption.length; j++) {
                                  var idCategoryMaster = masterOption[j].className.split(" ");
                                  for (var k = 0; k < idCategoryMaster.length; k++) {
                                    if (idCategoryMaster[k] == (categoryOption[i].value+"IdCategory")){
                                        masterOption[j].style.display = "";
                                    }
                                  }

                            }

                        } else {
                            categoryOption[i].style.display = "none";
                        }
                    }
                }
            }
</script>