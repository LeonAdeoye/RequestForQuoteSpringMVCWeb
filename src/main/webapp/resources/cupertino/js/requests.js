var SNIPPET_REGEX = /^([+-]?[1-9]*[CP]{1}){1}([-+]{1}[1-9]*[CP]{1})* ([\d]+){1}(,{1}[\d]+)* [\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2}(,{1}[\d]{1,2}(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)20[\d]{2})* (\w){4,7}\.[A-Z]{1,2}(,{1}(\w){4,7}\.[A-Z]{1,2})*$/;

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
	$(".btn").button();
	
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

	$("#requests_clear_button").click(function()
	{
		$("input.new_request").val($(this).attr("default_value"));
		
		disableAddButton();		
	});	
});



