package com.wolvesgroup.inventorymanagment.utils.models

class AlertModel {

    var productName = ""
    var time = ""
    var key = ""
    var quantityThen = ""

    constructor(productName : String, time: String, key: String, quantityThen: String) {
        this.productName = productName
        this.time = time
        this.key = key
        this.quantityThen = quantityThen
    }

    constructor()
}