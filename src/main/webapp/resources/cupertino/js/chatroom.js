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
	
	function handleNewChatMessage(chatMessage, requestId, userId)
	{
		$("#chatroom-message-list").append($("<li></li>")
				.addClass("added-chat-message-class")
				.text("[" + userId + "] " + chatMessage));
	}
	
	$( "#new-chatroom-message" ).keypress(function( event ) 
	{
		if(event.which == 13) 
		{
			if(trimSpaces($(this).val()) !== "" && $(this).val() !== $(this).attr("default_value"))
			{
				var chatMessage = $(this).val();
				$(this).val("");
				handleNewChatMessage(chatMessage, 200, "ladeoye");	// TODO			
			}
		    event.preventDefault();
		}
	});
	
	// TODO	
	// Style of chat message - google list styling
	// Flash messages received
	// Small pop-up with fade-out.
	// Scroll up so bottom <li> is always visible.
	// Color the name and time in chat message
});


