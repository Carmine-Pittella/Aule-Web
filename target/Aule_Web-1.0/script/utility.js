function eliminaAttrezzo() {
   // Ottieni l'ID del bottone premuto
   var attrId = event.target.id;
   // Ottieni l'URL attuale
   var currentUrl = window.location.href;
   // Crea l'URL per la cancellazione dell'attrezzo
   var deleteUrl = currentUrl + "&deleteAttrezzo=" + attrId;
   // Reindirizza la pagina all'URL creato
   window.location.href = deleteUrl;
}
