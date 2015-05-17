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
		<section>
			<div class="jumbotron">
				<div class="container">
					<div id="title">
						<h1><spring:message code="requests.requests.label"/></h1>
					</div>
					<div class="pull-right" id="language_link">
						<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
					</div>
					<div class="addNew" id="add_new">
						<form:form modelAttribute="newRequest" class="form-horizontal">
							<form:errors path="*" cssClass="alert alert-danger" element="div"/>			
							<fieldSet>
								<legend><spring:message code="request.addNewRequest.label"/></legend>
								<div class="form-group" id="requestSnippet">
									<label class="control-label col-lg-2 col-lg-2" for="request"><spring:message code="request.snippet.label"/></label>
									<div class="col-lg-10">
										<form:input id="request" path="request" type="text" class="form:input-large"/>
									</div>
								</div>
								<div class="form-group" id="requestClient">
									<label class="control-label col-lg-2 col-lg-2" for="clientId"><spring:message code="request.client.label"/></label>
									<div class="col-lg-10">
										<form:input id="clientId" path="clientId" type="text" class="form:input-large"/>
									</div>
								</div>
								<div class="form-group" id="requestBookCode">
									<label class="control-label col-lg-2 col-lg-2" for="bookCode"><spring:message code="request.bookCode.label"/></label>
									<div class="col-lg-10">
										<form:input id="bookCode" path="bookCode" type="text" class="form:input-large"/>
									</div>
								</div>
								<div class="form-group" id="requestAddButton">
									<div class="col-lg-offset-2 col-lg-10">
										<input type="submit" id="buttonAdd" class="btn btn-primary" value="Add"/>
									</div>
								</div>															
							</fieldSet>
						</form:form>
					</div>			
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