- [core-shop](#core-shop)
- [Explanations](#explanations)
    * [Shop stock functional aspect](#shop-stock-functional-aspect)
    * [Technical aspect](#technical-aspect)
    * [Tests](#tests)
    * [Improvements](#improvements)

# core-shop
Core shop example for https://github.com/brenierr/core-abstract-sample

# Explanations

We provide here a shoe shop core API implementation.

We consider the shop to be our global managed entity.
Patch method can add/withdraw shoe boxes from the shop (by sending a partial shop state on how
the shop shoe boxes should be updated).

For now, as there is a one to one relation between a shoe and a stock, we consider it in the same
entity. A shoe is a `com.example.demo.core.domain.ShoeStockEntity` with quantity 0.

## Shop stock functional aspect

A shoe key is represented by
- name
- color
- size

All shoes are displayed in the shop stock even if their quantity is 0.
You cannot have more than 30 shoe boxes in the shop.
You will get a `com.example.demo.core.exception.StockFullException` in case the is more than 30 shoe boxes in the shop.
The maximum shoe boxes allowed in the shop can be overriden in properties file :
Patch a shoe quantity will automatically update existing quantity if it exists, or create a shoe
stock with incoming quantity if it doesn't.
If you search for shoes without any filter, it will return all shoes.

```properties
shop.shoes.boxes.maximum_allowed=50
```

## Technical aspect

This core implementation uses :
- an h2 in memory database with initial data provided in resources/data.sql
- spring jpa to store and retrieve data

## Tests

To demonstrate how module works, we create a "fake" spring boot application and use an empty database.
See tests `com.example.demo.core.service.StockShopCoreIntegrationTest` and `com.example.demo.core.service.ShoeShopCoreIntegrationTest` which demonstrate how application behave on specific cases.

## Improvements

To go further, we might need to split model, and historize shoe stock change (store quantity delta
instead of final quantity in the shop).
