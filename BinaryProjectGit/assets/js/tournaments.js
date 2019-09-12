/*
*
*
* TOURNAMENTS ARRAY FEEDING
*
*
 */


window.onresize = function (event) {
    centerMoves();
    fixTournamentsListHeight();
    if($('#colPrizeHeader').css('background-color') ===  'rgb(20, 24, 36)') {
        fixRulesHeight();
    }else if($('#colTradeHeader').css('background-color') ===  'rgb(20, 24, 36)') {
        fixTournamentsActionPanelHeight();
        fixContainerTableOpenTrades();
        fixContainerTableHistoryTrades();
    }else if($('#colRankHeader').css('background-color') ===  'rgb(20, 24, 36)'){
        fixRanking();
    }
    // fixContainerTableOpenTrades();
    // fixRankingHeight();
};

$(function(){
    hideOrders();
});



var tournamentsGlobal = null;

var countdowns = [];

var cleaveTime = new Cleave('#tradeDurationInput', {
    time: true,
    timePattern: ['h', 'm']
});

// var cleaveAmount = new Cleave('#tradeAmountInput', {
//     numeral: true,
//     numeralThousandsGroupStyle: 'thousand'
// });

// $(function(){
//     fixTournamentsListHeight();
// });

function fixContainerTableOpenTrades(){



    var headerHeight = $('.listTabsTrade').height();
    var tabHeight = $('.tabTournamentOpenTrades').height();
    var graphHeight = $('.colGraphDiv').outerHeight();
    var actionPanel = $('.tournamentsActionPanel').outerHeight();

    var height;
    if(headerHeight + tabHeight < graphHeight - actionPanel - 20){
        height = headerHeight + tabHeight + 5;
    }else{
        height = graphHeight - actionPanel - 20;
    }
    if(height <= 0){
        height = 250;
    }
    var newTabHeight = height - headerHeight - 4;
    $('#divTabOpen').css('height', newTabHeight + 'px');
    $('.containerTableTrades').css('height', height + 'px');
}

function fixContainerTableHistoryTrades(){

    var headerHeight = $('.listTabsTrade').height();
    var tabHeight = $('.tabTournamentHistoryTrades').height();
    var graphHeight = $('.colGraphDiv').outerHeight();
    var actionPanel = $('.tournamentsActionPanel').outerHeight();

    var height;
    if(headerHeight + tabHeight < graphHeight - actionPanel - 20){
        console.log("PLUS PETIT");
        height = headerHeight + tabHeight + 5;
    }else{
        height = graphHeight - actionPanel - 20;
        console.log("PLUS GRAND");
    }
    if(height <= 0){
        height = 250;
    }
    var newTabHeight = height - headerHeight - 4;
    $('#divTabHistory').css('height', newTabHeight + 'px');
    $('.containerTableTrades').css('height', height + 'px');
}

function fixDisplayOpenNothingToShowHeight(){
    // $('#divTabOpen').css('padding-top', '10px');
    // $('.notifNothing').css('margin-top', '2px');
    //
    // var headerHeight = $('.listTabsTrade').height();
    // var tabHeight = $('#divTabOpen').height();
    //
    // var height = headerHeight + tabHeight + 4;

    var height = $('.notifNothing').height() + $('.listTabsTrade').height() + 44;
    if(height === 0) height = 100;
    $('.divTabs').css('height', $('.notifNothing').height());
    $('#divTabOpen').css('height', $('.notifNothing').height());
    $('.containerTableTrades').css('height', height + 'px');
}

function fixDisplayHistoryNothingToShowHeight(){
    // $('#divTabHistory').css('padding-top', '10px');
    // $('.notifNothing').css('margin-top', '2px');
    //
    // var headerHeight = $('.listTabsTrade').height();
    // var tabHeight = $('#divTabHistory').height();
    //
    // var height = headerHeight + tabHeight + 4;

    var height = $('.notifNothing').height() + $('.listTabsTrade').height() + 44;

    console.log("ON PASSE ICI");
    console.log("H: " + height);
    if(height === 0) height = 100;
    $('.divTabs').css('height', $('.notifNothing').height());
    $('#divTabHistory').css('height', $('.notifNothing').height());
    $('.containerTableTrades').css('height', height + 'px');
}

