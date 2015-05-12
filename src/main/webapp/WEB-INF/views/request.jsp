<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="styleSheet" href=//netda.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<title>Individual Request</title>
	</head>
	<body>
		<section>
			<div class="jumbotron">
				<div class="pull-right" style="padding-right:50px">
					<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
				</div>			
				<div class="container">								
					<h1>Individual Request</h1>
				</div>
			</div>
		</section>
		<section class="container">
			<form:form modelAttribute="request" class="form-horizontal">
				<fieldSet>
					<legend><spring:message code="request.existingRequestDetails.label"/></legend>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="requestId"><spring:message code="request.requestId.label"/></label>
						<div class="col-lg-10">
							<form:input id="requestId" path="requestId" type="text" class="form:input-large" value="${request.requestId}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="request"><spring:message code="request.snippet.label"/></label>
						<div class="col-lg-10">
							<form:input id="request" path="request" type="text" class="form:input-large" value="${request.request}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="status"><spring:message code="request.status.label"/></label>
						<div class="col-lg-10">
							<form:input id="status" path="status" type="text" class="form:input-large" value="${request.status}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="pickedUpBy"><spring:message code="request.pickedUpBy.label"/></label>
						<div class="col-lg-10">
							<form:input id="pickedUpBy" path="pickedUpBy" type="text" class="form:input-large" value="${request.pickedUpBy}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="clientId"><spring:message code="request.client.label"/></label>
						<div class="col-lg-10">
							<form:input id="clientId" path="clientId" type="text" class="form:input-large" value="${request.clientId}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="bookCode"><spring:message code="request.bookCode.label"/></label>
						<div class="col-lg-10">
							<form:input id="bookCode" path="bookCode" type="text" class="form:input-large" value="${request.bookCode}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="premiumAmount"><spring:message code="request.premiumAmount.label"/></label>
						<div class="col-lg-10">
							<form:input id="premiumAmount" path="premiumAmount" type="text" class="form:input-large" value="${request.premiumAmount}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="delta"><spring:message code="request.delta.label"/></label>
						<div class="col-lg-10">
							<form:input id="delta" path="delta" type="text" class="form:input-large" value="${request.delta}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="gamma"><spring:message code="request.gamma.label"/></label>
						<div class="col-lg-10">
							<form:input id="gamma" path="gamma" type="text" class="form:input-large" value="${request.gamma}"/>
						</div>
					</div>
																				<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="vega"><spring:message code="request.vega.label"/></label>
						<div class="col-lg-10">
							<form:input id="vega" path="vega" type="text" class="form:input-large" value="${request.vega}"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="theta"><spring:message code="request.theta.label"/></label>
						<div class="col-lg-10">
							<form:input id="theta" path="theta" type="text" class="form:input-large" value="${request.theta}"/>
						</div>
					</div>
															<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="rho"><spring:message code="request.rho.label"/></label>
						<div class="col-lg-10">
							<form:input id="rho" path="rho" type="text" class="form:input-large" value="${request.rho}"/>
						</div>
					</div>
										
					<div class="form-group">
						<div class="col-lg-offset-2 col-lg-10">
							<input type="submit" id="buttonSave" class="btn btn-primary" value="Update"/>
						</div>
					</div>															
				</fieldSet>
			</form:form>
		</section>
	</body>
</html>