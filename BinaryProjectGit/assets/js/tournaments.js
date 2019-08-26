/*
*
*
* TOURNAMENTS ARRAY FEEDING
*
*
 */

var tournamentsGlobal = null;

var countdowns = [];

function feedTournamentsTab(response){
    var tbody = $('#tabTournamentsBody');
    tbody.empty();
    var tournaments = JSON.parse(response);
    tournamentsGlobal = response;
    countdowns = [];
    tournaments.forEach(function(key, index){
        var tournament = tournaments[index];
        var endingDate = getDateFromStringAt(tournament.endingDate);
        var now = new Date(Date.now());
        var dateText = tournament.endingDate;
        if(endingDate.getDate() === now.getDate() && endingDate.getMonth() === now.getMonth() && endingDate.getFullYear() === now.getFullYear()){
            var dateArray = dateText.split(" at ");
            dateText = "Today at " + dateArray[1];
        }else if(endingDate.getDate() === now.getDate() + 1 && endingDate.getMonth() === now.getMonth() && endingDate.getFullYear() === now.getFullYear()){
            var dateArray = dateText.split(" at ");
            dateText = "Tomorrow at " + dateArray[1];
        }

        var playingPartyText = '<p class="partyP">' + tournament.party.length + '/' + tournament.minPlayers + '</p></td>';

        if(tournament.registered){
            playingPartyText = '<p class="registeredParty partyPRegistered">' + tournament.party.length + '/' + tournament.minPlayers + '</p><p class="registeredTextList">registered</p></td>';
        }

        var line = "<tr id='tournamentIndex-" + index + "' class='rowTournamentPanel'>" +
            "<td class=\"countdownCell\"><p class='countdownP'>" + getTimeToShow(tournament.startingDate) + "</p></td>" +
            "<td class=\"endingDateCell\"><p class='endingDateP'>" + dateText + "</p></td>" +
            "<td class=\"partyCell\">" + playingPartyText +
            "<td class=\"entryBidCell\"><p class='entryBidP'>" + tournament.bid + "</p></td></tr>";

        tbody.append(line);

        if(tournament.registered === true){
            var lines = $('.rowTournamentPanel');
            line = lines[index];
            line.style.backgroundColor = "#118258";
        }

        var countdown = setInterval(function(){
            startCountdown(index, tournament.startingDate);
        }, 1000);
        countdowns.push(countdown);



    });
    $('#tabTournamentsSpinner').hide();
    $('#tabTournamentsBody').show();
}

function startCountdown(index, startingDate){
    var timeToShow = getTimeToShow(startingDate);
    var lines = $('.rowTournamentPanel');
    var line = lines[index];
    line.firstChild.firstChild.textContent = timeToShow;
}

function getTimeToShow(startingDate){
    var countdownDate = getDateFromStringAt(startingDate);
    var now = Date.now();
    var distance = countdownDate.getTime() - now;
    var timeToShow;
    if (distance > 0){
        // Time calculations for days, hours, minutes and seconds
        var days = Math.floor(distance / (1000 * 60 * 60 * 24));
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);
        if(days > 0){
            if(hours === 0){
                hours = "00";
            }else if(hours.toString().length === 1){
                hours = "0" + hours;
            }
            if(minutes === 0){
                minutes = "00";
            }else if(minutes.toString().length === 1){
                minutes = "0" + minutes;
            }
            if(seconds === 0){
                seconds = "00";
            }else if(seconds.toString().length === 1){
                seconds = "0" + seconds;
            }
            timeToShow = days + ":" + hours + ":" + minutes + ":" + seconds;
        }else if(hours > 0){
            if(minutes === 0){
                minutes = "00";
            }else if(minutes.toString().length === 1){
                minutes = "0" + minutes;
            }
            if(seconds === 0){
                seconds = "00";
            }else if(seconds.toString().length === 1){
                seconds = "0" + seconds;
            }
            timeToShow = hours + ":" + minutes + ":" + seconds;
        }else{
            if(seconds === 0){
                seconds = "00";
            }else if(seconds.toString().length === 1){
                seconds = "0" + seconds;
            }
            timeToShow = minutes + ":" + seconds;
        }
    }else{
        timeToShow =  "0:00";
    }
    return timeToShow;
}

$(document).on('click','.rowTournamentPanel',function(e){
    if(e != null){
        e.preventDefault();
    }
    var indexArray = $(this).attr('id').split("-");
    var index = indexArray[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    console.log(tournament);
    if(tournament['registered'] && tournamentHasStarted(tournament['startingDate'])){
        goTrade(tournament);
    }else{
        console.log("RULES");
    }
});

function tournamentHasStarted(dateString){
    var dateTime = getDateFromStringAt(dateString);
    return dateTime.getTime() < Date.now();
}

function getDateFromStringAt(dateAt){
    var dateArray = dateAt.split(" at ");
    var date = dateArray[0].split("/");
    var day = date[0];
    var month = date[1];
    var year = date[2];
    var time = dateArray[1];
    return new Date(year + "-" + month + "-" + day + " " +  time);
}

function goTrade(tournament){
    $('.tournamentsListPanel').css('display', 'none');
    $('.rulesTournamentsActionPanel').css('display', 'none');
    $('.rankTournamentsActionPanel').css('display', 'none');
    $('.tournamentsActionPanel').css('display', 'block');
    $('.tradeTournamentsActionPanel').css('display', 'block');
    $('#colPrizeHeader').css('background-color', 'rgba(20,24,36, 0.85)');
    $('#colRankHeader').css('background-color', 'rgba(20,24,36, 0.85)');
    $('#colTradeHeader').css('background-color', 'rgba(20,24,36, 1)');
}

function goRules(tournament){

}

function goRank(){

}

/*
*
*
*  TOURNAMENT REGISTRATION AND CONFIRM BOX
*
*
 */

$('#btnCancelPopUp').on('click', function () {
    $('.confirmDiv').css('display', 'none');
    $('.confirmDiv').removeAttr('id');
});

$('#btnConfirmPopUp').on('click', function () {
    $('.confirmDiv').css('display', 'none');
    var tournamentId = $('.confirmDiv').attr('id');
    $('.confirmDiv').removeAttr('id');
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            'action': "registerTournament",
            'tournamentId' : tournamentId
        },
        success: function (response) {
            registerTournamentSuccessful(response);
        },
        error: function(e){
            registerTournamentError(e);
        }

    });
});


$(document).on('click', '.joinButton', function (event) {
    event.stopPropagation();
    event.stopImmediatePropagation();
    var tournamentId = $(this).attr('id');
    $('.confirmDiv').attr('id', tournamentId);
    $('.confirmDiv').css('display', 'block');
});

function registerTournamentSuccessful(reponse){
    console.log("SUCCES");
    goTournaments(null);
}

function registerTournamentError(e){
    if(e.responseText === "Unauthorized"){
        goLogin(null);
    }
}

/*
*
*
* TOURNAMENTS GO PLAY
*
*
 */
$(document).on('click', '.playButton', function (e) {
    e.stopPropagation();
    e.stopImmediatePropagation();
    var tournamentId = $(this).attr('id');
    console.log("ON VA PLAY LE TOURNOI: " + tournamentId);
    goPlay();
});

function goPlay(){
    //ON L'APPELLERA EN CAS DE SUCCES DE L'APPEL AJAX
    $('.landingPage').css('display','none');
    $('.playingPage').css('display','block');
    $('.tournamentsPage').css('display', 'none');
    $('.login-page').css('display', 'none');
    topFunction();
}