function fixTournamentsActionPanelHeight(){
    var containerTradeHeight = $('.tradeTournamentsActionPanel').height();
    var containerSubHeaderHeight = $('.containerSubHeader').height();
    var headerHeight = $('.headerTournamentsActionPanel').height();

    var height = containerSubHeaderHeight + containerTradeHeight + headerHeight;

    $('.tournamentsActionPanel').css('height',height+'px');

}

function fixRulesHeight() {
    var headerHeight = $('.headerTournamentsActionPanel').outerHeight();
    var linkHeight = $('.linkAllTournaments').outerHeight();
    var rulesHeight = $('.rulesTournamentsActionPanel').outerHeight();
    var allHeight = headerHeight + linkHeight + rulesHeight + 18;

    var graphHeight = $('.colGraphDiv').outerHeight();

    console.log(graphHeight);

    if(allHeight < graphHeight){
        allHeight = graphHeight;
    }
    $('.tournamentsActionPanel').css('height', allHeight + 'px');
}

function fixTournamentsListHeight() {


    var rowGraphHeight = $('.rowGraph').height();
    var inputSymbolHeight = $('.selectSymbol').height();

    var graphHeightTo = rowGraphHeight + inputSymbolHeight + 12;

    // $('.colGraphDiv').css('height', graphHeightTo + 'px');

    var graphHeight = $('.colGraphDiv').height();
    var tableHeight = $('.table-tournaments').height();

    console.log("TABLE HEIGHT: " + tableHeight);

    var height;
    if(tableHeight < graphHeight) {
        height = tableHeight + 52;
        $('.divTableTournaments').css('height', tableHeight + 'px');
    }else{
        height = graphHeight + 18;
        var tHeight = graphHeight - 34;
        $('.divTableTournaments').css('height', tHeight + 'px');
    }
    height += 2;
    $('.tournamentsListPanel').css('height', height + 'px');
}

function fixRanking(){
    var rowGraphHeight = $('.rowGraph').height();
    var inputSymbolHeight = $('.selectSymbol').height();

    // var graphHeightTo = rowGraphHeight + inputSymbolHeight + 12;

    // $('.colGraphDiv').css('height', graphHeightTo + 'px');

    var graphHeight = $('.colGraphDiv').height();
    var tableHeight = $('.table-ranking').height();

    var headerHeight = $('.headerTournamentsActionPanel').outerHeight();
    var linkHeight = $('.linkAllTournaments').outerHeight();

    var height = headerHeight + linkHeight + tableHeight;
    if(tableHeight + headerHeight + linkHeight < graphHeight) {
        // height = tableHeight + 52;
        $('.divTableRanking').css('height', tableHeight + 'px');
        height += 12;
    }else{
        console.log("ON EST LAAAAAA");
        height = graphHeight;
        var tHeight = graphHeight - headerHeight - linkHeight + 8;
        $('.divTableRanking').css('height', tHeight + 'px');
        height += 20;
    }
    $('.tournamentsActionPanel').css('height', height + 'px');
}

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

        var line = '<tr id="tournamentIndex-' + index + '" class="rowTournamentPanel">' +
            '<td class="countdownCell"><p class="countdownP">' + getTimeToShow(tournament.startingDate) + '</p></td>' +
            '<td class="endingDateCell"><p class="endingDateP">' + dateText + '</p></td>' +
            '<td class="partyCell">' + playingPartyText + '</p></td>' +
            '<td class="entryBidCell"><p class="entryBidP">' + tournament.bid + '</p></td></tr>';

        tbody.append(line);

        if (tournament.registered === true) {
            var lines = $('.rowTournamentPanel');
            line = lines[index];
            line.style.backgroundColor = "#118258";
        }

        // var startingDate = getDateFromStringAt(tournament.startingDate);
        // if (startingDate > new Date(Date.now())) {
        //     var countdown = setInterval(function () {
        //         startCountdown(index, tournament.startingDate);
        //     }, 1000);
        //     countdowns.push(countdown);
        // }else{
        //     // $('.countdownP').css('font-size', '10px');
        // }


    });
    $('#tabTournamentsSpinner').hide();
    $('#tabTournamentsBody').show();
    fixTournamentsListHeight();
}

