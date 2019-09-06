/*
*
*
* TOURNAMENTS ARRAY FEEDING
*
*
 */

window.onresize = function (event) {
    centerMoves();
};


var tournamentsGlobal = null;

var countdowns = [];

var cleaveTime = new Cleave('#tradeDurationInput', {
    time: true,
    timePattern: ['h', 'm']
});

var cleaveAmount = new Cleave('#tradeAmountInput', {
    numeral: true,
    numeralThousandsGroupStyle: 'thousand'
});

function centerMoves() {
    var title = $('.cardTitleMovesTrade');
    var totalHeight = $('.cardMovesTrade').height();
    var myHeight = title.height();
    var margin = (totalHeight - myHeight) / 2;
    title.css('margin-top', margin);
    title.css('margin-bottom', margin);
}

function feedTournamentsTab(response) {
    var tbody = $('#tabTournamentsBody');
    tbody.empty();
    var tournaments = JSON.parse(response);
    tournamentsGlobal = response;
    countdowns = [];
    tournaments.forEach(function (key, index) {
        var tournament = tournaments[index];
        var endingDate = getDateFromStringAt(tournament.endingDate);
        var now = new Date(Date.now());
        var dateText = tournament.endingDate;
        if (endingDate.getDate() === now.getDate() && endingDate.getMonth() === now.getMonth() && endingDate.getFullYear() === now.getFullYear()) {
            var dateArray = dateText.split(" at ");
            dateText = "Today at " + dateArray[1];
        } else if (endingDate.getDate() === now.getDate() + 1 && endingDate.getMonth() === now.getMonth() && endingDate.getFullYear() === now.getFullYear()) {
            var dateArray = dateText.split(" at ");
            dateText = "Tomorrow at " + dateArray[1];
        }

        var playingPartyText = '<p class="partyP">' + tournament.party.length + '/' + tournament.minPlayers + '</p></td>';

        if (tournament.registered) {
            playingPartyText = '<p class="registeredParty partyPRegistered">' + tournament.party.length + '/' + tournament.minPlayers + '</p><p class="registeredTextList">registered</p></td>';
        }

        var line = "<tr id='tournamentIndex-" + index + "' class='rowTournamentPanel'>" +
            "<td class=\"countdownCell\"><p class='countdownP'>" + getTimeToShow(tournament.startingDate) + "</p></td>" +
            "<td class=\"endingDateCell\"><p class='endingDateP'>" + dateText + "</p></td>" +
            "<td class=\"partyCell\">" + playingPartyText +
            "<td class=\"entryBidCell\"><p class='entryBidP'>" + tournament.bid + "</p></td></tr>";

        tbody.append(line);

        if (tournament.registered === true) {
            var lines = $('.rowTournamentPanel');
            line = lines[index];
            line.style.backgroundColor = "#118258";
        }

        var countdown = setInterval(function () {
            startCountdown(index, tournament.startingDate);
        }, 1000);
        countdowns.push(countdown);


    });
    $('#tabTournamentsSpinner').hide();
    $('#tabTournamentsBody').show();
}

function startCountdown(index, startingDate) {
    var timeToShow = getTimeToShow(startingDate);
    var lines = $('.rowTournamentPanel');
    var line = lines[index];
    line.firstChild.firstChild.textContent = timeToShow;
}

function getTimeToShow(startingDate) {
    var countdownDate = getDateFromStringAt(startingDate);
    var now = Date.now();
    var distance = countdownDate.getTime() - now;
    var timeToShow;
    if (distance > 0) {
        // Time calculations for days, hours, minutes and seconds
        var days = Math.floor(distance / (1000 * 60 * 60 * 24));
        var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((distance % (1000 * 60)) / 1000);
        if (days > 0) {
            if (hours === 0) {
                hours = "00";
            } else if (hours.toString().length === 1) {
                hours = "0" + hours;
            }
            if (minutes === 0) {
                minutes = "00";
            } else if (minutes.toString().length === 1) {
                minutes = "0" + minutes;
            }
            if (seconds === 0) {
                seconds = "00";
            } else if (seconds.toString().length === 1) {
                seconds = "0" + seconds;
            }
            timeToShow = days + ":" + hours + ":" + minutes + ":" + seconds;
        } else if (hours > 0) {
            if (minutes === 0) {
                minutes = "00";
            } else if (minutes.toString().length === 1) {
                minutes = "0" + minutes;
            }
            if (seconds === 0) {
                seconds = "00";
            } else if (seconds.toString().length === 1) {
                seconds = "0" + seconds;
            }
            timeToShow = hours + ":" + minutes + ":" + seconds;
        } else {
            if (seconds === 0) {
                seconds = "00";
            } else if (seconds.toString().length === 1) {
                seconds = "0" + seconds;
            }
            timeToShow = minutes + ":" + seconds;
        }
    } else {
        timeToShow = "0:00";
    }
    return timeToShow;
}

