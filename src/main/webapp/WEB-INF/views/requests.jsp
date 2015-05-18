<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">		
		<link rel="styleSheet" href=//netda.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<link href="<c:url value="/resources/css/requests.css" />" rel="stylesheet" type="test/css">
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>		
		<script type="text/javascript">
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");
		</script>		
		<title><spring:message code="requests.title.label"/></title>
	</head>
	<body>
				<div id="requests_bar">
					<div id="requests_title">
						<p id="new_requests"><spring:message code="request.addNewRequest.label"/></p>
					</div>
					<div class="addNew" id="requests_add_new">
						<form:form modelAttribute="newRequest" class="form-horizontal">
							<form:errors path="*" cssClass="alert alert-danger" element="div"/>			
							<fieldSet>
								<div class="form-group" id="requests_snippet">
									<form:input id="request" path="request" value="Enter request snippet..." type="text" class="form:input-large"/>
								</div>
								<div class="form-group" id="requests_client">
									<form:input id="clientId" path="clientId" value="Select client..." type="text" class="form:input-large"/>
								</div>
								<div class="form-group" id="requests_bookCode">
									<form:input id="bookCode" path="bookCode" value="select book code..."type="text" class="form:input-large"/>
								</div>
								<div class="form-group" id="requests_add_button">
									<input type="submit" id="buttonAdd" class="btn btn-primary" value="Add"/>
								</div>
								<div class="form-group" id="requests_clear_button">
									<button>Clear</button>									
								</div>																							
							</fieldSet>
						</form:form>
					</div>
					<div class="pull-right" id="requests_language_link">
						<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
					</div>								
				</div>

		<section>
			<table style="width:100%" border="1">
				<tr>
					<th><spring:message code="request.requestId.label"/></th>
					<th><spring:message code="request.snippet.label"/></th>
					<th><spring:message code="request.status.label"/></th> 
					<th><spring:message code="request.pickedUpBy.label"/></th>
					<th><spring:message code="request.client.label"/></th>
					<th><spring:message code="request.bookCode.label"/></th>
					<th><spring:message code="request.tradeDate.label"/></th>
					<th><spring:message code="request.premiumAmount.label"/></th>
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