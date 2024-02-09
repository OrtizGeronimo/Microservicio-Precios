## Microservicio de Precios

### Introducción

En este microservicio nos encargaremos de gestionar los precios de un artículo, quitando el atributo de precio del catálogo para separar en un servicio distinto la forma en la que se aumenta o disminuye el precio de un producto dejando registrado con fecha todo cambio que se realizó sobre el precio del mismo, permitiendo además agregar a productos cupones de descuento u ofertas, los cuales se podrá consultar el precio del producto con este mismo, también permitiendo habilitar cupones a futuro.

### Interacción con otros servicios
Este servicio se comunicará con el carrito y la orden, cada vez que uno de ellos necesite saber el precio de un artículo, consultará a este servicio.


### Casos de Uso

####  Cambiar Precio: 
Se modifica el precio de un producto o varios, permitiendo si se desea modificar a futuro
#### Crear beneficio 
Crea un beneficio 
####  Asociar beneficio
Asociar el beneficio para un artículo
####  Consultar Precio actual
####  Consultar histórico de precios
####  Consultar precio con beneficio
 Se podrá ver el precio de un artículo con el beneficio aplicado

### Modelo

Price (Precio)
```json
{
 "id": Integer,
 "articleId": String,
 "price": Float,
 "effectiveDate": Date, //fechaFinVigencia
 "dealIds": []int,
}
```
Deal (Beneficio) <br>

El beneficio representa una oferta ya sea un cupón o un descuento, una disminución del valor del artículo, puede ser de tipo Porcentaje o Valor, tomando el monto lo que se deberá descontar de manera absoluta o el porcentaje. 
```json
{
 "id": Integer,
 "amount": Float,
 "code": String,
 "startDate": Date,
 "endDate": Date,
 "type": Enum{"Porcentaje", "Valor"}
}
```

### Interfaz REST

**Cambiar el precio de un artículo o varios** <br>
`POST /v1/price/update/`

Body

```json
{
 "articleId": Integer,
 "amount": Float,
}
```

*Response* <br>
`200 OK` si existen los artículos | `404 NOT FOUND` si no existe 


-----

**Crear un beneficio** <br>
`POST /v1/price/createDeal/`

Body

```json
{
 "startDate": Date,
 "endDate": Date,
 "amount": Float,
 "code": string,
 "type": Enum{"Porcentaje", "Valor"}
}
```

*Response* <br>
`200 OK` si se realizó la creación correctamente | `400 BAD REQUEST` | `500 INTERNAL SERVER ERROR`

-----

**Agregar un beneficio** <br>
`POST /v1/price/addDeal/{dealId}`

Body

```json
{
 "articleId": Integer
}
```

*Response* <br>
`200 OK` si existe el artículo y se añade el beneficio correctamente | `400 BAD REQUEST` | `500 INTERNAL SERVER ERROR`


----

**Consultar precio actual** <br>
`GET /v1/price/{articleId}`



*Response* <br>
`200 OK` si existe el artículo 
```json
{
 "articleId": {"articleId"},
 "amount": {"amount"}
}
```

`400 BAD REQUEST` si no existe | `500 INTERNAL SERVER ERROR`

----

**Consultar histórico de precios** <br>
`GET /v1/price/{articleId}/history`


*Response* <br>
`200 OK` si existe el artículo 
```json
{
    "articleId": {"articleId"},
    "prices": [
        "price": {"price"},
        "effectiveDate": {"effectiveDate"}
    ]
}
```

`400 BAD REQUEST` si no existe | `500 INTERNAL SERVER ERROR`

----

**Consultar precio con beneficio aplicado** <br>
`POST /v1/price/`

Body

```json
{
 "articleId": Integer,
 "dealId": Integer
}
```

*Response* <br>
`200 OK` si existe el artículo 
```json
{
    "articleId": {"articleId"},
    "dealId": {"dealId"},
    "price": {"price"},
    "finalPrice": {"finalPrice"}
}
```

`400 BAD REQUEST` si no existe | `500 INTERNAL SERVER ERROR`


### Interfaz asincronica (rabbit)

**Notificación de actualización de precio**

Notifica cambio de precios para reportes estadísticos en direct a canal **price-update** 

**Body**

```json
{
    "type": "price-update",
    "exchange": "prices",
    "queue": "prices-update",
    "message": {
        "articleId": {"id"},
        "price": {"price"},
        "updateDate": {"updateDate"}
    }
}
```

**Validación de artículos**

GET

Valida artículo direct a través de  **article-exist** 


```json
{
    "type": "article-exist",
    "message": {
        "articleId": "{articleId}",
        "valid": true|false
    }
}
```

POST

 Se envía mensaje a catalog para comprobar que existe un artículo

```json
{
   "type": "article-exist",
   "queue": "price-article-exist",
   "exchange": "prices",
    "message": {
        "referenceId": "{cartId}",
        "articleId": "{articleId}"
   }
}
```