$(document).on('click', '.rowTournamentPanel', function (e) {
    if (e != null) {
        e.preventDefault();
    }
    var indexArray = $(this).attr('id').split("-");
    var index = indexArray[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    $('.tournamentsActionPanel').attr('id', 'selectedTournamentIndex-' + index);
    if (tournament['registered'] && tournamentHasStarted(tournament['startingDate'])) {
        goTrade(index);
    } else {
        goRules(null);
    }
});

function tournamentHasStarted(dateString) {
    var dateTime = getDateFromStringAt(dateString);
    return dateTime.getTime() < Date.now();
}

function getDateFromStringAt(dateAt) {
    var dateArray = dateAt.split(" at ");
    var date = dateArray[0].split("/");
    var day = date[0];
    var month = date[1];
    var year = date[2];
    var time = dateArray[1];
    return new Date(year + "-" + month + "-" + day + " " + time);
}

$(document).on('click', '.btnTournamentsHeader', function (e) {
    e.preventDefault();
    switch ($(this).attr('id')) {
        case "colTradeHeader":
            goTrade(null);
            break;
        case "colRankHeader":
            goRank();
            break;
        case "colPrizeHeader":
            goRules(null);
            break;
    }
});

function goTrade(tournament) {
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];

    //TODO Faudra bien recuperer la balance de l'utilisateur a un moment
    $('.tournamentsListPanel').css('display', 'none');
    $('.rulesTournamentsActionPanel').css('display', 'none');
    $('.rankTournamentsActionPanel').css('display', 'none');
    $('.tournamentsActionPanel').css('display', 'block');
    $('.tradeTournamentsActionPanel').css('display', 'block');
    centerMoves();
    $('#colPrizeHeader').css('background-color', 'rgba(20,24,36, 0.95)');
    $('#colRankHeader').css('background-color', 'rgba(20,24,36, 0.95)');
    $('#colTradeHeader').css('background-color', 'rgba(20,24,36, 1)');
}

$('.colTimeButtonPlus').on('click', function (e) {
    e.preventDefault();
    var timeToShow = plusTimeInput($('#tradeDurationInput').val());
    changeTimeInput(timeToShow);
});

$('.colTimeButtonLess').on('click', function (e) {
    e.preventDefault();
    var timeToShow = lessTimeInput($('#tradeDurationInput').val());
    changeTimeInput(timeToShow);
});

function plusTimeInput(time) {
    if (time !== "") {
        var timeArray = time.split(':');
        var hours = timeArray[0];
        var minutes = timeArray[1];
        if (hours > 0) {
            hours++;
        } else {
            if (minutes == 59) {
                hours++;
                minutes = "00";
            } else {
                minutes++;
            }
        }
        return addZeroes(hours, minutes);
    } else {
        return "15:00";
    }
}

function lessTimeInput(time) {
    if (time !== "") {
        var timeArray = time.split(':');
        var hours = timeArray[0];
        var minutes = timeArray[1];
        if (hours > 0) {
            hours--;
        } else {
            if (minutes == 0) {
                minutes = 59;
            } else {
                minutes--;
            }
        }
        return addZeroes(hours, minutes);
    } else {
        return "15:00";
    }
}

function addZeroes(hours, minutes) {
    if (hours.toString().length === 1) {
        hours = "0" + hours;
    }
    if (minutes.toString().length === 1) {
        minutes = "0" + minutes;
    }
    return hours + ":" + minutes;
}

function changeTimeInput(timeToShow) {
    $('#tradeDurationInput').val(timeToShow);
}


$('.colAmountButtonPlus').on('click', function (e) {
    e.preventDefault();
    var amountVal = $('#tradeAmountInput').val();
    var amountToShow;
    if (amountVal !== "") {
        amountToShow = plusAmountInput(amountVal);
    } else {
        amountToShow = $('#tradeAmountInput').attr('placeholder');
        amountToShow++;
    }
    changeAmountInput(amountToShow);
});

$('.colAmountButtonLess').on('click', function (e) {
    e.preventDefault();
    var amountVal = $('#tradeAmountInput').val();
    var amountToShow;
    if (amountVal !== "") {
        amountToShow = lessAmountInput(amountVal);
    } else {
        amountToShow = $('#tradeAmountInput').attr('placeholder');
        amountToShow--;
    }
    changeAmountInput(amountToShow);
});

function plusAmountInput(amount) {
    if (amount < 0) {
        return 0;
    }
    return ++amount;
}

function lessAmountInput(amount) {
    if (amount <= 0) {
        return 0;
    }
    return --amount;

}

function changeAmountInput(amountToShow) {
    $('#tradeAmountInput').val(amountToShow);
}

