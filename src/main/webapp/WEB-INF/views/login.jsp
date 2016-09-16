<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="springForm"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:url value="/j_spring_security_check" var="loginUrl" />
     <t:master>
	    <jsp:body>
			<div class="container">
			    <div class="login-container">
			        <div id="output"></div>
			        <div class="avatar"><img src="<c:url value="/img/IMG_2258.JPG" />"/></div>
			        <div class="form-box">
			        	<springForm:form id="loginForm" action="${loginUrl}" method="POST" commandName="login">
				            <c:if test="${not empty error}">
								<div class="alert alert-danger">${error}</div>
							</c:if>
							<c:if test="${not empty msg}">
								<div class="alert alert-success">${msg}</div>
							</c:if>
			                <div class="control-group" id="username">
				                <div class="input-group">
				                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
				                	<springForm:input path="username" cssClass="form-control" />
				                </div>
				                <small class="help-block" style="color:red"><springForm:errors path="username" /></small>
			                </div>
			                <div class="control-group" id="password">
				                <div class="input-group">
				                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
				                    <springForm:input type="password" path="password" cssClass="form-control" />
				                </div>
				                <small class="help-block" style="color:red"><springForm:errors path="password" /></small>
			                </div>
			                <!-- <div>
			                    <label class="login-checkbox"><input id="remember" name="_spring_security_remember_me" type="checkbox"></label>
			                    <label class="login-remember">Remember me</label>
			                </div> -->
			                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			                <input class="btn btn-info" type="submit" value="Login">
			        	</springForm:form>
			        </div>
			    </div>
			</div>
	    </jsp:body>
    </t:master>
 <script type="text/javascript">
	$(document).ready(function() {
		var $form = $('#loginForm');
		$form.bind('submit', function(e) {
			// Ajax validation
			var $inputs = $form.find('input');
			var data = collectFormData($inputs);
			
			$.post('checkLogin.json', data, function(response) {
				$form.find('.control-group').removeClass('error');
				$form.find('.help-block').empty();
				$form.find('.alert').remove();
				
				if (response.status == 'FAIL') {
					for (var i = 0; i < response.errorMessageList.length; i++) {
						var item = response.errorMessageList[i];
						var $controlGroup = $('#' + item.fieldName);
						$controlGroup.addClass('error');
						$controlGroup.find('.help-block').html(item.message);
					}
				} else {
					$form.unbind('submit');
					$form.submit();
				}
			}, 'json');
			
			e.preventDefault();
			return false;
		});
	});
</script>
 