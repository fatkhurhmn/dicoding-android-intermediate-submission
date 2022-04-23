package academy.bangkit.storyapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class Story(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("photo_url")
    val photoUrl: String,

    @ColumnInfo("created_at")
    val createdAt: String,

    @ColumnInfo("lat")
    val lat: Double? = null,

    @ColumnInfo("lon")
    val lon: Double? = null,
) : Parcelable