$(document).ready(function() {
//Preloader
    $(window).on("load", function() {
        replaceNavbar();
        preloaderFadeOutTime = 200;
        function hidePreloader() {
            var preloader = $('.spinner-wrapper');
            preloader.fadeOut(preloaderFadeOutTime);
        }
        hidePreloader();
    });
});


function formToJson(form, inputs) {

    var tab = {};

    $(inputs, form)
        .each(
            function () {

                if (this.name === 'phone_number'
                    && this.value.length !== 0)
                    tab[this.name] = "+"
                        + $(this).intlTelInput(
                            "getSelectedCountryData").dialCode
                        + this.value;
                else
                    tab[this.name] = this.value;
            });

    return JSON.stringify(tab);
}

function checkFields(name, value) {
    if (name === 'emailRegister' || name === 'emailLogin') {
        return /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/
            .test(value);
    } else if (name === 'passwordRegister') {
        /* Passwords must contain : - At least 8
            * et max - Include at least 1 lowercase
            * letter - 1 capital letter - 1 number -
            * 1 special character => !@#$%^&*
            *
            */
        return /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,25}$/
            .test(value) || /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,25}$/
            .test(value);

    } else if (name === 'usernameRegister'){
        return /^[a-zA-Z0-9]+$/.test(value);
    }
}