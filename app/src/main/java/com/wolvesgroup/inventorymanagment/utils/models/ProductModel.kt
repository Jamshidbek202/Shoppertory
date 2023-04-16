package com.wolvesgroup.inventorymanagment.utils.models

class ProductModel {

    var name = ""
    var quantity = ""
    var price = ""
    var sales_num = ""
    var desc = ""
    var key = ""

    constructor()
    constructor(
        name: String,
        quantity: String,
        price: String,
        sales_num: String,
        desc: String,
        key: String
    ) {
        this.name = name
        this.quantity = quantity
        this.price = price
        this.sales_num = sales_num
        this.desc = desc
        this.key = key
    }
}