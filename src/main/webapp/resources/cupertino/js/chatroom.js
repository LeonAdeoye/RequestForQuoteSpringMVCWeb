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
	
	function save(chatMessage, requestId, userId)
	{
	    var newChatMessage = {"sender" : userId, "content" : chatMessage, "requestId" : requestId};
	    
		$.ajax({
		    url: contextPath + "/chatroom/ajaxSendChatMessage", 
		    type: 'POST',
		    data: JSON.stringify(newChatMessage),
		    dataType: 'json',  
		    contentType: 'application/json', 
		    mimeType: 'application/json',
		    timeout: 5000,
		    cache: false,
		    success: function(newlySavedChatMessage) 
		    {
		    	if(newlySavedChatMessage)
					handleNewChatMessage(newlySavedChatMessage);	    		
		    },
	        error: function (xhr, textStatus, errorThrown) 
	        {
	        	console.log(xhr.responseText);
	        	if(textStatus !== "timeout")
        		{
	        		if(xhr.status === 404)
	        			alert('Failed to save the chat message because the server is no longer available. Please try to reload the page.');
	        		else
	        			alert('Failed to save the chat message because of a server error.');   
        		}
	        	else
	        		alert('Failed to save a chat message because of a timeout after five seconds');
	        }
		});		
	}
	
	var chatMessageCount = 0;
	
	function handleNewChatMessage(newlySavedChatMessage)
	{
		var newChatMessageId = "REQ" + newlySavedChatMessage.requestId + "MSG" +  chatMessageCount++;
		
		var newChatMessageElem = $("div.message-to-clone")
			.clone(true)
			.addClass("message-cloned")
			.removeClass("message-to-clone")
			.attr("id", newChatMessageId);
					
		$("<li/>").add(newChatMessageElem).appendTo("ul#chatroom-message-list");
		
		$("div#" + newChatMessageId + " div.chat-message-sender-class")
		.html(newlySavedChatMessage.sender + "[" + convertToTime(newlySavedChatMessage.timeStamp) + "]");
	
		$("div#" + newChatMessageId + " div.chat-message-content-class").html(newlySavedChatMessage.content);
		
		//$("<li/>").appendTo("ul#chatroom-rooms-list").html("[" + userId + "] " + chatMessage);
		
		/*$("#chatroom-message-list")
			.append("<li></li>")
			.addClass("added-chat-message-class")
			.text("[" + userId + "] " + chatMessage);*/
	}
	
	$( "#new-chatroom-message" ).keypress(function( event ) 
	{
		if(event.which == 13) 
		{
			if(trimSpaces($(this).val()) !== "" && $(this).val() !== $(this).attr("default_value"))
			{
				var chatMessage = $(this).val();
				$(this).val("");
				
				save(chatMessage, 991, "leon.adeoye"); //TODO		
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


