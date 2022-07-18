<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "ctg" uri="/WEB-INF/custom.tld" %>

<fmt:setLocale value="${lang}"/>
<fmt:setBundle basename="messages"/>

<div class="topnav">
    <a href="/beauty/index.jsp"><fmt:message key="header.home" /></a>

    <a href="/beauty/b?command=category&generalCategory=-1"><fmt:message key="header.category"/></a>


    <c:if test="${empty user}">
        <a id = "log_in_system" href = "#"><fmt:message key="header.login"/></a>
        <a id = "registration-button" href = "#"><fmt:message key="header.registration" /></a>
    </c:if>
    <c:if test="${not empty user}">

        <h5 style="display: inline;"> Hello, ${user.getName()} </h5>

        <a id = "profile-cabinet-button" href = "/beauty/b?command=profileCabinet"><fmt:message key="header.myprofile" /></a>

        <c:if test="${user.getRoleId() == 1}">
            <a id = "recording-button" href = "/beauty/b?command=recordingCabinet"><fmt:message key="header.recording" /></a>
            <a id = "user-cabinet-button" href = "/beauty/b?command=userCabinet"><fmt:message key="header.mycabinet" /></a>
        </c:if>
        <c:if test="${user.getRoleId() == 3}">
            <a id = "master-cabinet-button" href = "/beauty/b?command=masterCabinet"><fmt:message key="header.mycabinet" /></a>
        </c:if>
        <c:if test="${user.getRoleId() == 2}">
            <a href = "/beauty/b?command=adminCabinet"><fmt:message key="header.mycabinet" /></a>
            <a id = "registration-button" href = "#"><fmt:message key="header.registration" /></a>
        </c:if>
        <a href = "/beauty/b?command=logout"><fmt:message key="header.logout" /></a>
    </c:if>

    <a href = "/beauty/b?command=local&lang=en">En</a>
    <a href = "/beauty/b?command=local&lang=uk">Uk</a>
</div>

<div id="front" style = "background-color: #efefef; opacity: 0.75; height: 100%; width: 100%; top: 0px; left: 0px; position: absolute;
    <c:if test="${empty errorEnter && empty errorRegistration && empty userCabinet && empty recordingCabinet && empty masterCabinet
                        && empty profileCabinet}">
       display: none;
    </c:if>">
</div>

<jsp:include page="enter.jsp"/>
<jsp:include page="registration.jsp"/>
<jsp:include page="recording.jsp"/>
<jsp:include page="master.jsp"/>
<jsp:include page="profile.jsp"/>
<jsp:include page="cabinet.jsp"/>

<script>
    var login = document.getElementById('log_in_system');
    if (login != null) {login.addEventListener('click',openLogIn,false);}

    function openLogIn(e){
        document.getElementById('login').style.display = "block";
        document.getElementById('front').style.display = "";
    }

    var registration = document.getElementById('registration-button');
    if (registration !=null) {registration.addEventListener('click',openRegistration,false);}

    function openRegistration(e){
            document.getElementById('registration').style.display = "block";
            document.getElementById('front').style.display = "";
    }

    var userCabinet = document.getElementById('user-cabinet-button');
    if (userCabinet != null) {userCabinet.addEventListener('click',openUserCabinet,false);}

    function openUserCabinet(e){
            document.getElementById('user-cabinet').style.display = "block";
            document.getElementById('front').style.display = "";
    }

    var recordingCabinet = document.getElementById('recording-button');
    if (recordingCabinet != null) {recordingCabinet.addEventListener('click',openRecordingCabinet,false);}

    function openRecordingCabinet(e){
                document.getElementById('recording-cabinet').style.display = "block";
                document.getElementById('front').style.display = "";
    }

    var masterCabinet = document.getElementById('master-cabinet-button');
    if (masterCabinet != null) {masterCabinet.addEventListener('click',openRecordingCabinet,false);}

    function openMasterCabinet(e){
          document.getElementById('master-cabinet').style.display = "block";
          document.getElementById('front').style.display = "";
    }

    var profileCabinet = document.getElementById('profile-cabinet-button');
    if (profileCabinet != null) {profileCabinet.addEventListener('click',openProfileCabinet,false);}

    function openProfileCabinet(e){
        document.getElementById('profile-cabinet').style.display = "block";
        document.getElementById('front').style.display = "";
     }



    var login = document.getElementById('front');
    login.addEventListener('click',closeLogIn,false);
    function closeLogIn(e){
        <% session.removeAttribute("errorRegistration"); %>
        <% session.removeAttribute("errorEnter"); %>
        <% session.removeAttribute("userCabinet"); %>
        <% session.removeAttribute("recordingCabinet"); %>
        <% session.removeAttribute("masterCabinet"); %>
        <% session.removeAttribute("profileCabinet"); %>

        if (document.getElementById('comment') != null) {
            document.getElementById('comment').style.display = "none";
        }
        document.getElementById('login').style.display = "none";
        document.getElementById('front').style.display = "none";
        document.getElementById('registration').style.display = "none";
        document.getElementById('user-cabinet').style.display = "none";
        document.getElementById('recording-cabinet').style.display = "none";
        document.getElementById('master-cabinet').style.display = "none";
        document.getElementById('profile-cabinet').style.display = "none";
    }
</script>


