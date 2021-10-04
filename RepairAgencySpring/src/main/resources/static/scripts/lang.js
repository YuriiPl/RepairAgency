function changeLang(lang){
    document.cookie="lang="+lang+";SameSite=Lax";
    location.reload();
}