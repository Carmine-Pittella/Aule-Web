// Funzione per gestire il click sugli elementi della tabella
function handleTableRowClick(event) {
   var id_corso = event.target.parentNode.id; // Ottieni l'ID dell'elemento selezionato
   location.href = "settingCorso?corso=" + id_corso;
}

// Aggiungi il gestore di eventi click a tutti gli elementi <tr> nella tabella
var tableRows = document.querySelectorAll("table tbody tr");
tableRows.forEach(function (row) {
   row.addEventListener("click", handleTableRowClick);
});
