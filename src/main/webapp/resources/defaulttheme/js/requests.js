$(.new_request).click(function()
{
	alert("clearing");
	if($(this).attr("value") == $(this).attr("default_value"))
		$(this).attr("value") = "";
});

$(.new_request).focusout(function()
{
	alert("defaulting");
	if($(this).attr("value") == "")
		$(this).attr("value") = $(this).attr("default_value");
});

$(#requests_clear_button).click(function()
{
	alert("reset");
	$(#requests_snippet).attr("value", "");
	//$(#requests_client).attr("value", ""); //TODO - generates an error
	$(#requests_bookCode).attr("value", "");
});