$("#tradeAmountInput").bind({
    keydown: function (e) {
        if (e.which == 189 || e.which == 109) {
            return false;
        }
    }
});

function goRules(tournament) {
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    if(!tournament['registered']){
        $('.containerButtonAbout').empty();
        $('.containerButtonAbout').append('<button class="btn btn-success btnAbout" id="btnJoinAbout" onclick="onJoinClick()">Join now !</button>');
    }else{
        if(tournament['state'] === 'o'){
            $('.containerButtonAbout').empty();
            $('.containerButtonAbout').append('<button class="btn btn-success btnAbout" id="btnPlayAbout" onclick="onPlayClick()">Play now !</button>');
        }else{
            $('.containerButtonAbout').empty();
            $('.containerButtonAbout').append('<img id="imgCheckedAbout" src="images/checked.png"><p id="pRegisteredAbout">You are registered in this tournament</p>');
        }
    }
    feedAbout(tournament);
    $('.tournamentsListPanel').css('display', 'none');
    $('.rulesTournamentsActionPanel').css('display', 'block');
    $('.rankTournamentsActionPanel').css('display', 'none');
    $('.tournamentsActionPanel').css('display', 'block');
    $('.tradeTournamentsActionPanel').css('display', 'none');
    $('#colPrizeHeader').css('background-color', 'rgba(20,24,36, 1)');
    $('#colRankHeader').css('background-color', 'rgba(20,24,36, 0.95)');
    $('#colTradeHeader').css('background-color', 'rgba(20,24,36, 0.95)');
}

function feedAbout(tournament){
    $('#prizePoolAbout').text("Prize pool: " + 0.8*tournament["minPlayers"]*tournament["bid"] + " (may grow larger as more people join in)");
    $('#distributionAbout').text("Distribution: " + getDistributionString(tournament["distributionString"]));
    $('#minPlayersAbout').text("Min. players required: " + tournament["minPlayers"]);
    $('#startingDateAbout').text("Starts: " + tournament["startingDate"]);
    $('#durationAbout').text("Duration: " + tournament["duration"]);
    $('#endingDate').text("Ends: " + tournament["endingDate"]);
}

function getDistributionString(distributionJson){
    var text = JSON.stringify(distributionJson["distribution"]);
    text = text.replace(/"/g, "");
    text = text.replace(/\\/g, "");
    text = text.replace("{", "");
    text = text.replace("}", "");
    text = text.replace(/,/g, "% - ");
    text = text.replace(/:/g, ": ");
    text += "%";
    return text;
}

function onPlayClick(e){
    if(e != null) {
        e.preventDefault();
    }
    goTrade(null);
}

function onJoinClick(e){
    if(e != null){
        e.preventDefault();
    }
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            'action': "registerTournament",
            'tournamentId': tournament["tournamentId"],
            'playingSum': tournament["playingSum"]
        },
        success: function (response) {
            tournament['registered'] = true;
            tournaments[index] = tournament;
            tournamentsGlobal = JSON.stringify(tournaments);
            if(tournament['state'] === 'r') {
                goRules(null);
            }else{
                goRules(null);
                goTrade(null);
            }
        },
        error: function (e) {
            goRules(null);
        }

    });
}

function goRank() {
    //TODO FETCH LE CLASSEMENT AVEC L'ID DE '.tournamentsActionPanel'
    $('.tournamentsListPanel').css('display', 'none');
    $('.rulesTournamentsActionPanel').css('display', 'none');
    $('.rankTournamentsActionPanel').css('display', 'block');
    $('.tournamentsActionPanel').css('display', 'block');
    $('.tradeTournamentsActionPanel').css('display', 'none');
    $('#colPrizeHeader').css('background-color', 'rgba(20,24,36, 0.95)');
    $('#colRankHeader').css('background-color', 'rgba(20,24,36, 1)');
    $('#colTradeHeader').css('background-color', 'rgba(20,24,36, 0.95)');
}

$('.linkAllTournaments').on('click', function (e) {
    e.preventDefault();
    goTournamentsList();
});

function goTournamentsList() {
    goTournaments(null);
    $('.tournamentsListPanel').css('display', 'block');
    $('.tournamentsActionPanel').css('display', 'none');
}

/*
*
*
*  TOURNAMENT REGISTRATION AND CONFIRM BOX
*
*
 */