function getTimeToShow(startingDateString) {
    var startingDate = getDateFromStringAt(startingDateString);
    if (startingDate > new Date(Date.now())) {
        var now = new Date(Date.now());
        var dateText = startingDateString;
        if (startingDate.getDate() === now.getDate() && startingDate.getMonth() === now.getMonth() && startingDate.getFullYear() === now.getFullYear()) {
            var dateArray = dateText.split(" at ");
            dateText = "Today at " + dateArray[1];
        } else if (startingDate.getDate() === now.getDate() + 1 && startingDate.getMonth() === now.getMonth() && startingDate.getFullYear() === now.getFullYear()) {
            var dateArray = dateText.split(" at ");
            dateText = "Tomorrow at " + dateArray[1];
        }
        return dateText;
    }else{
        return "Already started";
    }
}
//
// function startCountdown(index, startingDate) {
//
//     var timeToShow = getTimeToShow(startingDate);
//     var lines = $('.rowTournamentPanel');
//     var line = lines[index];
//     line.firstChild.firstChild.textContent = timeToShow;
//     if(timeToShow === "0:00"){
//         var tournaments = JSON.parse(tournamentsGlobal);
//         tournaments[index]['state'] = 'o';
//         tournamentsGlobal = JSON.stringify(tournaments);
//     }
// }

// function getTimeToShow(startingDate, registered) {
//     var countdownDate = getDateFromStringAt(startingDate);
//     var now = Date.now();
//     var distance = countdownDate.getTime() - now;
//     var timeToShow = "";
//     if (distance > 0) {
//         // Time calculations for days, hours, minutes and seconds
//         var days = Math.floor(distance / (1000 * 60 * 60 * 24));
//         var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
//         var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
//         var seconds = Math.floor((distance % (1000 * 60)) / 1000);
//         if (days > 0) {
//             if (hours === 0) {
//                 hours = "00";
//             } else if (hours.toString().length === 1) {
//                 hours = "0" + hours;
//             }
//             if (minutes === 0) {
//                 minutes = "00";
//             } else if (minutes.toString().length === 1) {
//                 minutes = "0" + minutes;
//             }
//             if (seconds === 0) {
//                 seconds = "00";
//             } else if (seconds.toString().length === 1) {
//                 seconds = "0" + seconds;
//             }
//             timeToShow = days + ":" + hours + ":" + minutes + ":" + seconds;
//         } else if (hours > 0) {
//             if (minutes === 0) {
//                 minutes = "00";
//             } else if (minutes.toString().length === 1) {
//                 minutes = "0" + minutes;
//             }
//             if (seconds === 0) {
//                 seconds = "00";
//             } else if (seconds.toString().length === 1) {
//                 seconds = "0" + seconds;
//             }
//             timeToShow = hours + ":" + minutes + ":" + seconds;
//         } else {
//             if (seconds === 0) {
//                 seconds = "00";
//             } else if (seconds.toString().length === 1) {
//                 seconds = "0" + seconds;
//             }
//             timeToShow = minutes + ":" + seconds;
//         }
//     } else {
//         timeToShow = "0:00";
//     }
//     return timeToShow;
// }

