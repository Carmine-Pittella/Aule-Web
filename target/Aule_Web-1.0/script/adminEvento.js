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

window.addEventListener("DOMContentLoaded", (event) => {
   // Ottenere i riferimenti agli elementi del DOM
   var selectTipologiaRicorrenza = document.getElementById("selectTipologiaRicorrenza");
   var dataFineRicorrenza = document.getElementById("dataFineRicorrenza");

   // Aggiungere un listener per l'evento di cambio selezione
   selectTipologiaRicorrenza.addEventListener("change", function () {
      // Controllare se l'opzione selezionata è "NESSUNA"
      if (selectTipologiaRicorrenza.value === "NESSUNA") {
         // Resettare il valore del campo "dataFineRicorrenza"
         dataFineRicorrenza.value = "";
         // Disabilitare l'input "dataFineRicorrenza"
         dataFineRicorrenza.disabled = true;
      } else {
         // Abilitare l'input "dataFineRicorrenza"
         dataFineRicorrenza.disabled = false;
      }
   });

   // Verificare lo stato iniziale dell'opzione di selezione
   if (selectTipologiaRicorrenza.value === "NESSUNA") {
      // Disabilitare l'input "dataFineRicorrenza" all'avvio dello script se l'opzione è "NESSUNA"
      dataFineRicorrenza.disabled = true;
   }
});
