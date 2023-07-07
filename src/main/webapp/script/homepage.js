// document.addEventListener("DOMContentLoaded", function () {
//    const dataInizioCSV = document.getElementById("dataInizioCSV");
//    const dataFineCSV = document.getElementById("dataFineCSV");
//    const downloadButton = document.querySelector(".btn-success");
//    function checkDates() {
//       const dataInizio = dataInizioCSV.value;
//       const dataFine = dataFineCSV.value;
//       if (dataInizio === "" || dataFine === "") {
//          downloadButton.disabled = true;
//       } else {
//          downloadButton.disabled = false;
//       }
//    }
//    // Aggiungi gli eventi di ascolto per il cambiamento delle date
//    dataInizioCSV.addEventListener("change", checkDates);
//    dataFineCSV.addEventListener("change", checkDates);
//    // Controlla lo stato iniziale
//    checkDates();
// });

// gruppo
document.getElementById("selectGruppo").addEventListener("change", function () {
   let gruppoSelect = document.getElementById("selectGruppo");
   let url = new URL(window.location.href);
   let searchParams = url.searchParams;

   if (gruppoSelect.value !== "") {
      // Aggiungi o sostituisci il parametro "gruppo" nell'URL
      searchParams.set("gruppo", gruppoSelect.value);
   } else {
      // Rimuovi il parametro "gruppo" dall'URL
      searchParams.delete("gruppo");
   }

   // Aggiorna l'URL con i nuovi parametri
   url.search = searchParams.toString();
   window.location.href = url.toString();
});

// aula
document.getElementById("selectAula").addEventListener("change", function () {
   let aulaSelect = document.getElementById("selectAula");
   let url = new URL(window.location.href);
   let searchParams = url.searchParams;

   if (aulaSelect.value !== "") {
      // Aggiungi o sostituisci il parametro "aula" nell'URL
      searchParams.set("aula", aulaSelect.value);
   } else {
      // Rimuovi il parametro "aula" dall'URL
      searchParams.delete("aula");
   }

   // Aggiorna l'URL con i nuovi parametri
   url.search = searchParams.toString();
   window.location.href = url.toString();
});

// corso
document.getElementById("selectCorso").addEventListener("change", function () {
   let corsoSelect = document.getElementById("selectCorso");
   let url = new URL(window.location.href);
   let searchParams = url.searchParams;

   if (corsoSelect.value !== "") {
      // Aggiungi o sostituisci il parametro "corso" nell'URL
      searchParams.set("corso", corsoSelect.value);
   } else {
      // Rimuovi il parametro "corso" dall'URL
      searchParams.delete("corso");
   }

   // Aggiorna l'URL con i nuovi parametri
   url.search = searchParams.toString();
   window.location.href = url.toString();
});

// Funzione per gestire il click sugli elementi della tabella
function handleTableRowClick(event) {
   var id_evento = event.target.parentNode.id; // Ottieni l'ID dell'elemento selezionato
   location.href = "dettagliEvento?evento=" + id_evento;
}

// Aggiungi il gestore di eventi click a tutti gli elementi <tr> nella tabella
var tableRows = document.querySelectorAll("table tbody tr");
tableRows.forEach(function (row) {
   row.addEventListener("click", handleTableRowClick);
});
