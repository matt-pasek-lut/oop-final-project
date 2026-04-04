package com.example.space_colony.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.space_colony.R;
import com.example.space_colony.logic.Storage;
import com.example.space_colony.model.Location;
import com.example.space_colony.utils.FileManager;

public class HomeFragment extends Fragment {
    private TextView quartersCount, simulatorCount, missionControlCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        quartersCount = view.findViewById(R.id.quarters_count);
        simulatorCount = view.findViewById(R.id.simulator_count);
        missionControlCount = view.findViewById(R.id.mission_control_count);

        view.findViewById(R.id.btn_quarters).setOnClickListener(v -> navigate(new QuartersFragment()));
        view.findViewById(R.id.btn_simulator).setOnClickListener(v -> navigate(new SimulatorFragment()));
        view.findViewById(R.id.btn_mission_control).setOnClickListener(v -> navigate(new MissionControlFragment()));
        view.findViewById(R.id.btn_recruit).setOnClickListener(v -> navigate(new RecruitFragment()));
        view.findViewById(R.id.btn_save).setOnClickListener(v -> {
            FileManager.save(requireContext());
            Toast.makeText(requireContext(), "Crew saved!", Toast.LENGTH_SHORT).show();
        });
        view.findViewById(R.id.btn_load).setOnClickListener(v -> confirmLoad());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshCounts();
    }

    private void confirmLoad() {
        boolean hasCrew = !Storage.getInstance().getAll().isEmpty();
        if (hasCrew) {
            new AlertDialog.Builder(requireContext())
                .setTitle("Load saved crew?")
                .setMessage("This will replace your current crew with the last saved state.")
                .setPositiveButton("Load", (dialog, which) -> load())
                .setNegativeButton("Cancel", null)
                .show();
        } else {
            load();
        }
    }

    private void load() {
        FileManager.tryLoad(requireContext());
        refreshCounts();
        Toast.makeText(requireContext(), "Crew loaded!", Toast.LENGTH_SHORT).show();
    }

    private void refreshCounts() {
        Storage s = Storage.getInstance();
        quartersCount.setText("Quarters: " + s.getByLocation(Location.QUARTERS).size());
        simulatorCount.setText("Simulator: " + s.getByLocation(Location.SIMULATOR).size());
        missionControlCount.setText("Mission Control: " + s.getByLocation(Location.MISSION_CONTROL).size());
    }

    private void navigate(Fragment fragment) {
        ((MainActivity) requireActivity()).navigateTo(fragment);
    }
}
