declare
var angular;

angular.module("generalLayout").filter("dateFormatter", function() {
    return function(inputDate) {
        var buildDate = new Date(inputDate);
        var month, date, hours, minutes, seconds;
        var formattedDate = buildDate.getFullYear().toString() + "-";
        month = buildDate.getMonth();
        month = month.toString().length === 1 ? "0" + month.toString() : month.toString();
        formattedDate += month.toString() + "-";
        date = buildDate.getDate();
        date = date.toString().length === 1 ? "0" + date.toString() : date.toString();
        formattedDate += date.toString();

        hours = buildDate.getHours();
        hours = hours.toString().length === 1 ? "0" + hours.toString() : hours.toString();
        formattedDate += " " + hours.toString() + ":";

        minutes = buildDate.getMinutes();
        minutes = minutes.toString().length === 1 ? "0" + minutes.toString() : minutes.toString();
        formattedDate += minutes.toString() + ":";

        seconds = buildDate.getSeconds();
        seconds = seconds.toString().length === 1 ? "0" + seconds.toString() : seconds.toString();
        formattedDate += seconds.toString();

        return formattedDate;
    };
});