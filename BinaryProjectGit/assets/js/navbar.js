
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

$('#tournamentsLinkHeader').on('click',function(e){
    goTournaments(e);
});

function goTournaments(e){
    console.log("On est goTournaments");
    e.preventDefault();
    //TODO AJAX CALL TO GET DATA
    $('.landingPage').css('display','none');
    $('.tournamentsPage').css('display', 'block');
    $('#tabTournamentsBody').hide();
    $('#tabTournamentsSpinner').show();
    console.log("ON FAIT L'APPEL");
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

function feedTournamentsTab(response){
    //TODO ON FEED LE TABLEAU
    var tbody = $('#tabTournamentsBody');
    tbody.empty();
    var tournaments = JSON.parse(response);
    tournaments.forEach(function(key, index){
        var tournament = tournaments[index];
        console.log("INDEX: " + index);
        console.log("TOURNAMENT ID: " + tournament.tournamentId);
        console.log(tournament.startingDate);
        console.log("REGISTERED| " + tournament.registered + " |");
        var line = " <tr>" +
                "<td class=\"partyCell\"><p>" + tournament.party.length + "</p></td>" +
                "<td class=\"startingCell\"><p>" + tournament.startingDate + "</p></td>" +
                "<td class=\"bidCell\"><p>" + tournament.bid + "</p></td>" +
                "<td class=\"joinCell\">";
        if(tournament.registered === true){
            line += "<img class='checkedTableImg' src='images/checked.png'></td></tr>";
        }else{
            line += "<button class='joinButton btn' onclick='onJoinClick()'>Join</button></td></tr>";
        }
        tbody.append(line);
    });
    $('#tabTournamentsSpinner').hide();
    $('#tabTournamentsBody').show();
}

$('#homeButtonHeader').on('click', function (e) {
    goHome(e);
});

$('#homeLinkHeader').on('click', function (e) {
    console.log("go home");
    goHome(e);
});

function goHome(e){
    e.preventDefault();
    $('.landingPage').css('display','block');
    $('.tournamentsPage').css('display', 'none');
};