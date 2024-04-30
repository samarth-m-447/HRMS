$(document).ready(function(){
 document.getElementById('logout').addEventListener('click', function(event) {
    event.preventDefault();
    if (confirm('MSG98: '+MSG98)) {
      window.location.href = _contextPath+'logout';
    }
  });
});



