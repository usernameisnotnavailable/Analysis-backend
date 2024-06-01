fetch('http://127.0.0.1:8080/stocks?stock=4ig&from=2021-04-10&to=2021-04-15')
    .then(respones => respones.json())
    .then(data => console.log(data));