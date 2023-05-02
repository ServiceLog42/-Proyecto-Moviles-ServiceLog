package com.example.servivelog.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.servivelog.databinding.FragmentEditarDiagnosticoBinding


class FragmentEditarLaboratorio : Fragment() {
    private lateinit var editarDiagnosticoBinding: FragmentEditarDiagnosticoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editarDiagnosticoBinding = FragmentEditarDiagnosticoBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return editarDiagnosticoBinding.root
    }


}