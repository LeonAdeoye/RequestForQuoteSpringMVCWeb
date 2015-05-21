$(".new_request").click(function()
{
	if($(this).attr("value") == $(this).attr("default_value"))
		$(this).attr("value") = "";
});

$(".new_request").focusout(function()
{
	if($(this).attr("value") == "")
		$(this).attr("value") = $(this).attr("default_value");
});

$("#requests_clear_button").click(function()
{
	$("#requests_snippet").attr("value", "");
	$("#requests_bookCode").attr("value", "");
});
