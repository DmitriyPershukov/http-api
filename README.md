# http api
Добавить телефон (строка) и имя (строка) в справочник
endpoint: POST /phones/createNewPhone
data: в теле запроса json вида { “name”: “qwerty”, “phoneNumber”: “12345” }

Получить список ранее добавленных вхождений в справочник (кортеж
<id, имя, номер>) в виде json
endpoint: GET /phones
response: json вида
[
{ “name”: “qwerty”, “phoneNumber”: “12345” },
{ “name”: “qwerty2”, “phoneNumber”: “12346” },
...
{ “name”: “qwerty”, “phoneNumber”: “12345” }
]

Изменить значения телефона или имени во вхождении выбранному по id
endpoint: POST /phone/{id}
data: в теле запроса json вида { “phoneNumber”: “12345”, “name”: “qwerty” }

Удалить из справочника вхождение по id
endpoint: DELETE /phone/{id}

Поиск всех вхождений по подстроке номера
endpoint: GET /phones/searchByNumber?phoneSubstring=<...>

Поиск всех вхождений по подстроке имени
endpoint: GET /phones/searchByName?nameSubstring=<...>
