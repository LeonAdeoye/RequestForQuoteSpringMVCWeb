var SNIPPET_REGEX = /^([+-]?[1-9]*[C|c|P|p]{1}){1}([-+]{1}[1-9]*[C|c|P|p]{1})* ([\d]+){1}(,{1}[\d]+)* [\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2}(,{1}[\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2})* (\w){4,7}\.[A-Z]{1,2}$/;

function enableAddButton()
{
	$("input#requests_add_button").removeAttr('disabled');
}

function enableClearButton()
{
	$("input#requests_clear_button").removeAttr('disabled');
}

function disableAddButton()
{
	$("input#requests_add_button").attr('disabled', 'disabled');
}

function disableClearButton()
{
	$("input#requests_clear_button").attr('disabled', 'disabled');
}

function snippetMatches(snippet)
{
	return SNIPPET_REGEX.test(snippet);
}

function toggleAddButtonState()
{
	if(snippetMatches($("input#requests_snippet").val()))
		enableAddButton();
	else
		disableAddButton();
}

function myTrim(x)
{
	return x.replace(/^\s+|\s+$/gm,'');
}

String.prototype.toPascalCase = function()
{
    return this.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
};

function dateFormatter(row, cell, value, columnDef, dataContext)
{
    if (value != null)
    {
    	var displayValue = new String(value.dayOfMonth);
    	displayValue = displayValue.concat(((value.month).substr(0,3)).toPascalCase());
    	displayValue = displayValue.concat(value.year);
    	return displayValue;
    	
    	//return value.monthValue + '/' + value.dayOfMonth + '/' + value.year;
    }
    else
        return "";
}

function pascalCaseFormatter(row, cell, value, columnDef, dataContext)
{
    if (value == null)
        return "";
    else
    	return value; //.toPascalCase();
}

function decimalFormatter(row, cell, value, columnDef, dataContext)
{
    if (value == null)
        return "0.000";
    else
    	return value.toFixed(3);     
}

var requestsGrid;
var dataView = new Slick.Data.DataView();

var columns = 
[
 	{id: "requestId", name: "Request ID", field: "identifier", sortable: true, toolTip: "Request unique identifier"},
	{id: "snippet", name: "Snippet", field: "request", cssClass: "cell-title", minWidth: 300, validator: requiredFieldValidator, toolTip: "Request snippet"},
	{id: "status", name: "Status", field: "status", sortable: true, toolTip: "Current status of request.", formatter: pascalCaseFormatter},
	{id: "pickedUpBy", name: "Picked Up By", field: "pickedUpBy", sortable: true, toolTip: "Request picked up by user:"},
	{id: "clientId", name: "Client ID", field: "clientId", sortable: true, toolTip: "Client this requests applies to"},
	{id: "bookCode", name: "Book Code", field: "bookCode", sortable: true, toolTip: "Book code"},
	{id: "tradeDate", name: "Trade Date", field: "tradeDate", formatter: dateFormatter, editor: Slick.Editors.Date, sortable: true, toolTip: "Trade date"},
	{id: "premiumAmount", name: "Theoretical Value", field: "premiumAmount", formatter: decimalFormatter, toolTip: "Theoretical value"},
	{id: "timeValue", name: "Time Value", field: "timeValue", formatter: decimalFormatter, toolTip: "Time value"},
	{id: "intrinsicValue", name: "Intrinsic Value", field: "intrinsicValue", formatter: decimalFormatter, toolTip: "Intrinsic value"},	    
	{id: "delta", name: "Delta", field: "delta", formatter: decimalFormatter, toolTip: "Delta"},
	{id: "gamma", name: "Gamma", field: "gamma", formatter: decimalFormatter, toolTip: "Gamma"},
	{id: "vega", name: "Vega", field: "vega", formatter: decimalFormatter, toolTip: "Vega"},
	{id: "theta", name: "Theta", field: "theta", formatter: decimalFormatter, toolTip: "Theta"},
	{id: "rho", name: "Rho", field: "rho", formatter: decimalFormatter, toolTip: "Rho"},
	{id: "underlyingDetails", name: "Underlying Price", field: "underlyingDetails", toolTip: "Underlying Price"},
	{id: "salesComment", name: "Sales Comment", field: "salesComment", editor: Slick.Editors.LongText, toolTip: "Comments made by sales."},
	{id: "traderComment", name: "Trader Comment", field: "traderComment", editor: Slick.Editors.LongText, toolTip: "Comments made by the traders."},
	{id: "clientComment", name: "Client Comment", field: "clientComment", editor: Slick.Editors.LongText, toolTip: "Comments made by the client."}		
];

var options = 
{
	enableCellNavigation: true,
	enableColumnReorder: true,		
    editable: true,
    enableAddRow: false,
    asyncEditorLoading: false,
    autoEdit: false,
    forceFitColumns: false,
    topPanelHeight:250	    
};

function requiredFieldValidator(value) 
{
	if (value == null || value == undefined || !value.length) 
	{
		return {valid: false, msg: "This is a required field"};
	}
	else 
	{
		return {valid: true, msg: null};
	}
}

