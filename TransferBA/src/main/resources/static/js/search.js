function buscarTransaccionEntrante() {
    var tabla = document.getElementById("entrantes");
    var textoBuscar = document.getElementById("busqueda-entrante").value.toLowerCase();
    var celdasFila = "";
    var encontrado = false;
    var comparar = "";
    var mensajeError = "No se encontraron transferencias entrantes";
    
    for (var i = 1; i < tabla.rows.length; i++) {       
        celdasFila = tabla.rows[i].getElementsByTagName("td");
        encontrado = false;
        for (var j = 0; j < celdasFila.length && !encontrado; j++) {           
            comparar = celdasFila[j].innerHTML.toLowerCase();
            if (textoBuscar.length === 0 || (comparar.indexOf(textoBuscar) > -1)) {
                encontrado = true;
            }
        }
        if (encontrado) {
            tabla.rows[i].style.display = '';
            mensajeError = "";
        } else {
            tabla.rows[i].style.display = 'none';
        }
    }
    document.getElementById('mensajeErrorEntrante').innerHTML = mensajeError;
}

function buscarTransaccionSaliente() {
    var tabla = document.getElementById("salientes");
    var textoBuscar = document.getElementById("busqueda-saliente").value.toLowerCase();
    var celdasFila = "";
    var encontrado = false;
    var comparar = "";
    var mensajeError = "No se encontraron transferencias salientes";
    
    for (var i = 1; i < tabla.rows.length; i++) {       
        celdasFila = tabla.rows[i].getElementsByTagName("td");
        encontrado = false;
        for (var j = 0; j < celdasFila.length && !encontrado; j++) {           
            comparar = celdasFila[j].innerHTML.toLowerCase();
            if (textoBuscar.length === 0 || (comparar.indexOf(textoBuscar) > -1)) {
                encontrado = true;
            }
        }
        if (encontrado) {
            tabla.rows[i].style.display = '';
            mensajeError = "";
        } else {
            tabla.rows[i].style.display = 'none';
        }
    }
    document.getElementById('mensajeErrorSaliente').innerHTML = mensajeError;
}