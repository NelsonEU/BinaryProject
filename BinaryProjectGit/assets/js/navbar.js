
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
    replaceNavbar();
};

$('.navbar-nav>li>a').on('click', function(){
    collapseNavbar();
});

$('.linkDropdown').on('click', function(){
    console.log("ON CLIQUE");
    collapseNavbar();
});

function collapseNavbar(){
    $('.navbar-collapse').collapse('hide');
}



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
    $('.dropdownMenuUser').empty();
    $('.dropdownMenuUser').append("<li><a class='nav-link linkDropdown' id='profileLinkHeader'>Account</a></li>");
    $('.dropdownMenuUser').append("<li><a class='nav-link linkDropdown loginLink' id='logoutLinkHeader' onclick='signOut()'>Sign out</a></li>");
}

function isNotConnected(){
    $('.dropdownMenuUser').empty();
    $('.dropdownMenuUser').append("<li><a class='nav-link linkDropdown loginLink' id='loginLinkHeader' onclick='goLogin(null)'>Sign in</a></li>");
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
// $('.loginLink').on( 'click', 'a', function (e) {
//     e.preventDefault();
//     console.log("ON EST LAAAAA");
//     var linkId = $(this).attr('id');
//     switch(linkId){
//         case "loginLinkHeader":
//             goLogin(e);
//             break;
//         case "logoutLinkHeader":
//             signOut();
//             break;
//         default:
//             break;
//     }
// });

function goLogin(e){
    collapseNavbar();
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
    collapseNavbar();
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
    e.preventDefault();
    goTournamentsList();
    // goTournaments(e);
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
    callAjaxTournaments();
}

function callAjaxTournaments(){
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