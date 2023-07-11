// Funzione per gestire il click sugli elementi della tabella
function handleTableRowClick(event) {
   var id_aula = event.target.parentNode.id; // Ottieni l'ID dell'elemento selezionato
   location.href = "settingAula?aula=" + id_aula;
}

// Aggiungi il gestore di eventi click a tutti gli elementi <tr> nella tabella
var tableRows = document.querySelectorAll("table tbody tr");
tableRows.forEach(function (row) {
   row.addEventListener("click", handleTableRowClick);
});

function eliminaAula() {
   var urlParams = new URLSearchParams(window.location.search); // Ottieni i parametri dell'URL
   var aulaValue = urlParams.get("aula"); // Ottieni il valore del parametro "aula"
   var currentPath = window.location.href; // Ottieni il percorso attuale
   var newPath = currentPath + "&delete=" + aulaValue; // Aggiungi "&delete=X" al percorso attuale
   window.location.href = newPath; // Reindirizza alla nuova pagina
}

function aggiungiAttrezzatura() {
   var urlParams = new URLSearchParams(window.location.search); // Ottieni i parametri dell'URL
   var aulaValue = urlParams.get("aula"); // Ottieni il valore del parametro "aula"
   window.location.href = "aggiungiAttrezzatura?aula=" + aulaValue; // Reindirizza alla nuova pagina
}
