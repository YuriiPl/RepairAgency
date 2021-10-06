function fetchPostJson(form, okFunc, errorFunc){
    const elements = form.elements;
    let data = new FormData();
    for (let item of elements) {
        if(!item.name)continue;
        if(item.type==="radio" || item.type==="checkbox"){
            if(!item.checked)continue;
        }
        data.append(item.name, item.value);
    }
    fetch(form.action, {
        method: form.method,
        body: data,
        credentials: "include"
    })
    .then(response => {
        let func = errorFunc;
        if(response.ok)func=okFunc;
        response.json().then(data => {
            func(data);
        }).catch(function () {
            func("{\"message\" : \"[unknown_error]\"}");
        })
    });
    return false;
}

