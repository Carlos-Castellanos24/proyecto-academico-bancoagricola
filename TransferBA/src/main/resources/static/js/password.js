function validarPassword() {

    var password1 = document.getElementById('clave').value;
    var password2 = document.getElementById('clave2').value;

    var desabilitar = document.getElementById('envio');

    if (password1 !== password2) {
        document.getElementById('alerta').innerHTML = "Las contraseñas deben ser iguales";
        desabilitar.disabled = true;
    } else {
        document.getElementById('alerta').innerHTML = "";
        desabilitar.disabled = false;
    }
}


