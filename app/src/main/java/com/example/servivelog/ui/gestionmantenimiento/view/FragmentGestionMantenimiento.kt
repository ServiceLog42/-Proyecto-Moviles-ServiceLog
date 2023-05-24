package com.example.servivelog.ui.gestionmantenimiento.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.servivelog.R
import com.example.servivelog.databinding.FragmentGestionMantenimientoBinding
import com.example.servivelog.domain.model.computer.ComputerItem
import com.example.servivelog.domain.model.mantenimiento.MantenimientoCUDItem
import com.example.servivelog.ui.MainActivity
import com.example.servivelog.ui.gestionmantenimiento.adapter.MantenimientoAdapter
import com.example.servivelog.ui.gestionmantenimiento.viewmodel.GestionManteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentGestionMantenimiento : Fragment(), MantenimientoAdapter.OnDeleteClickListener  {

    private val gestionManteViewModel: GestionManteViewModel by viewModels()
    private lateinit var gestionMantenimientoBinding: FragmentGestionMantenimientoBinding
    private lateinit var addBtn: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MantenimientoAdapter
    private var mantf: List<MantenimientoCUDItem> = emptyList<MantenimientoCUDItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        gestionMantenimientoBinding = FragmentGestionMantenimientoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Mostrar el cuadro de diálogo de confirmación aquí
            showExitConfirmationDialog()
        }
        callback.isEnabled = true

        gestionManteViewModel.onCreate()

        gestionManteViewModel.modeloMantenimiento.observe(viewLifecycleOwner){mantenimiento->

            mantf = mantenimiento

            CoroutineScope(Dispatchers.Main).launch {
                val listac = gestionManteViewModel.getAllComputers()
                setAdapter(mantf, listac)
            }

            gestionMantenimientoBinding.etServiceTag.addTextChangedListener {filter->
                val mantenimientofiltrados = mantf.filter { it.computadora.uppercase().contains(filter.toString().uppercase()) }
                adapter.updateRecycler(mantenimientofiltrados)
            }

        }

        addBtn = gestionMantenimientoBinding.fbtnagregar

        addBtn.setOnClickListener{
            val navController = Navigation.findNavController(it)
            navController.navigate(R.id.action_fragmentGestionMantenimiento_to_fragmentAgregarMantenimiento)
        }

        return gestionMantenimientoBinding.root
    }

    override fun onDeleteClicked(mantenimientoCUDItem: MantenimientoCUDItem) {
        val updatedList = adapter.listM.toMutableList()
        updatedList.remove(mantenimientoCUDItem)
        mantf = updatedList
        adapter.updateRecycler(updatedList)
    }

    private fun setAdapter(it: List<MantenimientoCUDItem>, listac: List<ComputerItem>) {
        adapter = MantenimientoAdapter(requireActivity(),requireActivity(), it, gestionMantenimientoBinding.root, gestionManteViewModel, listac)
        adapter.setOnDeleteClickListener(this)
        recyclerView = gestionMantenimientoBinding.rvMantenimiento
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
    }

    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Salir de la aplicación")
        alertDialogBuilder.setMessage("¿Estás seguro de que deseas cerrar sesión?")
        alertDialogBuilder.setPositiveButton("Sí") { dialog, which ->
            (activity as MainActivity).findViewById<BottomNavigationView>(R.id.BarraNavegacion).isVisible =
                false //esto funciona para dejar de mostrar el navigation view despues de cerrar sesion
            val navController = Navigation.findNavController(gestionMantenimientoBinding.root)
            navController.popBackStack()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, which ->
            // No hacer nada y cerrar el cuadro de diálogo
        }
        alertDialogBuilder.show()
    }

}