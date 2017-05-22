<%@tag description="Simple Wrapper Tag" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <%
        String url = (String)request.getAttribute("javax.servlet.forward.request_uri");
        String[] uri = url.split("/");
       	url = uri[uri.length - 1];
       	String home = "";
       	String internal = "";
       	String external = "";
       	if(url.equals("home")) home = "choose";
       	if(url.equals("addinternal")) internal = "choose";
       	if(url.equals("addexternal")) external = "choose";
        %>
        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="<%=home%>"><a href="${pageContext.request.contextPath}/home">หน้าแรก</a></li>
                <c:if test="${role == 'ADMIN'}">
	                <li class="<%=internal%>"><a href="${pageContext.request.contextPath}/addinternal">เพิ่มหนังสือรับ - จากภายนอก</a></li>
	                <li class="<%=external%>"><a href="${pageContext.request.contextPath}/addexternal">เพิ่มหนังสือส่ง - ออกภายนอก</a></li>
                </c:if>
            </ul>
            <sec:authorize access="isAuthenticated()">
	        <ul class="nav pull-right">
	            <li class="nav-user dropdown">
	                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
	                    <img src="<c:url value="/img/user.png" />" class="nav-avatar" />
	                    <b class="caret"></b>
	                </a>
	                <ul class="dropdown-menu">
	                    <c:if test="${role == 'ADMIN'}">
	                    	<li><a href="<c:url value="/ManageAccount" />">จัดการผู้ใช้ระบบ</a></li>
	                    </c:if>
	                    <li><a href="javascript:formSubmit()">ออกจากระบบ</a></li>
	                </ul>
	            </li>
	        </ul>
        </sec:authorize>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>
