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
});


