package com.example.space_colony.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button btnLaunch;
    private final Handler handler = new Handler(Looper.getMainLooper());

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
        btnLaunch = view.findViewById(R.id.btn_launch);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v ->
            requireActivity().getSupportFragmentManager().popBackStack());
        btnLaunch.setOnClickListener(v -> launchMission());

        view.findViewById(R.id.btn_send_home).setOnClickListener(v -> sendSelectedHome());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refresh(Storage.getInstance().getByLocation(Location.MISSION_CONTROL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

    private void sendSelectedHome() {
        List<CrewMember> selected = adapter.getChecked();
        if (selected.isEmpty()) {
            Toast.makeText(requireContext(), "Select crew members to send home", Toast.LENGTH_SHORT).show();
            return;
        }
        for (CrewMember m : selected) {
            m.setLocation(Location.QUARTERS);
        }
        adapter.refresh(Storage.getInstance().getByLocation(Location.MISSION_CONTROL));
    }

    private void launchMission() {
        List<CrewMember> selected = adapter.getChecked();
        if (selected.size() < 2) {
            Toast.makeText(requireContext(), "Select exactly 2 crew members", Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> log = MissionControl.runMission(selected.get(0), selected.get(1));

        missionLog.setText("");
        btnLaunch.setEnabled(false);

        long delay = 0;
        for (String line : log) {
            long d = delay;
            handler.postDelayed(() -> {
                missionLog.append(line + "\n");
                logScroll.post(() -> logScroll.fullScroll(View.FOCUS_DOWN));
            }, d);
            delay += delayFor(line);
        }

        handler.postDelayed(() -> {
            adapter.refresh(Storage.getInstance().getByLocation(Location.MISSION_CONTROL));
            btnLaunch.setEnabled(true);
        }, delay);
    }

    private long delayFor(String line) {
        if (line.startsWith("===")) return 700;
        if (line.startsWith("---")) return 500;
        if (line.isEmpty())         return 200;
        return 300;
    }
}
