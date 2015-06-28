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

function displayDate(row, cell, value, columnDef, dataContext)
{
    if (value == null)
    {
        return "";
    }
    else
    {
    	var displayValue = new String(value.dayOfMonth);
    	displayValue = displayValue.concat(" ");
    	displayValue = displayValue.concat(((value.month).substr(0,3)).toPascalCase());
    	displayValue = displayValue.concat(" ");
    	displayValue = displayValue.concat(value.year);
    	return displayValue;
    }        
}

function displayDecimal(row, cell, value, columnDef, dataContext)
{
    if (value == null)
    {
        return "0.000";
    }
    else
    {
    	return value.toFixed(3);
    }        
}

$(document).ready(function()
{	
	var columns = 
	[
	 	{id: "requestId", name: "Request ID", field: "identifier"},
		{id: "snippet", name: "Snippet", field: "request", minWidth: 300},
		{id: "status", name: "Status", field: "status", sortable: true},
		{id: "pickedUpBy", name: "Picked Up By", field: "pickedUpBy", sortable: true},
		{id: "clientId", name: "Client ID", field: "clientId", sortable: true},
		{id: "bookCode", name: "Book Code", field: "bookCode", sortable: true},
		{id: "tradeDate", name: "Trade Date", field: "tradeDate", formatter: displayDate, editor: "DateEditor", sortable: true},
		{id: "premiumAmount", name: "Theoretical Value", field: "premiumAmount", formatter: displayDecimal},
		{id: "timeValue", name: "Time Value", field: "timeValue", formatter: displayDecimal},
		{id: "intrinsicValue", name: "Intrinsic Value", field: "intrinsicValue", formatter: displayDecimal},	    
		{id: "delta", name: "Delta", field: "delta", formatter: displayDecimal},
		{id: "gamma", name: "Gamma", field: "gamma", formatter: displayDecimal},
		{id: "vega", name: "Vega", field: "vega", formatter: displayDecimal},
		{id: "theta", name: "Theta", field: "theta", formatter: displayDecimal},
		{id: "rho", name: "Rho", field: "rho", formatter: displayDecimal},
		{id: "underlyingDetails", name: "Underlying Price", field: "underlyingDetails"}
	];

	var options = 
	{
		enableCellNavigation: true,
		enableColumnReorder: true
	};
	
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
		    	var grid = new Slick.Grid("#requestsGrid", requests, columns, options);		    	
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



