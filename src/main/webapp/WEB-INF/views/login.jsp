<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		<link rel="styleSheet" href=//netda.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<title>Login</title>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>		
		<script type="text/javascript">
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");
		</script>			
	</head>
	<body>
		<section>
			<div class="jumbotron">
				<div class="container">
					<h1><spring:message code="login.plsSignIn.label"/></h1>
				</div>
			</div>
		</section>
		<div class="container">
			<div class="row">
				<div class="col-md-4 col-md-offset-4">
					<div class="panel panel-body">
						<c:if test="${not empty error}">
							<div class="alert alert-danger">
								<spring:message code="login.badCredentials.label"/>
							</div>
						</c:if>
						
						<form action="<c:url value="/j_spring_security_check"></c:url>" method="post">
							<fieldSet>
								<div class="form-group">								
									<input name='j_username' placeholder="Enter userName" type="text" class="form-control"/>
								</div>
								<div class="form-group">																
									<input name='j_password' placeholder="Enter password" type="password" value="" class="form-control"/>								
								</div>
								<input type="submit" class="btn btn-lg btn-success btn-block" value="Login"/>
							</fieldSet>
						</form>
						
					</div>
				</div>
			</div>
		</div>
	</body>
</html>