$(document).on('click', '.rowTournamentPanel', function (e) {
    if (e != null) {
        e.preventDefault();
    }
    var indexArray = $(this).attr('id').split("-");
    var index = indexArray[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    $('.tournamentsActionPanel').attr('id', 'selectedTournamentIndex-' + index);
    if (tournament['registered'] === true && tournamentHasStarted(tournament['startingDate'])) {
        goTrade(index);
    } else {
        goRules(null);
    }
});

function tournamentHasStarted(dateString) {
    var dateTime = getDateFromStringAt(dateString);
    return dateTime.getTime() <= Date.now();
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

$('#tabOpenTrades').on('click', function(e){
    e.preventDefault();
    resetBackgroundTabs();
    feedOpenTab();
    $('#divTabOpen').css('display', 'block');
    $('#divTabHistory').css('display', 'none');
    $('#tabOpenTrades').css('background-color', 'rgba(20,24,36,1)');
});

$('#tabHistoryTrades').on('click', function(e){
    e.preventDefault();
    resetBackgroundTabs();
    feedHistoryTab();
    $('#divTabOpen').css('display', 'none');
    $('#divTabHistory').css('display', 'block');
    $('#tabHistoryTrades').css('background-color', 'rgba(20,24,36,1)');
});

function resetBackgroundTabs(){
    $('#tabHistoryTrades').css('background-color', 'rgba(20, 24, 36,0.96)');
    $('#tabOpenTrades').css('background-color', 'rgba(20, 24, 36,0.96)');
}

function feedOpenTab(){
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    $.ajax({
        url:'/',
        type: 'POST',
        data: {
           'action':'getTournamentOpenTrades',
           'tournamentId':tournament['tournamentId']
        },
        success: function(response){
            feedOpenTabHtml(response);
        },
        error: function(e){
            displayOpenNothingToShow();
            // console.log("ERROR FEED OPEN");
            // hideOrders();
        }
    });
}

function displayOpenNothingToShow(){
    $('.containerTableTrades').css('background-color', '#26293b');
    $('#divTabOpen').css('background-color','rgb(20, 24, 36)');
    $('#divTabOpen').empty();
    $('#divTabOpen').append('<p class="notifNothing">Nothing to show</p>');
    fixDisplayOpenNothingToShowHeight();
}

function displayHistoryNothingToShow(){
    $('.containerTableTrades').css('background-color', '#26293b');
    $('#divTabHistory').css('background-color','rgb(20, 24, 36)');
    $('#divTabHistory').empty();
    $('#divTabHistory').append('<p class="notifNothing">Nothing to show</p>');
    fixDisplayHistoryNothingToShowHeight();
}

function getStringDate(sD){
    var date = sD.dayOfMonth + "/" + sD.monthValue + "/" + sD.year;
    date += " " + sD.hour + ":" + sD.minute;
    return date;
}

function feedOpenTabHtml(response){
    var trades = JSON.parse(response);
    if(trades.length > 0) {
        $('#divTabOpen').empty();
        $('#divTabOpen').addClass("table table-responsive");
        $('#divTabOpen').append('<table class="tabTournamentOpenTrades table-striped table-dark table-hover"></table>');
        $('.tabTournamentOpenTrades').append('<thead class="tHeadTournamentOpenTrades">' +
            + '<tr>'
            + '<th class="pairCell">Asset</th>'
            + '<th class="startingCell">Date</th>'
            + '<th class="endingCell">Expiration</th>'
            + '<th class="amountCell">Amount</th>'
            + '<th class="strikeCell">Strike price</th>'
            + '</tr>'
            + '</thead><tbody class="tBodyTournamentOpenTrades"></tbody>');

        trades.forEach(function (key, index) {
            var trade = trades[index];
            var line = '<tr>';
            if(trade.move === 'u'){
                line += '<td><img class="chevronTabTrades" src="images/chevronUp.png"> ' + trade.pair + '</td>';
            }else{
                line += '<td><img class="chevronTabTrades" src="images/chevronDown.png"> ' + trade.pair + '</td>';
            }
            line += '<td>' + getStringDate(trade.startingDate) + '</td>';
            line += '<td>' + getStringDate(trade.endingDate) + '</td>';
            line += '<td>' + trade.amount + ' $</td>';
            line += '<td>' + parseFloat(trade.strikingPrice.toFixed(8)) + '</td>';
            line += '</tr>';
            $('.tBodyTournamentOpenTrades').append(line);
            $('#divTabOpen').css('background-color', '#313452');
        });
        fixContainerTableOpenTrades();
    }else{
        $('#divTabOpen').removeClass("table table-responsive");
        displayOpenNothingToShow();

    }
}

function feedHistoryTab(){
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    $.ajax({
        url:'/',
        type: 'POST',
        data: {
            'action':'getTournamentHistoryTrades',
            'tournamentId':tournament['tournamentId']
        },
        success: function(response){
            feedHistoryTabHtml(response);
        },
        error: function(e){
            displayHistoryNothingToShow();
            // hideOrders();
        }
    });
}

function feedHistoryTabHtml(response){
    var trades = JSON.parse(response);
    if(trades.length > 0) {
        $('#divTabHistory').empty();
        $('#divTabHistory').addClass("table table-responsive");
        $('#divTabHistory').append('<table class="tabTournamentHistoryTrades table-striped table-dark table-hover"></table>');
        $('.tabTournamentHistoryTrades').empty();
        $('.tabTournamentHistoryTrades').append('<thead class="tHeadTournamentHistoryTrades">' +
            + '<tr>'
            + '<th class="resultCell">Result</th>'
            + '<th class="pairCell">Asset</th>'
            + '<th class="startingCell">Date</th>'
            + '<th class="endingCell">Expiration</th>'
            + '<th class="amountCell">Amount</th>'
            + '<th class="strikeCell">Strike price</th>'
            + '<th class="closeCell">Close price</th>'
            + '</tr>'
            + '</thead><tbody class="tBodyTournamentHistoryTrades"></tbody>');
        $('.tBodyTournamentHistoryTrades').empty();
        trades.forEach(function (key, index) {
            var trade = trades[index];
            console.log(trade);
            var line = '<tr>';
            if(trade.state === 'w'){
                line += '<td id="tdWin">W</td>';
            }else{
                line += '<td id="tdLose">L</td>';
            }
            if(trade.move === 'u'){
                line += '<td><img class="chevronTabTrades" src="images/chevronUp.png"> ' + trade.pair + '</td>';
            }else{
                line += '<td><img class="chevronTabTrades" src="images/chevronDown.png"> ' + trade.pair + '</td>';
            }
            line += '<td>' + getStringDate(trade.startingDate) + '</td>';
            line += '<td>' + getStringDate(trade.endingDate) + '</td>';
            line += '<td>' + trade.amount + ' $</td>';
            line += '<td>' + parseFloat(trade.strikingPrice.toFixed(8)) + '</td>';
            line += '<td>' + parseFloat(trade.endingPrice.toFixed(8)) + '</td>';
            line += '</tr>';
            $('.tBodyTournamentHistoryTrades').append(line);
        });
        fixContainerTableHistoryTrades();
    }else{
        $('#divTabHistory').removeClass("table table-responsive");
        displayHistoryNothingToShow();
    }
}

function goTrade(tournamenttt) {
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    var tournamentId = tournament['tournamentId'];

    $('#tabOpenTrades').css('background-color', 'rgba(20,24,36)');
    $('#tabHistoryTrades').css('background-color', '#26293b');
    // feedOpenTab();
    checkUserTournamentBalance(tournamentId);
    checkOrders(tournamentId);
    //TODO Faudra bien recuperer la balance de l'utilisateur a un moment
    $('.tournamentsListPanel').css('display', 'none');
    $('.rulesTournamentsActionPanel').css('display', 'none');
    $('.rankTournamentsActionPanel').css('display', 'none');
    $('.tournamentsActionPanel').css('display', 'block');
    $('.tradeTournamentsActionPanel').css('display', 'block');
    centerMoves();
    $('#colPrizeHeader').css('background-color', 'rgba(20, 24, 36,0.96)');
    $('#colRankHeader').css('background-color', 'rgba(20, 24, 36,0.96)');
    $('#colTradeHeader').css('background-color', 'rgba(20,24,36)');
    $('#divTabOpen').css('display', 'block');
    $('#divTabHistory').css('display', 'none');
    fixTournamentsActionPanelHeight();
}

function fixTournamentsActionPanelHeightWithAmount(){
    console.log("coucou on fait que passer");
    fixTournamentsActionPanelHeight();
    var height = $('.tournamentsActionPanel').height();
    var toAdd = $('.userTournamentBalanceP').outerHeight();
    height += toAdd;
    $('.tournamentsActionPanel').css('height',height + 'px');
}

function checkUserTournamentBalance(tournamentId) {
    $.ajax({
        url:'/',
        type:'POST',
        // async:false,
        data:{
            'action':'getUserTournamentBalance',
            'tournamentId':tournamentId
        },
        success:function(response){
            console.log("success");
            console.log(response);
            if(response != -1) {
                $(".userTournamentBalanceP").remove();
                $('.rowButtonsTrade').css('margin-top', '12px');
                $('.divPicAndAmount').after('<p class="userTournamentBalanceP">' + parseFloat(response).toFixed(2) + '$</p>');
                fixTournamentsActionPanelHeightWithAmount();
            }else{
                var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
                var tournaments = JSON.parse(tournamentsGlobal);
                var tournament = tournaments[index];
                var tournamentPlayingSum = tournament['playingSum'];
                $(".userTournamentBalanceP").remove();
                $('.rowButtonsTrade').css('margin-top', '12px');
                $('.divPicAndAmount').after('<p class="userTournamentBalanceP">' + parseFloat(tournamentPlayingSum).toFixed(2) + '$</p>');
            }
        },
        error:function(e){
            console.log("ERROR checkBALANCE");
            console.log(e.status);
            $(".userTournamentBalanceP").remove();
            $('.rowButtonsTrade').css('margin-top', '24px');
        }
    });
}

function checkOrders(){
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    $.ajax({
       url:'/',
       type:'POST',
       // async:false,
       data:{
           'action':'getTradesCount',
           'tournamentId':tournament['tournamentId']
       },
        success:function(){
           console.log("Y A des trucs");
           showOrders();
           checkOpenTrades(tournament['tournamentId']);
        },
        error:function(e){
           if(e.status === 488){
               hideOrders();
           }
        }
    });
}

function showOrders(){
    $('.containerTableTrades').css('display','block');
}

function checkOpenTrades(tournamentId){
    $.ajax({
        url:'/',
        type: 'POST',
        // async: false,
        data: {
            'action':'getTournamentOpenTrades',
            'tournamentId':tournamentId
        },
        success: function(response){
            var trades = JSON.parse(response);
            if(trades.length > 0) {
                resetBackgroundTabs();
                $('#tabOpenTrades').css('background-color', 'rgba(20,24,36)');
                $('#divTabOpen').css('display', 'block');
                $('#divTabHistory').css('display', 'none');
                feedOpenTabHtml(response);
            }else{
                displayOpenNothingToShow();
                checkHistoryTrades(tournamentId);
            }
        },
        error: function(e){
            displayOpenNothingToShow();
        }
    });
}

function checkHistoryTrades(tournamentId){
    $.ajax({
        url:'/',
        type: 'POST',
        // async: false,
        data: {
            'action':'getTournamentHistoryTrades',
            'tournamentId':tournamentId
        },
        success: function(response){
            var trades = JSON.parse(response);
            if(trades.length > 0) {
                resetBackgroundTabs();
                $('#tabHistoryTrades').css('background-color', 'rgba(20,24,36)');
                $('#divTabOpen').css('display', 'none');
                $('#divTabHistory').css('display', 'block');
                feedHistoryTabHtml(response);
            }else{
                displayHistoryNothingToShow();
            }
        },
        error: function(e){
            displayOpenNothingToShow();
        }
    });
}

function hideOrders(){
    $('.containerTableTrades').css('display','none');
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
        return 1;
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
        $('.containerButtonAbout').append('<button class="btn btnAbout" id="btnJoinAbout" onclick="onJoinClick()">Join now !</button>');
    }else{
        if(tournament['state'] === 'o'){
            $('.containerButtonAbout').empty();
            $('.containerButtonAbout').append('<button class="btn btnAbout" id="btnPlayAbout" onclick="onPlayClick()">Play now !</button>');
        }else{
            $('.containerButtonAbout').empty();
            $('.containerButtonAbout').append('<img id="imgCheckedAbout" src="images/checked.png"><p id="pRegisteredAbout">You are registered in this tournament</p>');
        }
    }
    feedAbout(tournament);
    hideOrders();
    $('.tournamentsListPanel').css('display', 'none');
    $('.rulesTournamentsActionPanel').css('display', 'block');
    $('.rankTournamentsActionPanel').css('display', 'none');
    $('.tournamentsActionPanel').css('display', 'block');
    $('.tradeTournamentsActionPanel').css('display', 'none');
    $('#colPrizeHeader').css('background-color', 'rgba(20,24,36, 1)');
    $('#colRankHeader').css('background-color', 'rgba(20, 24, 36,0.96)');
    $('#colTradeHeader').css('background-color', 'rgba(20, 24, 36,0.96)');
    fixRulesHeight();
}

function feedAbout(tournament){
    // $('#prizePoolAbout').text("Prize pool: " + 0.8*tournament["minPlayers"]*tournament["bid"] + " (may grow larger as more people join in)");
    $('#prizePoolAbout').empty();
    $('#prizePoolAbout').append('<span class="titleAbout">Prize pool:</span>  <span>  ' + 0.8*tournament["minPlayers"]*tournament["bid"] + ' Credits <span class="prizeIntel">(may grow larger as more people check in)</span></span>');
    // $('#distributionAbout').text("Distribution: " + getDistributionString(tournament["distributionString"]));

    $('#distributionAbout').empty();
    $('#distributionAbout').append('<span class="titleAbout">Distribution:</span>  <span>  ' + getDistributionString(tournament["distributionString"]) + '</span>');

    $('#minPlayersAbout').empty();
    $('#minPlayersAbout').append('<span class="titleAbout">Min. players required:</span>  <span>  ' + tournament["minPlayers"] + '</span>');

    $('#startingDateAbout').empty();
    $('#startingDateAbout').append('<span class="titleAbout">Starts:</span>  <span>  ' + tournament["startingDate"] + '</span>');

    $('#durationAbout').empty();
    $('#durationAbout').append('<span class="titleAbout">Duration:</span>  <span>  ' + tournament["duration"] + '</span>');

    $('#playingSumAbout').empty();
    $('#playingSumAbout').append('<span class="titleAbout">Playing sum:</span>  <span>  ' + tournament["playingSum"] + '$ <span class="prizeIntel">(this is the balance you are using to trade in the tournament, it does not affect your real balance)</span></span>');

    $('#endingDate').empty();
    $('#endingDate').append('<span class="titleAbout">Ends:</span>  <span>  ' + tournament["endingDate"] + '</span>');

    $('#bidAbout').empty();
    $('#bidAbout').append('<span class="titleAbout">Bid:</span>  <span>  ' + tournament["bid"] + ' Credits</span>');

    // $('#minPlayersAbout').text("Min. players required: " + tournament["minPlayers"]);
    // $('#startingDateAbout').text("Starts: " + tournament["startingDate"]);
    // $('#durationAbout').text("Duration: " + tournament["duration"]);
    // $('#playingSumAbout').text("Playing sum: " + tournament["playingSum"]);
    // $('#endingDate').text("Ends: " + tournament["endingDate"]);
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
            'playingSum': tournament["playingSum"],
            'bid': tournament["bid"]
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

            switch(e.status){
                //NOT LOGIN
                case 401:
                    goLogin(null);
                    break;
                //NOT ENOUGH FUND
                case 408:
                    showToast("Check-in error", "You don't have enough funds in your account, please fill your balance to enter new tournaments.");
                    break;
            }


        }

    });
}


