package com.septianen.imagemachine.constant

object Message {

    const val SOMETHING_HAPPENED = "Something Happened"
    const val NO_DATA = "No Data"
    const val DATA_NOT_FOUND = "Data Not Found"
    const val SUCCESS_SAVE_DATA = "Saved"

    const val EMPTY_MACHINE_NAME = "Machine Name is Empty"
    const val EMPTY_MACHINE_TYPE = "Machine Type is Empty"
    const val EMPTY_MACHINE_NUMBER = "Machine QR Number is Empty"
    const val EMPTY_MACHINE_DATE = "Last Maintenance Date is Empty"
    const val EMPTY_IMAGES = "Images is Empty"

    const val REQUIRE_PERMISSION = "All permissions are required"

    object Dialog {
        const val LEAVE = "You Wanna Leave?"
        const val PLEASE_SAVE_DATA = "Unsaved data will be lost! Please make sure to save data before leave."

        const val DELETE = "Are You Sure to Delete the Machine?"
        const val DELETED_DATA_WILL_LOST = "Deleted data won't be able to recover! Please make sure before you delete it."
    }
}