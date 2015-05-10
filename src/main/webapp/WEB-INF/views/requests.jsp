<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		<link rel="styleSheet" href=//netda.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<script type="text/javascript" src="jquery-2.1.3.min.js"/>
		<script type="text/javascript">
			if (typeof jQuery != 'undefined')				 
			    alert("jQuery library is loaded!");
		</script>		
		<title><spring:message code="requests.title.label"/></title>
	</head>
	<body>
		<section>
			<div class="jumbotron">
				<div class="container" id="title-header">
					<h1>Users</h1>
					<div class="pull-right" style="padding-right:50px">
						<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
					</div>
					<p><spring:message code="requests.allRequests.label"/></p>
				</div>
			</div>
		</section>
		<section>
			<table style="width:100%" border="1">
				<tr>
					<th><spring:message code="request.requestId.label"/></th>
					<th><spring:message code="request.snippet.label"/></th>
					<th><spring:message code="request.status.label"/></th> 
					<th><spring:message code="request.pickedUpBy.label"/></th>
					<th><spring:message code="request.client.label"/></th>
					<th><spring:message code="request.bookName.label"/></th>
					<th><spring:message code="request.tradeDate.label"/></th>
					<th><spring:message code="request.price.label"/></th>
					<th><spring:message code="request.delta.label"/></th>
					<th><spring:message code="request.gamma.label"/></th>
					<th><spring:message code="request.theta.label"/></th>
					<th><spring:message code="request.vega.label"/></th>
					<th><spring:message code="request.rho.label"/></th>
				</tr>					
				<div class="row">
					<c:forEach items="${requests}" var="request">
						<div class="caption">
							<tr>
						  		<td><a href="<spring:url value="/requests/request?requestId=${request.identifier}"/>">${request.identifier}</a></td>
						  		<td>${request.request}</td> 
						  		<td>${request.status}</td>
						  		<td>${request.pickedUpBy}</td>
						  		<td>${request.clientId}</td>
						  		<td>${request.bookCode}</td>
						  		<td>${request.tradeDate}</td>
						  		<td>${request.premiumAmount}</td>
						  		<td>${request.delta}</td>
						  		<td>${request.gamma}</td>
						  		<td>${request.theta}</td>
						  		<td>${request.vega}</td>
						  		<td>${request.rho}</td>
							</tr>
						</div>
					</c:forEach>
				</div>
			</table>		
		</section>
	</body>
</html>