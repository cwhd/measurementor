declare
var angular: any;

angular.module("generalLayout").directive("slideable", function() {
    return {
        restrict: "C",
        compile: function(element, attr) {
            var contents = element.html();
            element.html("<div class='slideable_content' style='margin:0 !important; padding:0 !important'>" + contents + "</div>");

            return function postLink(scope, element, attrs) {
                // default properties
                attrs.duration = (!attrs.duration) ? "1s" : attrs.duration;
                attrs.easing = (!attrs.easing) ? "ease-in-out" : attrs.easing;
                element.css({
                    "overflow": "hidden",
                    "height": "0px",
                    "transitionProperty": "height",
                    "transitionDuration": attrs.duration,
                    "transitionTimingFunction": attrs.easing,
                });
            };
        }
    };
});