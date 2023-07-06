document.addEventListener("DOMContentLoaded", function () {
   const dataInizioCSV = document.getElementById("dataInizioCSV");
   const dataFineCSV = document.getElementById("dataFineCSV");
   const downloadButton = document.querySelector(".btn-success");
   function checkDates() {
      const dataInizio = dataInizioCSV.value;
      const dataFine = dataFineCSV.value;
      if (dataInizio === "" || dataFine === "") {
         downloadButton.disabled = true;
      } else {
         downloadButton.disabled = false;
      }
   }
   // Aggiungi gli eventi di ascolto per il cambiamento delle date
   dataInizioCSV.addEventListener("change", checkDates);
   dataFineCSV.addEventListener("change", checkDates);
   // Controlla lo stato iniziale
   checkDates();
});

// GRUPPO
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

// AULA
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

// CORSO
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

window.onload = function () {
   var tableRows = document.querySelectorAll(".table tbody tr");
   // Aggiungi un gestore di eventi a ciascun elemento della tabella
   tableRows.forEach(function (row) {
      row.addEventListener("click", function () {
         // Ottieni l'ID dell'elemento cliccato
         var idElemento = this.id;

         // Mostra un alert con l'ID dell'elemento cliccato
         alert(idElemento);
      });
   });
};
