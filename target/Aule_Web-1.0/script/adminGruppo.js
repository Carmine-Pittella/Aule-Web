// Funzione per gestire il click sugli elementi della tabella
function handleTableRowClick(event) {
   var id_gruppo = event.target.parentNode.id; // Ottieni l'ID dell'elemento selezionato
   location.href = "settingGruppo?gruppo=" + id_gruppo;
}

// Aggiungi il gestore di eventi click a tutti gli elementi <tr> nella tabella
var tableRows = document.querySelectorAll("table tbody tr");
tableRows.forEach(function (row) {
   row.addEventListener("click", handleTableRowClick);
});

function eliminaGruppo() {
   var urlParams = new URLSearchParams(window.location.search); // Ottieni i parametri dell'URL
   var gruppoValue = urlParams.get("gruppo"); // Ottieni il valore del parametro "gruppo"
   var currentPath = window.location.href; // Ottieni il percorso attuale
   var newPath = currentPath + "&delete=" + gruppoValue; // Aggiungi "&delete=X" al percorso attuale
   window.location.href = newPath; // Reindirizza alla nuova pagina
}
