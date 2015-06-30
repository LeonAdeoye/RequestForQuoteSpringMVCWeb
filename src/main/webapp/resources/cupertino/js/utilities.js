String.prototype.toPascalCase = function()
{
    return this.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
};

function trimSpaces(x)
{
	return x.replace(/^\s+|\s+$/gm,'');
}