$(document).ready(function()
{
	sortColumn = "requestId";	
	var requestsGrid = new Slick.Grid("#requestsGrid", dataView, columns, options);
	requestsGrid.setSelectionModel(new Slick.RowSelectionModel());
	requestsGrid.setTopPanelVisibility(false);
	getRequestsFromTodayOnly();
	
	requestsGrid.onSort.subscribe(function(e, args)
	{
	    sortColumn = args.sortCol.field;
	    
		function lexographicComparer(a, b)
		{
			var x = a[sortColumn], y = b[sortColumn];
			return (x == y ? 0 : (x > y ? 1 : -1));
		}
		
		function dateComparer(a, b)
		{
			var x = a[sortColumn], y = b[sortColumn];
			
			if(x.year == y.year)
			{
				if(x.monthValue == y.monthValue)
					return x.dayOfMonth - y.dayOfMonth; 
				else
					return x.monthValue - y.monthValue;
			}
			else
				return x.year - y.year;
		}		

		dataView.sort((sortColumn == "tradeDate" ? dateComparer : lexographicComparer), args.sortAsc);
	});
	
	dataView.onRowCountChanged.subscribe(function (e, args)
	{
		requestsGrid.updateRowCount();
		requestsGrid.render();
	});

	dataView.onRowsChanged.subscribe(function (e, args) 
	{
		requestsGrid.invalidateRows(args.rows);
		requestsGrid.render();
	});	

	function toggleSearchPanel() 
	{
		requestsGrid.setTopPanelVisibility(!requestsGrid.getOptions().showTopPanel);
	}
	
	requestsGrid.render();
	
	$("<Button class='toggleSearchPanel'>Close</Button>").appendTo(requestsGrid.getTopPanel());
	
	$('.toggleSearchPanel').bind('click', function(event) 
	{
		toggleSearchPanel();
	});
	
	requestsGrid.onContextMenu.subscribe(function (e) 
	{
        var cell = requestsGrid.getCellFromEvent(e);
        
        if (requestsGrid.getColumns()[cell.cell].id == "status")
        {
        	e.preventDefault();
        	
	        $("#contextMenu")
	            .data("row", cell.row)
	            .css("top", e.pageY)
	            .css("left", e.pageX)
	            .show();
        
	        $("body").one("click", function() 
	        {
	          $("#contextMenu").hide();
	        });
        }
    });	
	
	function getRequestsFromTodayOnly() 
	{		
		$.ajax({
		    url: contextPath + "/requests/today", 
		    type: 'GET', 
		    dataType: 'json',  
		    contentType: 'application/json',
		    mimeType: 'application/json',
		    success: function(requests) 
		    {
		    	dataView.setItems(requests, "identifier");
		    },
            error: function (xhr, textStatus, errorThrown) 
            {
                alert('Error: ' + xhr.responseText);
            }
		});
	}
	
	getRequestsFromTodayOnly();
		
	$("input#requests_bookCode").autocomplete(
	{
		minLength:3,
        source: function (request, response) 
        {
            $.ajax(
            {
                type: "GET",
                url: contextPath + "/requests/matchingBookTags?pattern=" + request.term,
                dataType: "json",
                data: 
                {
                    term: request.termCode
                },
                error: function (xhr, textStatus, errorThrown) 
                {
                    alert('Error: ' + xhr.responseText);
                },
                success: function (data) 
                {
                    response($.map(data, function (item) 
                    {
                        return {
                            		label: item.label,
                            		value: item.value
                        		}
                    }));
                }
            });
        }
    });
	
	$("input#requests_client").autocomplete(
	{
		minLength:2,
        source: function (request, response) 
        {
            $.ajax(
            {
                type: "GET",
                url: contextPath + "/requests/matchingClientTags?pattern=" + request.term,
                dataType: "json",
                data: 
                {
                    term: request.termCode
                },
                error: function (xhr, textStatus, errorThrown) 
                {
                    alert('Error: ' + xhr.responseText);
                },
                success: function (data) 
                {
                    response($.map(data, function (item) 
                    {
                        return {
                            		label: item.label,
                            		value: item.value
                        		}
                    }));
                }
            });
        }
    });
	
    $("#contextMenu").click(function (e) 
    {
    	if (!$(e.target).is("li")) 
    		return;
      
    	if (!requestsGrid.getEditorLock().commitCurrentEdit())
    		return;
      
    	var row = $(this).data("row");
    	dataView.getItem(row).status = $(e.target).attr("data");
    	requestsGrid.updateRow(row);
    });	
	
	$(".btn").button(); // TODO disable does not work yet. find disabled attribute and add it.
	
	disableAddButton();	
	$("input#requests_snippet").keyup(toggleAddButtonState);
	$("input#requests_snippet").focusout(toggleAddButtonState);

	$("input.new_request").click(function()
	{
		if($(this).val() == $(this).attr("default_value"))
		{
			$(this).val("");
			
			if($(this).is("input#requests_snippet"))
				disableAddButton();
		}
	});

	$("input.new_request").focusout(function()
	{
		if(myTrim($(this).val()) == "")
			$(this).val($(this).attr("default_value"));
	});

	$("input#requests_clear_button").click(function()
	{
		$("input.new_request").val($(this).attr("default_value"));
		
		disableAddButton();		
	});	
});



