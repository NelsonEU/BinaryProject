/*
*
*
* TOURNAMENTS ARRAY FEEDING
*
*
 */

function feedTournamentsAdminTab(response){
    var tbody = $('#tabAdminBody');
    tbody.empty();
    var tournaments = JSON.parse(response);
    console.log(tournaments);
    tournaments.forEach(function(key, index){
        var tournament = tournaments[index];
        var textDistribution = tournament.distributionString['distribution'];

        textDistribution = textDistribution.replace(/"/g, "");
        textDistribution = textDistribution.replace(/\\/g, "");
        textDistribution = textDistribution.replace("{", "");
        textDistribution = textDistribution.replace("}", "");
        textDistribution = textDistribution.replace(/,/g, "% - ");
        textDistribution = textDistribution.replace(/:/g, ": ");
        textDistribution += "%"
        var line = " <tr>" +
            "<td class=\"partyCell\"><p>" + tournament.party.length + "</p></td>" +
            "<td class=\"startingCell\"><p>" + tournament.startingDate + "</p></td>" +
            "<td class=\"durationCell\"><p>" + tournament.duration + "</p></td>" +
            "<td class=\"minPlayersCell\"><p>" + tournament.minPlayers + "</p></td>" +
            "<td class=\"entryBidCell\"><p>" + tournament.bid + "</p></td>" +
            "<td class=\"playingSumCell\"><p>" + tournament.playingSum + "</p></td>" +
            "<td class=\"prizeDistributionCell\"><p>" + textDistribution + "</p></td>" +
            "<td class=\"deletteCell\"><button class='deleteButtonAdmin btn btn-danger' id='" + tournament.tournamentId + "'>Delete</button></td></tr>";

        tbody.append(line);
    });
    $('#tabAdminSpinner').hide();
    $('#tabAdminBody').show();
}
/*
*
* ------------------------------------------------------------------------------------------
* -------------------------------- FEED SELECT DISTRIBUTIONS -------------------------------
* ------------------------------------------------------------------------------------------
*
 */
function getDistributionsSuccess(response) {
    console.log("RESPONSE: " + response + " LENGTH: " + response.length);
    for(var i = 0; i < response.length; i++){
        var id = response[i]['id'];
        var text = JSON.stringify(response[i]['distribution']);
        text = text.replace(/"/g, "");
        text = text.replace(/\\/g, "");
        text = text.replace("{", "");
        text = text.replace("}", "");
        text = text.replace(/,/g, "% - ");
        text = text.replace(/:/g, ": ");
        text += "%"

        $('.selectDistribution').append($('<option>', {
            value: id,
            text : text,
            id: id
        }));
    }
}

function getAllIndexes(arr, val) {
    var indexes = [], i = -1;
    while ((i = arr.indexOf(val, i+1)) != -1){
        indexes.push(i);
    }
    return indexes;
}


function getDistributionsError(e) {

}

$(function(){
    $.ajax({
        url: '/',
        type: 'POST',
        dataType: 'json',
        data: {
            'action': "getDistributions"
        },
        success: function (response) {
            getDistributionsSuccess(response);
        },
        error: function(e){
            getDistributionsError(e);
        }
    });
});

/*
*
* ------------------------------------------------------------------------------------------
* ------------------------------------ DELETE TOURNAMENT -----------------------------------
* ------------------------------------------------------------------------------------------
*
 */

function deleteTournamentSuccess(response) {
    console.log("SUCCESS DELETE");
    goAdmin(null);
}

function deleteTournamentError(e) {
    console.log("ERROR DELETE");
    goAdmin(null);
}

$(document).on('click', '.deleteButtonAdmin', function (event) {
    event.stopPropagation();
    event.stopImmediatePropagation();
    var tournamentId = $(this).attr('id');
    console.log("DELETE: " + tournamentId);
    $.ajax({
        url:'/',
        type:'POST',
        data:{
            'action':"deleteTournament",
            'id':tournamentId
        },
        success: function(response){
            deleteTournamentSuccess(response);
        },
        error: function(e){
            deleteTournamentError(e);
        }
    })
});

/*
*
* ------------------------------------------------------------------------------------------
* --------------------------------- CREATE A NEW TOURNAMENT --------------------------------
* ------------------------------------------------------------------------------------------
*
 */

function addTournamentSuccess(response) {
    console.log("ADD TOURNAMENT SUCCESS");
    goAdmin(null);
}

function addTournamentError(e) {
    console.log("ADD TOURNAMENT ERROR");
}

$('#btnCreateTournament').on('click', function(e){
    e.preventDefault();
    var bid = $('#bidInput').val();
    var playingSum = $('#playingSumInput').val();
    var startingDate = $('#startingDateInput').val();
    var duration = $('#durationInput').val();
    var minPlayers = $('#minPlayersInput').val();
    var prize = $('#selectDistribution').val();

    if(bid == null || playingSum == null || startingDate == null || duration == null || minPlayers == null || prize == null){

    }else {

        var jsonText = '{"bid":' + bid + ',';
        jsonText += '"playingSum":' + playingSum + ',';
        jsonText += '"startingDate":"' + startingDate + '",';
        jsonText += '"duration":"' + duration + '",';
        jsonText += '"minPlayers":' + minPlayers + ',';
        jsonText += '"distribution":' + prize + '}';

        $.ajax({
            url: '/',
            type: 'POST',
            data: {
                'action': "addTournament",
                'tournament': jsonText
            },
            success: function (response) {
                addTournamentSuccess(response);
            },
            error: function (e) {
                addTournamentError(e);
            }
        });
    }
});


/*
*
* ------------------------------------------------------------------------------------------
* --------------------------------- FIX NEW TOURNAMENT -------------------------------------
* ------------------------------------------------------------------------------------------
*
 */

$(function (){
    decaleTab();
});

function decaleTab(){
    var width = $('.adminNewCol').outerWidth();
    $('.adminTableCol').css('margin-left', width);
}