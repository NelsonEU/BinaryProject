
var stickySidebar = $('.navbar').height();

console.log("PX: " + stickySidebar);
$('header').css('margin-bottom',stickySidebar +'px');


function signOut() {
    // e.preventDefault();
    console.log("SIGN OUT");
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            'action': "logout"
        },
        success: function () {
            localStorage.removeItem("connectedUser");
            window.location.href="/";
        }

    });
}

$(function () {
    var user = localStorage.getItem("connectedUser");
    if(user != null){
        console.log("user not null frontend yesssss");
        $('#loginLinkHeader').attr('href',' ');
        $('#loginLinkHeader').attr('onclick','signOut()');
        $('#loginLinkHeader').text("Sign out");
    }
});