
/*
*
*
*  NAVBAR DISPLAY
*
*
 */
var stickySidebar = $('.navbar').height();
$('header').css('margin-bottom',stickySidebar +'px');

$.ajax({
   url: '/',
   type: 'POST',
   data:{
       'action': "isConnected"
   } ,
    success : function (response) {
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


/*
*
*
* TOURNAMENTS ARRAY FEEDING
*
*
 */

function feedTournamentsTab(response){
    var tbody = $('#tabTournamentsBody');
    tbody.empty();
    var tournaments = JSON.parse(response);
    tournaments.forEach(function(key, index){
        var tournament = tournaments[index];
        var line = " <tr>" +
            "<td class=\"partyCell\"><p>" + tournament.party.length + "</p></td>" +
            "<td class=\"startingCell\"><p>" + tournament.startingDate + "</p></td>" +
            "<td class=\"bidCell\"><p>" + tournament.bid + "</p></td>" +
            "<td class=\"joinCell\">";
        if(tournament.registered === true){
            line += "<img class='checkedTableImg' src='images/checked.png'></td></tr>";
        }else{
            line += "<button class='joinButton btn' id='" + tournament.tournamentId + "'>Join</button></td></tr>";
        }
        tbody.append(line);
    });
    $('#tabTournamentsSpinner').hide();
    $('#tabTournamentsBody').show();
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
    e.preventDefault();
    $('.landingPage').css('display','none');
    $('.tournamentsPage').css('display', 'none');
    $('.login-page').css('display', 'block');
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
            isNotConnected();
            goHome(null);
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
    $('.tournamentsPage').css('display', 'block');
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
    $('.landingPage').css('display','block');
    $('.tournamentsPage').css('display', 'none');
    $('.login-page').css('display', 'none');
};