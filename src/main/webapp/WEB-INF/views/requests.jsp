<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
		
		<link href="<c:url value="/resources/css/requests.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/css/jquery-ui.min.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/examples.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/slick.grid.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/css/smoothness/jquery-ui-1.8.16.custom.css" />" rel="stylesheet" type="text/css">
		<link href="<c:url value="/resources/slickGrid/controls/slick.columnpicker.css" />" rel="stylesheet" type="text/css">

		<script type="text/javascript" src="<c:url value="/resources/js/jquery-2.1.3.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery-ui.min.js" />"></script>				
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/lib/jquery.event.drag-2.2.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/jquery.browser.min.js" />"></script>
								
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.core.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.formatters.js" />"></script>	
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.editors.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.groupitemmetadataprovider.js" />"></script>			
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.grid.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/slick.dataview.js" />"></script>
		
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/lib/jquery.sparkline.min.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/plugins/slick.rowselectionmodel.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/slickGrid/controls/slick.columnpicker.js" />"></script>
		
		<script type="text/javascript" src="<c:url value="/resources/js/utilities.js" />"></script>
		<script type="text/javascript" src="<c:url value="/resources/js/requests.js" />"></script>
						
		<script type="text/javascript">
			var contextPath='<%=request.getContextPath()%>' /* needed for all ajax calls */			
		</script>
					
	<title><spring:message code="requests.title.label"/></title>
	</head>
	<body>
		<div id="requestsInlineConfigurePanel" class="inlinePanel" style="display:none;">
			<Label><spring:message code="requests.configure.turnOn.updates.label"/></Label>
			<div id="requestsRealTimeUpdatesCheckbox" class="requests_configure_item">
				<input type="checkbox" id="turnOnPriceUpdates"><label for="turnOnPriceUpdates"><spring:message code="requests.configure.turnOn.priceUpdates.label"/></label>
				<input type="checkbox" id="turnOnStatusUpdates"><label for="turnOnStatusUpdates"><spring:message code="requests.configure.turnOn.statusUpdates.label"/></label>
				<input type="checkbox" id="turnOnCalculationUpdates"><label for="turnOnCalculationUpdates"><spring:message code="requests.configure.turnOn.calculationUpdates.label"/></label>
			</div>
			<br>			
		  	<div class="requests_configure_item">
			  	<Label><spring:message code="requests.configure.price.frequency.label"/></Label>
			  	<div style="width:250px;display:inline-block;" id="priceFrequencySlider"></div>
			  	<Label id="priceFrequencySliderValue"></Label>
			</div>
			<br>
			<div class="requests_configure_item">
			  	<Label><spring:message code="requests.configure.price.timeout.label"/></Label>
			  	<div style="width:250px;display:inline-block;" id="priceTimeoutSlider"></div>
			  	<Label id="priceTimeoutSliderValue"></Label>
		  	</div>
		  	<br>		  	
		  	<div class="requests_configure_item">		  	
			  	<Label><spring:message code="requests.configure.calculation.frequency.label"/></Label>
			  	<div style="width:250px;display:inline-block;" id="calculationFrequencySlider"></div>
			  	<Label id="calculationFrequencySliderValue"></Label>			 
			</div>
			<br>
			<div class="requests_configure_item">			  	
			  	<Label><spring:message code="requests.configure.calculation.timeout.label"/></Label>
			  	<div style="width:250px;display:inline-block;" id="calculationTimeoutSlider"></div>
			  	<Label id="calculationTimeoutSliderValue"></Label>		  	
			</div>
			<br>
			<div class="requests_configure_item">		  	
			  	<Label><spring:message code="requests.configure.status.frequency.label"/></Label>
			  	<div style="width:250px;display:inline-block;" id="statusFrequencySlider"></div>
			  	<Label id="statusFrequencySliderValue"></Label>
			</div>
			<br>
			<div class="requests_configure_item">
			  	<Label><spring:message code="requests.configure.status.timeout.label"/></Label>
			  	<div style="width:250px;display:inline-block;" id="statusTimeoutSlider"></div>
			  	<Label id="statusTimeoutSliderValue"></Label>
		  	</div>
		  	<br>
			<div class="requests_configure_item">
			  	<Button id="requestsConfigureSave" class="btn configureBtn"><spring:message code="requests.configure.save.button.label"/></Button>
			  	<Button id="requestsConfigureSaveAndClose" class="btn configureBtn"><spring:message code="requests.configure.save_and_close.button.label"/></Button>
			  	<Button class="hideTopPanel btn configureBtn"><spring:message code="requests.close.button.label"/></Button>
		  	</div>		  	
		</div>
		
		<div id="requestsInlineGroupByPanel" class="inlinePanel" style="display:none;">
			<Label><spring:message code="requests.groupBy.title.label"/></Label>
		  	<div id="requestsGroupByRadio">
		  		<div class="requests_configure_item">
				  	<input type="radio" id="groupByNothing" name="requests.groupBy" checked="checked"><label for="groupByNothing"><spring:message code="requests.groupBy.nothing.label"/></label>
				</div>
				<div class="requests_configure_item">
				    <input type="radio" id="groupByBook" name="requests.groupBy"><label for="groupByBook"><spring:message code="requests.groupBy.book.label"/></label>
				</div>				    
				<div class="requests_configure_item">
				    <input type="radio" id="groupByClient" name="requests.groupBy"><label for="groupByClient"><spring:message code="requests.groupBy.client.label"/></label>
			    </div>
			    <div class="requests_configure_item">
				    <input type="radio" id="groupByUnderlying" name="requests.groupBy"><label for="groupByUnderlying"><spring:message code="requests.groupBy.underlying.label"/></label>
				</div>				    
				<div class="requests_configure_item">
				    <input type="radio" id="groupByStatus" name="requests.groupBy"><label for="groupByStatus"><spring:message code="requests.groupBy.status.label"/></label>
				</div>
				<div class="requests_configure_item">
				    <input type="radio" id="groupByTradeDate" name="requests.groupBy"><label for="groupByTradeDate"><spring:message code="requests.groupBy.tradeDate.label"/></label>
			    </div>
		  	</div>
		  	<br>
		  	<div class="requests_configure_item">
		  		<Button class="hideTopPanel btn"><spring:message code="requests.close.button.label"/></Button>
		  	</div>		  	
		</div>
		
		<div id="requestsInlineFilterPanel" class="inlinePanel" style="display:none;">
			<Label><spring:message code="requests.filter.title.label"/></Label>
			<br>
			<br>
		  	<div id="requestsFilter">
		  		<div class="requests_filter_item">
				  	<input id="requests_filter_bookCode" path="bookCode" class="filter_search_textBox filter_textbox requests_book_autocomplete" value="Select book code..." type="text" default_value="Select book code..." />				  	
				</div>
				<br>
		  		<div class="requests_filter_item">		  		
				  	<input id="requests_filter_client" path="clientId" class="filter_search_textBox filter_textbox requests_client_autocomplete" value="Select client name..." type="text" default_value="Select client name..." />				  	
				</div>
				<br>
				<div class="requests_filter_item">		  		
				  	<input id="requests_filter_tradeDate_start" class="filter_search_textBox filter_textbox" value="Select start trade date..." type="text" default_value="Select start trade date ..." />
				  	<input id="requests_filter_tradeDate_end" class="filter_search_textBox filter_textbox" value="Select end trade date..." type="text" default_value="Select end trade date ..." />
				</div>
				<br>
				<div class="requests_filter_item">		  		
				  	<input id="requests_filter_maturityDate_start" class="filter_search_textBox filter_textbox" value="Select start maturity date..." type="text" default_value="Select start maturity date ..." />
				  	<input id="requests_filter_maturityDate_end" class="filter_search_textBox filter_textbox" value="Select end maturity date..." type="text" default_value="Select end maturity date ..." />
				</div>
				<br>
		  		<div class="requests_filter_item">
				  	<input id="requests_filter_status" path="status" class="filter_search_textBox filter_textbox requests_status_autocomplete" value="Select status code..." type="text" default_value="Select status code..." />				  	
				</div>
				<br>
		  		<div class="requests_filter_item">
				  	<input id="requests_filter_underlying" path="underlying" class="filter_search_textBox filter_textbox requests_underlying_autocomplete" value="Select underlying code..." type="text" default_value="Select underlying code..." />				  	
				</div>				
		  	</div>
		  	<br>
		  	<div class="requests_configure_item">
		  	<Button id="requests_filter_clear_btn" class="btn requests_filter_search_clear_btn"><spring:message code="requests.clear.button.label"/></Button>
		  		<Button class="hideTopPanel btn"><spring:message code="requests.close.button.label"/></Button>
		  	</div>		  	
		</div>
		
		<div id="requestsInlineSearchPanel" class="inlinePanel" style="display:none;">
			<Label><spring:message code="requests.search.title.label"/></Label>
			<br>
			<br>
		  	<div id="requestsSearch">
		  		<div class="requests_search_item">
				  	<input id="requests_search_bookCode" class="filter_search_textBox requests_book_autocomplete" path="bookCode" value="Select book code..." type="text" default_value="Select book code..." />				  	
				</div>
				<br>
		  		<div class="requests_search_item">		  		
				  	<input id="requests_search_client" class="filter_search_textBox requests_client_autocomplete" path="clientId" value="Select client name..." type="text" default_value="Select client name..." />				  	
				</div>
				<br>
				<div class="requests_search_item">		  		
				  	<input id="requests_search_tradeDate_start" class="filter_search_textBox" value="Select start trade date..." type="text" default_value="Select start trade date ..." />
				  	<input id="requests_search_tradeDate_end" class="filter_search_textBox" value="Select end trade date..." type="text" default_value="Select end trade date ..." />
				</div>
				<br>
				<div class="requests_search_item">		  		
				  	<input id="requests_search_maturityDate_start" class="filter_search_textBox" value="Select start maturity date..." type="text" default_value="Select start maturity date ..." />
				  	<input id="requests_search_maturityDate_end" class="filter_search_textBox" value="Select end maturity date..." type="text" default_value="Select end maturity date ..." />
				</div>
				<br>
		  		<div class="requests_search_item">
				  	<input id="requests_search_status" path="status" class="filter_search_textBox requests_status_autocomplete" value="Select status code..." type="text" default_value="Select status code..." />				  	
				</div>
				<br>
		  		<div class="requests_search_item">
				  	<input id="requests_search_underlying" path="underlying" class="filter_search_textBox requests_underlying_autocomplete" value="Select underlying code..." type="text" default_value="Select underlying code..." />				  	
				</div>				
		  	</div>
		  	<br>
		  	<div class="requests_configure_item">
		  		<Button id="requests_search_clear_btn" class="btn requests_filter_search_clear_btn"><spring:message code="requests.clear.button.label"/></Button>
		  		<Button class="hideTopPanel btn"><spring:message code="requests.close.button.label"/></Button>
		  	</div>		  	
		</div>
		
		<span class='loading-indicator' style="display:none;"><label>Retrieving today's requests...</label></span>		

		<ul id="statusContextMenu" class="contextMenu" style="display:none;position:absolute">
		  <b><spring:message code="contextMenu.status.title.label"/></b>
		  <li data="PENDING"><spring:message code="contextMenu.status.pending.menuitem"/></li>
		  <li data="PICKED_UP"><spring:message code="contextMenu.status.picked_up.menuitem"/></li>
		  <li data="PASSED"><spring:message code="contextMenu.status.passed.menuitem"/></li>
		  <li data="TRADED_AWAY"><spring:message code="contextMenu.status.traded_away.menuitem"/></li>
		  <li data="TRADED_AWAY_ASK"><spring:message code="contextMenu.status.traded_away_ask.menuitem"/></li>
		  <li data="TRADED_AWAY_BID"><spring:message code="contextMenu.status.traded_away_bid.menuitem"/></li>
		  <li data="FILLED"><spring:message code="contextMenu.status.filled.menuitem"/></li>
		  <li data="FILLED_ASK"><spring:message code="contextMenu.status.filled_ask.menuitem"/></li>
		  <li data="FILLED_BID"><spring:message code="contextMenu.status.filled_bid.menuitem"/></li>		  
		  <li data="INVALID"><spring:message code="contextMenu.status.invalid.menuitem"/></li>
		</ul>
		<ul id="requestContextMenu" class="contextMenu" style="display:none;position:absolute">
		  <b><spring:message code="contextMenu.operation.title.label"/></b>
		  <li data="VIEW_DETAILS"><spring:message code="contextMenu.operation.view.menuitem"/></li>
		  <li data="PICKED_UP"><spring:message code="contextMenu.operation.pick_up.menuitem"/></li>
		  <li data="SAVE"><spring:message code="contextMenu.operation.save.menuitem"/></li>
		  <li data="RECALCULATE"><spring:message code="contextMenu.operation.recalculate.menuitem"/></li>
		  <li data="FREEZE"><spring:message code="contextMenu.operation.freeze.menuitem"/></li>
		  <li data="CHART"><spring:message code="contextMenu.operation.chart.menuitem"/></li>
		  <li data="CLONE"><spring:message code="contextMenu.operation.clone.menuitem"/></li>
		  <li data="DELETE"><spring:message code="contextMenu.operation.delete.menuitem"/></li>
		</ul>
		<div id="requests_bar">
			<div id="requests_title">
				<p id="new_requests"><spring:message code="requests.addNewRequest.label"/></p>
			</div>
			<div class="addNew" id="requests_add_new">
				<input id="requests_snippet" class="new_request" path="request" value="Enter request snippet..." type="text" default_value="Enter request snippet..." />
				<input id="requests_client" class="new_request requests_client_autocomplete" path="clientId" value="Select client name..." type="text" default_value="Select client name..." />
				<input id="requests_bookCode" class="new_request requests_book_autocomplete" path="bookCode" value="Select book code..." type="text" default_value="Select book code..." />
				<button id="requests_add_button" class="btn"><spring:message code="requests.addRequest.button.label"/></Button>
				<button id="requests_clear_button" class="btn"><spring:message code="requests.clearRequest.button.label"/></Button>																		
				<button id="requests_filter_button" data="filter" class="btn toggleTopPanel"><spring:message code="requests.filterRequest.button.label"/></Button>
				<button id="requests_search_button" data="search" class="btn toggleTopPanel"><spring:message code="requests.searchRequest.button.label"/></Button>
				<button id="requests_group_button" data="group" class="btn toggleTopPanel"><spring:message code="requests.groupRequest.button.label"/></Button>
				<button id="requests_chart_button" data="chart" class="btn toggleTopPanel"><spring:message code="requests.chartRequest.button.label"/></Button>
				<button id="requests_config_button" data="configure" class="btn toggleTopPanel"><spring:message code="requests.configRequest.button.label"/></Button>
			</div>
			<div class="pull-right" id="requests_language_link">
				<a href="?language=en">English</a>|<a href="?language=jp">Japanese</a>
			</div>								
		</div>
		
		<div style="width:1330px;">
    		<div id="requestsGrid" style="width:1330px;height:595px;"></div>
		</div>

	</body>
</html>