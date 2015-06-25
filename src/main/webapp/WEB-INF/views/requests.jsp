<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">		
		<link href="<c:url value="/resources/css/requests.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jquery-ui.min.css" />" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/requests.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/knockout-3.3.0.js" />"></script>		
		<script type="text/javascript">
			var contextPath='<%=request.getContextPath()%>' /* needed for autocomplete */
			if (typeof jQuery == 'undefined')				 
			    alert("WARNING: jQuery library is NOT loaded!");
		</script>		
		<title><spring:message code="requests.title.label"/></title>
	</head>
	<body>
				<div id="requests_bar">
					<div id="requests_title">
						<p id="new_requests"><spring:message code="requests.addNewRequest.label"/></p>
					</div>
					<div class="addNew" id="requests_add_new">
						<form:form modelAttribute="newRequest" class="form-horizontal">
							<form:errors path="*" cssClass="alert alert-danger" element="div"/>			
							<fieldSet>
								<div class="form-group">
									<form:input id="requests_snippet" class="new_request" path="request" value="Enter request snippet..." type="text" default_value="Enter request snippet..." />
								</div>
								<div class="form-group">
									<form:input id="requests_client" class="new_request" path="clientId" value="Select client name..." type="text" default_value="Select client name..." />
								</div>
								<div class="form-group">
									<form:input id="requests_bookCode" class="new_request" path="bookCode" value="Select book code..." type="text" default_value="Select book code..." />
								</div>
								<div class="form-group">
									<input type="submit" id="requests_add_button" class="btn" value="<spring:message code="requests.addRequest.button.label"/>"/>
								</div>
								<div class="form-group">
									<button id="requests_clear_button" value="Clear" class="btn"><spring:message code="requests.clearRequest.button.label"/></button>									
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
					<th><spring:message code="request.timeValue.label"/></th>
					<th><spring:message code="request.intrinsicValue.label"/></th>
					<th><spring:message code="request.underlyingDetails.label"/></th>
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
						  		<td>${request.intrinsicValue}</td>
						  		<td>${request.timeValue}</td>
						  		<td>${request.underlyingDetails}</td>
							</tr>
						</div>
					</c:forEach>
				</div>
			</table>		
		</section>
	</body>
</html>