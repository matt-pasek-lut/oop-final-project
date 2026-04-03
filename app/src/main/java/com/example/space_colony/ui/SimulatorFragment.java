package com.example.space_colony.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import com.example.space_colony.R;
import com.example.space_colony.logic.Storage;
import com.example.space_colony.model.CrewMember;
import com.example.space_colony.model.Location;
import com.example.space_colony.ui.adapters.CrewAdapter;

import java.util.List;

public class SimulatorFragment extends Fragment {
    private CrewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simulator, container, false);

        RecyclerView rv = view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CrewAdapter(Storage.getInstance().getByLocation(Location.SIMULATOR));
        rv.setAdapter(adapter);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v ->
            requireActivity().getSupportFragmentManager().popBackStack());
        view.findViewById(R.id.btn_train).setOnClickListener(v -> trainSelected());
        view.findViewById(R.id.btn_send_home).setOnClickListener(v -> sendHome());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(Storage.getInstance().getByLocation(Location.SIMULATOR));
    }

    private void trainSelected() {
        List<CrewMember> selected = adapter.getChecked();
        if (selected.isEmpty()) {
            Toast.makeText(requireContext(), "Select crew members first", Toast.LENGTH_SHORT).show();
            return;
        }
        for (CrewMember m : selected) {
            m.gainExperience();
        }
        adapter.refresh(Storage.getInstance().getByLocation(Location.SIMULATOR));
        Toast.makeText(requireContext(), selected.size() + " crew trained! (+1 XP each)", Toast.LENGTH_SHORT).show();
    }

    private void sendHome() {
        List<CrewMember> selected = adapter.getChecked();
        if (selected.isEmpty()) {
            Toast.makeText(requireContext(), "Select crew members first", Toast.LENGTH_SHORT).show();
            return;
        }
        for (CrewMember m : selected) {
            m.setLocation(Location.QUARTERS);
            m.restoreEnergy();
        }
        adapter.refresh(Storage.getInstance().getByLocation(Location.SIMULATOR));
        Toast.makeText(requireContext(), selected.size() + " crew sent home (energy restored)", Toast.LENGTH_SHORT).show();
    }
}
