declare
var angular: any;

angular.module("generalLayout").directive("slideToggle", function() {
    return {
        restrict: "A",
        link: function(scope, element, attrs) {
            var target: HTMLElement;
            target = < HTMLElement > document.querySelector(attrs.slideToggle);
            attrs.expanded = false;
           // element.style.hidden = true;
            element.bind("click", function() {
                var content: HTMLElement;
                content = < HTMLElement > target.querySelector(".slideable_content");
                if (!attrs.expanded) {
                    content.style.border = "1px solid rgba(0,0,0,0)";
                    var y = content.clientHeight;
                    content.style.border = "0";
                    target.style.height = y + "px";
                } else {
                    target.style.height = "0px";
                }
                attrs.expanded = !attrs.expanded;
            });
        }
    };
});