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