function goRank() {
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];

    hideOrders();
    //TODO FETCH LE CLASSEMENT AVEC L'ID DE '.tournamentsActionPanel'
    $('.tournamentsListPanel').css('display', 'none');
    $('.rulesTournamentsActionPanel').css('display', 'none');
    $('.rankTournamentsActionPanel').css('display', 'block');
    $('.tournamentsActionPanel').css('display', 'block');
    $('.tradeTournamentsActionPanel').css('display', 'none');
    $('#colPrizeHeader').css('background-color', 'rgba(20, 24, 36,0.96)');
    $('#colRankHeader').css('background-color', 'rgba(20,24,36, 1)');
    $('#colTradeHeader').css('background-color', 'rgba(20, 24, 36,0.96)');

    topFunction();
    $('#tabRankingBody').hide();
    $('#tabRankingSpinner').show();
    $.ajax({
        url: '/',
        type: 'POST',
        data: {
            'action': "getRanking",
            'tournamentId': tournament["tournamentId"]
        },
        success: function (response) {
            feedRankingTab(response);
        },
        error: function(e){
            console.log("ERROR RANKING AJAX");
        }
    });
}

function feedRankingTab(response) {
    var tbody = $('#tabRankingBody');
    tbody.empty();
    var rankings = JSON.parse(response);
    var indice = 1;
    rankings.forEach(function (key, index) {
        var rank = rankings[index];

        var ranking = indice;
        var username = rank['username'];
        var balance = rank['balance'];

        var line = '<tr class="rowRankingPanel">' +
            '<td class="rankCell"><p class="rankP">' + ranking + '</p></td>' +
            '<td class="usernameCell"><p class="usernameP">' + username + '</p></td>' +
            '<td class="balanceCell">' + balance + '$</td></tr>';

        tbody.append(line);
        indice++;
    });
    $('#tabRankingSpinner').hide();
    $('#tabRankingBody').show();
    fixRanking();
}

