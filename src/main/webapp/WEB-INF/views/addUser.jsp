<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="styleSheet" href=//netda.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
		<title><spring:message code="user.addNewUser.label"/></title>
	</head>
	<body>
		<section>
			<div class="jumbotron">
				<div class="container">
					<div class="pull-right" style="padding-right:50px">
						<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
					</div>												
					<h1><spring:message code="user.addNewUser.label"/></h1>
				</div>
			</div>
		</section>
		<section class="container">
			<form:form modelAttribute="newUser" class="form-horizontal">
				<form:errors path="*" cssClass="alert alert-danger" element="div"/>
			
				<fieldSet>
					<legend><spring:message code="user.addNewUser.label"/></legend>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="userId"><spring:message code="user.userId.label"/></label>
						<div class="col-lg-10">
							<form:input id="userId" path="userId" type="text" class="form:input-large"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="lastName"><spring:message code="user.lastName.label"/></label>
						<div class="col-lg-10">
							<form:input id="lastName" path="lastName" type="text" class="form:input-large"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="firstName"><spring:message code="user.firstName.label"/></label>
						<div class="col-lg-10">
							<form:input id="firstName" path="firstName" type="text" class="form:input-large"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="emailAddress"><spring:message code="user.emailAddress.label"/></label>
						<div class="col-lg-10">
							<form:input id="emailAddress" path="emailAddress" type="text" class="form:input-large"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="locationName"><spring:message code="user.locationName.label"/></label>
						<div class="col-lg-10">
							<form:input id="locationName" path="locationName" type="text" class="form:input-large"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="groupName"><spring:message code="user.groupName.label"/></label>
						<div class="col-lg-10">
							<form:input id="groupName" path="groupName" type="text" class="form:input-large"/>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-lg-2 col-lg-2" for="isValid"><spring:message code="user.isValid.label"/></label>
						<div class="col-lg-10">
							<form:checkbox id="isValid" path="isValid"/>
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