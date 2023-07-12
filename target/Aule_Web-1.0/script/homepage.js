function lallero() {
   // gruppo
   document.getElementById("selectGruppo").addEventListener("change", function () {
      let gruppoSelect = document.getElementById("selectGruppo");
      let url = new URL(window.location.href);
      let searchParams = url.searchParams;

      if (gruppoSelect.value !== "") {
         searchParams.delete("eventiAttuali");
         searchParams.delete("eventiTreOre");
         searchParams.set("gruppo", gruppoSelect.value);
      } else {
         searchParams.delete("gruppo");
         searchParams.delete("aula");
         searchParams.delete("corso");
         searchParams.delete("eventiAttuali");
         searchParams.delete("eventiTreOre");
         searchParams.delete("dataInizio");
         searchParams.delete("dataFine");
      }
      url.search = searchParams.toString();
      window.location.href = url.toString();
   });
}

// aula
document.getElementById("selectAula").addEventListener("change", function () {
   let aulaSelect = document.getElementById("selectAula");
   let url = new URL(window.location.href);
   let searchParams = url.searchParams;

   if (aulaSelect.value !== "") {
      searchParams.set("aula", aulaSelect.value);
   } else {
      searchParams.delete("aula");
   }
   url.search = searchParams.toString();
   window.location.href = url.toString();
});

// corso
document.getElementById("selectCorso").addEventListener("change", function () {
   let corsoSelect = document.getElementById("selectCorso");
   let url = new URL(window.location.href);
   let searchParams = url.searchParams;

   if (corsoSelect.value !== "") {
      searchParams.set("corso", corsoSelect.value);
   } else {
      searchParams.delete("corso");
   }
   url.search = searchParams.toString();
   window.location.href = url.toString();
});

// data inizio
document.getElementById("dataInizio").addEventListener("change", function () {
   let dataInizio = document.getElementById("dataInizio");
   let url = new URL(window.location.href);
   let searchParams = url.searchParams;

   if (dataInizio.value !== "") {
      searchParams.set("dataInizio", dataInizio.value);
   } else {
      searchParams.delete("dataInizio");
   }
   url.search = searchParams.toString();
   window.location.href = url.toString();
});

// data fine
document.getElementById("dataFine").addEventListener("change", function () {
   let dataFine = document.getElementById("dataFine");
   let url = new URL(window.location.href);
   let searchParams = url.searchParams;

   if (dataFine.value !== "") {
      searchParams.set("dataFine", dataFine.value);
   } else {
      searchParams.delete("dataFine");
   }
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

function EventiAttuali() {
   window.location.href = "HomePage?eventiAttuali=1";
}

function EventiTreOre() {
   window.location.href = "HomePage?eventiTreOre=1";
}

document.addEventListener("DOMContentLoaded", function () {
   var selectGruppo = document.getElementById("selectGruppo");
   var selectAula = document.getElementById("selectAula");
   var selectCorso = document.getElementById("selectCorso");
   var dataInizio = document.getElementById("dataInizio");
   var dataFine = document.getElementById("dataFine");
   selectGruppo.addEventListener("change", function () {
      if (selectGruppo.value === "") {
         selectAula.disabled = true;
         selectCorso.disabled = true;
         dataInizio.disabled = true;
         dataFine.disabled = true;
         selectAula.value = "";
         selectCorso.value = "";
         dataInizio.value = "";
         dataFine.value = "";
      } else {
         selectAula.disabled = false;
         selectCorso.disabled = false;
         dataInizio.disabled = false;
         dataFine.disabled = false;
      }
   });
   // Disabilita gli altri campi all'avvio se "selectGruppo" è impostato su "Tutti"
   if (selectGruppo.value === "") {
      selectAula.disabled = true;
      selectCorso.disabled = true;
      dataInizio.disabled = true;
      dataFine.disabled = true;
      selectAula.value = "";
      selectCorso.value = "";
      dataInizio.value = "";
      dataFine.value = "";
   }
   lallero();
});
