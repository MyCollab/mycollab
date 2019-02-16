/*Scroll to top when arrow up clicked BEGIN*/
$(window).scroll(function() {
    var height = $(window).scrollTop();
    if (height > 100) {
        $('#linkToTop').fadeIn();
    } else {
        $('#linkToTop').fadeOut();
    }
});
$("#main-body").scroll(function() {
    var height = $(window).scrollTop();
    if (height > 100) {
        $('#linkToTop').fadeIn();
    } else {
        $('#linkToTop').fadeOut();
    }
});

$("#project-module").scroll(function() {
    var height = $(window).scrollTop();
    if (height > 100) {
        $('#linkToTop').fadeIn();
    } else {
        $('#linkToTop').fadeOut();
    }
});
$("#project-view").scroll(function() {
    var height = $(window).scrollTop();
    if (height > 100) {
        $('#linkToTop').fadeIn();
    } else {
        $('#linkToTop').fadeOut();
    }
});
$(document).ready(function() {
    $("#linkToTop").click(function(event) {
        event.preventDefault();
        $("html, body").animate({ scrollTop: 0 }, "slow");
        return false;
    });

});
 /*Scroll to top when arrow up clicked END*/