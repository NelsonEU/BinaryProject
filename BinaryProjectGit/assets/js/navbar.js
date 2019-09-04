
/*
*
*
*  NAVBAR DISPLAY
*
*
 */

function replaceNavbar(){
    var stickySidebar = $('.navbar').outerHeight();
    $('header').css('margin-bottom',stickySidebar +'px');
}

window.onresize = function(e){
    // replaceNavbar();
};


$.ajax({
   url: '/',
   type: 'POST',
   data:{
       'action': "isConnected"
   } ,
    success : function (response) {
       var user = JSON.parse(response);
       if(user.admin){
           isAdmin();
       }
       isConnected();
    },
    error: function(jxqr){
       isNotConnected();
    }
});

function isConnected(){
    $('.loginLink').empty();
    $('.loginLink').append("<a class='nav-link' href='/' id='logoutLinkHeader'>Sign out</a>");
}

function isNotConnected(){
    $('.loginLink').empty();
    $('.loginLink').append("<a class='nav-link' id='loginLinkHeader' href='/'>Sign in</a>");
}

function isAdmin(){
    $('.adminLiHeader').css('display','block');
}



/*
*
*
*  NAVIGATION FUNCTIONS
*
*
 */


// LOGIN
$( '.loginLink' ).on( 'click', 'a', function (e) {
    e.preventDefault();
    var linkId = $(this).attr('id');
    switch(linkId){
        case "loginLinkHeader":
            goLogin(e);
            break;
        case "logoutLinkHeader":
            signOut();
            break;
        default:
            break;
    }
});

function goLogin(e){
    if(e != null) {
        e.preventDefault();
    }
    $('.landingPage').css('display','none');
    $('.playingPage').css('display','none');
    $('.tournamentsPage').css('display', 'none');
    $('.login-page').css('display', 'block');
    $('.adminPage').css('display', 'none');
    topFunction();
}

// LOGOUT
function signOut(){
    console.log("SIGN OUT");
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            'action': "logout"
        },
        success: function () {
            // window.location.href = "/";
            location.reload(true);
            // isNotConnected();
        }
    });
}

//ADMIN PAGE
$('#adminLinkHeader').on('click', function(e){
    goAdmin(e);
});

function goAdmin(e){
    if(e != null){
        e.preventDefault();
    }
    $('.login-page').css('display', 'none');
    $('.landingPage').css('display','none');
    $('.playingPage').css('display','none');
    $('.tournamentsPage').css('display', 'none');
    $('.adminPage').css('display', 'block');
    decaleTab();
    topFunction();
    $('#tabAdminBody').hide();
    $('#tabAdminSpinner').show();
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            'action': "getWeeklyTournaments"
        },
        success: function (response) {
            feedTournamentsAdminTab(response);
        }
    });
}


//TOURNAMENTS PAGE
$('#tournamentsLinkHeader').on('click',function(e){
    goTournaments(e);
});

function goTournaments(e){
    if(e != null) {
        e.preventDefault();
    }
    $('.login-page').css('display', 'none');
    $('.landingPage').css('display','none');
    $('.playingPage').css('display','none');
    $('.tournamentsPage').css('display', 'block');
    $('.adminPage').css('display', 'none');
    topFunction();
    $('#tabTournamentsBody').hide();
    $('#tabTournamentsSpinner').show();
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            'action': "getTournaments"
        },
        success: function (response) {
            feedTournamentsTab(response);
        }
    });
}

//HOME PAGE
$('#homeButtonHeader').on('click', function (e) {
    goHome(e);
});

$('#homeLinkHeader').on('click', function (e) {
    goHome(e);
});

function goHome(e){
    if(e != null) {
        e.preventDefault();
    }
    $('.playingPage').css('display','none');
    $('.landingPage').css('display','block');
    $('.tournamentsPage').css('display', 'none');
    $('.login-page').css('display', 'none');
    $('.adminPage').css('display', 'none');
    topFunction();
};


function topFunction() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}