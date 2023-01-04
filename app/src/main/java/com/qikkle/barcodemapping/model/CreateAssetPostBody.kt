package com.qikkle.barcodemapping.model


import com.google.gson.annotations.SerializedName

data class CreateAssetPostBody(
    @SerializedName("DeliveryDate")
    val deliveryDate: String,
    @SerializedName("PONo")
    val pONo: String,
    @SerializedName("CategoryId")
    val categoryId: Int,
    @SerializedName("ProductId")
    val productId: Int,
    @SerializedName("Make")
    val make: String,
    @SerializedName("SerialNo")
    val serialNo: String,
    @SerializedName("ReceiverName")
    val receiverName: String,

    /*@SerializedName("AssetId")
    val assetId: String,
    @SerializedName("AvailableType")
    val availableType: Any,
    @SerializedName("Description")
    val description: Any,
    @SerializedName("DomainId")
    val domainId: Any,
    @SerializedName("FromReceived")
    val fromReceived: Any,
    @SerializedName("GRNNO")
    val gRNNO: Any,
    @SerializedName("INWARDID")
    val iNWARDID: Any,
    @SerializedName("InvoiceDate")
    val invoiceDate: Any,
    @SerializedName("InvoiceNo")
    val invoiceNo: Any,
    @SerializedName("IsSuccess")
    val isSuccess: Int,
    @SerializedName("LocationId")
    val locationId: String,
    @SerializedName("Model")
    val model: Any,
    @SerializedName("ModifiedBy")
    val modifiedBy: String,
    @SerializedName("Nos")
    val nos: Any,
    @SerializedName("PartNo")
    val partNo: Any,
    @SerializedName("PlaceLocation")
    val placeLocation: Any,
    @SerializedName("Qty")
    val qty: Int,
    @SerializedName("RepPersonId")
    val repPersonId: Any,
    @SerializedName("SrNo")
    val srNo: Any,
    @SerializedName("TotalBox")
    val totalBox: Any,
    @SerializedName("USDCallID")
    val uSDCallID: Any*/
)