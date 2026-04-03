package com.example.space_colony.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;

import com.example.space_colony.R;
import com.example.space_colony.logic.MissionControl;
import com.example.space_colony.logic.Storage;
import com.example.space_colony.model.CrewMember;
import com.example.space_colony.model.Location;
import com.example.space_colony.ui.adapters.CrewAdapter;

import java.util.List;

public class MissionControlFragment extends Fragment {
    private CrewAdapter adapter;
    private TextView missionLog;
    private ScrollView logScroll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mission_control, container, false);

        RecyclerView rv = view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CrewAdapter(Storage.getInstance().getByLocation(Location.MISSION_CONTROL));
        adapter.setMaxSelection(2);
        rv.setAdapter(adapter);

        missionLog = view.findViewById(R.id.mission_log);
        logScroll = view.findViewById(R.id.log_scroll);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v ->
            requireActivity().getSupportFragmentManager().popBackStack());
        view.findViewById(R.id.btn_launch).setOnClickListener(v -> launchMission());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(Storage.getInstance().getByLocation(Location.MISSION_CONTROL));
    }

    private void launchMission() {
        List<CrewMember> selected = adapter.getChecked();
        if (selected.size() < 2) {
            Toast.makeText(requireContext(), "Select exactly 2 crew members", Toast.LENGTH_SHORT).show();
            return;
        }
        List<String> log = MissionControl.runMission(selected.get(0), selected.get(1));
        StringBuilder sb = new StringBuilder();
        for (String line : log) {
            sb.append(line).append("\n");
        }
        missionLog.setText(sb.toString());
        logScroll.post(() -> logScroll.fullScroll(View.FOCUS_DOWN));
        adapter.refresh(Storage.getInstance().getByLocation(Location.MISSION_CONTROL));
    }
}
