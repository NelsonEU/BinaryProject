
function NoUserRegistered(){
    $('.form-control').backgroundColor('red');
}

function WrongPassword(){
    $('.form-control').backgroundColor('red');
}

$('#submitLogin').on('click', function(e){
    e.preventDefault();
    $('.notifError').remove();
    var executeAjax = true;
    var email = $('#emailLogin');
    if (!checkFields(email.attr('name'), email.val())) {
        fieldError($('#emailLogin'),$('#emailLogin'));
        $('#emailLogin').after("<p class='notifError'>Email not valid</p>");
        executeAjax = false;
    }
    var password = $('#passwordLogin');
    if(executeAjax) {
        $.ajax({
            url: '/',
            type: 'POST',
            data: {
                'action': "login",
                'email': email.val(),
                'password': password.val()
            },
            success: function (response) {
                loginSuccessful(response)
            },
            error: function (jqXHR, textStatus, errorThrown) {
                loginError(jqXHR, textStatus, errorThrown)
            }
        });
    }
});

$('#submitRegister').on('click', function(e) {
    e.preventDefault();
    $('.notifError').remove();
    var email = $('#emailRegister');
    var password = $('#passwordRegister');
    var username = $('#usernameRegister');
    var executeAjax = true;
    if (!checkFields(email.attr('name'), email.val())) {
        fieldError($('#emailRegister'),$('#emailRegisterLabel'));
        $('#emailRegister').after("<p class='notifError'>Email not valid</p>");
        executeAjax = false;
    }
    if (!checkFields(username.attr('name'), username.val())) {
        fieldError($('#usernameRegister'),$('#usernameRegisterLabel'));
        $('#usernameRegister').after("<p class='notifError'>The username must contains letters and numbers only</p>");
        executeAjax = false;
    }
    if (!checkFields(password.attr('name'), password.val())) {
        fieldError($('#passwordRegister'),$('#passwordRegisterLabel'));
        fieldError($('#passwordRegisterBis'),$('#passwordRegisterBisLabel'));
        $('#passwordRegister').after("<p class='notifError'>8 characters minimum (upper/lower + number)</p>");
        executeAjax = false;
    }
    if(password.val() !== $('#passwordRegisterBis').val()){
        fieldError($('#passwordRegister'),$('#passwordRegisterLabel'));
        fieldError($('#passwordRegisterBis'),$('#passwordRegisterBisLabel'));
        $('#passwordRegister').after("<p class='notifError'>The passwords are not matching</p>");
        executeAjax = false;
    }

    if(executeAjax) {
        $.ajax({
            url: '/',
            type: 'POST',
            data: {
                'action': "register",
                'email': email.val(),
                'password': password.val(),
                'username': username.val()
            },
            success: function (response) {
                registerSuccessful(response)
            },
            error: function (jqXHR, textStatus, errorThrown) {
                registerError(jqXHR, textStatus, errorThrown)
            }
        });
    }

});

$('#registerLink').on('click', function(e) {
    $('.form-register').delay(100).fadeIn(100);
    $('.form-signin').fadeOut(100);
    $('.form-signin').css('display','none');
    $('.form-register').css('display','block');
    e.preventDefault();
});

$('#signInLink').on('click', function(e) {
    $('.form-signin').delay(100).fadeIn(100);
    $('.form-register').fadeOut(100);
    $('.form-register').css('display','none');
    $('.form-signin').css('display','block');
    e.preventDefault();
});


function loginSuccessful(response){
    var user = JSON.parse(response);
    $('.notifError').remove();
    isConnected();
    goTournaments(null);
    // window.location.href="/";
}

function loginError(jqXHR, textStatus, errorThrown){
    // var erreur = JSON.parse(e);
    console.log("ERREUR: " + jqXHR.status + " " + jqXHR.responseText + " /// " + errorThrown);
    switch(jqXHR.responseText){
        case "invalid_password":
            // $('#passwordLogin').css('background-color','rgba(207, 0, 15, 0.3)');
            fieldError($('#passwordLogin'), $('#passwordLoginLabel'));
            break;
        case "not_existing_user":
            // $('#emailLogin').css('background-color','rgba(207, 0, 15, 0.3)');
            fieldError($('#emailLogin'),$('#emailLoginLabel'));
            break;
    }
}

function fieldError(field, label){
    field.css('color', 'black');
    field.css('border', '2px solid rgba(207, 0, 15, 1)');
    field.focus(function(){
        field.css('border', '0px');
    });

    field.focusout(function(){
        field.css('border', '2px solid rgba(207, 0, 15, 1)');
    });
    label.css('color','darkred');
}

function registerSuccessful(response){
    var user = JSON.parse(response);
    $('.notifError').remove();
    isConnected();
    goTournaments(null);
}

function registerError(jqXHR, textStatus, errorThrown){
    switch(jqXHR.responseText){
        case "email_taken":
            fieldError($('#emailRegister'),$('#emailRegisterLabel'));
            $('#emailRegister').after("<p class='notifError'>This email is already taken</p>")
            break;
    }
}