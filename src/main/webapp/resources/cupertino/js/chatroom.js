$(document).ready(function()
{
	$("#chatroom-parent-pane").jqxSplitter(
	{
		orientation: 'horizontal',
		panels: [{ size: 250, min:200}, { size: 400 }]
	});
	
	$("#chatroom-parent-pane").bind(‘resize’, function (event) 
	{
		console.log(event.args);
	});	
});


