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


$('#refreshTabTournamentsA').on('click', function(e){
    goTournaments(e);
});
