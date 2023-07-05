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
