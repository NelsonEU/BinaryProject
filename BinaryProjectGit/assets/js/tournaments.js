$('#btnCancelPopUp').on('click', function () {
    $('.confirmDiv').css('display', 'none');
    $('.confirmDiv').removeAttr('id');
    console.log("on a appuyer sur cancel;")
});

$('#btnConfirmPopUp').on('click', function () {
    $('.confirmDiv').css('display', 'none');
    var tournamentId = $('.confirmDiv').attr('id');
    $('.confirmDiv').removeAttr('id');
    console.log("on a confirme on va faire ajax mtn");
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
    console.log("On a change l'id et on montre la box");
    //Here I do an Ajax call with the tournament ID
    //On server side we check if the user is connected
    //If it is, we do the request
    //If not we redirect to login
});

function registerTournamentSuccessful(reponse){
    console.log("SUCCES");
    goTournaments(null);
}

function registerTournamentError(e){
    if(e.responseText === "Unauthorized"){
        window.location.href = "/login.html";
    }
}