// Funzione per gestire il click sugli elementi della tabella
function handleTableRowClick(event) {
   var id_evento = event.target.parentNode.id; // Ottieni l'ID dell'elemento selezionato
   location.href = "settingEvento?evento=" + id_evento;
}

// Aggiungi il gestore di eventi click a tutti gli elementi <tr> nella tabella
var tableRows = document.querySelectorAll("table tbody tr");
tableRows.forEach(function (row) {
   row.addEventListener("click", handleTableRowClick);
});

function eliminaEvento() {
   var urlParams = new URLSearchParams(window.location.search); // Ottieni i parametri dell'URL
   var eventoValue = urlParams.get("evento"); // Ottieni il valore del parametro "evento"
   var currentPath = window.location.href; // Ottieni il percorso attuale
   var newPath = currentPath + "&delete=" + eventoValue; // Aggiungi "&delete=X" al percorso attuale
   window.location.href = newPath; // Reindirizza alla nuova pagina
}
