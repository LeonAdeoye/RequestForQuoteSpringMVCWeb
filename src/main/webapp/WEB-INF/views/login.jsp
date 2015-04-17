<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		<link rel="styleSheet" href=//netda.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<title>Login</title>
	</head>
	<body>
		<section>
			<div class="jumbotron">
				<div class="container">
					<form action=<c:url value="/j_spring_security_check"></c:url> method="post">
						<fieldSet>
							<div class="form-group">								
								<input name='j_username' placeholder="user name" type="text" class="form-control"/>
							</div>
							<div class="form-group">																
								<input name='j_password' placeholder="password" type="password" class="form-control"/>								
							</div>
							<div class="form-group">
								<div class="col-lg-offset-2 col-lg-10">
									<input type="submit" id="buttonAdd" class="btn btn-primary" value="Login"/>
								</div>
							</div>															
						</fieldSet>
					</form>
				</div>
			</div>
		</section>
	</body>
</html>