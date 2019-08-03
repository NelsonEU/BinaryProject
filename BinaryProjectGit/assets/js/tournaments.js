function onJoinClick(){
    $('.confirmDiv').css('display','block');
}

$('#btnCancelPopUp').on('click', function () {
    $('.confirmDiv').css('display','none');
})

$('#btnConfirmPopUp').on('click', function () {
    $('.confirmDiv').css('display','none');
    window.location.href="index.php?action=tournament&&tournament=1";
})