$('.linkAllTournaments').on('click', function (e) {
    e.preventDefault();
    goTournamentsList();
});

function goTournamentsList() {
    goTournaments(null);
    fixTournamentsListHeight();
    hideOrders();
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
    $('.listSymbols').css('display','none');
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
            "hide_side_toolbar": false,
            "container_id": "tradingview_tournament"
        }
    );
    $('.legendChartText').text(symbol);
}

$('#symbolInput').on('click', function(){
    this.select();
    $('.listSymbols').css('display','block');
});

$(document).on('click', '.cardMovesTrade', function (e) {
    e.preventDefault();
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    if(tournament['state'] === 'o') {
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
                newTradeSuccess();
            },
            error: function (e) {
                newTradeError(e);
            }
        });
    }else{
        showToast("Too soon", "Tihs tournament hasn't started yet.");
    }

});

function newTradeError(e){
    //TODO Develop that, go check the different possible error in dispatcher and tread accordingly
    console.log("NEW TRADE ERROR :" + e.status);
    switch(e.status){
        //NOT LOGIN
        case 401:
            goLogin(null);
            break;
        //NOT ENOUGH FUND
        case 408:
            showToast("Balance error", "You don't have enough demo credits left for this tournament.");
            break;
        case 416:
            showToast("Timing error", "The trade must be at least 1 minute long and must finish before the end of the tournament.");
            break;
        case 424:
            showToast("No registration", "You are not yet registered to this tournament, please consult the About section to register.");
            break;
        case 432:
            showToast("Invalid data", "The data you've entered is invalid. Please check again.");
            break;
        //SERVER ERROR
        default:
            showToast("Oops !", "Something went wrong");
            break;
    }
}

function newTradeSuccess(){
    var index = $('.tournamentsActionPanel').attr('id').split('-')[1];
    var tournaments = JSON.parse(tournamentsGlobal);
    var tournament = tournaments[index];
    var tournamentId = tournament['tournamentId'];
    feedOpenTab();
    checkUserTournamentBalance(tournamentId);
    showToast("New trade", "Your trade has been successfully processed !");
}

function showToast(title, body){
    $('.toast-title').text(title);
    $('.toast-body').text(body);
    $('.toastTrade').css('display','block');
    $('.toastTrade').css('max-height','80px');
    setTimeout(hideToast, 5000);
}

$('.closeToast').on('click', function(e){
   e.preventDefault();
   hideToast();
});

function hideToast(){
    $('.toastTrade').css('display','none');
    $('.toastTrade').css('max-height','0');
}









