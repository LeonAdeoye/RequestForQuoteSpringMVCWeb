<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="styleSheet" href=//netda.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<title><spring:message code="request.addNewRequest.label"/></title>
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
					<div class="pull-right" style="padding-right:50px">
						<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
					</div>												
					<h1><spring:message code="request.addNewRequest.label"/></h1>
				</div>
			</div>
		</section>
		<section class="container">
			<form:form modelAttribute="newRequest" class="form-horizontal">
				<form:errors path="*" cssClass="alert alert-danger" element="div"/>			
				<fieldSet>
					<legend><spring:message code="request.addNewRequest.label"/></legend>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="request"><spring:message code="request.snippet.label"/></label>
						<div class="col-lg-10">
							<form:input id="request" path="request" type="text" class="form:input-large"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="clientId"><spring:message code="request.client.label"/></label>
						<div class="col-lg-10">
							<form:input id="clientId" path="clientId" type="text" class="form:input-large"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="bookCode"><spring:message code="request.bookCode.label"/></label>
						<div class="col-lg-10">
							<form:input id="bookCode" path="bookCode" type="text" class="form:input-large"/>
						</div>
					</div>
					
					<div class="form-group">
						<div class="col-lg-offset-2 col-lg-10">
							<input type="submit" id="buttonAdd" class="btn btn-primary" value="Add"/>
						</div>
					</div>															
				</fieldSet>
			</form:form>
		</section>
	</body>
</html>