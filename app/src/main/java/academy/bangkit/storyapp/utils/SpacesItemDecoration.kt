package academy.bangkit.storyapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)

        with(outRect) {
            top = space
            bottom = space
        }

        if (parent.layoutManager is GridLayoutManager) {
            if (position % 2 == 0) {
                outRect.right = space
            } else {
                outRect.left = space
            }
        }

    }
}