// $('#btnCancelPopUp').on('click', function () {
//     $('.confirmDiv').css('display', 'none');
//     $('.confirmDiv').removeAttr('id');
// });
//
// $('#btnConfirmPopUp').on('click', function () {
//     $('.confirmDiv').css('display', 'none');
//     var tournamentId = $('.confirmDiv').attr('id');
//     $('.confirmDiv').removeAttr('id');
//     $.ajax({
//         url: '/',
//         type: 'POST',
//         data: {
//             'action': "registerTournament",
//             'tournamentId': tournamentId
//         },
//         success: function (response) {
//             registerTournamentSuccessful(response);
//         },
//         error: function (e) {
//             registerTournamentError(e);
//         }
//
//     });
// });
//
//
// $(document).on('click', '.joinButton', function (event) {
//     event.stopPropagation();
//     event.stopImmediatePropagation();
//     var tournamentId = $(this).attr('id');
//     $('.confirmDiv').attr('id', tournamentId);
//     $('.confirmDiv').css('display', 'block');
// });
//
// function registerTournamentSuccessful(reponse) {
//     console.log("SUCCES");
//     goTournaments(null);
// }
//
// function registerTournamentError(e) {
//     if (e.responseText === "Unauthorized") {
//         goLogin(null);
//     }
// }


var _widget = loadChart($('#symbolInput').val());

$.getJSON("js/symbols.json", function(tabSymbols){
    var toAppend = '<ul class="listSymbols">';
    for(var index in tabSymbols) {
        toAppend +='<li class="lineListSymbols">' + tabSymbols[index]['symbol'] +'</li>';
    }
    toAppend += "</ul>";
    $('.selectSymbol').append(toAppend);
    hideAllListSymbols();
});

function keyUpSymbolInput(){
    var filter = $('#symbolInput').val().toUpperCase();
    $('.listSymbols li').each(function(i)
    {
        var textLine = $(this).text();
        if(filter !== "" && textLine.toUpperCase().search(filter) > -1){
            $(this).css('display','block');
        }else{
            $(this).css('display','none');
        }
    });
}

function hideAllListSymbols(){
    $('.listSymbols li').each(function(i)
    {
        $(this).css('display','none');
    });
}

$(document).on('click','.lineListSymbols', function(e){
   e.preventDefault();
   var symbol = $(this).text();
   $('#symbolInput').val(symbol);
    hideAllListSymbols();
   loadChart(symbol);
});

function loadChart(symbol){
    new TradingView.widget(
        {
            "autosize": true,
            "allow_symbol_change": false,
            "symbol": symbol,
            "interval": "30",
            "timezone": "Etc/UTC",
            "theme": "Dark",
            "style": "1",
            "locale": "en",
            "toolbar_bg": "#253e52",
            "enable_publishing": false,
            "container_id": "tradingview_tournament"
        }
    );
}

$('#symbolInput').on('click', function(){
    this.select();
});

$(document).on('click', '.cardMovesTrade', function (e) {
    e.preventDefault();
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    if(!tournament['registered']){
        alert("You are not registered to this tournament. Please consult the \"About\" section and click on the \"Join\" button in order to compete in this tournament.");
    }else if(tournament["state"] !== 'o'){
        alert("The tournament has not started yet. Please consult the \"About\" section for more information.");
    }
    if(tournament['registered'] && tournament['state'] === 'o') {
        var pair = $('#symbolInput').val().split(':')[1];
        var startingDateTrade = Date.now();
        var urlPrice = 'https://financialmodelingprep.com/api/v3/forex/' + pair;
        var strikingPrice;
        $.ajax({
            url: urlPrice,
            dataType: 'json',
            async: false,
            success: function (data) {
                var bid = data['bid'];
                var ask = data['ask'];
                strikingPrice = (parseFloat(bid) + parseFloat(ask)) / 2;
            }
        });
        var movesId = $(this).attr('id');
        var move;
        switch (movesId) {
            case "cardMovesDown":
                move = 'd';
                break;
            case "cardMovesUp":
                move = 'u';
                break;
        }
        var time = $('#tradeDurationInput').val();
        var amount = $('#tradeAmountInput').val();
        var tournamentId = $('.tournamentsActionPanel').attr('id').split('-')[1];
        console.log("Starting_Date: " + startingDateTrade);
        console.log("Move: " + move);
        console.log("Time: " + time);
        console.log("Amount: " + amount);
        console.log("Tournament_ID: " + tournamentId);
        console.log("Pair: " + pair);
        console.log("Striking price: " + strikingPrice);
        //TODO VERIFIER LES DONNEES FRONTEND

        var jsonText = '{"tradeStartingDate":' + startingDateTrade + ',';
        jsonText += '"move":' + move + ',';
        jsonText += '"time":"' + time + '",';
        jsonText += '"amount":"' + amount + '",';
        jsonText += '"tournamentID":' + tournament['tournamentId'] + ',';
        jsonText += '"strikingPrice":' + strikingPrice + ',';
        jsonText += '"pair":' + pair + '}';

        $.ajax({
            url: "/",
            type: "POST",
            data: {
                "action": "newTrade",
                "data": jsonText
            },
            success: function (response) {

            },
            error: function (e) {

            }
        });
    }

});










