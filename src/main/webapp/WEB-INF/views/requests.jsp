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
		
<%-- 		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
  		<script type="text/javascript" src="<c:url value="/resources/js/lineChart.js" />"></script> --%>		
						
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
				  	<input id="requests_filter_bookCode" path="bookCode" class="filter_search_textBox filter_textbox requests_book_autocomplete" value="Select book code..." type="text" criterion_name="bookCode" default_value="Select book code..." />
				  	<input id="requests_filter_client" path="clientId" class="rhs filter_search_textBox filter_textbox requests_client_autocomplete" value="Select client name..." type="text" criterion_name="clientId" default_value="Select client name..." />				  	
				</div>
				<br>
				<div class="requests_filter_item">		  		
				  	<input id="requests_filter_tradeDate_start" class="filter_search_textBox dateTxtBox filter_textbox" value="Select start trade date..." type="text" default_value="Select start trade date..." criterion_name="startTradeDate" />
				  	<input id="requests_filter_tradeDate_end" class="rhs filter_search_textBox dateTxtBox filter_textbox" value="Select end trade date..." type="text" default_value="Select end trade date..." criterion_name="endTradeDate" />
				</div>
				<br>
				<div class="requests_filter_item">		  		
				  	<input id="requests_filter_maturityDate_start" class="filter_search_textBox dateTxtBox filter_textbox" value="Select start maturity date..." type="text" default_value="Select start maturity date..." criterion_name="startMaturityDate" />
				  	<input id="requests_filter_maturityDate_end" class="rhs filter_search_textBox dateTxtBox filter_textbox" value="Select end maturity date..." type="text" default_value="Select end maturity date..." criterion_name="endMaturityDate" />
				</div>
				<br>
		  		<div class="requests_filter_item">
				  	<input id="requests_filter_status" path="status" class="filter_search_textBox filter_textbox requests_status_autocomplete" value="Select status code..." type="text" criterion_name="status" default_value="Select status code..." />
				  	<input id="requests_filter_underlying" path="underlying" class="rhs filter_search_textBox filter_textbox requests_underlying_autocomplete" value="Select underlying code..." type="text" criterion_name="underlyingRIC" default_value="Select underlying code..." />				  	
				</div>
		  	</div>
		  	<br>
		  	<div class="requests_configure_item">
		  		<Button id="requests_filter_clear_btn" class="btn requests_filter_search_clear_btn"><spring:message code="requests.clear.button.label"/></Button>
		  		<Button id="filter_close_btn" class="filter_search_close_btn hideTopPanel btn"><spring:message code="requests.close.button.label"/></Button>
		  	</div>		  	
		</div>
		
		<div id="requestsInlineSearchPanel" class="inlinePanel" style="display:none;">
			<Label><spring:message code="requests.search.title.label"/></Label>
			<br>
			<br>
		  	<div id="requestsSearch">
		  		<div class="requests_search_item">
				  	<input id="requests_search_bookCode" class="search_textBox requests_search_collate filter_search_textBox requests_book_autocomplete" path="bookCode" value="Select book code..." type="text" default_value="Select book code..." criterion_name="bookCode" />
				  	<input id="requests_search_client" class="search_textBox rhs filter_search_textBox requests_client_autocomplete" path="clientId" value="Select client name..." type="text" default_value="Select client name..." criterion_name="clientId" />				  	
				</div>
				<br>
				<div class="requests_search_item">
				  	<input id="requests_search_tradeDate_start" class="search_textBox requests_search_collate filter_search_textBox dateTxtBox" value="Select start trade date..." type="text" default_value="Select start trade date..." criterion_name="startTradeDate" />
				  	<input id="requests_search_tradeDate_end" class="search_textBox requests_search_collate rhs filter_search_textBox dateTxtBox" value="Select end trade date..." type="text" default_value="Select end trade date..." criterion_name="endTradeDate" />
				</div>
				<br>
				<div class="requests_search_item">
				  	<input id="requests_search_maturityDate_start" class="search_textBox requests_search_collate filter_search_textBox dateTxtBox" value="Select start maturity date..." type="text" default_value="Select start maturity date..." criterion_name="startMaturityDate" />
				  	<input id="requests_search_maturityDate_end" class="search_textBox requests_search_collate rhs filter_search_textBox dateTxtBox" value="Select end maturity date..." type="text" default_value="Select end maturity date..." criterion_name="endMaturityDate" />
				</div>
				<br>
		  		<div class="requests_search_item">
				  	<input id="requests_search_status" path="status" class="search_textBox filter_search_textBox requests_status_autocomplete" value="Select status code..." type="text" default_value="Select status code..." criterion_name="status"/>
				  	<input id="requests_search_underlying" path="underlying" class="search_textBox requests_search_collate rhs filter_search_textBox requests_underlying_autocomplete" value="Select underlying code..." type="text" default_value="Select underlying code..." criterion_name="underlyingRIC"/>				  	
				</div>
				<br>
				<div class="requests_search_item">
					<label><spring:message code="requests.search.tier.label"/></label>
					<div>
						<input type="checkbox" id="requestSearchTopTierClient"><label for="requestSearchTopTierClient"><spring:message code="requests.search.topTier.label"/></label>
						<input type="checkbox" id="requestSearchMiddleTierClient"><label for="requestSearchMiddleTierClient"><spring:message code="requests.search.middleTier.label"/></label>
						<input type="checkbox" id="requestSearchBottomTierClient"><label for="requestSearchBottomTierClient"><spring:message code="requests.search.bottomTier.label"/></label>					
					</div>
				</div>				
		  	</div>
			<br>
		  	<div class="requests_configure_item">
		  		<Button id="requests_search_search_btn" class="btn"><spring:message code="requests.search.search.button.label"/></Button>
		  		<Button id="requests_search_save_btn" class="btn"><spring:message code="requests.search.save.button.label"/></Button>
		  		<Button id="requests_search_clear_btn" class="btn requests_filter_search_clear_btn"><spring:message code="requests.search.clear.button.label"/></Button>
		  		<Button class="filter_search_close_btn hideTopPanel btn"><spring:message code="requests.close.button.label"/></Button>
		  	</div>		  			  
		</div>
		
		<div id="requestsInlineChartPanel" class="inlinePanel" style="display:none;">
			<div>
		  		<div class="requests_configure_item">
				  	<input type="radio" id="profitAndLossChart" name="chart.profitAndLoss" checked="checked"><label for="profitAndLossChart">Profit and loss chart</label>
				</div>
		  		<div class="requests_configure_item">
				  	<input type="radio" id="deltaChart" name="chart.delta" checked="checked"><label for="deltaChart">Delta chart</label>
				</div>
		  		<div class="requests_configure_item">
				  	<input type="radio" id="gammaChart" name="chart.delta" checked="checked"><label for="gammaChart">Gamma chart</label>
				</div>								
			</div>
		  	<div class="requests_configure_item">
		  		<Button id="requests_chart_chart_btn" class="btn"><spring:message code="requests.chart.chart.button.label"/></Button>
		  		<Button id="requests_chart_clear_btn" class="btn requests_filter_search_clear_btn"><spring:message code="requests.chart.clear.button.label"/></Button>
		  		<Button class="filter_search_close_btn hideTopPanel btn"><spring:message code="requests.close.button.label"/></Button>
		  	</div>		  			  
		</div>
		
		<span class='loading-indicator' style="display:none;"><label><spring:message code="loading.indicator.label"/></label></span>		

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
				<button id="requests_add_more_button" class="btn">...</Button>
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
		</div
				
		<div id="new-request-dialog-parent" title="Add new request for quote" style="display:none;" >
			<input id="dialog_requests_snippet" class="new-requests-dialog new_request" path="request" value="Enter request snippet..." type="text" default_value="Enter request snippet..." />
			<input id="dialog_requests_strike" class="new-requests-dialog new_request" path="strike" value="Enter strike..." type="text" default_value="Enter strike..." />
			<input id="dialog_requests_maturity_date" class="new-requests-dialog new_request" path="maturity_date" value="Enter maturity date..." type="text" default_value="Enter maturity date..." />
			<input id="dialog_requests_underlying_ric" class="new-requests-dialog new_request" path="underlying_ric" value="Enter underlying RIC..." type="text" default_value="Enter underlying RIC..." />
			<input id="dialog_requests_underlying_price" class="new-requests-dialog new_request" path="underlying_price" value="Enter underlying price..." type="text" default_value="Enter underlying price..." />
		</div>
		
		<div class="chart-to-clone" title="Request For Quote Chart"> 
			<div class="chart-content" >
				<ul>
					<li><a href="#pnl">PnL</a></li>
					<li><a href="#delta">Delta</a></li>
					<li><a href="#gamma">Gamma</a></li>
					<li><a href="#vega">Vega</a></li>
					<li><a href="#theta">Theta</a></li>
					<li><a href="#rho">Rho</a></li>
				</ul>
				<div id="pnl">
					<div id="linechart_material"></div>
				</div>
				<div id="delta"></div>
				<div id="gamma"></div>
				<div id="vega"></div>
				<div id="theta"></div>
				<div id="rho"></div>
			</div				
		</div>

	</body>
</html>