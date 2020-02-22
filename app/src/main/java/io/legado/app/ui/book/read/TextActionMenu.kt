package io.legado.app.ui.book.read

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.legado.app.R
import io.legado.app.base.adapter.ItemViewHolder
import io.legado.app.base.adapter.SimpleRecyclerAdapter
import io.legado.app.utils.sendToClip
import kotlinx.android.synthetic.main.item_fillet_text.view.*
import kotlinx.android.synthetic.main.popup_action_menu.view.*
import org.jetbrains.anko.sdk27.listeners.onClick


class TextActionMenu(private val context: Context, private val callBack: CallBack) :
    PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) {

    init {
        @SuppressLint("InflateParams")
        contentView = LayoutInflater.from(context).inflate(R.layout.popup_action_menu, null)

        isTouchable = true
        isOutsideTouchable = false
        isFocusable = false

        initRecyclerView()
    }

    private fun initRecyclerView() = with(contentView) {
        val adapter = Adapter(context)
        recycler_view.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recycler_view.adapter = adapter
        val menu = MenuBuilder(context)
        SupportMenuInflater(context).inflate(R.menu.content_select_action, menu)
        adapter.setItems(menu.visibleItems)
    }

    inner class Adapter(context: Context) :
        SimpleRecyclerAdapter<MenuItemImpl>(context, R.layout.item_text) {

        override fun convert(
            holder: ItemViewHolder,
            item: MenuItemImpl,
            payloads: MutableList<Any>
        ) {
            with(holder.itemView) {
                text_view.text = item.title
            }
        }

        override fun registerListener(holder: ItemViewHolder) {
            holder.itemView.onClick {
                getItem(holder.layoutPosition)?.let {
                    if (!callBack.onMenuItemSelected(it)) {
                        onMenuItemSelected(it)
                    }
                }
            }
        }
    }

    private fun onMenuItemSelected(item: MenuItemImpl) {
        when (item.itemId) {
            R.id.menu_copy -> context.sendToClip(callBack.selectedText)
        }
        callBack.onMenuActionFinally()
    }


    interface CallBack {
        val selectedText: String

        fun onMenuItemSelected(item: MenuItemImpl): Boolean

        fun onMenuActionFinally()
    }
}