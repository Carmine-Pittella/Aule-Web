// Funzione per gestire il click sugli elementi della tabella
function handleTableRowClick(event) {
   var id_attrezzo = event.target.parentNode.id; // Ottieni l'ID dell'elemento selezionato
   location.href = "settingAttrezzo?attrezzo=" + id_attrezzo;
}

// Aggiungi il gestore di eventi click a tutti gli elementi <tr> nella tabella
var tableRows = document.querySelectorAll("table tbody tr");
tableRows.forEach(function (row) {
   row.addEventListener("click", handleTableRowClick);
});

function eliminaAttrezzo() {
   var urlParams = new URLSearchParams(window.location.search); // Ottieni i parametri dell'URL
   var attrezzoValue = urlParams.get("attrezzo"); // Ottieni il valore del parametro "attrezzo"
   var currentPath = window.location.href; // Ottieni il percorso attuale
   var newPath = currentPath + "&delete=" + attrezzoValue; // Aggiungi "&delete=X" al percorso attuale
   window.location.href = newPath; // Reindirizza alla nuova pagina
}
