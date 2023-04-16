package com.wolvesgroup.inventorymanagment.utils.models

class HistoryModel {

    var key = ""
    var name = ""
    var productKey = ""
    var quantity = ""
    var profit = ""
    var date = ""

    constructor(
        key: String,
        name: String,
        productKey: String,
        quantity: String,
        profit: String,
        date: String
    ) {
        this.key = key
        this.name = name
        this.productKey = productKey
        this.quantity = quantity
        this.profit = profit
        this.date = date
    }

    constructor()


}