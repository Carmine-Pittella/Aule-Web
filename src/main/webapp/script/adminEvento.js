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
