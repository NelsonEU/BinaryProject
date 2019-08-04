$(function(){
    var widthCreate = $('.btnCreate').width();
    var widthWatch = $('.btnWatch').width();
    var windowWidth = $(window).width();
    var add = 100;
    if(windowWidth < 380){
        add = 40;
        $('.btnCreate').css("font-size","small");
        $('.btnWatch').css("font-size","small");
    }
    if(windowWidth < 300){
        add = 10;
        $('.btnCreate').css("font-size","small");
        $('.btnWatch').css("font-size","small");
    }
    if(widthCreate < widthWatch){
        $('.btnCreate').width(widthWatch + add);
        $('.btnWatch').width(widthWatch + add);
    }else{
        $('.btnWatch').width(widthCreate + add);
        $('.btnCreate').width(widthCreate + add);
    }
});

$(function () {
    var headerHeight = $('header').height();
    $('.landingPage').css('padding-top', headerHeight + 'px');
});

function OnCookieAccept(){
    $('#cookie-msg').hide();
}

$('#btnStartCompeting').on('click', function (e) {
    console.log("On click home");
    goTournaments(e);
});

/*
$(function(){
    $('.parallax-window').parallax({imageSrc: '../images/bg-headLanding.jpg'});
    console.log("yo");
});
*/

