function changeLang(lang){
    document.cookie="lang="+lang+";SameSite=Lax;path=/";
    location.reload();
}