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

import com.example.space_colony.R;
import com.example.space_colony.logic.Storage;
import com.example.space_colony.model.CrewMember;
import com.example.space_colony.model.Location;
import com.example.space_colony.ui.adapters.CrewAdapter;

import java.util.List;

public class QuartersFragment extends Fragment {
    private CrewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quarters, container, false);

        RecyclerView rv = view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CrewAdapter(Storage.getInstance().getByLocation(Location.QUARTERS));
        rv.setAdapter(adapter);

        view.findViewById(R.id.btn_home).setOnClickListener(v ->
            requireActivity().getSupportFragmentManager().popBackStack());
        view.findViewById(R.id.btn_to_simulator).setOnClickListener(v -> moveSelected(Location.SIMULATOR));
        view.findViewById(R.id.btn_to_mission_control).setOnClickListener(v -> moveSelected(Location.MISSION_CONTROL));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(Storage.getInstance().getByLocation(Location.QUARTERS));
    }

    private void moveSelected(Location target) {
        List<CrewMember> selected = adapter.getChecked();
        if (selected.isEmpty()) {
            Toast.makeText(requireContext(), "Select crew members first", Toast.LENGTH_SHORT).show();
            return;
        }
        for (CrewMember m : selected) {
            m.setLocation(target);
        }
        adapter.refresh(Storage.getInstance().getByLocation(Location.QUARTERS));
        String dest = target == Location.SIMULATOR ? "Simulator" : "Mission Control";
        Toast.makeText(requireContext(), selected.size() + " crew moved to " + dest, Toast.LENGTH_SHORT).show();
    }
}
