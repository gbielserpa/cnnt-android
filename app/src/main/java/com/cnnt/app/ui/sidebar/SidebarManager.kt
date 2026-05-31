package com.cnnt.app.ui.sidebar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnnt.app.R
import com.cnnt.app.data.model.SpatialObjectType
import com.cnnt.app.databinding.ActivityMainBinding

class SidebarManager(
    private val binding: ActivityMainBinding,
    private val activity: AppCompatActivity
) {
    var onPageSelected: ((Int) -> Unit)? = null
    var onNewPageClicked: (() -> Unit)? = null
    var onInsertBlockClicked: ((SpatialObjectType) -> Unit)? = null

    init {
        setupSidebarToggle()
        setupPageList()
        setupBlockInserts()
    }

    private fun setupSidebarToggle() {
        binding.sidebarToggle.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun setupPageList() {
        binding.pageList.layoutManager = LinearLayoutManager(activity)
        binding.btnAddPage.setOnClickListener {
            onNewPageClicked?.invoke()
        }
    }

    private fun setupBlockInserts() {
        binding.btnInsertText.setOnClickListener {
            onInsertBlockClicked?.invoke(SpatialObjectType.TEXT)
            closeSidebar()
        }
        binding.btnInsertChecklist.setOnClickListener {
            onInsertBlockClicked?.invoke(SpatialObjectType.CHECKLIST)
            closeSidebar()
        }
        binding.btnInsertImage.setOnClickListener {
            onInsertBlockClicked?.invoke(SpatialObjectType.IMAGE)
            closeSidebar()
        }
        binding.btnInsertPdf.setOnClickListener {
            onInsertBlockClicked?.invoke(SpatialObjectType.PDF)
            closeSidebar()
        }
        binding.btnInsertLink.setOnClickListener {
            onInsertBlockClicked?.invoke(SpatialObjectType.LINK)
            closeSidebar()
        }
    }

    private fun closeSidebar() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }
}
