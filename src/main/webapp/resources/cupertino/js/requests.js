var SNIPPET_REGEX = /^([+-]?[1-9]*[C|c|P|p]{1}){1}([-+]{1}[1-9]*[C|c|P|p]{1})* ([\d]+){1}(,{1}[\d]+)* [\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2}(,{1}[\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2})* (\w){4,7}\.[A-Z]{1,2}(,{1}(\w){4,7}\.[A-Z]{1,2})*$/;

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

$(document).ready(function()
{	
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
	
/*	$("input#requests_add_button").click(function()
	{		
	    var request = 
	    {
	    	request:$("input#requests_snippet").val(), 
			bookCode:$("input#requests_bookCode").val(), 
			ClientId:$("input#requests_clientId").val()
	    };
	    
	    $.ajax(
	    {
	        url: contextPath + "/requests/add,
	        dataType : 'json',
	        type : 'POST',
	        data: JSON.stringify(request),
	        success: function(response)
	        {
	            alert('Load was performed.');
	        }
	    });		
	});	*/
	
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



