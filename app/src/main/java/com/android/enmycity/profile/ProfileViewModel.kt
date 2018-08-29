package com.android.enmycity.profile

import android.os.Parcel
import android.os.Parcelable

class ProfileViewModel(val name: String,
                       val coffeeLanguage: Boolean,
                       val nightLife: Boolean,
                       val localShopping: Boolean,
                       val gastronomicTour: Boolean,
                       val cityTour: Boolean,
                       val sportBreak: Boolean,
                       val volunteering: Boolean,
                       val photoUrl: String,
                       val email: String,
                       val id: String) : Parcelable {
  constructor(parcel: Parcel) : this(
      parcel.readString(),
      parcel.readByte() != 0.toByte(),
      parcel.readByte() != 0.toByte(),
      parcel.readByte() != 0.toByte(),
      parcel.readByte() != 0.toByte(),
      parcel.readByte() != 0.toByte(),
      parcel.readByte() != 0.toByte(),
      parcel.readByte() != 0.toByte(),
      parcel.readString(),
      parcel.readString(),
      parcel.readString())

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(name)
    parcel.writeByte(if (coffeeLanguage) 1 else 0)
    parcel.writeByte(if (nightLife) 1 else 0)
    parcel.writeByte(if (localShopping) 1 else 0)
    parcel.writeByte(if (gastronomicTour) 1 else 0)
    parcel.writeByte(if (cityTour) 1 else 0)
    parcel.writeByte(if (sportBreak) 1 else 0)
    parcel.writeByte(if (volunteering) 1 else 0)
    parcel.writeString(photoUrl)
    parcel.writeString(email)
    parcel.writeString(id)
  }

  override fun describeContents() = 0

  companion object CREATOR : Parcelable.Creator<ProfileViewModel> {
    override fun createFromParcel(parcel: Parcel) = ProfileViewModel(parcel)

    override fun newArray(size: Int) = arrayOfNulls<ProfileViewModel?>(size)
  }
}