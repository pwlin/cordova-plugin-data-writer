var exec = require("cordova/exec");

function DataWriter() {};

DataWriter.prototype.writeToFile = function (fileUrl, params, win, fail) {
	if (!fail) { win = params; }
	exec(win, fail, "DataWriter", "writeToFile", [fileUrl, params]);
};

var dataWriter = new DataWriter();
module.exports = dataWriter;
