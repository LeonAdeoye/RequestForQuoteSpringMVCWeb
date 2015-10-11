$(document).ready(function()
{
	$("#chatroom-all-panes").jqxSplitter(
	{
		orientation: 'vertical',
		width: '100%', 
		height: '100%', 
		panels: [{ size: '20%', min: 250 }, { size: '80%'}] 
	});
	
	$('#chatroom-left-pane').jqxSplitter(
	{ 
		width: '100%', 
		height: '100%', 
		orientation: 'horizontal', 
		panels: [{ size: '80%', collapsible: false },{ size: '20%', min: 100, collapsible: false }] 
	});
	
	$("#chatroom-all-panes").bind('resize', function (event) 
	{
		console.log(event.args);
	});
	
	$("#new-chatroom-message").click(function()
	{
		if(trimSpaces($(this).val()) === $(this).attr("default_value"))
			$(this).val("");
	});
	
	$("#new-chatroom-message").focusout(function()
	{
		if(trimSpaces($(this).val()) === "")
			$(this).val($(this).attr("default_value"));
	});
	
	function handleNewChatMessage(chatMessage, requestId, user)
	{
		alert(chatMessage);
	}
	
	$( "#new-chatroom-message" ).keypress(function( event ) 
	{
		if(event.which == 13) 
		{
			if(trimSpaces($(this).val()) !== "" && $(this).val() !== $(this).attr("default_value"))
			{
				var chatMessage = $(this).val();
				$(this).val("");
				handleNewChatMessage(chatMessage, 200, "ladeoye");				
			}
		    event.preventDefault();
		}
	});	
});


