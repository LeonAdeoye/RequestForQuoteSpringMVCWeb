<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		<link rel="styleSheet" href=//netda.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<script type="text/javascript" src="<c:url value="/resources/javascript/jquery-2.1.3.min.js" />"/>
		<script type="text/javascript">
			if (typeof jQuery != 'undefined') {
				 
			    alert("jQuery library is loaded!");
			 
			}else{
			 
			    alert("jQuery library is not found!");
			 
			}		
			jQuery().ready(function() 
			{			 
				$('#mbu').Hide();
				alert("fred");
			});
		</script>		
		<title>User Maintenance</title>
	</head>
	<body>
		<section>
			<div class="jumbotron">
				<div class="container" id="title-header">
					<h1>Users</h1>
					<div class="pull-right" style="padding-right:50px">
						<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
					</div>
					<p><spring:message code="user.allUsers.label"/></p>
				</div>
			</div>
		</section>
		<section>
			<table style="width:100%" border="1">
				<tr>
					<th><spring:message code="user.userId.label"/></th>
					<th><spring:message code="user.firstName.label"/></th>
					<th><spring:message code="user.lastName.label"/></th> 
					<th><spring:message code="user.emailAddress.label"/></th>
				</tr>					
				<div class="row">
					<c:forEach items="${users}" var="user">
						<div class="caption">
							<tr>
						  		<td><a href="<spring:url value="/users/user?userId=${user.userId}"/>">${user.userId}</a></td>
						  		<td>${user.firstName}</td> 
						  		<td>${user.lastName}</td>
						  		<td>${user.emailAddress}</td>
							</tr>
						</div>
					</c:forEach>
				</div>
			</table>
			<div id="mbu">
				Help
			</div>			
		</section>
	</body>
</html>